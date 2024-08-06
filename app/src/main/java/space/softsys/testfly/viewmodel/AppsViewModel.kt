package space.softsys.testfly.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import space.softsys.testfly.model.ApiResponse
import space.softsys.testfly.remote.RetrofitClient


class AppsViewModel : ViewModel() {

    val apps = MutableLiveData<ApiResponse>()

    fun queryApps() {
        viewModelScope.launch {
            try {
                val call = RetrofitClient.apiService.getApps()
                call.enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {

                        if(response.isSuccessful) {
                            apps.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Log.e("RetrofitError", t.message.toString())
                    }

                })
            } catch(e: Exception) {
                Log.e("RetrofitError", e.message.toString())
            }
        }
    }

    fun fetchApps(): MutableLiveData<ApiResponse>{
        queryApps()
        return apps;
    }
}