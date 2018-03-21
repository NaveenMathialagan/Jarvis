package com.example.naveen.jarvis.asynctask;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import com.example.naveen.jarvis.utils.StringAlter;
import com.example.naveen.jarvis.view.activity.ChatbotActivity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static com.example.naveen.jarvis.utils.Constants.Registration.NO_ANSWER;
import static com.example.naveen.jarvis.utils.Constants.Registration.TAG;

public class WikiAsyncTask extends AsyncTask<String, Void, String> {

    private static String keyword="";
    private String out = "";
    private String text="";
    private boolean fnds=false;

    @SuppressLint("StaticFieldLeak")
    private ChatbotActivity mChatbotActivity;

    public WikiAsyncTask(ChatbotActivity chatbotActivity){
        mChatbotActivity=chatbotActivity;
    }

    @Override
    protected String doInBackground(String... p) {
        try {
            URL url = new URL(p[0]);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);
            try {
                int event = myparser.getEventType();

                while (event != XmlPullParser.END_DOCUMENT) {
                    String name=myparser.getName();

                    switch (event){
                        case XmlPullParser.START_TAG:
                            if(name.equals("page")){
                                Log.d("nav","title found");
                                String key=myparser.getAttributeValue(null,"title");
                                Log.d("nav",key);
                                keyword=key;
                            }
                            break;

                        case XmlPullParser.TEXT:
                            text = myparser.getText();
                            break;

                        case XmlPullParser.END_TAG:
                            if(name.equals("rev")){
                                out=new StringAlter().alter(text,keyword);
                                Log.d("nav", out);
                                fnds=true;
                            }

                            break;
                    }
                    if (fnds){
                        break;
                    }else{
                        event = myparser.next();
                    }
                }
            }
            catch (Exception e){
                Log.d("nav","Exception 2\t"+e);
            }
            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }
    @Override
    protected void onPostExecute(String response)
    {
        Log.d(TAG,response);
        if (response.isEmpty()){
           mChatbotActivity.onResultReceived(NO_ANSWER);
        }else if (response.contains("#REDIRECT")) {
            Log.d("nav", "Redirected");
            response = response.replace("#REDIRECT", "");
            response = response.trim();
            keyword = response;
            mChatbotActivity.requestWiki(response);
        } else if (response.contains("#redirect")) {

            Log.d("nav", "Redirected");
            response = response.replace("#redirect", "");
            response = response.trim();
            keyword = response;
            mChatbotActivity.requestWiki(response);
        } else {
            Log.d("nav", "reponse before setText\t" + response);
            String spch = "";
            response = response.trim();
            response = response.replace("#", " ");
            int i = 0;
            String[] strary = response.split("\n");
            while (i < strary.length) {
                if (spch.length() < 800) {
                    spch = spch + strary[i] + "\n";
                    i++;
                } else {
                    break;
                }
            }
            mChatbotActivity.onResultReceived(spch);
            Log.d("nav", spch.length() + "\t" + i);
        }

        }
    }

