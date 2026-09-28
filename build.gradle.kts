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
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("runAllTests") {
    group = "verification"
    description = "Запускает все тесты в проекте."
    dependsOn(tasks.test)
}

tasks.register("testRunIsOver") {
    group = "verification"
    description = "Пишет в консоль сообщение после прогона тестов."
    dependsOn("runAllTests")
    doLast {
        println("Test run is over")
    }
}
