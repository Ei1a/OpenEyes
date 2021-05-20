package com.example.openeyes.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.openeyes.bean.PersonalCount;

@Dao
public interface PersonalCountDao {

    @Query("SELECT count FROM personal_count WHERE count IS :count")
    String queryCount(String count);

    @Query("SELECT count FROM personal_count WHERE count IS :count  AND password IS :password")
    String confirmPassword(String count, String password);

    @Query("SELECT count FROM personal_count WHERE recently_login = 1")
    String queryRecentlyLogin();

    @Query("SELECT password FROM personal_count WHERE count IS :count")
    String queryPassword(String count);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PersonalCount item);
}
