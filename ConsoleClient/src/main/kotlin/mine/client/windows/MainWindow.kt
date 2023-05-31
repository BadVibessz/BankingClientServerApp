package mine.client.windows

import mine.client.windows.abstractions.MyWindow
import mine.client.windows.utils.FlatButton
import mine.client.windows.utils.thumbnails.*
import mine.models.BankAccountModel
import mine.models.CardModel
import mine.serializable.BankAccountSerializable
import mine.serializable.BankClientSerializable
import mine.serializable.CardSerializable
import mine.serializable.TransactionSerializable
import mine.types.AccountType
import mine.ui.GUI
import org.joda.time.DateTime
import java.awt.*
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.GroupLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.SwingConstants

class MainWindow(gui: GUI) : MyWindow(gui, 1) {

    private val _minSize = Dimension(200, 100)
    private val _startSize = Dimension(800, 600)

    // panels
    private val _wrapperPanel = JPanel().apply { background = Color.LIGHT_GRAY }
    private val _controlPanel = JPanel().apply { background = Color.white }
    private val _contentPanel = JPanel().apply {
        background = Color.LIGHT_GRAY
        //minimumSize = Dimension(500,500)
        //preferredSize = Dimension(500,500)
        //maximumSize = Dimension(1000,1000)
    }

    // scrolls
    private val _scroll = JScrollPane(_contentPanel)

    // colors
    private val _pressedButtonColor = Color(184, 211, 209)
    private val _buttonColor = Color.white

    // buttons
    private val _profileButton = FlatButton("Profile").apply {
        size = Dimension(205, 50)
        background = _buttonColor
        horizontalAlignment = SwingConstants.LEFT
        isBorderPainted = false
        isContentAreaFilled = true
    }

    private val _bankAccountsButton = FlatButton("Bank Accounts").apply {
        background = _buttonColor
        horizontalAlignment = SwingConstants.LEFT
        isBorderPainted = false
        isContentAreaFilled = true

    }

    private val _cardsButton = FlatButton("Cards").apply {
        background = _buttonColor
        horizontalAlignment = SwingConstants.LEFT
        isBorderPainted = false
        isContentAreaFilled = true
    }

    private val _transactionsButton = FlatButton("Transactions").apply {
        background = _buttonColor
        horizontalAlignment = SwingConstants.LEFT
        isBorderPainted = false
        isContentAreaFilled = true
    }

    init {
        minimumSize = _minSize
        size = _startSize

        setLocationRelativeTo(null)

        setupLayout()
        setupEventListeners()

        isVisible = true

        clickButton(_profileButton)
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
                            .addComponent(_scroll, GROW, GROW, GROW)
                    )
            )

            setHorizontalGroup(

                createSequentialGroup()
                    .addComponent(_controlPanel, 200, 200, 200)
                    .addComponent(_scroll, GROW, GROW, GROW)
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
                    .addComponent(_transactionsButton, 50, 50, 50)
                    .addGap(0, 0, Int.MAX_VALUE)
            )

            setHorizontalGroup(
                createParallelGroup()
                    .addComponent(_profileButton, 200, 200, 200)
                    .addComponent(_bankAccountsButton, 200, 200, 200)
                    .addComponent(_cardsButton, 200, 200, 200)
                    .addComponent(_transactionsButton, 200, 200, 200)
            )
        }
    }


    private fun clickButton(button: FlatButton) {
        button.doClick()
    }

    private fun pressButton(button: FlatButton) {

        unpressButton(_bankAccountsButton)
        unpressButton(_cardsButton)
        unpressButton(_profileButton)
        unpressButton(_transactionsButton)

        button.background = _pressedButtonColor
    }

    private fun unpressButton(button: FlatButton) {
        button.background = _buttonColor;
    }

    private fun setupEventListeners() {

        _profileButton.addActionListener {
            pressButton(_profileButton)
            gui.getClientInfo()
        }

        _bankAccountsButton.addActionListener {
            pressButton(_bankAccountsButton)
            gui.getAllAccounts()
        }

        _cardsButton.addActionListener {
            pressButton(_cardsButton)
            gui.getAllCards()
        }

        _transactionsButton.addActionListener {
            pressButton(_transactionsButton)
            gui.getAllTransactions()
        }

    }

    private fun clearLayout(panel: JPanel) {
        for (component in panel.components)
            panel.remove(component)
    }

    // todo:
    fun updateProfileInfo(bankClient: BankClientSerializable) {

        clearLayout(_contentPanel)

        _contentPanel.background = Color.white

        class CenteredLabel(text: String) : JLabel(text, CENTER) {
            init {
                alignmentX = CENTER_ALIGNMENT
            }
        }

        val labels = listOf (
            CenteredLabel("Id: ${bankClient.id}"),
            CenteredLabel("Login: ${bankClient.login}"),
            CenteredLabel("First name: ${bankClient.firstName}"),
            CenteredLabel("Second name: ${bankClient.secondName}"),
            CenteredLabel("Last name: ${bankClient.lastName}"),
            CenteredLabel("Phone number: ${bankClient.phoneNumber}")
        )

        _contentPanel.layout = BoxLayout(_contentPanel, BoxLayout.Y_AXIS)

        labels.forEach { _contentPanel.add(it) }

        _contentPanel.revalidate()
        _contentPanel.repaint()
    }

    fun updateAccountThumbnails(accounts: List<BankAccountSerializable>) {
        val thumbnails = accounts.map { BankAccountThumbnail(it) }

        clearLayout(_contentPanel)

        _contentPanel.background = Color.LIGHT_GRAY

        _contentPanel.layout = FlowLayout(0, 20, 20)

        _contentPanel.add(AddBankAccountThumbnail {
            val model = BankAccountModel(
                "408",
                "02",
                810,
                0,
                "0000",
                AccountType.Checking,
                DateTime.now().plusYears(3)
            )
            gui.createAccount(model)
            gui.getAllAccounts()
        }
        )

        thumbnails.forEach { _contentPanel.add(it) }

        _contentPanel.revalidate()
        _contentPanel.repaint()
    }

    fun updateCardThumbnails(cards: List<CardSerializable>) {
        val thumbnails = cards.map { CardThumbnail(it) }

        clearLayout(_contentPanel)

        _contentPanel.background = Color.LIGHT_GRAY

        _contentPanel.layout = FlowLayout(0, 20, 20)

        _contentPanel.add(AddCardThumbnail {
            CreateCardWindow(gui, {
                clickButton(_cardsButton)
            })

        })

        thumbnails.forEach { _contentPanel.add(it) }

        _contentPanel.revalidate()
        _contentPanel.repaint()

    }


    fun updateTransactionThumbnails(transactions: List<TransactionSerializable>) {

        val thumbnails = transactions.map { TransactionThumbnail(it) }

        clearLayout(_contentPanel)

        _contentPanel.background = Color.LIGHT_GRAY


        _contentPanel.layout = BoxLayout(_contentPanel, BoxLayout.Y_AXIS)
        _contentPanel.add(AddTransactionThumbnail {
            TransferWindow(this.gui, { clickButton(_transactionsButton) })
        })

        thumbnails.forEach { _contentPanel.add(it) }

        _contentPanel.revalidate()
        _contentPanel.repaint()
    }
}