package mine.client.windows.utils.thumbnails

import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel


class AddBankAccountThumbnail(val creationCallback: () -> Unit) : JPanel() {

    // todo: override paint and paint here + sign

    init {
        minimumSize = Dimension(200, 100)
        preferredSize = Dimension(200, 100)
        size = Dimension(200, 100)

        setupEventListeners()
    }

    private fun setupEventListeners() {
        this.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(me: MouseEvent?) {
                creationCallback()
            }
        })
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        // todo
        g?.drawString("Create account", this.width / 2 - 35, this.height / 2)
    }

}