package mine.client.gui.windows.utils.thumbnails

import mine.serializable.BankAccountSerializable
import java.awt.Color
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.BorderFactory
import javax.swing.JPanel

class BankAccountThumbnail(val bankAccount: BankAccountSerializable) : JPanel() {

    private var isSelected = false

    private val _minSize = Dimension(50,50)

    init {
        minimumSize = Dimension(200,100)
        preferredSize = Dimension(200,100)
        size = Dimension(200,100)

        background = Color.RED
        setupEventListeners()
    }

    fun select()
    {
        isSelected = true
        border = BorderFactory.createLineBorder(Color.RED,2)
    }

    fun unselect()
    {
        isSelected = false
        border = null
    }

    private fun setupEventListeners() {
        this.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                e?.apply {
                    if (button == 1) {
                        if(!isSelected) select()
                        else unselect()
                        repaint()
                    }
                }

            }
        })
    }

//    override fun paintComponent(g: Graphics?) {
//        // todo
//
//
//
//    }
}