package mine.client.windows

import mine.client.windows.abstractions.MyWindow
import mine.client.windows.utils.FlatButton
import mine.models.TransactionModel
import mine.ui.GUI
import java.awt.Dimension
import javax.swing.*

class TransferWindow(gui: GUI, val repaintCallback: () -> Unit) : MyWindow(gui, 2) {

    private val _minSize = Dimension(200, 300)
    private val _startSize = Dimension(170, 150)

    private val _wrapperPanel = JPanel()

    // todo: combobox? выпадающий список типа карты
    private val _cardIdLabel = JLabel("Your card id")
    private val _cardIdField = JTextField()

    private val _receiverPhoneNumberLabel = JLabel("Receiver's phone number")
    private val _receiverPhoneNumberTextField = JTextField()

    private val _amountLabel = JLabel("Amount")
    private val _amountSpinner = JSpinner()

    private val _submitButton = FlatButton("Transfer")

    init {
        minimumSize = _minSize
        size = _startSize

        setLocationRelativeTo(null)
        setupLayout()
        setupEventListeners()

        _amountSpinner.model = SpinnerNumberModel(0F, -10000F, 10000F, 100F)

        isVisible = true
    }


    private fun setupEventListeners() {

        _submitButton.addActionListener {

            val model = TransactionModel(
                _cardIdField.text.toInt(),
                _receiverPhoneNumberTextField.text,
                _amountSpinner.value as Float
            )
            gui.createTransaction(model)
            repaintCallback()
            this.dispose()
        }

    }

    private fun setupLayout() {

        add(_wrapperPanel)
        _wrapperPanel.layout = GroupLayout(_wrapperPanel).apply {
            setVerticalGroup(
                createSequentialGroup()
                    .addGap(10)
                    .addComponent(_cardIdLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_cardIdField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_receiverPhoneNumberLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_receiverPhoneNumberTextField, 20, 20, 20)
                    .addGap(10)
                    .addGap(10)
                    .addComponent(_amountLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_amountSpinner, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_submitButton, 20, 20, 20)

            )

            setHorizontalGroup(

                createSequentialGroup()
                    .addGap(10)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(_cardIdLabel, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_cardIdField, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_receiverPhoneNumberLabel, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_receiverPhoneNumberTextField, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_amountLabel, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_amountSpinner, MyWindow.SHRINK, 150, MyWindow.GROW)
                            .addComponent(_submitButton, 100, 100, 100)
                    )
                    .addGap(10)

            )
        }

    }

}