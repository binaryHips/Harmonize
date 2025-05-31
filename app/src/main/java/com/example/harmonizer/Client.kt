package com.example.harmonizer

import android.media.Image
import android.util.Log
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okio.IOException
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


private fun md5Hash(str: String): String {
    val md = MessageDigest.getInstance("MD5")
    val bigInt = BigInteger(1, md.digest(str.toByteArray(Charsets.UTF_8)))
    return String.format("%032x", bigInt)
}

private fun sanitizeInputs(input:String): String{
    return input.trim()
}

private fun isNameLegit(input:String): Boolean{
    if (input.length < 6) return false
    if (input.length > 15) return false
    if (input.contains(" ")) return false
    return true;
}

private fun isPasswordLegit(input:String): Boolean{
    if (input.length < 8) return false
    if (input.length > 20) return false
    if (input.contains(" ")) return false
    return true;
}

class Client {
    private val hostUrl:String = "78.194.190.31"

    private val port = 3000

    var authToken:String = ""
        set(value) {
            if (value == ""){
                field = ""
                return
            }
            field = value.filterNot { it == '"' } // somehow okhttp adds ' " ' around it.
            val sharedPref = activity.getSharedPreferences("client", 0) ?: return // FIXME maybe not return...
            with (sharedPref.edit()) {
                putString("AUTH_TOKEN", authToken)
                apply()
            }
        }

    private val client = OkHttpClient()

    var activity: MainActivity // reference to current activity

    constructor (activity: MainActivity){
        this.activity = activity
        val sharedPref = activity.getSharedPreferences("client", 0) ?: return
        authToken = sharedPref.getString("AUTH_TOKEN", "")!!


        Log.d("HTTP_CLIENT", "starting authToken : $authToken")
    }

    private fun sendGet(path:String, params:Map<String, String>?, callbackObject: Callback){

        val builder = HttpUrl.Builder()
            .scheme("http")
            .host(hostUrl)
            .port(port)
            .addPathSegment(path)
            .addQueryParameter("token", authToken)

        params?.forEach{
            entry ->
            builder.addQueryParameter(entry.key, entry.value)
        }

        val request = Request.Builder().url(builder.build())
            .build()

        client.newCall(request).enqueue(callbackObject)
    }

    private fun sendPost(path:String, params:Map<String, String>?, body: RequestBody? , callbackObject: Callback){

        val builder = HttpUrl.Builder()
            .scheme("http")
            .host(hostUrl)
            .port(port)
            .addPathSegment(path)
            .addQueryParameter("token", authToken)

        params?.forEach{
                entry ->
            builder.addQueryParameter(entry.key, entry.value)
        }

        val requestBuilder = Request.Builder()
            .url(builder.build())

        if (body != null) requestBuilder.post(body)
        else requestBuilder.post(FormBody.Builder().build()) // create an empty one

        client.newCall(requestBuilder.build()).enqueue(callbackObject)
    }

    // request functions

