package com.nasserkhosravi.appcomponent.data

import androidx.annotation.RawRes
import com.nasserkhosravi.appcomponent.AppContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Builder pattern for OkHttp
 */
class OkHttpBuilder {
    private var okHttpBuilder = OkHttpClient.Builder()
    private var timeout = -1
    private var withSSL = false
    @RawRes
    private var sslCertificateRaw: Int = 0

    fun addInterceptor(interceptor: Interceptor): OkHttpBuilder {
        okHttpBuilder.addInterceptor(interceptor)
        return this
    }

    fun addNetworkInterceptor(interceptor: Interceptor): OkHttpBuilder {
        okHttpBuilder.addNetworkInterceptor(interceptor)
        return this
    }

    fun withSSL(@RawRes certificateRes: Int): OkHttpBuilder {
        withSSL = true
        sslCertificateRaw = certificateRes
        return this
    }

    fun withTimeOut(timeout: Int): OkHttpBuilder {
        this.timeout = timeout
        return this
    }

    fun build(): OkHttpClient {
        if (timeout == -1) {
            initDefaultTimeOut()
        }
        if (withSSL) {
            configSSL(okHttpBuilder)
        }
        return okHttpBuilder.build()
    }

    private fun initDefaultTimeOut() {
        val t = 6
        okHttpBuilder.readTimeout(t.toLong(), TimeUnit.SECONDS).connectTimeout(t.toLong(), TimeUnit.SECONDS)
    }

    @Throws(
        CertificateException::class,
        IOException::class,
        KeyStoreException::class,
        NoSuchAlgorithmException::class,
        KeyManagementException::class
    )

    private fun configSSL(okHttpClientBuilder: OkHttpClient.Builder) {
        val cf = CertificateFactory.getInstance("X.509")
        val caInput = AppContext.get().resources.openRawResource(sslCertificateRaw)
        val ca: Certificate
        try {
            ca = cf.generateCertificate(caInput)
        } finally {
            caInput.close()
        }
        val keyStoreType = KeyStore.getDefaultType()
        val keyStore = KeyStore.getInstance(keyStoreType)
        keyStore.load(null, null)
        keyStore.setCertificateEntry("ca", ca)

        val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
        val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
        tmf.init(keyStore)
        val trustManagers = tmf.trustManagers
        if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
            throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
        }
        val trustManager = trustManagers[0] as X509TrustManager
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), null)
        val sslSocketFactory = sslContext.socketFactory
        okHttpClientBuilder.sslSocketFactory(sslSocketFactory, trustManager)
    }


}