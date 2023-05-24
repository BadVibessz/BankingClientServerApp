package mine.client

import com.google.gson.Gson
import kotlinx.coroutines.*
import mine.Communicator
import mine.api.*
import mine.cryptography.symmetric.AES
import mine.models.*
import mine.types.AccountType
import mine.types.CardType
import mine.ui.ConsoleUI
import mine.ui.GUI
import mine.ui.UIType
import org.joda.time.DateTime
import java.net.Socket
import java.security.PublicKey

class Client(
    host: String,
    port: Int,
    uiType: UIType
) {
    private val _socket: Socket
    private val _communicator: Communicator
    private val _mainCoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    var rsaPublicKey: PublicKey? = null
    private val _aesSecretKey = AES.generateSecretKey()

    val ui = when (uiType) {

        UIType.Console -> ConsoleUI(this::send)

        UIType.GUI -> GUI(this::send)

        else -> null
    }

    init {
        _socket = Socket(host, port)
        _communicator = Communicator(_socket)
    }

    fun start() = _mainCoroutineScope.launch {
        launch {
            _communicator.startReceiving {
                ServerResponseHandler.handleRespone(it, this@Client)
            }
        }

        launch {
            if (isActive) {


                ui?.login(LoginModel("admin", "admin"))

//                ui?.createAccount(
//                    BankAccountModel(
//                        "408", "02", 810, 0, "0000",
//                        AccountType.Checking, DateTime.now().plusYears(3)
//                    )
//                )

                //ui?.createCard(CardModel("debitCard", CardType.Debit), 2)


                //ui?.createTransaction(TransactionModel(1, "+7123456888", 100F))

                //ui?.login(LoginModel("admin", "admin"))
                //ui?.requestLogin()

                // todo: implement type enum as constants and move type enum to server app

                //CryptoAPI.getRSAPublicKey(_communicator)

//                val model = RegisterModel(
//                    "test3",
//                    "test3",
//                    "firstName",
//                    "secondName",
//                    "lastName",
//                    "+7223456789",
//                )

                //RegisterAPI.register(model, this@Client::send)
                //LoginAPI.login("admin", "admin", this@Client::send)

//                val model = BankAccountModel(
//                    "408",
//                    "02",
//                    810,
//                    0,
//                    "0000",
//                    AccountType.Checking,
//                    DateTime.now().plusYears(3)
//                )

                //AccountAPI.create(model, this@Client::send)
                //AccountAPI.update(1, DateTime.now().plusYears(10), this@Client::send)

//                val model = CardModel(
//                    "myDebitCard",
//                    CardType.Debit
//                )
//
//                CardAPI.create(model, 1, this@Client::send)

                //AccountAPI.getAll(this@Client::send)
            }
        }
    }

    private fun send(msg: String) {

//        val (encrypted: ByteArray, iv: ByteArray) =
//            AES.encrypt(msg.toByteArray(Charsets.US_ASCII), _aesSecretKey)
//
//        val (a: Int, b: Int, c: Int) = Triple(1, 3, 2)
//
//        val message = Triple(encrypted, iv, _aesSecretKey.encoded)
//
//        val str = Gson().toJson(message)
//
//        val bytes = Gson().fromJson(str, Triple::class.java)
//
//        val decrypted = AES.decrypt(encrypted, _aesSecretKey, iv).toString(Charsets.US_ASCII)
//
//        // todo: send via socket data as byteArray and encrypted aes secret keys
//        _communicator.send(encrypted.toString(Charsets.US_ASCII))

        _communicator.send(msg)
    }


}