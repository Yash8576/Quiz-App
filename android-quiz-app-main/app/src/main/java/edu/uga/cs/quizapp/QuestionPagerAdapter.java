package edu.uga.cs.quizapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class QuestionPagerAdapter extends FragmentStateAdapter {

    private final List<QuizQuestion> quizQuestions;

    public QuestionPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<QuizQuestion> quizQuestions) {
        super(fragmentActivity);
        this.quizQuestions = quizQuestions;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < quizQuestions.size()) {
            return QuestionFragment.newInstance(quizQuestions.get(position), position);
        } else {
            return FinalScoreFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return quizQuestions.size() + 1; // 6 questions + final screen
    }
}