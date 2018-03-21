package com.example.naveen.jarvis.utils;


import android.content.Context;
import android.speech.SpeechRecognizer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CodeSnippet {

    private Context mContext;

    public CodeSnippet(Context context){
        this.mContext=context;
    }

    public void extractZipFile() {
        File fileExt = new File(mContext.getExternalFilesDir(null).getAbsolutePath() + "/bots");
        if (!fileExt.exists()) {

            String outputFolder = mContext.getExternalFilesDir(null).getAbsolutePath() + "/";
            try {
                ZipInputStream zin = new ZipInputStream(mContext.getAssets().open("bots.zip"));
                ZipEntry entry = null;
                int bytesRead;
                byte[] buffer = new byte[4096];

                while ((entry = zin.getNextEntry()) != null) {
                    if (entry.isDirectory()) {
                        File dir = new File(outputFolder, entry.getName());
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                    } else {
                        FileOutputStream fos = new FileOutputStream(outputFolder + entry.getName());
                        while ((bytesRead = zin.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        fos.close();
                    }
                }
                zin.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
