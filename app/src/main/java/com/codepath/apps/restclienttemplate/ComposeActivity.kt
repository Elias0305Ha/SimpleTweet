package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var tweetCount: TextView

    lateinit var client: TwitterClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose=findViewById(R.id.etTweetCompose)
        btnTweet= findViewById(R.id.btnTweet)
        tweetCount = findViewById(R.id.tweetCount)


        client = TwitterApplication.getRestClient(this)

        // Handling the users click on tweet button

        btnTweet.setOnClickListener {

            // grab the content of edit text

            val tweetContent = etCompose.text.toString()
            // 1. make sure that the tweet is not empty
            if(tweetContent.isEmpty()){
                Toast.makeText(this, "Empty tweet not allowed!", Toast.LENGTH_SHORT).show()
                // look into displaying snack message
            } else
            // 2. make sure the tweet is under character
            if(tweetContent.length > 140){
                Toast.makeText(this, "Twee is too long! Limit is 140 character", Toast.LENGTH_SHORT)
                    .show()
            } else{
                // to make an api call to twitter to publish tweet
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler(){

                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {

                    Log.i(TAG, "Successfully publish tweet!")
                    // to send the tweet back to timelineActivity

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()

                    }



                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG, "Failed to publish tweet", throwable)
                    }


                })

            }





        }
        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tweetCount.text = (140 - etCompose.text.length).toString()
                if (140 - etCompose.text.length < 0) {
                    tweetCount.setTextColor(Color.parseColor("#FF0000"))
                }else{
                    tweetCount.setTextColor(Color.parseColor("#000000"))
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed

            }
        })
    }

    companion object{
        val TAG = "ComposeActivity"
    }
}