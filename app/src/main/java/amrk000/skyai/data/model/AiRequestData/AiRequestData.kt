package amrk000.skyai.data.model.AiRequestData


import com.google.gson.annotations.SerializedName

data class AiRequestData(
    @SerializedName("contents")
    var contents: List<Content>
)