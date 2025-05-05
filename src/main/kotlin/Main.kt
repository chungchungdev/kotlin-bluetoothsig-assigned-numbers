import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlin.io.path.Path
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

fun main() {

  val client =
      HttpClient(CIO) {
        install(ContentNegotiation) {
          json(
              Json {
                prettyPrint = true
                ignoreUnknownKeys = true
              },
          )
        }
      }

  runBlocking {
    AssignedNumberFileMaker.createFile(client).writeTo(Path("generated"))
  }
}
