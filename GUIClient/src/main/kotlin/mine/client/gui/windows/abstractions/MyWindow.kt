package mine.client.gui.windows.abstractions

import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.GroupLayout
import javax.swing.JFrame
import javax.swing.JOptionPane

abstract class MyWindow : JFrame() {

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
                    System.exit(0) // todo: client.stop()?
                }
            }
        })
    }
}