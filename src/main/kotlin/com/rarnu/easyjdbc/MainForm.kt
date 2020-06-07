package com.rarnu.easyjdbc

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane

class MainForm : JFrame("EasyJDBC"), (JButton) -> Unit {
    init {
        UI.buildUI(this, this)
    }

    override fun invoke(e: JButton) {
        val fields = try {
            Util.checkFieldsValid(UI.fields.map { it.text })
        } catch (th: Throwable) {
            JOptionPane.showMessageDialog(this, th.message ?: "未知的异常.", "出错", JOptionPane.ERROR_MESSAGE)
            return
        }
        CodeGenerator.generate(fields) {
            JOptionPane.showMessageDialog(this, "生成代码完毕!", "完成", JOptionPane.INFORMATION_MESSAGE)
        }
    }

}