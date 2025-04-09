package edu.uga.cs.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.List;

public class FinalScoreFragment extends Fragment {

    public FinalScoreFragment() {
        // Required empty constructor
    }

    public static FinalScoreFragment newInstance() {
        return new FinalScoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_final, container, false);

        TextView scoreText = view.findViewById(R.id.text_score);
        int score = 0;

        List<QuizQuestion> questions = QuizDataHolder.getQuizQuestions();
        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion q = questions.get(i);
            int qNumContinent = (i * 2) + 1;
            int qNumNeighbor = qNumContinent + 1;

            if (q.selectedContinent != null &&
                    q.selectedContinent.trim().equalsIgnoreCase(q.correctContinent.trim())) {
                score++;
                Log.d("QUIZ_SCORE", "Q" + qNumContinent + " ✅ Continent correct | Selected: " +
                        q.selectedContinent + " | Expected: " + q.correctContinent);
            } else {
                Log.d("QUIZ_SCORE", "Q" + qNumContinent + " ❌ Continent wrong | Selected: " +
                        q.selectedContinent + " | Expected: " + q.correctContinent);
            }

            if (q.correctNeighbors.isEmpty()) {
                if ("No Neighbor".equalsIgnoreCase(q.selectedNeighbor != null ? q.selectedNeighbor.trim() : "")) {
                    score++;
                    Log.d("QUIZ_SCORE", "Q" + qNumNeighbor + " ✅ No Neighbor correct");
                } else {
                    Log.d("QUIZ_SCORE", "Q" + qNumNeighbor + " ❌ No Neighbor wrong | Selected: " + q.selectedNeighbor);
                }
            } else {
                String correct = q.correctNeighbors.get(0);
                if (q.selectedNeighbor != null &&
                        q.selectedNeighbor.trim().equalsIgnoreCase(correct.trim())) {
                    score++;
                    Log.d("QUIZ_SCORE", "Q" + qNumNeighbor + " ✅ Neighbor correct | Selected: " +
                            q.selectedNeighbor + " | Expected: " + correct);
                } else {
                    Log.d("QUIZ_SCORE", "Q" + qNumNeighbor + " ❌ Neighbor wrong | Selected: " +
                            q.selectedNeighbor + " | Expected: " + correct);
                }
            }
        }

        // ✅ Display score
        scoreText.setText(String.valueOf(score));
        Log.d("QUIZ_SCORE", "🔚 Final Score = " + score);

        // ✅ Save score to DB asynchronously
        new StoreScoreTask().execute(score);

        return view;
    }

    // ✅ AsyncTask to store score in DB (uses custom AsyncTask class)
    private class StoreScoreTask extends AsyncTask<Integer, Void> {
        @Override
        protected Void doInBackground(Integer... values) {
            int finalScore = values[0];
            QuizDBHelper.getInstance(requireContext()).insertQuizScore(finalScore);
            Log.d("QUIZ_ASYNC_DB", "Score stored in DB: " + finalScore);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("QUIZ_ASYNC_DB", "Score insertion complete (postExecute).");
        }
    }
}