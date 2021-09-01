package com.example.personalisedmobilepaindiary.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.personalisedmobilepaindiary.Retrofit.RetrofitInterface;



public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> mText;
    private RetrofitInterface retrofitInterface;


    public SharedViewModel(){
        mText = new MutableLiveData<>();
    }

    public void setMessage(String message) {
        mText.setValue(message);
    }
    public LiveData<String> getText() {
        return mText;
    }
    }




