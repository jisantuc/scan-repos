package io.jisantuc

import cats.effect.IO
import cats.effect.IOApp
import cats.effect.Resource
import cats.effect.unsafe.implicits.global
import cats.syntax.eq._
import github4s.GHResponse
import github4s.GithubConfig
import github4s.domain._
import github4s.http.HttpClient
import github4s.interpreters.PullRequestsInterpreter
import github4s.interpreters.StaticAccessToken
import org.http4s.client.Client

object GitHub {
  def listPullRequests(
      owner: String,
      repo: String,
      token: String,
      botUser: String,
      client: Client[IO]
  ): IO[List[PullRequest]] =
    implicit val githubClient: HttpClient[IO] = new HttpClient[IO](
      client,
      GithubConfig.default,
      new StaticAccessToken(Some(token))
    )
    val interpreter = new PullRequestsInterpreter[IO]
    interpreter.listPullRequests(
      owner,
      repo,
      filters = List(
        PRFilterOpen
      )
    ) map {
      case GHResponse(Right(prList), _, _) =>
        prList.filter({ _.user.map(_.login) === Some(botUser) })
      case resp =>
        Nil
    }
}
