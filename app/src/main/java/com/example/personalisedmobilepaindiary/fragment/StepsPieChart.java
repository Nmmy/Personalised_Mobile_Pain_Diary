package com.example.personalisedmobilepaindiary.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.example.personalisedmobilepaindiary.databinding.StepsPieChartBinding;
import com.example.personalisedmobilepaindiary.entity.PainRecord;
import com.example.personalisedmobilepaindiary.painecordviewmodel.PainRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StepsPieChart extends Fragment {

    private StepsPieChartBinding binding;
    private String steps;
    private String goal;

    private FirebaseAuth mAuth;
    private PainRecordViewModel painRecordViewModel;

    public StepsPieChart() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        binding = StepsPieChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        painRecordViewModel =
                new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);

        final Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        String currentDate = "Today is: " + currentYear + "-" + currentMonth + "-" + currentDay;
        binding.tvDate.setText(currentDate);

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

        //get steps/gaol data
        String email = mAuth.getCurrentUser().getEmail();
        Date date = new Date();
        String today = date.toString().substring(8,10);
        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            List<DataEntry> data = new ArrayList<>();
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                for (PainRecord temp : painRecords) {
                    if(email.equalsIgnoreCase(temp.email) &&
                            Integer.parseInt(temp.date.substring(8,10)) == Integer.parseInt(today)){
                        String[] str_array = temp.steps.split("/");
                        steps = str_array[0];
                        goal = str_array[1];
                    }
                }
                if(steps!= null){
                    data = new ArrayList<>();
                    data.add(new ValueDataEntry("Today steps", Integer.parseInt(steps)));
                    data.add(new ValueDataEntry("Remaining steps", Integer.parseInt(goal)-Integer.parseInt(steps)));
                    pie.data(data);
                }
                else{
                    Toast.makeText(getActivity(), "There's no data today!", Toast.LENGTH_SHORT).show();
                }

                pie.title("Today steps taken and remaining steps");

                pie.labels().position("inside").textDirection("middle");

                pie.legend().title().enabled(true);
                pie.legend().title()
                        .text("Pain location")
                        .fontSize(20)
                        .padding(0d, 0d, 10d, 0d);

                pie.legend()
                        .position("bottom")
                        .fontSize(18)
                        .itemsLayout(LegendLayout.HORIZONTAL)
                        .align(Align.CENTER);

                pie.innerRadius("60%");
                anyChartView.setChart(pie);
            }
        });

        return view;
    }

}

