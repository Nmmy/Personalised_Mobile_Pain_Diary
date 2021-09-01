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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalisedmobilepaindiary.adapter.RecyclerViewAdapter;
import com.example.personalisedmobilepaindiary.databinding.DailyRecordBinding;
import com.example.personalisedmobilepaindiary.entity.PainRecord;
import com.example.personalisedmobilepaindiary.painecordviewmodel.PainRecordViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class DailyRecord extends Fragment {
    private DailyRecordBinding binding;
    private RecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<PainRecord> painRecords;
    private List<PainRecord> newPainRecords;
    private PainRecordViewModel painRecordViewModel;
    private FirebaseAuth mAuth;

    public DailyRecord(){}

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        binding = DailyRecordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        painRecords  = new ArrayList<>();
        painRecordViewModel =
                new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);

        //get current user email
        String currentUserEmail = mAuth.getCurrentUser().getEmail();
        //pass data to adapter
        adapter = new RecyclerViewAdapter(painRecords);

        //creates a line divider between rows
        binding.recyclerView.addItemDecoration(new
                DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        binding.recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);



        //get all pain records from Room database
        painRecordViewModel.selectAll(currentUserEmail).thenApply(v ->{
            /*painRecords.clear();*/
            painRecords = v;
            adapter = new RecyclerViewAdapter(painRecords);
            binding.recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return painRecords;
        });




        //if data changed
/*        painRecordViewModel.getAllPainRecords().observe(getViewLifecycleOwner(), new Observer<List<PainRecord>>() {
            @Override
            public void onChanged(List<PainRecord> painRecords) {
                adapter.setPainRecords(painRecords);
                adapter.notifyDataSetChanged();
            }
        });*/

        //delete all records
        binding.deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                painRecordViewModel.deleteAll();
                Toast.makeText(getActivity(), "All records were deleted.",
                        Toast.LENGTH_LONG).show();
            }
        });

        //delete one record
        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                painRecordViewModel.deleteById(Integer.parseInt(binding.EdId.getText().toString()));

                Toast.makeText(getActivity(), "The record was deleted.",
                        Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
