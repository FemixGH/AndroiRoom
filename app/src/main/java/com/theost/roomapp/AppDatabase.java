package com.theost.roomapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PersonEntity.class}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao getPersonDao();
}
