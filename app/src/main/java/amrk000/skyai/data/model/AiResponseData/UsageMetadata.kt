package amrk000.skyai.data.model.AiResponseData


import com.google.gson.annotations.SerializedName

data class UsageMetadata(
    @SerializedName("candidatesTokenCount")
    var candidatesTokenCount: Int,
    @SerializedName("promptTokenCount")
    var promptTokenCount: Int,
    @SerializedName("totalTokenCount")
    var totalTokenCount: Int
)