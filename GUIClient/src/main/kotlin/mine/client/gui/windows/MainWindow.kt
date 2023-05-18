package mine.client.gui.windows

import mine.api.AccountAPI
import mine.client.gui.core.Client
import mine.client.gui.windows.abstractions.MyWindow
import mine.client.gui.windows.utils.thumbnails.BankAccountThumbnail
import mine.client.gui.windows.utils.FlatButton
import mine.client.gui.windows.utils.thumbnails.AddBankAccountThumbnail
import mine.serializable.BankAccountSerializable
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.SwingConstants

class MainWindow(private val _client: Client) : MyWindow() {

    private val _minSize = Dimension(200, 100)
    private val _startSize = Dimension(800, 600)

    // panels
    private val _wrapperPanel = JPanel().apply { background = Color.LIGHT_GRAY }
    private val _controlPanel = JPanel().apply { background = Color.white }
    private val _contentPanel = JPanel().apply { background = Color.LIGHT_GRAY }

    // colors
    private val _pressedButtonColor = Color(184, 211, 209)
    private val _buttonColor = Color.white

    // buttons
    private val _profileButton = FlatButton("Profile").apply {
        size = Dimension(205, 50)
        // background = Color.red
        background = _buttonColor
        horizontalAlignment = SwingConstants.LEFT
    }

    private val _bankAccountsButton = FlatButton("Bank Accounts").apply {
        //background = Color.red
        background = _buttonColor
        horizontalAlignment = SwingConstants.LEFT
    }

    private val _cardsButton = FlatButton("Cards").apply {
        //background = Color.red
        background = _buttonColor
        horizontalAlignment = SwingConstants.LEFT
    }

    private val _transferButton = FlatButton("Transfer").apply {
        // background = Color.red
        background = _buttonColor
        horizontalAlignment = SwingConstants.LEFT
    }


    init {
        minimumSize = _minSize
        size = _startSize

        setLocationRelativeTo(null)

        setupLayout()
        setupEventListeners()
    }

    private fun setupLayout() {

        setupWrapperLayout()
        setupControlPanelLayout()
    }

    private fun setupWrapperLayout() {
        add(_wrapperPanel)
        _wrapperPanel.layout = GroupLayout(_wrapperPanel).apply {
            setVerticalGroup(
                createSequentialGroup()
                    .addGroup(
                        createParallelGroup()
                            .addComponent(_controlPanel, GROW, GROW, GROW)
                            .addComponent(_contentPanel, GROW, GROW, GROW)
                    )
            )

            setHorizontalGroup(

                createSequentialGroup()
                    .addComponent(_controlPanel, 200, 200, 200)
                    .addComponent(_contentPanel, GROW, GROW, GROW)
            )
        }
    }

    private fun setupControlPanelLayout() {
        _controlPanel.layout = GroupLayout(_controlPanel).apply {
            setVerticalGroup(
                createSequentialGroup()
                    .addGap(10)
                    .addComponent(_profileButton, 50, 50, 50)
                    .addGap(1)
                    .addComponent(_bankAccountsButton, 50, 50, 50)
                    .addGap(1)
                    .addComponent(_cardsButton, 50, 50, 50)
                    .addGap(1)
                    .addComponent(_transferButton, 50, 50, 50)
                    .addGap(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)
            )

            setHorizontalGroup(
                createParallelGroup()
                    .addComponent(_profileButton, 200, 200, 200)
                    .addComponent(_bankAccountsButton, 200, 200, 200)
                    .addComponent(_cardsButton, 200, 200, 200)
                    .addComponent(_transferButton, 200, 200, 200)
            )
        }
    }


    private fun pressButton(button: FlatButton) {

        // todo: implement as list?
        unpressButton(_bankAccountsButton)
        unpressButton(_cardsButton)
        unpressButton(_profileButton)
        unpressButton(_transferButton)

        button.background = _pressedButtonColor
    }

    private fun unpressButton(button: FlatButton) {
        button.background = _buttonColor;
    }


//    private fun clearThumbs() {
//        for (thumb in thumbnails)
//            thumbsPanel.remove(thumb);
//    }

    fun updateThumbnails(bankAccounts: List<BankAccountSerializable>) {
        val thumbnails = bankAccounts.map { BankAccountThumbnail(it).apply { background = Color.red } }

        _contentPanel.layout = FlowLayout(0, 20, 20)

        _contentPanel.add(AddBankAccountThumbnail().apply { background = Color.blue })
        thumbnails.forEach { _contentPanel.add(it) }

        _contentPanel.repaint()
    }

    private fun setupEventListeners() {

        _bankAccountsButton.addActionListener {
            pressButton(_bankAccountsButton)
            AccountAPI.getAll(_client::send)
        }

    }


}