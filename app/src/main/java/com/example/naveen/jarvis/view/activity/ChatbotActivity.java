package com.example.naveen.jarvis.view.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.naveen.jarvis.R;
import com.example.naveen.jarvis.asynctask.BotAsyncTask;
import com.example.naveen.jarvis.asynctask.WikiAsyncTask;
import com.example.naveen.jarvis.listener.ResultListener;
import com.example.naveen.jarvis.presenter.ChatbotPresenter;
import com.example.naveen.jarvis.presenter.ipresenter.IChatbotPresenter;
import com.example.naveen.jarvis.view.iview.IChatbotView;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.naveen.jarvis.utils.Constants.Registration.NO_ANSWER;
import static com.example.naveen.jarvis.utils.Constants.Registration.PREFIX_URL;
import static com.example.naveen.jarvis.utils.Constants.Registration.REPEAT;
import static com.example.naveen.jarvis.utils.Constants.Registration.SUFFIX_URL;
import static com.example.naveen.jarvis.utils.Constants.Registration.TAG;

public class ChatbotActivity extends BaseActivity<IChatbotPresenter> implements IChatbotView,RecognitionListener,ResultListener {

    private ToggleButton mToogleButton;
    private ProgressBar mProgressBar;
    private MediaPlayer mMediaPlayer;
    private TextView vTQuery,vTResult;
    private TextToSpeech mTextToSpeech;
    private Intent mRecognizerIntent;
    private SpeechRecognizer mSpeechRecognizer;
    private static String mQuery;
    @NonNull
    @Override
    IChatbotPresenter bindView(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chatbot);
        varriableInitialization();
        ChatbotPresenter mChatbotPresenter = new ChatbotPresenter(this);
        return mChatbotPresenter;
    }

    private void varriableInitialization(){
        mToogleButton=findViewById(R.id.toggle_button);
        mProgressBar=findViewById(R.id.progressbar);
        vTQuery=findViewById(R.id.query_text);
        vTResult=findViewById(R.id.result);
        mTextToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    Log.d("nav","On Iniit");
                    mTextToSpeech.setLanguage(Locale.US);
                    mTextToSpeech.speak("Hellow! how can I help you",TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        mMediaPlayer=new MediaPlayer();
        mProgressBar.setVisibility(View.INVISIBLE);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(this);
        mRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        mToogleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){
                    if (mTextToSpeech.isSpeaking()){
                        mTextToSpeech.stop();
                    }
                    if(mMediaPlayer.isPlaying()){
                        mMediaPlayer.stop();
                    }
                    Thread t=Thread.currentThread();
                    t.interrupt();
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setIndeterminate(true);
                    mSpeechRecognizer.startListening(mRecognizerIntent);

                }else {
                    mProgressBar.setIndeterminate(false);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mSpeechRecognizer.stopListening();
                }
            }
        });

        startInstruction();
    }


    private void startInstruction(){
        try {
            AssetFileDescriptor mAssetFileDescriptor = getAssets().openFd("sample.mp3");
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(mAssetFileDescriptor.getFileDescriptor(), mAssetFileDescriptor.getStartOffset(), mAssetFileDescriptor.getLength());
                mMediaPlayer.prepare();
               /// mMediaPlayer.start();
        }
        catch (Exception io){
            Log.d("TAG",io.toString());
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {
        mProgressBar.setIndeterminate(false);
        mProgressBar.setMax(10);
    }

    @Override
    public void onRmsChanged(float v) {
        mProgressBar.setProgress((int) v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        mProgressBar.setIndeterminate(true);
        mToogleButton.setChecked(false);
    }

    @Override
    public void onError(int i) {
        String errorMessage = getCodeSnippet().getErrorText(i);
        vTQuery.setText(errorMessage);
        mTextToSpeech.speak(errorMessage, TextToSpeech.QUEUE_FLUSH, null);
        mToogleButton.setChecked(false);
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> matches = bundle
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        mQuery=matches.get(0);
        if (mQuery.toLowerCase().equals(REPEAT) && !vTResult.getText().toString().equals(getString(R.string.result))){
            provideResult(vTResult.getText().toString());
        }else {
            vTQuery.setText(matches.get(0));
            Log.d(TAG, matches.get(0));
            BotAsyncTask botAsyncTask = new BotAsyncTask(this);
            botAsyncTask.execute(matches.get(0), getExternalFilesDir(null).getAbsolutePath());
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    public void onResultReceived(String result) {
        if (result.equals(NO_ANSWER)){
            requestWiki(mQuery);
        }else {
           provideResult(result);
        }
    }

    @Override
    public void requestWiki(String keyword) {
        String URL;

        keyword=keyword.replaceAll(" ", "%20");
        URL = PREFIX_URL + keyword + SUFFIX_URL;
        Log.d("nav","call function called");
        Log.d("nav",URL);
        WikiAsyncTask wikiAsyncTask=new WikiAsyncTask(this);
        wikiAsyncTask.execute(URL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTextToSpeech.isSpeaking()){
            mTextToSpeech.stop();
        }
    }

    private void provideResult(String result){
        vTResult.setText(result);
        mTextToSpeech.speak(result, TextToSpeech.QUEUE_FLUSH, null);
    }
}
