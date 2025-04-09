package edu.uga.cs.quizapp;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private QuestionPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get quiz questions from shared holder
        List<QuizQuestion> quizQuestions = QuizDataHolder.getQuizQuestions();

        // Set up ViewPager with adapter
        viewPager = findViewById(R.id.viewPager);
        adapter = new QuestionPagerAdapter(this, quizQuestions);
        viewPager.setAdapter(adapter);

        // Lock backward swiping once user reaches the final screen
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            private boolean locked = false;

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == adapter.getItemCount() - 1 && !locked) {
                    Log.d("QUIZ_LOCK", "Reached final screen. Locking swipe.");
                    viewPager.setUserInputEnabled(false); // disables all swiping
                    locked = true;

                }
            }
        });
    }


}