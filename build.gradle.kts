import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.7.10"
}

subprojects {

    group = "com.mgumieniak"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "kotlin")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.springframework.boot")

    repositories {
        mavenCentral()
    }

    dependencies {
        // BOM
        implementation(platform("org.springframework.boot:spring-boot-dependencies:2.7.2"))
        implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2021.0.3"))
        implementation(platform("software.amazon.awssdk:bom:2.17.243"))

        //aws
        implementation("software.amazon.awssdk:iam")
        implementation("software.amazon.awssdk:s3")
        implementation("software.amazon.awssdk:sqs")
        implementation("software.amazon.awssdk:sts")

        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

        testImplementation("org.assertj:assertj-core:3.23.1")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.projectreactor:reactor-test")
    }

    allOpen{
        annotation("org.springframework.context.annotation.Configuration")
        annotation("org.springframework.web.bind.annotation.RestController")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.register("prepareKotlinBuildScriptModel"){}

}

repositories {
    mavenCentral()
}
