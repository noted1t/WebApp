import kotlinx.serialization.Serializable

//Сделан для возвращения ошибок, у которых нет кода или для уточнения ответа
@Serializable
data class Message(
    val message: String,
)