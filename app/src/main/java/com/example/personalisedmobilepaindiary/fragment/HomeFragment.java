package com.example.personalisedmobilepaindiary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.example.personalisedmobilepaindiary.Login;
import com.example.personalisedmobilepaindiary.Retrofit.RetrofitInterface;
import com.example.personalisedmobilepaindiary.databinding.HomeFragmentBinding;
import com.example.personalisedmobilepaindiary.model.WeatherResponse;
import com.example.personalisedmobilepaindiary.viewmodel.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    private SharedViewModel model;
    private HomeFragmentBinding addBinding;
    private FirebaseAuth mAuth;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        //initialise textView
        TextView textView = addBinding.tvResult;

        /*weather APi*/
        //build retrofit and convert json
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        //call
        Call<WeatherResponse> weatherResponseCall = retrofitInterface.getWeather();

        weatherResponseCall.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse weatherResponse = response.body();
                float temp = weatherResponse.getMain().temp;
                String name = weatherResponse.name;
                float humidity = weatherResponse.getMain().humidity;
                float pressure = weatherResponse.getMain().pressure;

                textView.setText("City: "+ name +"\n" +
                        "Temperature: " + temp + " °C" + "\n" +
                        "Humidity: "+ humidity + " %" + "\n" +
                        "Pressure: " + pressure + " hPa");
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });



        addBinding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }
            }
        });


        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}
