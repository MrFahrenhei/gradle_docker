plugins {
    id("java")
}

group = "com.estudo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.jar{
    manifest{
        attributes ("Main-Class" to "DatabaseConnector")
    }
}

dependencies {
    implementation("org.postgresql:postgresql:42.7.13")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}