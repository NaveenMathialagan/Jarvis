package com.example.naveen.jarvis.presenter.ipresenter;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Naveen on 05-03-2018.
 */

public interface IBasePresenter {
    void onCreatePresenter(Bundle bundle);

    void onStartPresenter();

    void onStopPresenter();

    void onPausePresenter();

    void onResumePresenter();

    void onDestroyPresenter();

    void onBackPressedPresenter();

    void onActivityResultPresenter(int requestCode, int resultCode, Intent data);
}
