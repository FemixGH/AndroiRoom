package com.theost.roomapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PersonDao {

    @Insert
    Completable insertPerson(PersonEntity personEntity);

    @Query("SELECT * FROM people")
    Single<List<PersonEntity>> getPeople();

    @Query("DELETE FROM people")
    Completable deleteAll();

    @Query("SELECT * FROM people WHERE (id > 2)")
    Single<List<PersonEntity>> sort();
}
