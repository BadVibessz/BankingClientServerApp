package mine.client.windows

import mine.api.RegisterAPI
import mine.client.windows.abstractions.MyWindow
import mine.client.windows.utils.FlatButton
import mine.models.RegisterModel
import mine.ui.GUI
import java.awt.Dimension
import javax.swing.*

class RegisterWindow(gui: GUI) : MyWindow(gui,1) {

    private val _loginLabel = JLabel("Login")
    private val _passwordLabel = JLabel("Password")
    private val _firstNameLabel = JLabel("First name")
    private val _secondNameLabel = JLabel("Second name")
    private val _lastNameLabel = JLabel("Last name")
    private val _phoneNumberLabel = JLabel("Phone number")

    private val _registerLable = JLabel("Already registered?")

    private val _loginTextField = JTextField()
    private val _passwordTextField = JPasswordField()
    private val _firstNameTextField = JTextField()
    private val _secondNameTextField = JTextField()
    private val _lastNameTextField = JTextField()
    private val _phoneNumberTextField = JTextField()

    private val _minSize = Dimension(200, 300)
    private val _startSize = Dimension(600, 500)

    private val _submitButton = FlatButton("Register")
    private val _loginButton = FlatButton("Log in")

    init {
        minimumSize = _minSize
        size = _startSize

        setLocationRelativeTo(null)
        setupLayout()
        setupEventListeners()

        isVisible = true
    }

    private fun setupLayout() {
        layout = GroupLayout(contentPane).apply {
            setVerticalGroup(
                createSequentialGroup()
                    .addGap(10)
                    .addComponent(_loginLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_loginTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_passwordLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_passwordTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_firstNameLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_firstNameTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_secondNameLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_secondNameTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_lastNameLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_lastNameTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_phoneNumberLabel, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_phoneNumberTextField, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_submitButton, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_registerLable, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_loginButton, 20, 20, 20)
                    .addGap(10)
            )

            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(50)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(_loginLabel, GROW, GROW, GROW)
                            .addComponent(_loginTextField, GROW, GROW, GROW)
                            .addComponent(_passwordLabel, GROW, GROW, GROW)
                            .addComponent(_passwordTextField, GROW, GROW, GROW)
                            .addComponent(_firstNameLabel, GROW, GROW, GROW)
                            .addComponent(_firstNameTextField, GROW, GROW, GROW)
                            .addComponent(_secondNameLabel, GROW, GROW, GROW)
                            .addComponent(_secondNameTextField, GROW, GROW, GROW)
                            .addComponent(_lastNameLabel, GROW, GROW, GROW)
                            .addComponent(_lastNameTextField, GROW, GROW, GROW)
                            .addComponent(_phoneNumberLabel, GROW, GROW, GROW)
                            .addComponent(_phoneNumberTextField, GROW, GROW, GROW)
                            .addComponent(_submitButton, GROW, GROW, GROW)
                            .addComponent(_registerLable, GROW, GROW, GROW)
                            .addComponent(_loginButton, GROW, GROW, GROW)
                    )
                    .addGap(50)
            )
        }

    }

    fun showMessageBox(message: String) {
        JOptionPane.showMessageDialog(this, message)
        _loginTextField.text = ""
        _passwordTextField.text = ""
    }

    private fun setupEventListeners() {
        _submitButton.addActionListener {
            val email = _loginTextField.text


            val model = RegisterModel(
                _loginTextField.text,
                String(_passwordTextField.password),
                _firstNameTextField.text,
                _secondNameTextField.text,
                _lastNameTextField.text,
                _phoneNumberLabel.text
            )

            // todo: VALIDATE

            gui.register(model)

            /* For stronger security, it is recommended
            that the returned character array be cleared
            after use by setting each character to zero.*/
            model.password = "" // todo: correct?
        }

        _loginButton.addActionListener {
            gui.requestLogin()
        }

    }


}