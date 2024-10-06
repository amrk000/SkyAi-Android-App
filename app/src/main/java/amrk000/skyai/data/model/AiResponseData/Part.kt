package amrk000.skyai.data.model.AiResponseData


import com.google.gson.annotations.SerializedName

data class Part(
    @SerializedName("text")
    var text: String
)