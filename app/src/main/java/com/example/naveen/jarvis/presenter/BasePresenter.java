package com.example.naveen.jarvis.presenter;


import com.example.naveen.jarvis.presenter.ipresenter.IBasePresenter;
import com.example.naveen.jarvis.view.iview.IBaseView;

abstract class BasePresenter<T extends IBaseView> implements IBasePresenter {

    T iView;

    BasePresenter(T iView)
    {
        this.iView = iView;
    }


    @Override
    public void onStartPresenter() {

    }

    @Override
    public void onStopPresenter() {

    }

    @Override
    public void onPausePresenter() {

    }

    @Override
    public void onResumePresenter() {

    }

    @Override
    public void onDestroyPresenter() {

    }
}