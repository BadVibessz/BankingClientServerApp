package mine.client.windows.utils.thumbnails.abstractions

import java.awt.Color
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.JPanel

open class Thumbnail : JPanel() {

    private var isSelected = false
    private val _minSize = Dimension(50, 50)

    init {
        minimumSize = Dimension(200, 100)
        preferredSize = Dimension(200, 100)
        size = Dimension(200, 100)

        setupEventListeners()
    }

    fun select() {
        isSelected = true
        border = BorderFactory.createLineBorder(Color(184, 211, 209), 2)
    }

    fun unselect() {
        isSelected = false
        border = null
    }

    private fun setupEventListeners() {
        this.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                e?.apply {
                    if (button == 1) {
                        if (!isSelected) select()
                        else unselect()
                        repaint()
                    }
                }

            }
        })
    }

}