package com.nasserkhosravi.appcomponent.error

import android.support.annotation.IntDef
import android.support.annotation.StringRes

class NetworkErrorModel constructor(val errorId: Int, @Type val errorType: Int, @StringRes var stringRes: Int) {

    @IntDef(HTTP, NO_INTERNET, NO_SERVER_CONNECTION, UN_KNOWN)
    annotation class Type

    companion object {
        const val HTTP = 1
        const val NO_INTERNET = 2
        const val NO_SERVER_CONNECTION = 3
        const val UN_KNOWN = 0
    }

}
