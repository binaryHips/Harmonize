package com.example.harmonizer

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Headers
import okhttp3.Headers.Companion.toHeaders
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.internal.http2.Header
import okio.HashingSource.Companion.md5
import okio.IOException
import java.math.BigInteger
import java.security.MessageDigest

private fun md5Hash(str: String): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}

class Client {
    val host_url:String = "localhost"

    var authToken:String = ""

    val client = OkHttpClient()

    private fun sendGet(path:String, params:Map<String, String>, callbackObject: Callback){

        val builder = HttpUrl.Builder()
            .host(host_url)
            .addPathSegment(path)
            .addQueryParameter("token", authToken)

        params.forEach{
            entry ->
            builder.addQueryParameter(entry.key, entry.value)
        }
        builder.build()

        var request = Request.Builder().url(builder.build())
            .build()

        client.newCall(request).enqueue(callbackObject)
    }

    fun requestToken(name:String, password:String){
        val passwordHash = md5Hash(password)

        sendGet(
            "authenticate",
            mapOf(
                "name" to name,
                "password" to passwordHash
            ),
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    Log.d("HTTP_CLIENT", "HTTP response: $response")

                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.d("HTTP_CLIENT", "HTTP response: ${e.message}")
                }
            }
        )
    }
}
/* fun runPostApi() {
 var url = "https://reqres.in/api/users"

 // add parameter
 val formBody = FormBody.Builder().add("name", " Parker")
         .build()

 // creating request
 var request = Request.Builder().url(url)
         .post(formBody)
         .build()

 var client = OkHttpClient();
 client.newCall(request).enqueue(object : Callback {
     override fun onResponse(call: Call, response: Response) {
         println(response.body?.string())

     }

     override fun onFailure(call: Call, e: IOException) {
         println(e.message.toString())
     }
 })}*/