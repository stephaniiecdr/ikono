plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.edu.pradita.pos"
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


javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
    implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:4.0.1")
    implementation("org.hibernate.common:hibernate-commons-annotations:5.1.2.Final")
    implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("net.bytebuddy:byte-buddy:1.14.12")
    implementation("com.fasterxml:classmate:1.5.1")
// Versi terbaru yang stabil
    implementation("org.jboss.logging:jboss-logging:3.5.3.Final")
// Versi terbaru yang stabil
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
application {
    mainClass = "org.edu.pradita.pos.tax.Main"
    mainModule = "org.edu.pradita.pos.tax"
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
