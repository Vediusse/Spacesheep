plugins {
    kotlin("multiplatform") version "1.9.0"
    java
}
tasks.withType<JavaCompile>{
    options.encoding="utf-8"
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
        compilations.all{kotlinOptions.jvmTarget="17"}
        tasks.register<Jar>("fatJar"){
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            group = "build"
            this.archiveBaseName = "proglab7client"
            manifest{
                attributes["Main-Class"] = "io.github.zeculesu.itmo.prog5.MainClient"
            }
            from(
                compilations["main"].runtimeDependencyFiles
                    .filter {f -> f.exists() }
                    .map{ d -> if (d.isDirectory) d else zipTree(d)}
            )
            with(tasks.jar.get() as CopySpec)
            destinationDirectory = rootProject.projectDir.resolve("out")
        }
    }
    js()
    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val jvmMain by getting{
            dependencies{
                implementation("org.postgresql:postgresql:42.7.3")
                implementation("ru.landgrafhomyak.utility:java-resource-loader:2.0")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}