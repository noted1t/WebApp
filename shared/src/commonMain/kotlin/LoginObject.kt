import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//Класс данных для представления объекта аутентификации.
@Serializable
data class LoginObject(
    @SerialName("login") val login: String,
    @SerialName("password") val password: String,
)