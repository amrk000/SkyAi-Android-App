package amrk000.skyai.data.model.AiResponseData


import com.google.gson.annotations.SerializedName

data class Candidate(
    @SerializedName("content")
    var content: Content,
    @SerializedName("finishReason")
    var finishReason: String,
    @SerializedName("index")
    var index: Int,
    @SerializedName("safetyRatings")
    var safetyRatings: List<SafetyRating>
)