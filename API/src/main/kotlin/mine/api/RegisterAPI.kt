package mine.api

import com.google.gson.Gson
import mine.Communicator
import mine.models.RegisterModel
import mine.requests.Request
import mine.utils.Hasher
import kotlin.text.Charsets.UTF_8

object RegisterAPI {


    // todo: is it safe to hash on client?


    fun register(model: RegisterModel, callback: (String) -> Unit) {

        // todo: https://habr.com/ru/articles/594071/

        // todo: use bcrypt/scrypt or PBDKF2
        //  https://security.stackexchange.com/questions/16354/whats-the-advantage-of-using-pbkdf2-vs-sha256-to-generate-an-aes-encryption-key

        val hashedPassword = Hasher.hashString(model.password, "SHA-256", UTF_8)

        val service = "register-service"
        val requestCommand = "register-command"

        val requestContent = mutableMapOf<String, Any>()

        // TODO: SERIALIZE MODEL AND DESERIALIZE IT ON SERVER
        requestContent["login"] = model.login
        requestContent["password"] = hashedPassword
        requestContent["firstName"] = model.firstName
        requestContent["secondName"] = model.secondName
        requestContent["lastName"] = model.lastName
        requestContent["phoneNumber"] = model.phoneNumber

        val request = Request(service, requestCommand, requestContent)

        val json = Gson().toJson(request)
        callback(json)
    }
}