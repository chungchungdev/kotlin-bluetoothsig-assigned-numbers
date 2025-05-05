import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [Bitbucket_API_doc](https://developer.atlassian.com/cloud/bitbucket/rest/api-group-commits/#api-repositories-workspace-repo-slug-commits-get)
 */
@Serializable
data class BitbucketCommitResponse(
    @SerialName("values") val values: List<BitbucketCommitInfo>,
)

@Serializable
data class BitbucketCommitInfo(
    @SerialName("hash") val hash: String,
    @SerialName("date")
    @Serializable(with = LocalDateTimeIso8601WithOffsetSerializer::class)
    val date: LocalDateTime,
)
