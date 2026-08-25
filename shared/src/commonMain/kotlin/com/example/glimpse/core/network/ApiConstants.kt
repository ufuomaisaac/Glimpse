package com.example.glimpse.core.network

internal object ApiEndPoints {
    //const val BASE_URL = "https://unadventurous-marsh.outray.app/"
    const val BASE_URL = "https://glimpse.outray.app/"

    private const val AUTH = "/v1/client"

    const val GET_ALL_UPLOADS            = "/uploads"
    const val UPLOAD_WITH_FILES          = "/api/v1/uploads/with-files"
    const val GET_RECEIVED_CLUSTER       = "/clusters"
    const val GENERATED_CLUSTER          = "/generated_cluster"

    const val SIGN_IN                    = "$AUTH/sign_ins"
    const val SIGN_UP                    = "$AUTH/sign_ups"



    fun getUploadById(id: String)        = "/uploads/$id"
    fun uploadStatus(id: String)         = "/uploads/$id/complete"
    fun updateUpload(id: String)         = "/uploads/$id"
    fun deleteUpload(id: String)         = "/uploads/$id"

    fun session(sessionId: String)       = "$AUTH/sessions/$sessionId"

    fun prepareEmailVerification(signUpId: String) =
        "$SIGN_UP/$signUpId/prepare_email_address_verification"

    fun verifyEmail(signUpId: String) =
        "$SIGN_UP/$signUpId/attempt_email_address_verification"



    //fun getReceivedClusterById(id: String) = "/clusters/$id"

}

internal object AuthApiConstants {
    const val LOG_TAG = "AuthApiService"
    const val IDENTIFIER = "identifier"
    const val STRATEGY = "strategy"
    const val PASSWORD = "password"
    const val EMAIL_ADDRESS = "email_address"
    const val USERNAME = "username"
    const val PASSWORD_STRATEGY = "password"
    const val EMAIL_CODE_STRATEGY = "email_code"
}
