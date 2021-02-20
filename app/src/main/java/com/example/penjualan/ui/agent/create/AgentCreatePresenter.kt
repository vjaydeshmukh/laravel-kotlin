package com.example.penjualan.ui.agent.create

import com.example.penjualan.data.model.agent.ResponseAgentUpdate
import com.example.penjualan.network.ApiService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AgentCreatePresenter(val view: AgentCreateContract.View) : AgentCreateContract.Presenter {

    init {
        view.initActivity()
        view.initListener()
        view.onLoading(false)
    }

    override fun insertAgent(
        nama_toko: String,
        nama_pemilik: String,
        alamat: String,
        latitude: String,
        longitude: String,
        gambar_toko: File
    ) {

        val requestBody: RequestBody = gambar_toko.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody: MultipartBody.Part? =
            MultipartBody.Part.createFormData("gambar_toko", gambar_toko.name, requestBody)

        view.onLoading(true)
        ApiService.endPoint.insertAgent(
            nama_toko,
            nama_pemilik,
            alamat,
            latitude,
            longitude,
            multipartBody!!
        )
            .enqueue(object : Callback<ResponseAgentUpdate> {

                override fun onFailure(call: Call<ResponseAgentUpdate>, t: Throwable) {
                    view.onLoading(false)
                }

                override fun onResponse(
                    call: Call<ResponseAgentUpdate>,
                    response: Response<ResponseAgentUpdate>
                ) {
                    view.onLoading(false)
                    if (response.isSuccessful) {
                        val responseAgentUpdate: ResponseAgentUpdate? = response.body()
                        view.onResult(responseAgentUpdate!!)
                    }
                }

            })
    }
}