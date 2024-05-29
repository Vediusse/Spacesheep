plugins {
    kotlin("multiplatform") version "1.9.0"
    id("java")
    id("org.openjfx.javafxplugin") version "0.0.14"
}

tasks.withType<JavaCompile> {
    options.encoding = "utf-8"
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.landgrafhomyak.ru/")
        maven("https://mvnrepository.com/artifact/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/") // Репозиторий для JFoenix
    }
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
        compilations.all { kotlinOptions.jvmTarget = "17" }

        tasks.register<Jar>("fatJar") {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            group = "build"
            archiveBaseName.set("proglab7client")
            manifest {
                attributes["Main-Class"] = "io.github.zeculesu.itmo.prog5.Main"
            }
            from(
                    compilations["main"].runtimeDependencyFiles
                            .filter { it.exists() }
                            .map { if (it.isDirectory) it else zipTree(it) }
            )
            with(tasks.jar.get() as CopySpec)
            destinationDirectory.set(rootProject.projectDir.resolve("out"))
        }
    }

    js {
        browser()
        nodejs()
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val jvmMain by getting {
            dependencies {
                implementation("org.postgresql:postgresql:42.7.3")
                implementation("ru.landgrafhomyak.utility:java-resource-loader:2.0")

                // Add JavaFX dependencies
                val javafxVersion = "17"
                implementation("org.openjfx:javafx-base:$javafxVersion")
                implementation("org.openjfx:javafx-controls:$javafxVersion")
                implementation("org.openjfx:javafx-fxml:$javafxVersion")
                implementation("org.openjfx:javafx-graphics:$javafxVersion")
                // https://mvnrepository.com/artifact/de.saxsys/svgfx
                // https://mvnrepository.com/artifact/de.codecentric.centerdevice/javafxsvg
                implementation("de.codecentric.centerdevice:javafxsvg:1.3.0")

            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

javafx {
    version = "17.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

// Register the run task after project evaluation
// Register the run task after project evaluation
afterEvaluate {
    tasks.register<JavaExec>("jvmRun") {
        group = "application"
        mainClass.set("io.github.zeculesu.itmo.prog5.MainClient")
        classpath = project.tasks.getByName("fatJar").outputs.files
    }
}

