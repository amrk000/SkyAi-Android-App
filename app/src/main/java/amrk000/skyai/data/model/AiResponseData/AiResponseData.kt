package amrk000.skyai.data.model.AiResponseData


import amrk000.skyai.data.model.ResponseData
import com.google.gson.annotations.SerializedName

data class AiResponseData(
    @SerializedName("candidates")
    var candidates: List<Candidate>?,
    @SerializedName("usageMetadata")
    var usageMetadata: UsageMetadata?
): ResponseData()