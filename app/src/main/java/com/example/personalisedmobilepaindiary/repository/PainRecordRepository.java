package com.example.personalisedmobilepaindiary.repository;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.example.personalisedmobilepaindiary.dao.PainRecordDAO;
import com.example.personalisedmobilepaindiary.database.PainRecordDatabase;
import com.example.personalisedmobilepaindiary.entity.PainRecord;
import com.example.personalisedmobilepaindiary.model.HumPain;
import com.example.personalisedmobilepaindiary.model.PainCount;
import com.example.personalisedmobilepaindiary.model.PrePain;
import com.example.personalisedmobilepaindiary.model.TempPain;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;


public class PainRecordRepository {
    private PainRecordDAO painRecordDao;
    private LiveData<List<PainRecord>> allPainRecords;
    public PainRecordRepository(Application application){
        PainRecordDatabase db = PainRecordDatabase.getInstance(application);
        painRecordDao =db.painRecordDao();
        allPainRecords= painRecordDao.getAll();
    }
    // Room executes this query on a separate thread
    public LiveData<List<PainRecord>> getAllPainRecords() {
        return allPainRecords;
    }
    public  void insert(final PainRecord painRecord){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.insert(painRecord);
            }
        });
    }
    public void deleteAll(){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.deleteAll();
            }
        });
    }
    public void delete(final PainRecord painRecord){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.delete(painRecord);
            }
        });
    }
    public void updateCustomer(final PainRecord painRecord){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.updatePainRecord(painRecord);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<List<PainRecord>> selectAll(final String currentUserEmail) {
        return CompletableFuture.supplyAsync(new Supplier<List<PainRecord>>() {
            @Override
            public List<PainRecord> get() {
                return painRecordDao.selectAll(currentUserEmail);
            }
        }, PainRecordDatabase.databaseWriteExecutor);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<PainRecord> findByIDFuture(final int pId) {
        return CompletableFuture.supplyAsync(new Supplier<PainRecord>() {
            @Override
            public PainRecord get() {
                return painRecordDao.findByID(pId);
            }
        }, PainRecordDatabase.databaseWriteExecutor);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<List<PainCount>> painCount(final String currentUserEmail) {
        return CompletableFuture.supplyAsync(new Supplier<List<PainCount>>() {
            @Override
            public List<PainCount> get() {
                return painRecordDao.painCount(currentUserEmail);
            }
        });
    }

    public LiveData<PainRecord> getTodaySteps(final String currentUserEmail,
                                           final String today) {
        return painRecordDao.getTodaySteps(currentUserEmail, today);
    }


    public void deleteById(final int pid){
        PainRecordDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                painRecordDao.deleteById(pid);
            }
        });
    }



}
