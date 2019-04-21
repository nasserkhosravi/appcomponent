package com.nasserkhosravi.appcomponent.error

import org.greenrobot.eventbus.EventBus

object ErrorPublisher {

    /**
     * publish network exception event
     */
    fun publishNetworkException(model: NetworkErrorModel) {
        EventBus.getDefault().post(model)
    }

}