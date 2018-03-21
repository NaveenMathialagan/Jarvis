package com.example.naveen.jarvis.presenter;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.naveen.jarvis.presenter.ipresenter.IChatbotPresenter;
import com.example.naveen.jarvis.view.iview.IChatbotView;

import static com.example.naveen.jarvis.utils.Constants.Registration.MY_PERMISSIONS_RECORD_AUDIO;

public class ChatbotPresenter extends BasePresenter<IChatbotView> implements IChatbotPresenter{

    public ChatbotPresenter(IChatbotView iView) {
        super(iView);
    }

    @Override
    public void onCreatePresenter(Bundle bundle) {
        iView.getCodeSnippet().extractZipFile();
        checkPermission();
    }

    @Override
    public void onBackPressedPresenter() {

    }

    @Override
    public void onActivityResultPresenter(int requestCode, int resultCode, Intent data) {

    }

    public void checkPermission(){

        if (ContextCompat.checkSelfPermission(iView.getActivity(),
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(iView.getActivity(),
                    Manifest.permission.RECORD_AUDIO)) {
                iView.showMessage("Please grant permissions to record audio");

                ActivityCompat.requestPermissions(iView.getActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);

            } else {
                ActivityCompat.requestPermissions(iView.getActivity(),
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }
        }
    }
}
