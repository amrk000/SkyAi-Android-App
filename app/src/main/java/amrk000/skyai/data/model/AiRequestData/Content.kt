package amrk000.skyai.data.model.AiRequestData


import com.google.gson.annotations.SerializedName

data class Content(
    @SerializedName("parts")
    var parts: List<Part>
)