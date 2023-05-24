package mine.client.windows.utils.thumbnails

import mine.client.windows.utils.thumbnails.abstractions.Thumbnail
import mine.serializable.TransactionSerializable
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.GroupLayout

class TransactionThumbnail(val transaction: TransactionSerializable) : Thumbnail() {


    init {
        size = Dimension(100,30)
        maximumSize = Dimension(1000,100)
    }


    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        // todo
        g?.drawString("Sender: ${transaction.senderPhone}", 10, 20)
        g?.drawString("Receiver: ${transaction.receiverPhone}", 10, 40)
        g?.drawString("Amount: ${transaction.amount}", 10, 60)
        g?.drawString("Date: ${transaction.dateTime}", 10, 80)
    }

}