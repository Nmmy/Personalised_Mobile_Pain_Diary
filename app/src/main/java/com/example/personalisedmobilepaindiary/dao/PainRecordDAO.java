package com.example.personalisedmobilepaindiary.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.personalisedmobilepaindiary.entity.PainRecord;
import com.example.personalisedmobilepaindiary.model.HumPain;
import com.example.personalisedmobilepaindiary.model.PainCount;
import com.example.personalisedmobilepaindiary.model.PrePain;
import com.example.personalisedmobilepaindiary.model.TempPain;

import java.util.Date;
import java.util.List;

@Dao
public interface PainRecordDAO {
    @Query("SELECT * FROM painRecord ORDER BY date DESC")
    LiveData<List<PainRecord>> getAll();
    @Query("SELECT * FROM painRecord WHERE pid = :painRecordId LIMIT 1")
    PainRecord findByID(int painRecordId);
    @Insert
    void insert(PainRecord painRecord);
    @Delete
    void delete(PainRecord painRecord);
    @Query("DELETE FROM painrecord where pid=:pid")
    void deleteById(int pid);
    @Update
    void updatePainRecord(PainRecord painRecord);
    @Query("DELETE FROM painrecord")
    void deleteAll();
    @Query("SELECT * FROM painRecord where painRecord.email = :currentUserEmail order by date DESC")
    List<PainRecord> selectAll(String currentUserEmail);

    @Query("SELECT * FROM painRecord where painRecord.email = :currentUserEmail AND date = :today")
    LiveData<PainRecord> getTodaySteps(String currentUserEmail, String today);

    @Query("SELECT COUNT(pain_location) as count, pain_location FROM painRecord where painRecord.email = :currentUserEmail group by pain_location")
    List<PainCount> painCount(String currentUserEmail);

}
