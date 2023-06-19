package org.d3if3083.galerihewan.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import org.d3if3083.assessment2.model.Resep
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

private const val BASE_URL = "https://mopro-dishgenius-default-rtdb.asia-southeast1.firebasedatabase.app/"
private const val URL_BASE_URL = "https://firebasestorage.googleapis.com/v0/b/mopro-dishgenius.appspot.com/o/img%2F"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ResepApiService {
    @GET("resep.json")
    suspend fun getResep(): List<Resep>

    @PUT("resep.json") // Replace `{id}` with the actual ID parameter in your API endpoint
    suspend fun putResep(@Body resep: List<Resep>?): Response<ResponseBody>

}

object ResepApi {
    val service: ResepApiService by lazy {
        retrofit.create(ResepApiService::class.java)
    }

    fun getResepUrl(imageId: String): String {
        return "$URL_BASE_URL$imageId?alt=media"
    }

}

enum class ApiStatus { LOADING, SUCCESS, FAILED }