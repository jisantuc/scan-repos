package io.jisantuc

import cats.effect.ExitCode
import cats.effect.IO
import cats.effect.IOApp
import cats.syntax.apply._
import com.monovore.decline.Opts
import com.monovore.decline.effect.CommandIOApp
import fs2.io.file.Files
import fs2.io.file.{Path => Fs2Path}
import fs2.text
import github4s.domain.PullRequest
import org.http4s.blaze.client.BlazeClientBuilder

import java.nio.file.Path
import java.time.Duration
import java.time.Instant

object Main
    extends CommandIOApp(
      "scan-repos",
      "Scan for repositories with open bot pull requests",
      true,
      "0.1.0"
    ) {

  val inFilePathOpt: Opts[Path] = Opts.argument[Path](
    "REPOS_FILE"
  )
  val botNameOpt: Opts[String] = Opts
    .option[String](
      "bot-name",
      help = "Login name of the bot account that opens dependency upgrade PRs",
      short = "b"
    )
    .withDefault("47erbot")

  val tokenOpt: Opts[String] = Opts.env(
    "GITHUB_TOKEN",
    help = "Token to use to authenticate calls to GitHub"
  )

  case class Config(
      inputFile: Path,
      botName: Option[String]
  )

  def lag(pr: PullRequest): String = {
    val now = Instant.now
    val difference =
      now.toEpochMilli - Instant.parse(pr.created_at).toEpochMilli
    val differenceDuration = Duration.ofMillis(difference)
    val daysDifference = differenceDuration.toDays
    s"${daysDifference} day${if (daysDifference != 1) { "s" }
    else { "" }} ago"
  }

  def main: Opts[IO[ExitCode]] =
    (inFilePathOpt, botNameOpt, tokenOpt) mapN { case (path, bot, token) =>
      (for {
        (org :: repo :: Nil) <- Files[IO]
          .readAll(Fs2Path.fromNioPath(path))
          .through(text.utf8.decode)
          .through(text.lines)
          .map(_.split("/").toList)
        blazeClient <- fs2.Stream.resource(BlazeClientBuilder[IO].resource)
        prUrls <- fs2.Stream.eval(
          GitHub.listPullRequests(
            org,
            repo,
            token,
            bot,
            blazeClient
          )
        )
        _ <- fs2.Stream
          .emits[IO, PullRequest](prUrls)
          .foreach(pr =>
            cats.effect.std
              .Console[IO]
              .println(
                s"${org} / ${repo} / ${pr.title}: ${pr.html_url} (${lag(pr)})"
              )
          )
      } yield ()).compile.drain.as(ExitCode.Success)
    }
}
