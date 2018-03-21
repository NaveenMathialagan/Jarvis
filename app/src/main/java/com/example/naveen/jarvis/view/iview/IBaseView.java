package com.example.naveen.jarvis.view.iview;


import android.support.v7.app.AppCompatActivity;

import com.example.naveen.jarvis.utils.CodeSnippet;

public interface IBaseView {
    void showMessage(String message);
    AppCompatActivity getActivity();
    CodeSnippet getCodeSnippet();
}
