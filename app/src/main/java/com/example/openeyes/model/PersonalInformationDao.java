package com.example.openeyes.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.openeyes.bean.PersonalInformation;

@Dao
public interface PersonalInformationDao {

    @Query("SELECT * FROM personal_information WHERE count IS :count")
    PersonalInformation query(String count);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PersonalInformation item);

    @Delete
    void delete(PersonalInformation item);
}
