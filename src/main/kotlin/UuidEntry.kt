import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UuidEntry(
    @SerialName("uuid") val uuid: String,
    @SerialName("name") val name: String,
    @SerialName("id") val id: String,
)

@Serializable
data class UuidList(
    val uuids: List<UuidEntry>
)