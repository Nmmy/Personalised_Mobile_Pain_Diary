package com.example.personalisedmobilepaindiary.painecordviewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.personalisedmobilepaindiary.entity.PainRecord;
import com.example.personalisedmobilepaindiary.model.HumPain;
import com.example.personalisedmobilepaindiary.model.PainCount;
import com.example.personalisedmobilepaindiary.model.PrePain;
import com.example.personalisedmobilepaindiary.model.TempPain;
import com.example.personalisedmobilepaindiary.repository.PainRecordRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PainRecordViewModel extends AndroidViewModel {
    private PainRecordRepository cRepository;
    private LiveData<List<PainRecord>> allPainRecords;
    public PainRecordViewModel (Application application) {
        super(application);
        cRepository = new PainRecordRepository(application);
        allPainRecords = cRepository.getAllPainRecords();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByIDFuture(final int pId){
        return cRepository.findByIDFuture(pId);
    }
    public LiveData<List<PainRecord>> getAllPainRecords() {
        return allPainRecords;
    }
    public void insert(PainRecord painRecord) {
        cRepository.insert(painRecord);
    }

    public void deleteAll() {
        cRepository.deleteAll();
    }
    public void update(PainRecord painRecord) {
        cRepository.updateCustomer(painRecord);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<List<PainRecord>> selectAll(final String currentUserEmail){
        /*allPainRecords = cRepository.getAllPainRecords();*/
        return cRepository.selectAll(currentUserEmail);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<List<PainCount>> painCount(final String currentUserEmail){
        return cRepository.painCount(currentUserEmail);
    }

    public LiveData<PainRecord> getTodaySteps(final String currentUserEmail,
                                final String today) {
        return cRepository.getTodaySteps(currentUserEmail, today);
    }

    public void deleteById(final int pId){
        cRepository.deleteById(pId);
    }
}
