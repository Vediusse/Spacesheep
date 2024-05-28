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
                attributes["Main-Class"] = "io.github.zeculesu.itmo.prog5.MainClient"
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
    js()
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
