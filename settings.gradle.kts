// bmc4j consumer test: consumes bmc4j PURELY BY COORDINATES — no includeBuild,
// no project substitution. This repo exists to prove the published artifacts
// install and work the way a stranger's build would experience them.
//
// Repositories: GitHub Packages (the pre-Central channel; needs SOME authenticated
// token with read:packages even for public packages — in CI the workflow's own
// GITHUB_TOKEN, locally e.g. `gh auth token`; see README) with mavenLocal first so
// local development against a `publishToMavenLocal` of the main repo also works.
pluginManagement {
    // The version under test, REQUIRED via -PbmcVersion=… . Deliberately no
    // default: this harness exists to prove a SPECIFIC version, and a dropped or
    // typo'd property must fail loudly here, not silently green-light the release
    // line while claiming to have tested a snapshot. Lives here because the
    // project-level plugins {} block only accepts constant versions.
    plugins {
        id("org.bmc4j") version (providers.gradleProperty("bmcVersion").orNull
                ?: throw GradleException(
                        "Pass -PbmcVersion=<version under test> (e.g. -PbmcVersion=0.1.2 or a"
                                + " tag-shortsha snapshot). There is no default: a silently-substituted"
                                + " version would defeat the point of this harness."))
    }
    repositories {
        mavenLocal()
        maven {
            name = "Bmc4jGitHubPackages"
            url = uri("https://maven.pkg.github.com/bmc4j/bmc4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                    ?: providers.gradleProperty("gpr.user").orNull
                password = System.getenv("GITHUB_TOKEN")
                    ?: providers.gradleProperty("gpr.token").orNull
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        maven {
            name = "Bmc4jGitHubPackages"
            url = uri("https://maven.pkg.github.com/bmc4j/bmc4j")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                    ?: providers.gradleProperty("gpr.user").orNull
                password = System.getenv("GITHUB_TOKEN")
                    ?: providers.gradleProperty("gpr.token").orNull
            }
        }
        mavenCentral()
    }
}

rootProject.name = "bmc4j-consumer-test"
