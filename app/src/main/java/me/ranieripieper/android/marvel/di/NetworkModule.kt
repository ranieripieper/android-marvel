package me.ranieripieper.android.marvel.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import me.ranieripieper.android.marvel.BuildConfig
import me.ranieripieper.android.marvel.core.extensions.md5
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

val networkModule = module {

    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    fun provideOkHttpClient(
        loggingInterceptor: Interceptor,
        apiKeyInterceptor: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        return builder.build()
    }

    fun provideLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MARVEL_API_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->

            val original = chain.request()
            val originalHttpUrl = original.url()

            val ts = Date().time.toString()

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("ts", ts)
                .addQueryParameter("apikey", BuildConfig.MARVEL_PUBLIC_KEY)
                .addQueryParameter(
                    "hash",
                    String.format(
                        "%s%s%s",
                        ts,
                        BuildConfig.MARVEL_PRIVATE_KEY,
                        BuildConfig.MARVEL_PUBLIC_KEY
                    ).md5()
                )
                .build()

            val requestBuilder = original.newBuilder()
                .url(url)

            val request = requestBuilder.build()

            chain.proceed(request)
        }
    }

    single { provideGson() }
    single(qualifier = named("loggingInterceptor")) { provideLoggingInterceptor() }
    single(qualifier = named("apiKeyInterceptor")) { provideApiKeyInterceptor() }
    single {
        provideOkHttpClient(
            get(qualifier = named("loggingInterceptor")),
            get(qualifier = named("apiKeyInterceptor"))
        )
    }
    single { provideRetrofit(get(), get()) }

}
