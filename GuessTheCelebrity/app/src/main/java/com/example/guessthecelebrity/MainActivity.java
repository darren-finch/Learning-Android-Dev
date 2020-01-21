package com.example.guessthecelebrity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    private ImageView celebImageView;
    private ConstraintLayout guessingLayout;
    private Button guess0;
    private Button guess1;
    private Button guess2;
    private Button guess3;
    private TextView loadingText;

    private String curCelebName;

    private ArrayList<String> celebNames;
    private ArrayMap<String, Bitmap> celebs;

    private Random rng;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        celebImageView = findViewById(R.id.celebImage);
        guessingLayout = findViewById(R.id.guessingLayout);
        guess0 = findViewById(R.id.guess0);
        guess1 = findViewById(R.id.guess1);
        guess2 = findViewById(R.id.guess2);
        guess3 = findViewById(R.id.guess3);
        loadingText = findViewById(R.id.loadingText);

        celebNames = new ArrayList<>();
        celebs = new ArrayMap<>();
        rng = new Random();

        getCelebrities();

        loadingText.setVisibility(View.GONE);
        guessingLayout.setVisibility(View.VISIBLE);

        setCelebrity();
    }

    public void guessCelebrity(View view)
    {
        Button button = (Button) view;
        String guessText = button.getText().toString();
        String guessedIt = "";
        if(guessText.equals(curCelebName))
        {
            guessedIt = "You got it!";
        }
        else
        {
            guessedIt = "Sorry but that's incorrect.";
        }

        Toast.makeText(this, guessedIt, Toast.LENGTH_SHORT).show();

        setCelebrity();
    }

    private void setCelebrity()
    {
        int[] randomCelebs = new int[4];
        for(int i = 0; i < randomCelebs.length; i++)
        {
            randomCelebs[i] = rng.nextInt(celebNames.size());
        }

        guess0.setText(celebNames.get(randomCelebs[0]));
        guess1.setText(celebNames.get(randomCelebs[1]));
        guess2.setText(celebNames.get(randomCelebs[2]));
        guess3.setText(celebNames.get(randomCelebs[3]));

        curCelebName = celebNames.get(randomCelebs[rng.nextInt(randomCelebs.length)]);
        celebImageView.setImageBitmap(celebs.get(curCelebName));
    }

    private void getCelebrities()
    {
        HTMLDownloader pageDownloader = new HTMLDownloader();
        String celebsPage;
        try
        {
            celebsPage = pageDownloader.execute("http://www.posh24.se/kandisar").get();

            Pattern p = Pattern.compile("alt=\"(.*?)\"");
            Matcher m = p.matcher(celebsPage);

            while (m.find())
            {
                celebNames.add(m.group(1));
            }

            ArrayList<Bitmap> celebImages = new ArrayList<>();
            p = Pattern.compile("<img src=\"(.*?)\" alt=\"");
            m = p.matcher(celebsPage);

            while (m.find())
            {
                String imageURL = m.group(1);
                Bitmap bm = null;
                ImageDownloader downloader = new ImageDownloader();
                try
                {
                    bm = downloader.execute(imageURL).get();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                celebImages.add(bm);
            }

            for(int i = 0; i < celebNames.size(); i++)
            {
                celebs.put(celebNames.get(i), celebImages.get(i));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... urls)
        {
            try
            {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap bm = BitmapFactory.decodeStream(in);
                return bm;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }

    public class HTMLDownloader extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls)
        {
            String result = null;

            try
            {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data != -1)
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "Failed";
            }
        }
    }
}
