package mine.client.windows

import mine.client.ServerResponseHandler
import mine.client.windows.abstractions.MyWindow
import mine.client.windows.utils.FlatButton
import mine.models.CardModel
import mine.types.CardType
import java.awt.Dimension
import javax.swing.*

class TransferWindow : JFrame() {

    private val _minSize = Dimension(200, 300)
    private val _startSize = Dimension(170, 150)

    private val _wrapperPanel = JPanel()

    // todo: combobox? выпадающий список типа карты
    private val _nameLabel = JLabel("Name")
    private val _nameTextField = JTextField()

    private val _typeLabel = JLabel("Card type")
    private val _typeComboBox = JComboBox<CardType>().apply {
        addItem(CardType.Debit)
        addItem(CardType.Credit)
    }

    private val _accountNumberLabel = JLabel("Account number")
    private val _accountNumberTextField = JTextField()

    private val _submitButton = FlatButton("Create")

    init {
        minimumSize = _minSize
        size = _startSize

        setLocationRelativeTo(null)
        setupLayout()
        setupEventListeners()

        isVisible = true
    }


    private fun setupEventListeners() {

        _submitButton.addActionListener {

        }

    }

    private fun setupLayout() {

        add(_wrapperPanel)
        _wrapperPanel.layout = GroupLayout(_wrapperPanel).apply {
            setVerticalGroup(
                createSequentialGroup()
                    .addGap(10)
                    .addComponent(_nameLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_nameTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_typeLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_typeComboBox, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_accountNumberLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_accountNumberTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_submitButton, 20, 20, 20)

            )

            setHorizontalGroup(

                createSequentialGroup()
                    .addGap(10)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(_nameLabel, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_nameTextField, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_typeLabel, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_typeComboBox, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_accountNumberLabel, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_accountNumberTextField, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_submitButton, 100, 100, 100)
                    )
                    .addGap(10)

            )
        }

    }

}