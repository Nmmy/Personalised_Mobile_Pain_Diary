package com.example.personalisedmobilepaindiary.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.personalisedmobilepaindiary.databinding.ReportsBinding;
import com.example.personalisedmobilepaindiary.painecordviewmodel.PainRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Reports extends Fragment {
    private ReportsBinding binding;
    private PainRecordViewModel painRecordViewModel;
    private FirebaseAuth mAuth;

    public Reports(){}
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the View for this fragment
        binding = ReportsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        painRecordViewModel =
                new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);

        //AnyChart
        AnyChartView anyChartView = binding.anyChartView;
        anyChartView.setProgressBar(binding.progressBar);

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        //get all data
        String email = mAuth.getCurrentUser().getEmail();
        painRecordViewModel.painCount(email).thenApply(model->{
            List<DataEntry> data = new ArrayList<>();
            model.forEach(v->{
                data.add(new ValueDataEntry(v.getPain_location(), v.getCount()));

            });


            pie.data(data);

            pie.title("Pain location and frequencies");

            pie.labels().position("inside").textDirection("middle");

            pie.legend().title().enabled(true);
            pie.legend().title()
                    .text("Pain location")
                    .fontSize(20)
                    .padding(0d, 0d, 10d, 0d);

            pie.legend().position("center-top")
                    .fontSize(18)
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER);

            return model;
        });




        anyChartView.setChart(pie);


        return view;
    }

}
