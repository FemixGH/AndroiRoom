package com.theost.roomapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "people")
public class PersonEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public PersonEntity(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person mapToPerson() {
        return new Person(id, name);
    }

}
