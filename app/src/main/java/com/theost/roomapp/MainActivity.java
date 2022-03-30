package com.theost.roomapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.theost.roomapp.databinding.ActivityMainBinding;

import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = Room.databaseBuilder(this, AppDatabase.class, "my_database").build();

        binding.getButton.setOnClickListener(view -> loadUsers());
        binding.submitButton.setOnClickListener(view -> {
            String name = binding.inputView.getText().toString().trim();
            if (!name.equals("")) addUser(name);
        });
    }

    private void loadUsers() {
        compositeDisposable.add(database.getPersonDao().getPeople()
                .subscribeOn(Schedulers.io())
                .map(personEntities -> personEntities.stream()
                        .map(PersonEntity::mapToPerson)
                        .collect(Collectors.toList()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(people -> {
                    binding.outputView.setText(people.toString());
                }, throwable -> {
                    binding.outputView.setText(getString(R.string.error));
                }));
    }

    private void addUser(String name) {
        compositeDisposable.add(database.getPersonDao().insertPerson(new PersonEntity(name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    binding.outputView.setText(getString(R.string.done));
                }, throwable -> {
                    binding.outputView.setText(getString(R.string.error));
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}