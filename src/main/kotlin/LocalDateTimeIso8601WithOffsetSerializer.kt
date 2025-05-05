import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalDateTimeIso8601WithOffsetSerializer : KSerializer<LocalDateTime> {

  override val descriptor: SerialDescriptor =
      PrimitiveSerialDescriptor("kotlinx.datetime.LocalDateTime", PrimitiveKind.STRING)

  override fun deserialize(decoder: Decoder): LocalDateTime {
    val timeString = decoder.decodeString().dropLast(6)
    return LocalDateTime.parse(timeString)
  }

  override fun serialize(encoder: Encoder, value: LocalDateTime) {
    encoder.encodeString("$value+00:00")
  }
}
