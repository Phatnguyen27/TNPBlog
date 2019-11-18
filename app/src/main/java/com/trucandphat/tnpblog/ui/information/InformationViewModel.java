package com.trucandphat.tnpblog.ui.information;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InformationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InformationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}