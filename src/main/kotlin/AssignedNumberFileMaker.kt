import com.charleskorn.kaml.Yaml
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

object AssignedNumberFileMaker {

  fun createUuidObject(name: String, raw: String, objectKdoc: String): TypeSpec {
    val uuidList: UuidList = Yaml.default.decodeFromString(UuidList.serializer(), raw)

    val objectBuilder = TypeSpec.objectBuilder(name)
    uuidList.uuids.forEach { uuidEntry ->
      val kdoc =
          """
            ${uuidEntry.name}
            
            ${uuidEntry.uuid}
            
            ${uuidEntry.id}
        """
              .trimIndent()
      objectBuilder
          .addProperty(
              PropertySpec.builder(
                      uuidEntry.name.toScreamingSnakeCase(),
                      UInt::class,
                      KModifier.CONST,
                  )
                  .initializer("%L", "${uuidEntry.uuid}_u")
                  .addKdoc(kdoc)
                  .build(),
          )
    }
    return objectBuilder.addKdoc(objectKdoc).build()
  }

  suspend fun HttpClient.getBitbucketYamlAsString(filepath: String): String {
    return get("https://bitbucket.org/bluetooth-SIG/public/raw") {
          url { appendPathSegments(filepath.split("/")) }
        }
        .bodyAsText()
  }

  suspend fun createFile(httpClient: HttpClient): FileSpec {

    return coroutineScope {
      val yamlFiles =
          listOf(
                  async {
                    httpClient.getBitbucketYamlAsString(
                        "main/assigned_numbers/uuids/service_uuids.yaml")
                  },
                  async {
                    httpClient.getBitbucketYamlAsString(
                        "main/assigned_numbers/uuids/characteristic_uuids.yaml")
                  },
                  async {
                    httpClient.getBitbucketYamlAsString(
                        "main/assigned_numbers/uuids/descriptors.yaml")
                  },
              )
              .awaitAll()

      val serviceKdoc =
          "https://bitbucket.org/bluetooth-SIG/public/src/main/assigned_numbers/uuids/service_uuids.yaml"
      val characteristicKdoc =
          "https://bitbucket.org/bluetooth-SIG/public/src/main/assigned_numbers/uuids/characteristic_uuids.yaml"
      val descriptorKdoc =
          "https://bitbucket.org/bluetooth-SIG/public/src/main/assigned_numbers/uuids/descriptors.yaml"

      val fileKdoc = """
        https://www.bluetooth.com/specifications/assigned-numbers/
        
        Last update: ${Clock.System.todayIn(TimeZone.UTC)} (UTC+0)
      """.trimIndent()

      val assignedNumberObject =
          TypeSpec.objectBuilder("AssignedNumbers")
              .addType(createUuidObject("Services", yamlFiles[0], serviceKdoc))
              .addType(createUuidObject("Characteristics", yamlFiles[1], characteristicKdoc))
              .addType(createUuidObject("Descriptors", yamlFiles[2], descriptorKdoc))
              .addKdoc(fileKdoc)
              .build()

      FileSpec.builder("", "AssignedNumbers").addType(assignedNumberObject).build()
    }
  }
}
