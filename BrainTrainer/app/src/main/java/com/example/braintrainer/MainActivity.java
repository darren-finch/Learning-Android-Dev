package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{
    //References
    private TableLayout answersLayout;
    private TextView equationText;
    private TextView counterText;
    private TextView scoreText;
    private Button[] answerButtons;
    private Random rng;
    private CountDownTimer countdown;

    //Current correct answer
    private int correctAnswer;

    //Score variables
    private int correctAnswers;
    private int selectedAnswers;

    //Random range for addition
    private int sumRangeMin = 3;
    private int sumRangeMax = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        answersLayout = findViewById(R.id.answersLayout);
        equationText = findViewById(R.id.equationText);
        counterText = findViewById(R.id.counterText);
        scoreText = findViewById(R.id.scoreText);
        answerButtons = new Button[4];
        answerButtons[0] = findViewById(R.id.answer1);
        answerButtons[1] = findViewById(R.id.answer2);
        answerButtons[2] = findViewById(R.id.answer3);
        answerButtons[3] = findViewById(R.id.answer4);
        rng = new Random();
    }
    public void startTraining(View view)
    {
        view.setVisibility(View.INVISIBLE);
        answersLayout.setVisibility(View.VISIBLE);
        findViewById(R.id.resultsText).setVisibility(View.GONE);

        if(countdown != null)
            countdown.cancel();

        countdown = new CountDownTimer(30000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                updateTimer((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish()
            {
                answersLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.tryAgainButton).setVisibility(View.VISIBLE);
                TextView resultsText = findViewById(R.id.resultsText);
                resultsText.setVisibility(View.VISIBLE);
                resultsText.setText("You got " + correctAnswers + " out of " + selectedAnswers + " answers within the time limit!");
            }
        }.start();

        correctAnswers = 0;
        selectedAnswers = 0;

        setupAnswers();
    }
    public void selectAnswer(View view)
    {
        Button selectedButton = (Button) view;
        int selectedAnswer = Integer.parseInt(selectedButton.getText().toString());

        if(selectedAnswer == correctAnswer)
            correctAnswers++;
        selectedAnswers++;

        scoreText.setText(correctAnswers + "/" + selectedAnswers);
        setupAnswers();
    }
    private void updateTimer(int secondsLeft)
    {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);

        String minutesLeftText = (minutes < 10 ? "0" : "") + minutes;
        String secondsLeftText = (seconds < 10 ? "0" : "") + seconds;
        if(seconds == 0)
            secondsLeftText = "00";
        if(minutes == 0)
            minutesLeftText = "00";

        counterText.setText(minutesLeftText + ':' + secondsLeftText);
    }
    private void setupAnswers()
    {
        ArrayList<Integer> equation = makeEquation(0);
        String equationString = equation.get(0) + " + " + equation.get(1) + " = ?";
        equationText.setText(equationString);

        correctAnswer = equation.get(equation.size() - 1);
        int correctAnswerIndex = rng.nextInt(3) + 1;

        for(int i = 0; i < answerButtons.length; i++)
        {
            // generate the random color for each button, but keep the color bright
            float h = rng.nextFloat() * 255;
            float s = rng.nextFloat();
            float b = 0.8f + ((1f - 0.8f) * rng.nextFloat());
            int randomColor = Color.HSVToColor(new float[] {h, s, b});
            answerButtons[i].setBackgroundColor(randomColor);

            if(i == correctAnswerIndex)
            {
                String correctAnswerText = Integer.toString(correctAnswer);
                answerButtons[i].setVisibility(View.GONE);
                answerButtons[i].setText(correctAnswerText);
                answerButtons[i].setVisibility(View.VISIBLE);
            }
            else
            {
                String incorrectAnswerText = Integer.toString(rng.nextInt(correctAnswer) + correctAnswer / 2   );
                answerButtons[i].setVisibility(View.GONE);
                answerButtons[i].setText(incorrectAnswerText);
                answerButtons[i].setVisibility(View.VISIBLE);
            }
        }
    }
    //Returns a list of numbers representing an equation. The last number in the equation is the answer, and the previous numbers are the equation.
    private ArrayList<Integer> makeEquation(int equationType)
    {
        ArrayList<Integer> equation = new ArrayList<>();

        switch (equationType)
        {
            //If equation type is addition...
            case 0:
                int newSum = rng.nextInt(sumRangeMax - sumRangeMin + 1) + sumRangeMin;
                equation.add(rng.nextInt(newSum - 1) + 1);
                equation.add(newSum - equation.get(0));
                equation.add(newSum);
                break;
        }

        return equation;
    }
}