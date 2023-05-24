package mine.client.windows.utils

import javax.swing.BorderFactory
import javax.swing.JButton

class FlatButton(text: String? = null) : JButton(text) {

    init{
        isBorderPainted = true
        //border = BorderFactory.createCompoundBorder().
        isFocusPainted = false
        isContentAreaFilled = false
    }

}