plugins {
    war
}
repositories {
    mavenCentral()
}
dependencies {
    // https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
}
val nameWar: String = "PrimitiveServlet"
tasks.war {
    destinationDirectory.set(file("./webapp"))
    archiveBaseName.set(nameWar)
    from("webapp/index.html") // only integrate if index.html should be provided/used
    doLast {
        copy {
            println("*** copying war to root webapps folder ... ")
            val fromS: String = "${project.projectDir.absolutePath}\\webapp\\$nameWar.war"
            val intoS: String = "${rootProject.projectDir.absolutePath}\\webapps"
            from(fromS)
            into(intoS)
        }
    }
}
tasks.clean {
    val fN_a: String = "${project.projectDir.absolutePath}\\webapp\\$nameWar.war"
    val fN_b: String = "${rootProject.projectDir.absolutePath}\\webapps\\$nameWar.war"
    delete(files(fN_a))
    delete(files(fN_b))
}
// exclude duplicates while integrating libraries through dependencies
tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
