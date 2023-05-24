package mine.client.windows.utils.thumbnails

import mine.client.windows.utils.thumbnails.abstractions.Thumbnail
import mine.serializable.BankAccountSerializable
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.lang.StringBuilder
import javax.swing.BorderFactory
import javax.swing.JPanel

class BankAccountThumbnail(val account: BankAccountSerializable) : Thumbnail() {

    private val _minSize = Dimension(50, 50)

    private fun getIdWithLeadingZeros(id: Int): String {

        val leadingZeros = 7 - id.toString().length

        if (leadingZeros <= 0) return id.toString()

        val builder = StringBuilder()

        repeat(leadingZeros, { builder.append('0') })
        builder.append(id)

        return builder.toString()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        // todo
        val accNumber = account.firstOrder + account.secondOrder +
                account.currency + account.checkDigit +
                account.department + getIdWithLeadingZeros(account.id)

        g?.drawString(accNumber, 10, 20)
        g?.drawString("Balance: ${account.balance}", 10, 40)
        g?.drawString("Expires at: ${account.expiresAt}", 10, 60)
    }
}