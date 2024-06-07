import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


//Класс данных для представления задачи
@Serializable
data class Task(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("isCompleted") val isCompleted: Boolean
)