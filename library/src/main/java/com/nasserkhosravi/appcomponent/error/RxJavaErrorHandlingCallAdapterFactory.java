package com.nasserkhosravi.appcomponent.error;

import io.reactivex.Observable;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * You can catch all exception here
 */
public class RxJavaErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

    private final RxJava2CallAdapterFactory original;

    private RxJavaErrorHandlingCallAdapterFactory() {
        original = RxJava2CallAdapterFactory.create();
    }

    public static CallAdapter.Factory create() {
        return new RxJavaErrorHandlingCallAdapterFactory();
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(retrofit, original.get(returnType, annotations, retrofit));
    }

    private class RxCallAdapterWrapper implements CallAdapter<Observable, Observable> {
        private final CallAdapter<?, ?> wrapped;

        public RxCallAdapterWrapper(Retrofit retrofit, CallAdapter<?, ?> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public Observable adapt(Call call) {
            Observable adapt = (Observable) wrapped.adapt(call);
            return adapt.doOnNext(new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                }
            }).doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                }
            });
        }

    }

}
