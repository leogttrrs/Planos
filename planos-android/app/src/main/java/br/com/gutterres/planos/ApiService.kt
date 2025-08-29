package br.com.gutterres.planos

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// IMPORTANTE: Este é o endereço do seu computador (localhost) visto de dentro do emulador Android.
private const val BASE_URL = "http://10.0.2.2:8080/"

// Interface que define os endpoints da nossa API
interface ApiService {
    @GET("planos")
    suspend fun getPlanosPorCategoria(@Query("categoria") categoria: String): List<Plano>
}

// Objeto que cria e configura o cliente Retrofit
object RetrofitClient {
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}