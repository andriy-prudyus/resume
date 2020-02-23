package com.andriiprudyus.myresume.network

import com.andriiprudyus.myresume.network.model.GistResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface CompanyApi {

    @GET("/gists/7ec48c427ea377a5c3719ab9bd575f57")
    fun loadCompanies(): Single<Response<GistResponse>>
}