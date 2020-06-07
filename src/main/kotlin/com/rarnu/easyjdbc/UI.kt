package com.rarnu.easyjdbc

import com.rarnu.swingfx.showDirectoryDialog
import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

object UI {

    val fields = mutableListOf<JTextField>()

    fun buildUI(frame: JFrame, callback: (JButton) -> Unit) {
        frame.apply {
            contentPane = buildMainForm(callback)
            contentPane.background = Color.white
            pack()
            isResizable = false
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            setLocationRelativeTo(null)
            isVisible = true
        }
    }

    private fun buildMainForm(callback: (JButton) -> Unit) = JPanel(GridLayout(0, 1)).apply {
        uiElements.forEach {
            add(buildInputText(it.first, it.second) { t ->
                fields.add(t)
            })
        }
        add(buildOutputText(uiOutput.first, uiOutput.second) { t -> fields.add(t) })
        add(buildGenerateButton(callback))
    }

    private fun buildInputText(label: String, hint: String, callback: (JTextField) -> Unit) = JPanel(BorderLayout()).apply {
        border = EmptyBorder(8, 8, 8, 8)
        minimumSize = Dimension(400, 0)
        add(JLabel(label), BorderLayout.NORTH)
        add(JTextField().apply {
            preferredSize = Dimension(400, 28)
            callback(this)
        }, BorderLayout.CENTER)
        add(JLabel(hint).apply { this.foreground = Color.GRAY }, BorderLayout.SOUTH)
    }

    private fun buildOutputText(label: String, hint: String, callback: (JTextField) -> Unit) = JPanel(BorderLayout()).apply {
        border = EmptyBorder(8, 8, 8, 8)
        minimumSize = Dimension(400, 0)
        add(JLabel(label), BorderLayout.NORTH)
        val txt: JTextField
        add(JPanel(BorderLayout()).apply {
            add(JTextField().apply {
                txt = this
                isEditable = false
                background = Color.WHITE
                preferredSize = Dimension(400, 28)
                callback(this)
            }, BorderLayout.CENTER)
            add(JButton("...").apply {
                addActionListener {
                    showDirectoryDialog { f -> txt.text = f.absolutePath }
                }
            }, BorderLayout.EAST)
        }, BorderLayout.CENTER)
        add(JLabel(hint).apply { this.foreground = Color.GRAY }, BorderLayout.SOUTH)
    }

    private fun buildGenerateButton(callback: (JButton) -> Unit) = JPanel().apply {
        border = EmptyBorder(8, 8, 8, 8)
        add(JButton("生成代码").apply {
            preferredSize = Dimension(200, 30)
            addActionListener { callback(this) }
        })
    }
}

private val uiElements = listOf(
    "工程名称" to "最终打包出的文件为 <工程名称>.jar",
    "代码前缀" to "生成的代码文件的统一前缀",
    "包名" to "要生成的代码的基础包名",
    "JDBC URL" to "用于 JDBC schema 识别的名称,，如 jdbc:myurl://，则此处填入 myurl",
    "默认端口" to "访问 JDBC 时使用的默认端口号，默认设为 80",
    "主版本号" to "主版本号默认设为 0",
    "次版本号" to "次版本号默认设为 0",
    "驱动名称" to "展示给用户看的驱动名称，默认设置为 代码前缀 所对应的字符串"
)

private val uiOutput = "输出路径" to "将生成的代码保存到的路径"
