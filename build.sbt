val GlobalSettingsGroup: Seq[Setting[_]] = Seq(
  githubOwner := "djspiewak",
  githubRepository := "packages-test",
  credentials +=
    Credentials(
      "GitHub Package Registry",
      "maven.pkg.github.com",
      sys.env.get("GITHUB_ACTOR").getOrElse("N/A"),
      sys.env.getOrElse("GITHUB_TOKEN", "N/A")
    ))

lazy val common = project
  .in(file("lib/common"))
  .settings(GlobalSettingsGroup)

lazy val `sdk-a` = project
  .in(file("lib/sdk-a"))
  .settings(GlobalSettingsGroup)
  .dependsOn(common)

lazy val `sdk-b` = project
  .in(file("lib/sdk-b"))
  .settings(GlobalSettingsGroup)
  .dependsOn(common)

lazy val `domain-api-a` = project
  .in(file("domain/domain-api-a"))
  .settings(GlobalSettingsGroup)
  .dependsOn(`sdk-a`)

lazy val `domain-api-b` = project
  .in(file("domain/domain-api-b"))
  .settings(GlobalSettingsGroup)
  .dependsOn(`sdk-a`, `sdk-b`)

lazy val domainApis: Seq[ProjectReference] = Seq(
  `domain-api-a`,
  `domain-api-b`)

lazy val libs: Seq[ProjectReference] = Seq(
  `common`,
  `sdk-a`,
  `sdk-b`)

lazy val all = domainApis ++ libs

lazy val root = project
  .in(file("."))
  .aggregate(all: _*)
  .settings(
    name := "foundation-backend")
  .settings(GlobalSettingsGroup)
