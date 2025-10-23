plugins {
    java
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "online.demonzdevelopment"
version = "1.0.0"
description = "Professional multi-currency teleportation plugin with smart RTP"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()

    // PaperMC Repository
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }

    // PlaceholderAPI Repository
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") {
        name = "placeholderapi"
    }

    // EngineHub Repository (WorldEdit, WorldGuard)
    maven("https://maven.enginehub.org/repo/") {
        name = "enginehub"
    }
}

dependencies {
    // Paper API (PaperMC 1.21.1)
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    // LuckPerms API (from Maven Central)
    compileOnly("net.luckperms:api:5.4")

    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.6")

    // WorldEdit API
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.0")

    // WorldGuard API
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9")

    // HikariCP for MySQL connection pooling (shaded into plugin)
    implementation("com.zaxxer:HikariCP:5.1.0")

    // SQLite JDBC (shaded into plugin)
    implementation("org.xerial:sqlite-jdbc:3.45.0.0")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    processResources {
        filteringCharset = "UTF-8"

        filesMatching("plugin.yml") {
            expand(
                "version" to project.version,
                "group" to project.group,
                "description" to project.description
            )
        }
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("${project.name}-${project.version}.jar")

        // Relocate dependencies to avoid conflicts
        relocate("com.zaxxer.hikari", "online.demonzdevelopment.dzqtp.libs.hikari")
        relocate("org.sqlite", "online.demonzdevelopment.dzqtp.libs.sqlite")

        // Exclude unnecessary files
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")

        // Minimize jar size
        minimize()
    }

    build {
        dependsOn(shadowJar)
    }

    jar {
        enabled = false
    }
}

// Configure Java compilation
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:deprecation")
    options.compilerArgs.add("-Xlint:unchecked")
}
