package space.softsys.testfly.remote

import retrofit2.Call
import retrofit2.http.GET
import space.softsys.testfly.model.ApiResponse

interface ApiService {
    @GET("api/applications/getAll") // Replace with your actual endpoint
    fun getApps(): Call<ApiResponse>
}