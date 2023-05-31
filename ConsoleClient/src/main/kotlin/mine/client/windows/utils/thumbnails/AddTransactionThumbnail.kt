package mine.client.windows.utils.thumbnails

import mine.client.windows.utils.thumbnails.abstractions.Thumbnail
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel

class AddTransactionThumbnail(val creationCallback: () -> Unit) : Thumbnail() {

    init {
        size = Dimension(100, 30)
        maximumSize = Dimension(1000, 100)

        setupEventListeners()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        // todo
        g?.drawString("Transfer money", this.width / 2 - 50, this.height / 2)
    }

    private fun setupEventListeners() {
        this.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(me: MouseEvent?) {
                creationCallback()
            }
        })
    }

}