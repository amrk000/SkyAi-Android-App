package amrk000.skyai.data.model.AiResponseData


import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("parts")
    var parts: List<Part>,
    @SerializedName("role")
    var role: String
)