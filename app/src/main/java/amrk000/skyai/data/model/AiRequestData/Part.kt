package amrk000.skyai.data.model.AiRequestData


import com.google.gson.annotations.SerializedName

data class Part(
    @SerializedName("text")
    var text: String
)