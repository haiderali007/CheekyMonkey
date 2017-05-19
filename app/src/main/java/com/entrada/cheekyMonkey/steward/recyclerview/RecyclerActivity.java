package com.entrada.cheekyMonkey.steward.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.entrada.cheekyMonkey.R;

import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity {

    ArrayList<String> SubjectNames;
    RecyclerView recyclerview;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);

        recyclerview = (RecyclerView)findViewById(R.id.recyclerview1);

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerview.setLayoutManager(RecyclerViewLayoutManager);

        AddItemsToRecyclerViewArrayList();

        RecyclerView.Adapter adapter = new RecyclerViewAdapter(SubjectNames);

        recyclerview.setAdapter(adapter);

    }

    public void AddItemsToRecyclerViewArrayList(){

        SubjectNames = new ArrayList<>();
        SubjectNames.add("ANDROID");
        SubjectNames.add("PHP");
        SubjectNames.add("BLOGGER");
        SubjectNames.add("CSS");
        SubjectNames.add("MATHS");
        SubjectNames.add("ASP.NET");
        SubjectNames.add("JAVA");
        SubjectNames.add("C++");
        SubjectNames.add("HTML");
        SubjectNames.add("PHOTOSHOP");

    }

}