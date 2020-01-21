package com.example.multiplicationtables;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Integer> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<Integer>();
        final ListView multiplicationTable = findViewById(R.id.multiplicationTable);
        SeekBar baseNumberSeekbar = findViewById(R.id.baseNumberSeekBar);
        baseNumberSeekbar.setMax(100);
        final TextView curBaseNumberText = findViewById(R.id.curBaseNumber);

        baseNumberSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                curBaseNumberText.setText(Integer.toString(progress));

                productList.clear();
                for(int i = 1; i <= 10; i++)
                {
                    productList.add(i * progress);
                }

                ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, productList);
                multiplicationTable.setAdapter(arrayAdapter);

                Log.i("INFO", productList.toString());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }
}
