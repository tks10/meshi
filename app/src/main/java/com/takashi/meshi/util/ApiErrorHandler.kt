package com.takashi.meshi.util

import android.support.design.widget.Snackbar
import android.view.View
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import com.takashi.meshi.R
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException

class ApiErrorHandler(private val view: View, private val message: String) {
    constructor(view: View, resId: Int): this(view, view.resources.getString(resId))

    companion object {
        data class Detail(val detail: String)

        fun map(view: View, t: Throwable): ApiErrorHandler {
            return when(t) {
                is UnknownHostException, is ConnectException -> ApiErrorHandler(view, R.string.unable_to_connect)
                is SocketException -> ApiErrorHandler(view, R.string.timeout)
                is HttpException -> {
                    try {
                        val adapter = Moshi.Builder().build().adapter(Detail::class.java)
                        t.response().errorBody()?.let {
                            ApiErrorHandler(view, adapter.fromJson(it.string())?.detail ?: "Error")
                        } ?: throw JsonEncodingException("")
                    } catch (e: JsonEncodingException){
                        when (t.code()){
                        // TODO: もっとわける
                            401 -> ApiErrorHandler(view, R.string.not_authorized)
                            404 -> ApiErrorHandler(view, R.string.not_found)
                            500 -> ApiErrorHandler(view, R.string.internal_server_error)
                            else -> ApiErrorHandler(view, R.string.unable_to_connect)
                        }
                    }
                }
                else -> ApiErrorHandler(view, R.string.unable_to_connect)
            }
        }
    }

    fun post(duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(view, message, duration).show()
    }
}
