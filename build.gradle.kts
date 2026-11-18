import com.epages.restdocs.apispec.gradle.PostmanTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val authToken = providers.gradleProperty("authToken")

plugins {
    id("org.asciidoctor.jvm.convert") version "4.0.5"
    id("org.springframework.boot") version "4.1.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.jetbrains.kotlin.jvm") version "2.4.21"
    id("org.jetbrains.kotlin.plugin.spring") version "2.4.21"
    id("com.adarshr.test-logger") version "4.0.0"
    id("org.kordamp.gradle.livereload") version "0.4.0"
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("com.epages.restdocs-api-spec") version "0.20.2"
}

group = "com.boclips"
version = "1"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
        credentials { username = authToken.get() }
    }
}

val snippetsDir = layout.buildDirectory.dir("generated-snippets")
val asciidoctorExt = configurations.register("asciidoctorExt")

ktlint {
    version = "1.8.0"
}

// The dependency-management plugin forces the Kotlin plugin's version onto every configuration, and ktlint
// can fail to parse with a newer compiler than it was built against ("Extensions storage is not
// registered"). Give ktlint the Kotlin versions it asks for.
configurations.matching { it.name.startsWith("ktlint") }.configureEach {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") requested.version?.let(::useVersion)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("com.github.kittinunf.fuel:fuel:2.2.1")
    implementation("com.github.kittinunf.fuel:fuel-jackson:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:2.2.1")

    implementation("org.imsglobal:basiclti-util:1.1.2")

    asciidoctorExt("org.springframework.restdocs:spring-restdocs-asciidoctor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // spring-restdocs-restassured 3.0.6 brings REST Assured 5, which does not run on the Groovy 5 that Spring Boot 4 manages.
    testImplementation(platform("io.rest-assured:rest-assured-bom:6.3.0"))
    // The last released REST Assured modules: REST Docs 4 has only milestones and restdocs-api-spec 0.20 has none.
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured:3.0.6")
    testImplementation("com.epages:restdocs-api-spec:0.20.2")
    testImplementation("com.epages:restdocs-api-spec-restassured:0.19.4")

    testImplementation("com.github.boclips:videos:v2456")
    // The videos client brings feign at runtime only; the tests build its clients directly.
    testImplementation(platform("io.github.openfeign:feign-bom:13.6.1"))
    testImplementation("io.github.openfeign:feign-okhttp")
    testImplementation("com.damnhandy:handy-uri-templates:2.1.8")
    testImplementation("org.springframework.boot:spring-boot-starter-hateoas")
    testImplementation("org.springframework.hateoas:spring-hateoas")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_21
    }
}

tasks.test {
    useJUnitPlatform()
    outputs.dir(snippetsDir)
}

asciidoctorj {
    setVersion("3.0.1")
}

tasks.asciidoctor {
    dependsOn(tasks.test)
    configurations(asciidoctorExt.name)
    attributes(mapOf("snippets" to snippetsDir.get().asFile))
    outputDirProperty = layout.buildDirectory.dir("asciidoc/html5")
    resources {
        from("src/docs/asciidoc/resources")
        into("resources")
    }
    baseDirFollowsSourceFile()
}

postman {
    baseUrl = "https://api.boclips.com"
    title = "Boclips API"
    outputDirectory =
        layout.buildDirectory
            .dir("postman")
            .get()
            .asFile.path
}

openapi3 {
    setServer("https://api.boclips.com")
    title = "Boclips API"
    format = "yaml"
}

tasks.bootJar {
    from(tasks.asciidoctor) { into("BOOT-INF/classes/static/docs") }
    from(tasks.named<PostmanTask>("postman").map { it.outputDirectory }) { into("BOOT-INF/classes/static/docs/postman") }
}

tasks.liveReload {
    port = 8080
    docRoot = tasks.asciidoctor.map { it.outputDir.path }
}
