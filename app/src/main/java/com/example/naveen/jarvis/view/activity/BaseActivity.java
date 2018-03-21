package com.example.naveen.jarvis.view.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.example.naveen.jarvis.presenter.ipresenter.IBasePresenter;
import com.example.naveen.jarvis.utils.CodeSnippet;
import com.example.naveen.jarvis.view.iview.IBaseView;

abstract class BaseActivity<T extends IBasePresenter> extends AppCompatActivity implements IBaseView {

    protected View mParentView;
    protected T iPresenter;
    protected CodeSnippet mCodeSnippet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iPresenter = bindView(savedInstanceState);
        iPresenter.onCreatePresenter(getIntent().getExtras());
    }

    @NonNull
    abstract T bindView(@Nullable Bundle savedInstanceState);

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        mParentView = getWindow().getDecorView().findViewById(android.R.id.content);
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public CodeSnippet getCodeSnippet() {
        if (mCodeSnippet == null) {
            mCodeSnippet = new CodeSnippet(this);
            return mCodeSnippet;
        }
        return mCodeSnippet;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (iPresenter != null) iPresenter.onStartPresenter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (iPresenter != null) iPresenter.onStopPresenter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (iPresenter != null) iPresenter.onPausePresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (iPresenter != null) iPresenter.onResumePresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iPresenter != null) iPresenter.onDestroyPresenter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (iPresenter != null) iPresenter.onBackPressedPresenter();
    }

    @Override
    public AppCompatActivity getActivity() {
        return BaseActivity.this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (iPresenter!=null)
            iPresenter.onActivityResultPresenter(requestCode, resultCode, data);
    }
}