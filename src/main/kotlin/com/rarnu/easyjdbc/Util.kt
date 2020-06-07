package com.rarnu.easyjdbc

import com.rarnu.common.RegexpUtil.isStringReg
import java.io.File
import java.lang.Exception

object Util {
    fun checkFieldsValid(fields: List<String>): List<String> {
        if (fields[0].isEmpty()) throw Exception("工程名称不能为空.")
        if (fields[1].isEmpty()) throw Exception("代码前缀不能为空.")
        if (fields[2].isEmpty()) throw Exception("包名不能为空.")
        if (fields[3].isEmpty()) throw Exception("JDBC URL 不能为空.")
        if (fields[8].isEmpty()) throw Exception("保存路径不能为空.")

        if (!isProjectNameValid(fields[0])) throw Exception("非法的工程名称.")
        if (!isPrefixValid(fields[1])) throw Exception("非法的代码前缀.")
        if (!isPackageNameValid(fields[2])) throw Exception("非法的包名.")
        if (!isJdbcUrlValid(fields[3])) throw Exception("非法的 JDBC URL.")
        if (File(fields[8], fields[0]).exists()) throw Exception("要保存到的路径已存在.")

        val port = try { (if (fields[4].isEmpty()) "80" else fields[4]).toInt() } catch (th: Throwable) { throw Exception("默认端口不是数字.") }
        val major = try { (if (fields[5].isEmpty()) "0" else fields[5]).toInt() } catch (th: Throwable) { throw Exception("主版本号不是数字.") }
        val minor = try { (if (fields[6].isEmpty()) "0" else fields[6]).toInt() } catch (th: Throwable) { throw Exception("次版本号不是数字.") }
        val driverName = if (fields[7].isEmpty()) fields[1] else fields[7]

        return listOf(fields[0], fields[1], fields[2], fields[3], port.toString(), major.toString(), minor.toString(), driverName, File(fields[8], fields[0]).absolutePath)
    }

    private fun isProjectNameValid(s: String) = isStringReg(s, 5)
    private fun isPrefixValid(s: String) = isStringReg(s, 2)
    private fun isPackageNameValid(s: String) = s.matches("[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*".toRegex())
    private fun isJdbcUrlValid(s: String) = isStringReg(s, 4)

}