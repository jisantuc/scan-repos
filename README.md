# scan-repos

An application for monitoring repos kept up-to-date by scala-steward for open PRs.

A common workflow with `scala-steward` is to have some kind of automatic merge rule, either with [mergify] (like [here](https://notthatbreezy.io/2020/02/refined-examples/)) or with GitHub actions, like in [47 Degrees open source repos](https://github.com/47degrees/github4s/blob/v0.31.0/.github/workflows/ci.yml#L49-L54).

That works great when the upgrade PRs don't cause any problems. It works _dramatically less great_ when you have a lot of repositories managed this way, and sometimes dependency upgrades fail.

The application here attempts to address the problem by scanning a list of repos for open PRs opened by a specified bot user. You can use it like so:

```
Usage: scan-repos [--bot-name <string>] <REPOS_FILE>

Scan for repositories with open bot pull requests

Options and flags:
    --help
        Display this help text.
    --version, -v
        Print the version number and exit.
    --bot-name <string>, -b <string>
        Login name of the bot account that opens dependency upgrade PRs

Environment Variables:
    GITHUB_TOKEN=<string>
        Token to use to authenticate calls to GitHub
```

Note that because this isn't packaged as an executable or jar or anything, you'll invoke it as `sbt 'run [args]'` isntead of `scan-repos [args]`. For example, if your repo list was in a file called `repos`, your command would be `sbt 'run repos'`.

Your repos file should be a line-separated list of repositories like:

```
47degrees/github4s
47degrees/sbt-microsites
...
```

If your bot user isn't `47erbot`, you can specify a different bot user with the `--bot-name` / `-b` option.