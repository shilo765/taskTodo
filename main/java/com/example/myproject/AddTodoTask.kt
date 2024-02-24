package com.example.myproject

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class AddTodoTask(
    private val key: Int,
    private val cord: String,
    private val date: String,
    private val userId: String
) : AsyncTask<Void, Void, String>() {

    // Background task to be executed
    override fun doInBackground(vararg params: Void?): String {
        // API endpoint URL
        val url = URL("http://10.0.2.2:3000/api/todos")

        // Open connection to the server
        val connection = url.openConnection() as HttpURLConnection

        // Set request method to POST
        connection.requestMethod = "POST"

        // Set content type as JSON
        connection.setRequestProperty("Content-Type", "application/json")

        // Allow output for the request body
        connection.doOutput = true

        // Create JSON request body
        val requestBody = "{\"key\":\"$key\",\"cord\":\"$cord\",\"date\":\"$date\",\"userId\":\"$userId\"}"

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
            // Read response from the server
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            return response.toString()
        } else {
            return "Error: ${connection.responseMessage}"
        }
    }

    // Executed after the background task is complete
    override fun onPostExecute(result: String?) {
        // Handle the result as needed
        // You can add your result handling logic here
    }
}
