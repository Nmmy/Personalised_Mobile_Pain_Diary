package com.example.personalisedmobilepaindiary.fragment;

import android.app.DatePickerDialog;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.anychart.scales.Linear;

import com.example.personalisedmobilepaindiary.R;
import com.example.personalisedmobilepaindiary.databinding.PainWeatherLineChartBinding;
import com.example.personalisedmobilepaindiary.entity.PainRecord;
import com.example.personalisedmobilepaindiary.painecordviewmodel.PainRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class PainWeatherLineChart extends Fragment{

    private PainWeatherLineChartBinding binding;
    private Date userStartDate;
    private Date userEndDate;
    private FirebaseAuth mAuth;
    private PainRecordViewModel painRecordViewModel;
    private Set set;


    public PainWeatherLineChart() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        binding = PainWeatherLineChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        painRecordViewModel =
                new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);

        //Spinner
        binding.spWeather.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] weatherItem = getResources().getStringArray(R.array.weather);

                Toast.makeText(getContext(), "You select :" + weatherItem[pos], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "You must select one item", Toast.LENGTH_LONG).show();
            }
        });




        binding.btnDateStart.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(year, month, dayOfMonth);
                    userStartDate = calendar.getTime();
                    if(userStartDate!=null){
                        binding.edDateStart.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                    else{
                        Toast.makeText(getActivity(), "The start date should before the end date, please try again.", Toast.LENGTH_LONG).show();
                    }

                }
            }, currentYear, currentMonth, currentDay
            );
            datePickerDialog.show();

        });

        binding.btnDateEnd.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    calendar.set(year, month, dayOfMonth);
                    userEndDate = calendar.getTime();
                    if(userStartDate!=null && userEndDate!=null && userStartDate.before(userEndDate)){
                        binding.edDateEnd.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                    else{
                        Toast.makeText(getActivity(), "The start date should before the end date, please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }, currentYear, currentMonth, currentDay
            );
            datePickerDialog.show();

        });

        binding.btnConfirm.setOnClickListener(v->{
            if(userStartDate!=null && userEndDate!=null) {
                String select = binding.spWeather.getSelectedItem().toString();
                createLineChart(select, userStartDate.toString(), userEndDate.toString());
            }
            else{
                Toast.makeText(getActivity(), "Please select start and end date!", Toast.LENGTH_LONG).show();
            }

        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createLineChart(String select, String startDate, String endDate) {
        binding.anyChartView.setProgressBar(binding.progressBar);

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Pain and weather relation line chart");

        Linear scale1 = Linear.instantiate();
        //pain level y axes
        Linear scale2 = Linear.instantiate();
        scale2.minimum(0);
        scale2.maximum(10);
        cartesian.yAxis(0).title("Pain Level").orientation("left").scale(scale2);
        cartesian.xAxis(0).title("Day of month").labels().padding(5d, 5d, 5d, 5d);
        set = Set.instantiate();


        //get all data
        String email = mAuth.getCurrentUser().getEmail();

        painRecordViewModel =
                new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);

        String sd = startDate.substring(8,10);
        String ed = endDate.substring(8,10);

        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {

                List<PainRecord> selectedPainRecords = new ArrayList<>();
                for (PainRecord temp : painRecords) {
                    if(email.equalsIgnoreCase(temp.email) && Integer.parseInt(temp.date.substring(8,10)) >= Integer.parseInt(sd)
                            && Integer.parseInt(temp.date.substring(8,10)) <= Integer.parseInt(ed)){
                        selectedPainRecords.add(temp);
                    }
                }
                Collections.sort(selectedPainRecords, new Comparator<PainRecord>() {
                    public int compare(PainRecord obj1, PainRecord obj2) {
                        // ## Ascending order
                        return Integer.valueOf(obj1.date.substring(8,10)).compareTo(Integer.valueOf(obj2.date.substring(8,10))); // To compare integer values
                    }
                });
                if (select.equalsIgnoreCase("temperature")) {
                    List<DataEntry> seriesData = new ArrayList<>();
                    //weather y axes
                    //set scale for two y axes
                    scale1.minimum(0);
                    scale1.maximum(30);
                    cartesian.yAxis(1).title("Temperature").orientation("right").scale(scale1);
                    for (PainRecord temp: selectedPainRecords){
                        seriesData.add(new CustomDataEntry(temp.date.substring(8,10), temp.temp, Integer.parseInt(temp.painLevel)));
                    }
                    set.data(seriesData);
                }
                else if (select.equalsIgnoreCase("humidity")){
                    List<DataEntry> seriesData = new ArrayList<>();
                    //weather y axes
                    //set scale for y axes
                    scale1.minimum(0);
                    scale1.maximum(150);
                    cartesian.yAxis(1).title("Humidity").orientation("right").scale(scale1);
                    for (PainRecord temp: selectedPainRecords){
                        seriesData.add(new CustomDataEntry(temp.date.substring(8,10), temp.humidity, Integer.parseInt(temp.painLevel)));
                    }
                    set.data(seriesData);
                }
                else if (select.equalsIgnoreCase("pressure")){
                    List<DataEntry> seriesData = new ArrayList<>();
                    //weather y axes
                    //set scale for y axes
                    scale1.minimum(900);
                    scale1.maximum(1300);
                    cartesian.yAxis(1).title("Pressure").orientation("right").scale(scale1);
                    for (PainRecord temp: selectedPainRecords){
                        seriesData.add(new CustomDataEntry(temp.date.substring(8,10), temp.pressure, Integer.parseInt(temp.painLevel)));
                    }
                    set.data(seriesData);
                }

                Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value1' }");
                Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

                Line series1 = cartesian.line(series1Mapping);
                series1.name("Weather");
                series1.yScale(scale1);
                series1.hovered().markers().enabled(true);
                series1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5d)
                        .offsetY(5d);

                Line series2 = cartesian.line(series2Mapping);
                series2.name("Pain").color("red");
                series2.yScale(scale2);
                series2.hovered().markers().enabled(true);
                series2.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series2.tooltip()
                        .position("left")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5d)
                        .offsetY(5d);



                cartesian.legend().enabled(true);
                cartesian.legend().fontSize(13d);
                cartesian.legend().padding(0d, 0d, 10d, 0d);

                binding.anyChartView.setChart(cartesian);


            }
        });
    }



    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value1, Number value2) {
            super(x, value1);
            setValue("value2", value2);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
