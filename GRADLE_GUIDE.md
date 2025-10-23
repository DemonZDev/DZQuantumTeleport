# Gradle Build Guide for DZQuantumTeleport

## Quick Start

### Prerequisites
- Java 21 or higher installed
- No need to install Gradle (uses Gradle Wrapper)

### Build the Plugin

**Linux/Mac:**
```bash
./gradlew clean shadowJar
```

**Windows:**
```cmd
gradlew.bat clean shadowJar
```

**Output:** `build/libs/DZQuantumTeleport-1.0.0.jar`

---

## Common Gradle Commands

### Clean Build Directory
```bash
./gradlew clean
```
Removes all compiled files and build artifacts.

### Compile Java Sources
```bash
./gradlew compileJava
```
Compiles all Java source files without creating JARs.

### Process Resources
```bash
./gradlew processResources
```
Copies and processes resource files (plugin.yml, config files).

### Create Shadow JAR
```bash
./gradlew shadowJar
```
Creates a fat JAR with all dependencies included and relocated.

### Full Build
```bash
./gradlew build
```
Runs all tasks: clean, compile, test, and shadowJar.

### List All Tasks
```bash
./gradlew tasks
```
Shows all available Gradle tasks.

### Dependencies
```bash
./gradlew dependencies
```
Shows the dependency tree for the project.

---

## Development Workflow

### 1. Make Code Changes
Edit Java files in `src/main/java/`

### 2. Test Compilation
```bash
./gradlew compileJava
```
Quickly check if your code compiles.

### 3. Create Test Build
```bash
./gradlew shadowJar
```
Create a JAR file for testing.

### 4. Full Build with Checks
```bash
./gradlew clean build
```
Clean build with all validation.

---

## IDE Integration

### IntelliJ IDEA
1. Open IntelliJ IDEA
2. File → Open → Select project folder
3. IDEA will automatically detect `build.gradle.kts`
4. Wait for Gradle sync to complete

### Eclipse
1. Install Buildship Gradle plugin
2. File → Import → Gradle → Existing Gradle Project
3. Select project folder
4. Wait for sync to complete

### VS Code
1. Install "Java Extension Pack"
2. Install "Gradle for Java"
3. Open project folder
4. VS Code will detect Gradle automatically

---

## Dependency Management

### Adding a New Dependency

Edit `build.gradle.kts`:

```kotlin
dependencies {
    // Add new dependency
    compileOnly("group:artifact:version")
}
```

**compileOnly**: For APIs provided by server (Paper, LuckPerms, etc.)
**implementation**: For libraries to include in JAR

### Relocating Dependencies

To avoid conflicts with other plugins:

```kotlin
shadowJar {
    relocate("original.package", "your.plugin.libs.package")
}
```

Example:
```kotlin
relocate("com.zaxxer.hikari", "online.demonzdevelopment.dzqtp.libs.hikari")
```

---

## Troubleshooting

### Problem: Gradle wrapper not executable
**Solution:**
```bash
chmod +x gradlew
```

### Problem: Java version mismatch
**Solution:**
Set JAVA_HOME to Java 21:
```bash
export JAVA_HOME=/path/to/java21
./gradlew build
```

### Problem: Dependency not found
**Solution:**
1. Check repository configuration in `build.gradle.kts`
2. Verify dependency coordinates (group:artifact:version)
3. Run `./gradlew dependencies` to see what's resolved

### Problem: Build cache issues
**Solution:**
```bash
./gradlew clean build --refresh-dependencies
```

### Problem: Out of memory
**Solution:**
Create `gradle.properties` with:
```properties
org.gradle.jvmargs=-Xmx2048m
```

---

## Advanced Configuration

### Change Output JAR Name

Edit `build.gradle.kts`:
```kotlin
shadowJar {
    archiveFileName.set("CustomName-${project.version}.jar")
}
```

### Add Custom Task

```kotlin
tasks.register("customTask") {
    doLast {
        println("Running custom task!")
    }
}
```

Run with: `./gradlew customTask`

### Parallel Builds

```bash
./gradlew build --parallel
```

### Build Scan

```bash
./gradlew build --scan
```
Creates detailed build report (requires Gradle.com account).

---

## Gradle vs Maven Comparison

| Feature | Maven | Gradle |
|---------|-------|--------|
| Build File | pom.xml | build.gradle.kts |
| Language | XML | Kotlin DSL |
| Clean | mvn clean | ./gradlew clean |
| Compile | mvn compile | ./gradlew compileJava |
| Package | mvn package | ./gradlew shadowJar |
| Build | mvn clean install | ./gradlew clean build |
| Dependencies | mvn dependency:tree | ./gradlew dependencies |

---

## Continuous Integration

### GitHub Actions

`.github/workflows/build.yml`:
```yaml
name: Build Plugin

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    - name: Build with Gradle
      run: ./gradlew build
    - name: Upload artifact
      uses: actions/upload-artifact@v3
      with:
        name: DZQuantumTeleport
        path: build/libs/*.jar
```

### Jenkins

```groovy
pipeline {
    agent any
    tools {
        jdk 'Java 21'
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew clean shadowJar'
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'build/libs/*.jar'
            }
        }
    }
}
```

---

## Performance Tips

### 1. Use Gradle Daemon (Default)
Gradle daemon speeds up builds by keeping JVM running.

### 2. Enable Parallel Execution
```bash
./gradlew build --parallel
```

### 3. Use Build Cache
```bash
./gradlew build --build-cache
```

### 4. Offline Mode
If dependencies are cached:
```bash
./gradlew build --offline
```

---

## Gradle Wrapper Update

To update Gradle version:

```bash
./gradlew wrapper --gradle-version 8.6
```

Or edit `gradle/wrapper/gradle-wrapper.properties`:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.6-bin.zip
```

---

## Project-Specific Notes

### Shadow JAR Configuration
This project uses Shadow plugin to:
- Include HikariCP (database connection pooling)
- Include SQLite JDBC (database driver)
- Relocate packages to avoid conflicts
- Minimize JAR size

### Resource Processing
- `plugin.yml` has version substituted automatically
- Other YAML files copied as-is to preserve placeholders

### Java 21 Requirement
- This plugin requires Java 21 due to Paper API 1.21.1
- Gradle toolchain ensures correct Java version is used

---

## Getting Help

### Gradle Documentation
- Official Docs: https://docs.gradle.org/current/userguide/userguide.html
- Shadow Plugin: https://github.com/GradleUp/shadow

### Project Issues
- Check `BUILD_SUCCESS.md` for build status
- Review `IMPLEMENTATION.md` for project architecture
- Read `README.md` for plugin features

---

## Summary

**Build Command:**
```bash
./gradlew clean shadowJar
```

**Output:**
```
build/libs/DZQuantumTeleport-1.0.0.jar
```

**Deploy:**
Copy JAR to PaperMC server's `plugins/` folder

---

**Last Updated**: 2025-10-23
**Gradle Version**: 8.5
**Java Version**: 21
**Build Time**: ~6 seconds (after first run)
