package mine.client.gui.windows

import mine.api.LoginAPI
import mine.client.gui.core.Client
import mine.client.gui.windows.abstractions.MyWindow
import mine.utils.Hasher
import java.awt.Dimension
import javax.swing.*
import javax.swing.JOptionPane.showMessageDialog

class LoginWindow(private val _client: Client) : MyWindow() {

    private val _loginLabel = JLabel("Email")
    private val _passwordLabel = JLabel("Password")
    private val _registerLable = JLabel("Dont have account yet?")

    private val _loginTextField = JTextField()
    private val _passwordTextField = JPasswordField()

    private val _minSize = Dimension(100, 200)
    private val _startSize = Dimension(200, 300)

    private val _submitButton = JButton("Log in")
    private val _registerButton = JButton("Register")



    init {
        minimumSize = _minSize
        size = _startSize


        // todo: for debug

        setLocationRelativeTo(null)
        setupLayout()
        setupEventListeners()
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
                    .addComponent(_submitButton, 20, 20, 20)
                    .addGap(10)
                    .addComponent(_registerLable, 20, 20, 20)
                    .addGap(5)
                    .addComponent(_registerButton, 20, 20, 20)
                    .addGap(10)
            )

            setHorizontalGroup(
                createSequentialGroup()
                    .addGap(10)
                    .addGroup(
                        createParallelGroup()
                            .addComponent(_loginLabel, GROW, GROW, GROW)
                            .addComponent(_loginTextField, GROW, GROW, GROW)
                            .addComponent(_passwordLabel, GROW, GROW, GROW)
                            .addComponent(_passwordTextField, GROW, GROW, GROW)
                            .addComponent(_submitButton, GROW, GROW, GROW)
                            .addComponent(_registerLable, GROW, GROW, GROW)
                            .addComponent(_registerButton, GROW, GROW, GROW)
                    )
                    .addGap(10)
            )
        }

    }

    fun showMessageBox(message: String) {
        showMessageDialog(this, message)
        _loginTextField.text = ""
        _passwordTextField.text = ""
    }

    private fun setupEventListeners() {
        _submitButton.addActionListener {
            val email = _loginTextField.text

            /* For stronger security, it is recommended
            that the returned character array be cleared
            after use by setting each character to zero.*/
            var password = String(_passwordTextField.password)
            LoginAPI.login(email, password, _client::send)

            password = "" // todo: correct?
        }

        _registerButton.addActionListener {
            dispose()
            _client.loginWindow = null
            _client.registerWindow = RegisterWindow(_client).apply { isVisible = true }
        }
    }


}