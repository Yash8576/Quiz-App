package edu.uga.cs.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class QuestionFragment extends Fragment {

    private static final String ARG_COUNTRY = "country";
    private static final String ARG_CONTINENTS = "continent_options";
    private static final String ARG_NEIGHBORS = "neighbor_options";
    private static final String ARG_POSITION = "question_number";

    private int position;
    private QuizQuestion question;

    public static QuestionFragment newInstance(QuizQuestion question, int position) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COUNTRY, question.country);
        args.putStringArrayList(ARG_CONTINENTS, new ArrayList<>(question.continentOptions));
        args.putStringArrayList(ARG_NEIGHBORS, new ArrayList<>(question.neighborOptions));
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        String country = args.getString(ARG_COUNTRY);
        ArrayList<String> continentOptions = args.getStringArrayList(ARG_CONTINENTS);
        ArrayList<String> neighborOptions = args.getStringArrayList(ARG_NEIGHBORS);
        position = args.getInt(ARG_POSITION);
        question = QuizDataHolder.getQuizQuestions().get(position);

        int questionNumber1 = (position * 2) + 1;
        int questionNumber2 = questionNumber1 + 1;

        TextView q1 = view.findViewById(R.id.text_que_continent);
        TextView q2 = view.findViewById(R.id.text_que_neighbor);

        q1.setText("Q" + questionNumber1 + ". In which continent is " + country + " located?");
        q2.setText("Q" + questionNumber2 + ". Which of the following is a neighbor of " + country + "?");

        RadioGroup continentGroup = view.findViewById(R.id.radio_group_continent);
        for (int i = 0; i < continentGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) continentGroup.getChildAt(i);
            if (i < continentOptions.size()) {
                rb.setText(continentOptions.get(i));
                rb.setChecked(continentOptions.get(i).equals(question.selectedContinent));
            } else {
                rb.setVisibility(View.GONE);
            }
        }

        RadioGroup neighborGroup = view.findViewById(R.id.radio_group_neighbor);
        for (int i = 0; i < neighborGroup.getChildCount(); i++) {
            RadioButton rb = (RadioButton) neighborGroup.getChildAt(i);
            if (i < neighborOptions.size()) {
                rb.setText(neighborOptions.get(i));
                rb.setChecked(neighborOptions.get(i).equals(question.selectedNeighbor));
            } else {
                rb.setVisibility(View.GONE);
            }
        }

        continentGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = view.findViewById(checkedId);
            if (selected != null) {
                question.selectedContinent = selected.getText().toString();
                Log.d("QUIZ_SAVE", "Q" + (position + 1) + ": Selected Continent = " + question.selectedContinent);
            }
        });

        neighborGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = view.findViewById(checkedId);
            if (selected != null) {
                question.selectedNeighbor = selected.getText().toString();
                Log.d("QUIZ_SAVE", "Q" + (position + 1) + ": Selected Neighbor = " + question.selectedNeighbor);
            }
        });
    }

}