package com.example.myproject

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class UpdateTodoTask(private val todoId: String, private val updatedCord: String, private val updatedDate: String,private val oldCord: String?,private val oldDate: String?) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String {
        val url = URL("http://10.0.2.2:3000/api/todos/$todoId")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PUT"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        // Create JSON request body with updated data
        val requestBody = "{\"cord\":\"$updatedCord\",\"date\":\"$updatedDate\",\"oldCord\":\"$oldCord\",\"oldDate\":\"$oldDate\"}"

        // Write the request body
        val outputStream: OutputStream = connection.outputStream
        outputStream.write(requestBody.toByteArray())
        outputStream.close()

        // Get the response from the server
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            return response.toString()
        } else {
            return "Error: ${connection.responseMessage}"
        }
    }

    override fun onPostExecute(result: String?) {
        // Handle the result as needed
    }
}