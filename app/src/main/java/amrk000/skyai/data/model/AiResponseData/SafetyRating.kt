package amrk000.skyai.data.model.AiResponseData


import com.google.gson.annotations.SerializedName

data class SafetyRating(
    @SerializedName("category")
    var category: String,
    @SerializedName("probability")
    var probability: String
)