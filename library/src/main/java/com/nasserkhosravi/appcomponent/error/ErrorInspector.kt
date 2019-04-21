package com.nasserkhosravi.appcomponent.error

import com.google.gson.stream.MalformedJsonException
import com.nasserkhosravi.appcomponent.R
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorInspector {

    /**
     * Inspect throwable object to create proper network error message
     * @param throwable object from web service
     * @param errorId is a id for tracking
     * @return proper [NetworkErrorModel]
     */
    fun networkExceptionTypeFactory(throwable: Throwable, errorId: Int): NetworkErrorModel {
        val code: Int
        val resourceString: Int
        if (throwable is SocketTimeoutException || throwable is NoRouteToHostException) {
            code = NetworkErrorModel.NO_SERVER_CONNECTION
            resourceString = R.string.No_server_connection
        } else if (throwable is ConnectException || throwable is UnknownHostException) {
            code = NetworkErrorModel.NO_INTERNET
            resourceString = R.string.No_internet_connection
        } else if (throwable is HttpException || throwable is MalformedJsonException) {
            code = NetworkErrorModel.HTTP
            resourceString = R.string.Problem_in_catching_data
        } else {
            code = NetworkErrorModel.UN_KNOWN
            resourceString = R.string.Un_expected_exception
        }
        return NetworkErrorModel(errorId, code, resourceString)
    }
}