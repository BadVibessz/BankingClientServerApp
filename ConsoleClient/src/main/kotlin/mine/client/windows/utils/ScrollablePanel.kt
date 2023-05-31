package mine.client.windows.utils

import java.awt.Dimension
import java.awt.Rectangle
import javax.swing.JPanel
import javax.swing.Scrollable
import javax.swing.SwingConstants


class ScrollablePanel : JPanel(), Scrollable {
    override fun getPreferredScrollableViewportSize(): Dimension {
        return preferredSize
    }

    override fun getScrollableUnitIncrement(visibleRect: Rectangle?, orientation: Int, direction: Int): Int {
        return 10
    }

    override fun getScrollableBlockIncrement(visibleRect: Rectangle, orientation: Int, direction: Int): Int {
        return (if (orientation == SwingConstants.VERTICAL) visibleRect.height else visibleRect.width) - 10
    }

    override fun getScrollableTracksViewportWidth(): Boolean {
        return true
    }

    override fun getScrollableTracksViewportHeight(): Boolean {
        return false
    }
}