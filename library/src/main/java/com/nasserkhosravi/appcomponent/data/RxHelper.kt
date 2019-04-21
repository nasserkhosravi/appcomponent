package com.nasserkhosravi.appcomponent.data

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * A helper class to summarize rx functions
 */
object RxHelper {

    /**
     * config observer ro web services
     */
    fun <T> commonWebServiceThreadConfig(observable: Observable<T>): Observable<T> {
        return observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }


}