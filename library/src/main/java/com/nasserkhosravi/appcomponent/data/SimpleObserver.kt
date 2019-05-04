package com.nasserkhosravi.appcomponent.data

class SimpleObservable<T>(value: T) {

    private var observer: SimpleObserver<T>? = null

    var value: T = value
        set(value) {
            observer?.onChange(value)
            field = value
        }

    fun observeOn(observer: SimpleObserver<T>) {
        this.observer = observer
    }

    fun removeObserver() {
        observer = null
    }
}

interface SimpleObserver<T> {
    fun onChange(v: T)
}