package com.example.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.sharedpreferences", Context.MODE_PRIVATE);

        ArrayList<String> friends = new ArrayList<>();
        friends.add("Joe");
        friends.add("Micah");
        friends.add("Hope");

//        try
//        {
//            sharedPreferences.edit().putString("friends", ObjectSerializer.serialize(friends)).apply();
//            Log.i("Friends serialized", ObjectSerializer.serialize(friends));
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }

        try
        {
            ArrayList<String> newFriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("friends", ""));
            Log.i("New friends", newFriends.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
