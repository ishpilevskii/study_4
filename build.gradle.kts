plugins {
    id("java")
}

group = "inno.tech.study"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:3.27.7")
    implementation("io.rest-assured:rest-assured:6.2.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("runAllTests") {
    group = "verification"
    description = "Запускает автотесты с фильтрацией по тегу base-java и пакету inno.tech.study."
    useJUnitPlatform {
        includeTags("base-java")
    }
    filter {
        includeTestsMatching("inno.tech.study.*")
    }
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
}

tasks.register("testRunIsOver") {
    group = "verification"
    description = "Пишет в консоль сообщение после прогона тестов."
    dependsOn("runAllTests")
    doLast {
        println("Test run is over")
    }
}

tasks.register<Test>("runApiTests") {
    group = "verification"
    description = "Запускает все API-тесты (тег api)."
    useJUnitPlatform {
        includeTags("api")
    }
    testClassesDirs = sourceSets["test"].output.classesDirs
    classpath = sourceSets["test"].runtimeClasspath
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }
}