    fun requestToken(name:String, password:String){
        //val passwordHash = md5Hash(password)
        val name = sanitizeInputs(name)
        val password = sanitizeInputs(password)

        if (!isNameLegit(name)){

            Toast.makeText(
                activity,
                activity.getString(R.string.username_error_message),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        else if (!isPasswordLegit(password)) {
            Toast.makeText(
                activity,
                activity.getString(R.string.password_error_message),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val passwordHash = md5Hash(password)


        activity.navController.navigate(Screen.Loading)

        sendGet(
            "authenticate",
            mapOf(
                "username" to name,
                "password_hash" to passwordHash
            ),
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.code != 200 /*network authentication required*/){
                        val errMessage = (response.body?.string() ?: response.message)
                        activity.runOnUiThread {
                            activity.navController.navigate(Screen.Login)
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.error_message) + "\n" + errMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        response.close()
                        return
                    }
                    // else

                    authToken = response.body?.string() ?: ""
                    response.close()

                    activity.runOnUiThread {
                        Log.d("HTTP_CLIENT", "Connected with token : $authToken")
                        activity.navController.navigate(Screen.Main)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {

                    activity.runOnUiThread {
                        activity.navController.navigate(Screen.Login)
                        Toast.makeText(
                            activity,
                            activity.getString(R.string.error_message) + "\n" + e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Log.d("HTTP_CLIENT", "HTTP response: ${e.message}")
                }
            }
        )
    }

    fun requestCreateAccount(name:String, password:String){
        val name = sanitizeInputs(name)
        val password = sanitizeInputs(password)

        if (!isNameLegit(name)){

            Toast.makeText(
                activity,
                activity.getString(R.string.username_error_message),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        else if (!isPasswordLegit(password)) {
            Toast.makeText(
                activity,
                activity.getString(R.string.password_error_message),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val passwordHash = md5Hash(password)

        activity.navController.navigate(Screen.Loading)

        sendPost(
            "createAccount",
            mapOf(
                "username" to name,
                "password_hash" to passwordHash
            ),
            null
            ,
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.code != 200 /*network authentication required*/){
                        val errMessage = (response.body?.string() ?: response.message)
                        activity.runOnUiThread {
                            activity.navController.navigate(Screen.Login)
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.error_message) + "\n" + errMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        response.close()
                        return
                    }
                    // else

                    authToken = response.body?.string() ?: ""
                    response.close()
                    activity.runOnUiThread {
                        Log.d("HTTP_CLIENT", "Created account : $name")
                        Log.d("HTTP_CLIENT", "Connected with token : $authToken")
                        activity.navController.navigate(Screen.Main)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {

                    activity.runOnUiThread {
                        activity.navController.navigate(Screen.Login)
                        Toast.makeText(
                            activity,
                            activity.getString(R.string.error_message) + "\n" + e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Log.d("HTTP_CLIENT", "HTTP response: ${e.message}")
                }
            }
        )
    }

    fun requestVerifyToken(){

        activity.navController.navigate(Screen.Loading)

        sendGet(
            "validateToken",
            null,
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.code != 200 /*network authentication required*/){
                        authToken = ""
                        Log.d("HTTP_CLIENT", "reset token: $authToken")
                        activity.runOnUiThread {
                            activity.navController.navigate(Screen.Login)
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.authentication_failed),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        response.close()
                        return
                    }
                    // else
                    response.close()

                    activity.runOnUiThread {
                        Log.d("HTTP_CLIENT", "Connected with token : $authToken")
                        activity.navController.navigate(Screen.Main)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    authToken = ""
                    activity.runOnUiThread {

                        activity.navController.navigate(Screen.Login)
                        Toast.makeText(
                            activity,
                            activity.getString(R.string.error_message) + "\n" + e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Log.d("HTTP_CLIENT", "HTTP response: ${e.message}")
                }
            }
        )
    }

    fun requestImageHarmonization(image:PhotoItem){

        activity.navController.navigate(Screen.Loading)

        val file = File(" ")

        val body = file.asRequestBody()


        sendPost(
            "createAccount",
            null,
            body
            ,
            object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    if (response.code != 200 /*network authentication required*/){
                        val errMessage = (response.body?.string() ?: response.message)
                        activity.runOnUiThread {
                            activity.navController.navigate(Screen.Login)
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.error_message) + "\n" + errMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        response.close()
                        return
                    }
                    // else

                    response.close()

                    activity.runOnUiThread {
                        Log.d("HTTP_CLIENT", "Harmonized image")
                        activity.navController.navigate(Screen.Main)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {

                    activity.runOnUiThread {
                        activity.navController.navigate(Screen.Login)
                        Toast.makeText(
                            activity,
                            activity.getString(R.string.error_message) + "\n" + e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Log.d("HTTP_CLIENT", "HTTP response: ${e.message}")
                }
            }
        )
    }
}
