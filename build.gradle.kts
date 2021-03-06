import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.changelog.closure
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("jacoco")

    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "0.6.5"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "0.6.2"
    // detekt linter - read more: https://detekt.github.io/detekt/gradle.html
    id("io.gitlab.arturbosch.detekt") version "1.14.2"
    // ktlint linter - read more: https://github.com/JLLeitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

// Import variables from gradle.properties file
val pluginGroup: String by project
// `pluginName_` variable ends with `_` because of the collision with Kotlin magic getter in the `intellij` closure.
// Read more about the issue: https://github.com/JetBrains/intellij-platform-plugin-template/issues/29
val pluginName_: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project
val pluginVerifierIdeVersions: String by project

val platformType: String by project
val platformVersion: String by project
val platLocalPath: String by project
val platformPlugins: String by project
val platformDownloadSources: String by project

val javaVersion: String by project
val pluginHomepage: String by project

group = pluginGroup
version = pluginVersion

// Configure project's dependencies
buildscript {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter/") }
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/google/") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/spring") }
        maven { setUrl("https://maven.aliyun.com/repository/spring-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/grails-core") }
        maven { setUrl("https://maven.aliyun.com/repository/apache-snapshots") }
        mavenCentral()
        jcenter()
    }
}
allprojects {
    repositories {
        maven { setUrl("https://maven.aliyun.com/repository/central") }
        maven { setUrl("https://maven.aliyun.com/repository/jcenter/") }
        maven { setUrl("https://maven.aliyun.com/repository/public/") }
        maven { setUrl("https://maven.aliyun.com/repository/google/") }
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/spring") }
        maven { setUrl("https://maven.aliyun.com/repository/spring-plugin") }
        maven { setUrl("https://maven.aliyun.com/repository/grails-core") }
        maven { setUrl("https://maven.aliyun.com/repository/apache-snapshots") }
        mavenCentral()
        jcenter()
    }
}
dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.14.2")

    compileOnly("org.projectlombok:lombok:1.18.16")
    annotationProcessor("org.projectlombok:lombok:1.18.16")
    implementation("cglib:cglib:3.3.0")
}

// Configure gradle-intellij-plugin plugin.
// Read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName = pluginName_
    version = platformVersion
//    localPath = platLocalPath
    type = platformType
    downloadSources = platformDownloadSources.toBoolean()
    updateSinceUntilBuild = true

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    setPlugins(*platformPlugins.split(',').map(String::trim).filter(String::isNotEmpty).toTypedArray())
}

// Configure detekt plugin.
// Read more: https://detekt.github.io/detekt/kotlindsl.html
detekt {
    config = files("./detekt-config.yml")
    buildUponDefaultConfig = true

    reports {
        html.enabled = false
        xml.enabled = false
        txt.enabled = false
    }
}

tasks {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    listOf("compileKotlin", "compileTestKotlin").forEach {
        getByName<KotlinCompile>(it) {
            kotlinOptions.jvmTarget = javaVersion
        }
    }

    withType<Detekt> {
        jvmTarget = javaVersion
    }

    patchPluginXml {
        version(pluginVersion)
        sinceBuild(pluginSinceBuild)
        untilBuild(pluginUntilBuild)

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription(
            closure {
                (
                    "<p><strong>Setting Navigation for IntelliJ IDEA.</strong></p>" +
                        "<p><b><a href='$pluginHomepage'>GitHub</a> | " +
                        "<a href='$pluginHomepage/issues'>Issues</a></b></p>" +
                        File("./README.md").readText().lines().run {
                            val start = "<!-- Plugin description -->"
                            val end = "<!-- Plugin description end -->"

                            if (!containsAll(listOf(start, end))) {
                                throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                            }
                            subList(indexOf(start) + 1, indexOf(end))
                        }.joinToString("\n").run { markdownToHTML(this) }
                    ).replace(Regex("(?<!:)</p>"), "</p><br />")
            }
        )

        // Get the latest available change notes from the changelog file
        changeNotes(
            closure {
                changelog.getLatest().toHTML() +
                    "<br /><p>See also the complete <a href='$pluginHomepage/blob/main/CHANGELOG.md'>changelog</a>.</p>"
            }
        )
    }

    runPluginVerifier {
        ideVersions(pluginVerifierIdeVersions)
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://jetbrains.org/intellij/sdk/docs/tutorials/build_system/deployment.html#specifying-a-release-channel
        channels(pluginVersion.split('-').getOrElse(1) { "default" }.split('.').first())
    }

    /*
    test {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    jacocoTestReport {
        reports {
            html.isEnabled = true
            csv.isEnabled = false
            xml.isEnabled = true
            xml.destination = file("$buildDir/jacocoXml.xml")
            html.destination = file("$buildDir/jacocoHtml")
        }
    }
    */

//    buildSearchableOptions {
//        setJbrVersion("JBR-11.0.7.10-765.65-jfx")
//    }
}

sourceSets {
    main {
        java {
            srcDirs("/src/main/java")
            // exclude unused sources
            exclude("kotlin", "demo")
        }
        resources {
            srcDirs("/src/main/resources")
        }
    }
    test {
        java {
            srcDirs("/src/test/java")
        }
        resources {
            srcDirs("/src/test/resources")
        }
    }
}
