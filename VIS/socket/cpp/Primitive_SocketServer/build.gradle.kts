import org.apache.tools.ant.taskdefs.condition.Os

plugins {
   id("cpp-application")
}

// task to run cpp program
tasks.register("run", Exec::class) {
    dependsOn("build")         // make sure project has been built
    group = "application"                // set task group
    standardInput = System.`in`    // enable commandline input
    val bDir: String = layout.buildDirectory.get().toString()
    val exeDir:  String = "${bDir}/exe/main/debug/"
    var exeFile: String? = null
    when {
        Os.isFamily(Os.FAMILY_WINDOWS) -> {
            exeFile = "Primitive_SocketServer.exe"
			commandLine("cmd", "/k", exeDir+exeFile) // start command in cmd shell
        }
        Os.isFamily(Os.FAMILY_MAC) -> {
            exeFile = "Primitive_SocketServer"
			commandLine("bash", "-c", exeDir+exeFile) // start command in cmd shell
        }
        Os.isFamily(Os.FAMILY_UNIX) -> {
            exeFile = "Primitive_SocketServer"
			commandLine("bash", "-c", exeDir+exeFile) // start command in cmd shell
        }
        else -> { throw GradleException(":cpp:HelloWorld run-target -> unknown OS family encountered")}
    }
}


// task to run cpp program
tasks.register("kill", Exec::class) {
    group = "application"                // set task group
    val port = 4949
    when {
        Os.isFamily(Os.FAMILY_WINDOWS) -> {
            commandLine("cmd", "/k", "for /f \"tokens=5\" %a in ('netstat -aon ^| findstr ${port}') do taskkill /F /PID %a")
        }
        Os.isFamily(Os.FAMILY_MAC) -> {
            commandLine("bash", "-c", "lsof -nti:${port} | xargs kill -9")
        }
        Os.isFamily(Os.FAMILY_UNIX) -> {
            commandLine("bash", "-c", "lsof -nti:${port} | xargs kill -9")
            // commandLine("sh", "-c", "kill \"\$(lsof -t -i:$port)\"") // possible alternative
        }
        else -> { throw GradleException(":cpp:HelloWorld kill-target -> unknown OS family encountered")}
    }
}
