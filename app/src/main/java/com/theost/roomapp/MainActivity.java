package com.theost.roomapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.theost.roomapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
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

        // SharedPreferences
        int counter = PrefUtils.getInteger(this, PrefUtils.PREF_KEY_COUNTER);
        PrefUtils.putInteger(this, PrefUtils.PREF_KEY_COUNTER, counter + 1);
        if (counter > 2)
            binding.outputView.setText(String.format(getString(R.string.session), counter));

        // Room
        database = Room.databaseBuilder(this, AppDatabase.class, "my_database").build();

        binding.getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.loadUsers();
            }
        });
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.inputView.getText().toString().trim();
                if (!name.equals("")) MainActivity.this.addUser(name);
            }
        });

        binding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.deleteAll();
            }
        });


        binding.filterToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.sortById();
            }
        });

    }

    private void loadUsers() {
        compositeDisposable.add(database.getPersonDao().getPeople()
                .subscribeOn(Schedulers.io())
                .map(personEntities -> {
                    List<Person> list = new ArrayList<>();
                    for (PersonEntity personEntity : personEntities) {
                        Person mapToPerson = personEntity.mapToPerson();
                        list.add(mapToPerson);
                    }
                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(people -> binding.outputView.setText(people.toString()), throwable -> binding.outputView.setText(MainActivity.this.getString(R.string.error))));
    }

    private void addUser(String name) {
        compositeDisposable.add(database.getPersonDao().insertPerson(new PersonEntity(name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> binding.outputView.setText(MainActivity.this.getString(R.string.done)), throwable -> binding.outputView.setText(MainActivity.this.getString(R.string.error))));
    }


    private void deleteAll() {
        compositeDisposable.add(database.getPersonDao().deleteAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> binding.outputView.setText(MainActivity.this.getString(R.string.done)), throwable -> binding.outputView.setText(MainActivity.this.getString(R.string.error))));
    }

    private void sortById() {
        compositeDisposable.add(database.getPersonDao().sort()
                .subscribeOn(Schedulers.io())
                .map(personEntities -> {
                    List<Person> list = new ArrayList<>();
                    for (PersonEntity personEntity : personEntities) {
                        Person mapToPerson = personEntity.mapToPerson();
                        list.add(mapToPerson);
                    }
                    return list;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(people -> binding.outputView.setText(people.toString()), throwable -> binding.outputView.setText(MainActivity.this.getString(R.string.error))));
    }
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}