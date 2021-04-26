package com.example.learningwords.ui.home;

import android.os.Build;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Integer> sdkBuild;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        sdkBuild = new MutableLiveData<>();
        sdkBuild.setValue(Build.VERSION.SDK_INT);

    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Integer> getSdkBuild(){
        return sdkBuild;
    }
}