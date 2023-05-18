package mine.client.gui.windows.utils

import javax.swing.BorderFactory
import javax.swing.JButton

class FlatButton(text: String? = null) : JButton(text) {

    init{
        isBorderPainted = false
        //border = BorderFactory.createCompoundBorder().
        isFocusPainted = false
        //isContentAreaFilled = false
    }

}