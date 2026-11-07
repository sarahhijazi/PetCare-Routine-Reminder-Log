
package com.example.petcare_routinereminderlog.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object CatFactApi {

    suspend fun getRandomFact(): String =
        withContext(Dispatchers.IO) {

            val url = URL("https://catfact.ninja/fact")
            val connection =
                url.openConnection() as HttpURLConnection

            try {
                connection.requestMethod = "GET"
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                val responseCode = connection.responseCode

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw Exception("Server error: $responseCode")
                }

                val response = connection.inputStream
                    .bufferedReader()
                    .use { it.readText() }

                val json = JSONObject(response)

                json.getString("fact")

            } finally {
                connection.disconnect()
            }
        }
}


