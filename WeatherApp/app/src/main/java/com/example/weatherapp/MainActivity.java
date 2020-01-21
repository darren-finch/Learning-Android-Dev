package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity
{
    EditText cityName;
    TextView weatherText;
    TextView descriptionText;
    TextView tempText;
    TextView windSpeedText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        weatherText = findViewById(R.id.weatherText);
    }

    public void getWeather(View view)
    {
        try
        {
            WeatherDownloader downloader = new WeatherDownloader();
            String cityNameText = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            downloader.execute("https://openweathermap.org/data/2.5/weather?q=" + cityNameText + "&appid=b6907d289e10d714a6e88b30761fae22").get();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Couldn't get the weather! Check your spacing and capitalization.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);
    }

    public class WeatherDownloader extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            String result = "";
            try
            {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1)
                {
                    char curChar = (char) data;
                    result += curChar;
                    data = reader.read();
                }

                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "FAILED";
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            String[] weatherInfoArr = new String[] {"", "", "", ""};

            try
            {
                JSONObject jsonObj = new JSONObject(s);
                JSONObject mainInfo = new JSONObject(jsonObj.getString("main"));
                JSONObject windInfo = new JSONObject(jsonObj.getString("wind"));

                String weatherInfo = jsonObj.getString("weather");
                JSONArray weatherArray = new JSONArray(weatherInfo);

                for(int i = 0; i < weatherArray.length(); i++)
                {
                    JSONObject jsonPart = weatherArray.getJSONObject(i);
                    weatherInfoArr[0] = jsonPart.getString("main");
                    weatherInfoArr[1] = jsonPart.getString("description");
                }

                weatherInfoArr[2] = mainInfo.getString("temp");
                weatherInfoArr[3] = windInfo.getString("speed");

                String weatherInfoText = weatherInfoArr[0] + ": " + weatherInfoArr[1] + "\nTemperature: " + weatherInfoArr[2] + "\nWind Speed: " + weatherInfoArr[3];
                weatherText.setText(weatherInfoText);
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Couldn't get the weather!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
