package com.example.personalisedmobilepaindiary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalisedmobilepaindiary.databinding.RvLayoutBinding;
import com.example.personalisedmobilepaindiary.entity.PainRecord;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter
        <RecyclerViewAdapter.ViewHolder> {
    private List<PainRecord> painRecords;
    public RecyclerViewAdapter(List<PainRecord> painRecords) {
        this.painRecords = painRecords;
    }
    //This method creates a new view holder that is constructed with a new View,inflated from a layout
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                                     parent, int viewType) {
        RvLayoutBinding binding=
                RvLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }
    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder
                                         viewHolder, int position) {
        final PainRecord painRecord = painRecords.get(position);
        /*viewHolder.binding.tvId.setText(painRecord.pid);*/
        viewHolder.binding.tvpainlevel.setText(painRecord.painLevel);
        viewHolder.binding.tvpainlocation.setText(painRecord.painLocation);
        viewHolder.binding.tvmood.setText(painRecord.moodLevel);
        viewHolder.binding.tvsteps.setText(painRecord.steps);
        viewHolder.binding.tvdate.setText(painRecord.date);
        viewHolder.binding.tvtemp.setText(Float.toString(painRecord.temp));
        viewHolder.binding.tvhum.setText(Float.toString(painRecord.humidity));
        viewHolder.binding.tvpre.setText(Float.toString(painRecord.pressure));
        viewHolder.binding.tvemail.setText(painRecord.email);
    }
    @Override
    public int getItemCount() {
        return painRecords.size();
    }

    public List<PainRecord> getPainRecords(){
        return painRecords;
    }

    public void setPainRecords(List<PainRecord> painRecords){
        this.painRecords = painRecords;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RvLayoutBinding binding;
        public ViewHolder(RvLayoutBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
