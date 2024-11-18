package com.example.riseandroid.data
import android.content.Context
import com.example.riseandroid.R
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.net.ssl.HostnameVerifier

object SslHelper {

    fun loadCertificate(context: Context): X509TrustManager {
        val certInputStream: InputStream = context.resources.openRawResource(R.raw.certificate_local)
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val certificate = certificateFactory.generateCertificate(certInputStream)

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry("ca", certificate)
        }

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore)
        }

        return trustManagerFactory.trustManagers.first() as X509TrustManager
    }

    private val hostnameVerifier = HostnameVerifier { hostname, session ->
        hostname == "localhost" || hostname == "10.0.2.2"
    }

    fun createOkHttpClient(context: Context, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val trustManager = loadCertificate(context)

        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), null)
        }

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .addInterceptor(loggingInterceptor)
            .hostnameVerifier(hostnameVerifier)
            .build()
    }
}
