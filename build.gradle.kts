import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("java")
}

group = "com.yonkwon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/us.hebi.matlab.mat/mfl-core
    implementation("us.hebi.matlab.mat:mfl-core:0.5.15")

    // https://mvnrepository.com/artifact/org.apache.commons/commons-math3
    implementation("org.apache.commons:commons-math3:3.6.1")

    // https://mvnrepository.com/artifact/org.uncommons/uncommons-maths
    implementation("org.uncommons:uncommons-maths:1.2")

    // https://mvnrepository.com/artifact/gov.nist.math/jama
    implementation("gov.nist.math:jama:1.0.3")

    // https://mvnrepository.com/artifact/com.google.guava/guava
    implementation("com.google.guava:guava:33.4.8-jre")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"))

    archiveFileName.set("ks-$timestamp.jar")

    manifest {
        attributes["Main-Class"] = "KSFinal.Main"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}