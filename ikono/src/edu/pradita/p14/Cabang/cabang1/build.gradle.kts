plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.edu.pradita.cabang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"
val javafxVersion = "21.0.6"
val mockitoVersion = "5.11.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.edu.pradita.cabang.cabangs")
    mainClass.set("org.edu.pradita.cabang.cabangs.MainApp")
}

javafx {
    version = "21.0.6"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics", "javafx.base")
}
val jacksonVersion = "2.17.1"
dependencies {
        implementation("org.controlsfx:controlsfx:11.2.1")
        implementation("org.openjfx:javafx-controls:${javafxVersion}")
        runtimeOnly("com.h2database:h2:2.2.224")

        implementation("org.openjfx:javafx-fxml:${javafxVersion}")
        implementation("org.openjfx:javafx-graphics:${javafxVersion}")
        implementation("org.openjfx:javafx-base:${javafxVersion}")

        implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
        implementation("jakarta.transaction:jakarta.transaction-api:2.0.1")
        implementation("jakarta.interceptor:jakarta.interceptor-api:2.1.0")
        implementation("jakarta.enterprise:jakarta.enterprise.cdi-api:4.1.0")
        implementation("com.fasterxml:classmate:1.5.1")

        implementation("org.jboss.logging:jboss-logging:3.5.3.Final")
        implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")

        implementation("org.glassfish.jaxb:jaxb-runtime:4.0.5")
        implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")
        testImplementation(platform("org.junit:junit-bom:${junitVersion}"))

        implementation("org.hibernate.common:hibernate-commons-annotations:5.1.2.Final")
        implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
        implementation("com.mysql:mysql-connector-j:8.2.0")
        implementation("net.bytebuddy:byte-buddy:1.14.12")

        testImplementation(platform("org.junit:junit-bom:${junitVersion}"))
        testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")


        implementation("org.slf4j:slf4j-api:2.0.12")
        implementation("org.slf4j:slf4j-simple:2.0.12")

        testImplementation("org.mockito:mockito-core:${mockitoVersion}")
        testImplementation("org.mockito:mockito-junit-jupiter:${mockitoVersion}")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    jlink {
        imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
        options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
        launcher {
            name = "appCabang"

        }
    }


