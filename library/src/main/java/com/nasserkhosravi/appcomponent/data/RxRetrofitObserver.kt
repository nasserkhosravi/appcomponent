package com.nasserkhosravi.appcomponent.data

import com.nasserkhosravi.appcomponent.AppComponent
import com.nasserkhosravi.appcomponent.error.ErrorInspector
import com.nasserkhosravi.appcomponent.error.ErrorPublisher
import com.nasserkhosravi.appcomponent.error.HttpException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Response


/**
 * Rx retrofit response for Having start end successful events, catching and publishing error, tracking web service.
 */
abstract class RxRetrofitObserver<T> constructor(val appComponent: AppComponent) :
    Observer<Response<GeneralResponse<T>>> {

    private var disposable: Disposable? = null

    /**
     * used to tracking and identifying service in another classes
     */
    private var requestId: Int = 0

    constructor(appComponent: AppComponent, requestId: Int) : this(appComponent) {
        this.requestId = requestId
    }

    protected fun onSuccessful(response: T) {}

    /**
     * This method will be call on start of service
     * Good place to init UI or relevant things
     * @param disposable used as disposing service at onComplete
     */
    override fun onSubscribe(disposable: Disposable) {
        this.disposable = disposable
        appComponent.getViewAccessor().progress.show()
    }

    /**
     * Inspect your http response here
     * If response is not between from 200 to 300 we don't interpret it as successful response
     */
    override fun onNext(response: Response<GeneralResponse<T>>) {
        if (response.isSuccessful) {
            onSuccessful(response.body()!!.response)
        } else {
            httpException()
        }
    }

    /**
     * Generate and publish an error from @[Throwable]
     * @param e will be create from network exception
     */
    override fun onError(e: Throwable) {
        ErrorPublisher.publishNetworkException(
            ErrorInspector.networkExceptionTypeFactory(e, requestId)
        )
        onComplete()
    }

    /**
     * this will be called either web response is successful or is not
     * Good place to terminate UI or tasks are relevant web service
     */
    override fun onComplete() {
        if (disposable != null) {
            if (!disposable!!.isDisposed) {
                disposable!!.dispose()
            }
        }
        appComponent.getViewAccessor().progress.hide()
    }

    fun httpException() {
        onError(HttpException())
    }

}
