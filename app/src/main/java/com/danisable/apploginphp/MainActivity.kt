package com.danisable.apploginphp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btnLogin.setOnClickListener{
            validUser(edtUsuario.text.toString(),edtPassword.text.toString())
        }
    }

    private fun validUser(name: String, pass: String) {
        // url to post our data
        val url = "<url>/<endpoint>"
        idLoadingPB.visibility = View.VISIBLE

        // creating a new variable for our request queue
        val queue = Volley.newRequestQueue(this@MainActivity)

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        val request: StringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                // inside on response method we are
                // hiding our progress bar
                // and setting data to edit text as empty
                idLoadingPB.visibility = View.GONE
                edtUsuario.setText("")
                edtPassword.setText("")

                // on below line we are displaying a success toast message.

                try {
                    // on below line we are passing our response
                    // to json object to extract data from it.
                    val respObj = JSONObject(response)

                    // below are the strings which we
                    // extract from our json object.
                    val status = respObj.getString("status")

                    if(!status.toBoolean()){
                        Toast.makeText(this@MainActivity, "User or Password Invalid!!!", Toast.LENGTH_SHORT)
                            .show()
                    }else{
                        val intent = Intent(this, PrincipalActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }

                    //Log.e("TAG",respObj.toString())
                    // on below line we are setting this string s to our text view.
                    //idTVResponse.text = "Name : $name\nJob : $pass"
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> // method to handle errors.
                Toast.makeText(
                    this@MainActivity,
                    "Fail to get response = $error",
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            override fun getParams(): Map<String, String> {
                // below line we are creating a map for
                // storing our values in key and value pair.
                val params: MutableMap<String, String> =
                    HashMap()

                // on below line we are passing our key
                // and value pair to our parameters.
                params["name"] = name
                params["pass"] = pass

                // at last we are
                // returning our params.
                return params
            }
        }
        // below line is to make
        // a json object request.
        queue.add(request)
    }
}

