package mine.client.windows.abstractions

import mine.ui.GUI
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.GroupLayout
import javax.swing.JFrame
import javax.swing.JOptionPane

abstract class MyWindow(protected val gui: GUI, closingType: Int) : JFrame() {

    companion object {
        val SHRINK = GroupLayout.PREFERRED_SIZE
        val GROW = GroupLayout.DEFAULT_SIZE
    }

    init {
        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(windowEvent: WindowEvent) {
                if (JOptionPane.showConfirmDialog(
                        this@MyWindow,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    ) == JOptionPane.YES_OPTION
                ) {

                    when (closingType) {
                        1 -> System.exit(0)
                        2 -> dispose()
                    }
                } else {
                    return
                }
            }
        })
        //pack()
    }
}