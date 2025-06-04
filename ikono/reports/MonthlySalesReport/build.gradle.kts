plugins {
    id ("java")
    id ("application")
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.edu.pradita"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        url = uri("https://mvnrepository.com/artifact/com.fasterxml/classmate")
    }
    maven {
        url = uri("https://mvnrepository.com/artifact/org.hibernate.common/hibernate-commons-annotations")
    }
    mavenLocal()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.edu.pradita.main")
    mainClass.set("org.edu.pradita.main.user.PenjualanBulananApp")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}

dependencies {
    implementation("org.openjfx:javafx-controls:17.0.2")

    implementation("org.openjfx:javafx-fxml:17.0.2")

    implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")
    implementation("org.hibernate.common:hibernate-commons-annotations:5.1.2.Final")

    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    runtimeOnly("org.glassfish.jaxb:jaxb-runtime:4.0.5")

    implementation("com.fasterxml:classmate:1.7.0")

    implementation("mysql:mysql-connector-java:8.0.33")

    implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:4.0.1")

    implementation("org.jboss.logging:jboss-logging:3.5.3.Final")

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.9")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}