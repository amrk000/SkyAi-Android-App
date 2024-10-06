package amrk000.skyai.data.model

import com.google.gson.annotations.SerializedName

open class ResponseData {
    companion object {
        const val SUCCESSFUL_OPERATION = 200 //Successful: Data Process, Data Update, Data Retrieval
        const val FAILED_AUTH = 401 //Failed: Wrong Password, Not Authorized User
        const val FAILED_RATE_LIMIT = 429 //Failed: api requests limit reached
        const val FAILED_REQUEST_FAILURE = 504 //Failed: Request Error
    }

    @SerializedName("code")
    var code: Int = 200

    @SerializedName("status")
    var status: String = ""

    @SerializedName("message")
    var message: String = ""

}
