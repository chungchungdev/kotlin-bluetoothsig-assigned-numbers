import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

// probably not worth checking update, just regenerate all files
interface CommitInfoRepository {

  suspend fun getLatestServiceUuidCommitInfo(): BitbucketCommitInfo

  suspend fun getLatestCharacteristicUuidCommitInfo(): BitbucketCommitInfo

  suspend fun getLatestDescriptorUuidCommitInfo(): BitbucketCommitInfo
}

class CommitInfoRepositoryImpl(
    private val httpClient: HttpClient,
) : CommitInfoRepository {

  override suspend fun getLatestServiceUuidCommitInfo(): BitbucketCommitInfo {
    return getUuidCommitInfo("service_uuids.yaml")
  }

  override suspend fun getLatestCharacteristicUuidCommitInfo(): BitbucketCommitInfo {
    return getUuidCommitInfo("characteristic_uuids.yaml")
  }

  override suspend fun getLatestDescriptorUuidCommitInfo(): BitbucketCommitInfo {
    return getUuidCommitInfo("descriptors.yaml")
  }

  private suspend fun getUuidCommitInfo(filename: String): BitbucketCommitInfo {
    val response =
        httpClient.get(MAIN_COMMIT_URL) {
          url {
            parameters.append("path", "assigned_numbers/uuids/$filename")
            parameters.append("pagelen", "1")
          }.also { print(url) }
        }

    require(response.status == HttpStatusCode.OK) { "Http request not OK." }

    val commitInfoList = response.body<BitbucketCommitResponse>().values

    require(commitInfoList.size == 1) { "No commit info found." }

    return commitInfoList[0]
  }

  companion object {

    const val MAIN_COMMIT_URL =
        "https://api.bitbucket.org/2.0/repositories/bluetooth-SIG/public/commits/main"
  }
}
