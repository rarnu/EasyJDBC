package com.rarnu.easyjdbc

import com.rarnu.common.Resource
import com.rarnu.common.replaceTag
import java.io.File

object CodeGenerator {
    fun generate(fields: List<String>, callback: () -> Unit) {
        val proj = fields[0]
        val prefix = fields[1]
        val pkg = fields[2]
        val jdbc = fields[3]
        val port = fields[4]
        val major = fields[5]
        val minor = fields[6]
        val driverName = fields[7]
        val outPath = fields[8]
        val output = File(outPath).apply { mkdirs() }
        generateGradleFiles(proj, output)

        val outSrc = File(output, "src/main/kotlin/${pkg.replace(".", "/")}").apply { mkdirs() }
        generateConstsFile(pkg, prefix, jdbc, port, major, minor, driverName, outSrc)
        generateDriverFiles(pkg, prefix, outSrc)
        val outTest = File(output, "src/test/kotlin").apply { mkdirs() }
        generateTestFile(pkg, prefix, jdbc, outTest)
        callback()
    }


    private fun generateGradleFiles(proj: String, output: File) {
        File(output, "build.gradle").writeText(Resource.read("build.gradle.tmp"))
        File(output, "gradle.properties").writeText(Resource.read("gradle.properties.tmp"))
        File(output, "settings.gradle").writeText(Resource.read("settings.gradle.tmp")
            .replaceTag("{{project}}") { proj })
    }

    private fun generateConstsFile(pkg: String, prefix: String, jdbc: String, port: String, major: String, minor: String, driverName: String, output: File) {
        File(output, "${prefix}Constants.kt").writeText(Resource.read("Constants.kt.tmp")
            .replaceTag("{{package}}") { pkg }
            .replaceTag("{{jdbc}}") { jdbc }
            .replaceTag("{{major}}") { major }
            .replaceTag("{{minor}}") { minor }
            .replaceTag("{{port}}") { port }
            .replaceTag("{{driverName}}") { driverName }
        )
    }

    private fun generateDriverFiles(pkg: String, prefix: String, output: File) {
        val outMeta = File(output, "metadata").apply { mkdirs() }
        val outResult = File(output, "resultset").apply { mkdirs() }
        val outStatement = File(output, "statement").apply { mkdirs() }
        val outTest = File(output, "test").apply { mkdirs() }
        val outUtil = File(output, "util").apply { mkdirs() }

        mapOf(
            "Driver" to output, "Connection" to output, "IO" to output,
            "DatabaseMetaData" to outMeta, "ResultSetMetaData" to outMeta,
            "ResultSet" to outResult,
            "AbsStatement" to outStatement, "PreparedStatement" to outStatement, "Statement" to outStatement,
            "TestRequset" to outTest,
            "DriverUtil" to outUtil, "ResultSetUtil" to outUtil
        ).forEach { (t, u) ->
            File(u, "$prefix$t.kt").writeText(Resource.read("$t.kt.tmp")
                .replaceTag("{{package}}") { pkg }
                .replaceTag("{{prefix}}") { prefix })
        }
    }

    private fun generateTestFile(pkg: String, prefix: String, jdbc: String, output: File) {
        File(output, "Test.kt").writeText(Resource.read("Test.kt.tmp")
            .replaceTag("{{package}}") { pkg }
            .replaceTag("{{prefix}}") { prefix }
            .replaceTag("{{jdbc}}") { jdbc })
    }


}