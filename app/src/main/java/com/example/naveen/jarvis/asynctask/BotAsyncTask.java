package com.example.naveen.jarvis.asynctask;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.example.naveen.jarvis.view.activity.ChatbotActivity;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;


public class BotAsyncTask extends AsyncTask<String,Void,String> {

    @SuppressLint("StaticFieldLeak")
    private ChatbotActivity mResultListener;
    public BotAsyncTask(ChatbotActivity chatbotActivity){
        mResultListener=chatbotActivity;
    }
    @Override
    protected String doInBackground(String... strings) {
        Bot bot = new Bot("subjects", strings[1]);
        Chat chatSession = new Chat(bot);
        String response = chatSession.multisentenceRespond(strings[0]);
        Log.v("nhh", "response = " + response);
        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mResultListener.onResultReceived(s);
    }
}
