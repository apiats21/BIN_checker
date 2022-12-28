package com.example.binchecker

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CallAPILoginAsyncTask().execute()
    }

    private inner class CallAPILoginAsyncTask() : AsyncTask<Any, Void, String>() {

        private lateinit var customProgressDialog: Dialog
        override fun onPreExecute() {
            super.onPreExecute()

            showProgressDialog()
        }

        override fun doInBackground(vararg p0: Any?): String {
            var result: String

            var connection: HttpURLConnection? = null

            try {
                val url = URL("https://run.mocky.io/v3/360c9093-37bb-4a8d-88fb-99301a3f7eb8")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.doOutput = true


                val httpResult: Int = connection.responseCode
                if (httpResult == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream

                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?
                    try{
                        while(reader.readLine().also { line = it } != null) {
                            stringBuilder.append(line + "\n")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }finally {
                        try{
                            inputStream.close()
                        } catch (e: Exception) {
                            e.stackTrace
                        }
                    }
                    result = stringBuilder.toString()
                }else {
                    result = connection.responseMessage
                }
            }catch (e: SocketTimeoutException) {
                result = "Connection timeout"
            }catch (e: Exception) {
                result = "Error: " + e.message
            }finally {
                connection?.disconnect()
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            cancelProgressDialog()
            if (result != null) {
                Log.i("JSON respond result: ", result)
            }

            val responseData = Gson().fromJson(result, ResponseData::class.java)
            Log.i("Number ", "${responseData.number.length}")
            Log.i("Luhn ", "${responseData.number.luhn}")

            Log.i("Scheme ", responseData.scheme)
            Log.i("Type ", responseData.type)
            Log.i("Brand", responseData.brand)
            Log.i("Prepaid: ","${responseData.prepaid}")

            Log.i("Bank", responseData.bank.name)
            Log.i("URL", responseData.bank.url)
            Log.i("Bank city", responseData.bank.city)
            Log.i("Bank phone", responseData.bank.phone)
        }

        /**
         * Method is used to show the Custom Progress Dialog.
         */
        private fun showProgressDialog() {
            customProgressDialog = Dialog(this@MainActivity)

            /*Set the screen content from a layout resource.
            The resource will be inflated, adding all top-level views to the screen.*/
            customProgressDialog.setContentView(R.layout.dialog_custom_progress)

            //Start the dialog and display it on screen.
            customProgressDialog.show()
        }

        /**
         * This function is used to dismiss the progress dialog if it is visible to user.
         */
        private fun cancelProgressDialog() {
            customProgressDialog.dismiss()
        }
    }
}