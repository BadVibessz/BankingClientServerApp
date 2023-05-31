package mine.client.windows.utils.thumbnails

import mine.client.windows.utils.thumbnails.abstractions.Thumbnail
import mine.serializable.CardSerializable
import java.awt.Graphics

class CardThumbnail(val card: CardSerializable) : Thumbnail(){

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        // todo
        g?.drawString("Id: ${card.id}", 10, 20)
        g?.drawString("Name: ${card.name}", 10, 40)
        g?.drawString("Balance: ${card.balance}", 10, 60)
    }
}