package com.example.glimpse.core.network

internal object ApiEndPoints {
    //const val BASE_URL = "https://unadventurous-marsh.outray.app/"
    const val BASE_URL = "https://glimpse.outray.app/"

    const val AUTH_URL = "/v1/client/"

    const val GET_ALL_UPLOADS            = "/uploads"
    const val UPLOAD_WITH_FILES          = "/api/v1/uploads/with-files"
    const val GET_RECEIVED_CLUSTER       = "/clusters"
    const val GENERATED_CLUSTER          = "/generated_cluster"

    const val SIGN_IN                    = "/v1/client/sign_ins"

    const val SIGN_UP                    = "/v1/client/sign_ups"

    const val PREPARE_EMAIL_ADDRESS_VERIFICATION           = "/prepare_email_address_verification"

    const val SESSION                    = "/sessions"



    fun getUploadById(id: String)        = "/uploads/$id"
    fun uploadStatus(id: String)         = "/uploads/$id/complete"
    fun updateUpload(id: String)         = "/uploads/$id"
    fun deleteUpload(id: String)         = "/uploads/$id"

    fun session(sessionId: String)       = "/v1/client/sessions/$sessionId"

    fun prepareEmailVerification(signUpId : String)       = "/v1/client/sign_ups/$signUpId/prepare_email_address_verification"

    fun verifyEmail(signUpId: String)        = "/v1/client/sign_ups/$signUpId/attempt_email_address_verification"

    fun signOut(sessionId: String)      = "/v1/client/sessions/$sessionId"



    //fun getReceivedClusterById(id: String) = "/clusters/$id"

}
