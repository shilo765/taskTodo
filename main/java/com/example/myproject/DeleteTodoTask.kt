package com.example.myproject

import android.os.AsyncTask
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// AsyncTask for deleting a todo
class DeleteTodoTask(private val todoId: String, private val cord: String, private val date: String) :
    AsyncTask<Void, Void, String>() {

    // Background task to perform the DELETE request
    override fun doInBackground(vararg params: Void?): String {
        // Create the URL for the DELETE request
        val url = URL("http://10.0.2.2:3000/api/todos/$todoId,$cord,$date")
        val connection = url.openConnection() as HttpURLConnection

        // Set the request method to DELETE
        connection.requestMethod = "DELETE"

        // Get the response from the server
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // If the response code is OK, read the response
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            return response.toString()
        } else {
            // If there's an error, return an error message
            return "Error: ${connection.responseMessage}"
        }
    }

    // Method called after the background task is completed
    override fun onPostExecute(result: String?) {
        // Handle the result as needed
        // Note: You can add code here to handle the result, such as displaying a Toast message
        // For example, if you want to show a Toast message for success:
        // Toast.makeText(context, "Todo deleted successfully", Toast.LENGTH_SHORT).show()
    }
}
