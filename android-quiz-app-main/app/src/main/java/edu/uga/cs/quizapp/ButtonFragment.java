package edu.uga.cs.quizapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ButtonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        Button btnNewQuiz = view.findViewById(R.id.btnNewQuiz);
        Button btnScoreboard = view.findViewById(R.id.btnScoreboard);

        btnNewQuiz.setOnClickListener(v -> {
            QuizState.resetScore();
            List<QuizQuestion> quizQuestions = new ArrayList<>();
            SQLiteDatabase db = QuizDBHelper.getInstance(requireContext()).getReadableDatabase();

            Cursor countryCursor = db.rawQuery("SELECT name, continent FROM country ORDER BY RANDOM() LIMIT 6", null);
            while (countryCursor.moveToNext()) {
                String country = countryCursor.getString(0);
                String correctContinent = countryCursor.getString(1);

                // Step 1: Continent options (1 correct + 2 wrong, shuffled)
                List<String> continentOptions = new ArrayList<>();
                continentOptions.add(correctContinent);
                Cursor wrongContCursor = db.rawQuery(
                        "SELECT DISTINCT continent FROM country WHERE continent != ? ORDER BY RANDOM() LIMIT 2",
                        new String[]{correctContinent});
                while (wrongContCursor.moveToNext()) {
                    continentOptions.add(wrongContCursor.getString(0));
                }
                wrongContCursor.close();

                // shuffling the continent options we got
                Collections.shuffle(continentOptions);

                // Step 2: Neighbor options — only 1 correct + 2 non-neighbors + "No Neighbor"
                List<String> allNeighbors = new ArrayList<>();
                Cursor neighborCursor = db.rawQuery(
                        "SELECT neighbor_name FROM neighbor WHERE country_name = ?",
                        new String[]{country});
                while (neighborCursor.moveToNext()) {
                    allNeighbors.add(neighborCursor.getString(0));
                }
                neighborCursor.close();

                // Choose only one correct neighbor (if any)
                String oneCorrectNeighbor = null;
                if (!allNeighbors.isEmpty()) {
                    Collections.shuffle(allNeighbors);
                    oneCorrectNeighbor = allNeighbors.get(0);
                }

                // Get two non-neighbors
                List<String> nonNeighbors = new ArrayList<>();
                Cursor nonNeighborCursor = db.rawQuery(
                        "SELECT name FROM country WHERE name != ? AND name NOT IN " +
                                "(SELECT neighbor_name FROM neighbor WHERE country_name = ?) " +
                                "ORDER BY RANDOM() LIMIT 5",
                        new String[]{country, country});
                while (nonNeighborCursor.moveToNext()) {
                    nonNeighbors.add(nonNeighborCursor.getString(0));
                }
                nonNeighborCursor.close();

                // Assemble neighbor options
                List<String> neighborOptions = new ArrayList<>();
                HashSet<String> added = new HashSet<>();

                if (oneCorrectNeighbor != null) {
                    neighborOptions.add(oneCorrectNeighbor);
                    added.add(oneCorrectNeighbor);
                }

                // Add up to 2 unique non-neighbors
                for (String name : nonNeighbors) {
                    if (!added.contains(name)) {
                        neighborOptions.add(name);
                        added.add(name);
                    }
                    if (neighborOptions.size() == 3) break;
                }

                // Ensure exactly 3 options before adding "No Neighbor"
                while (neighborOptions.size() < 3) {
                    neighborOptions.add("Placeholder"); // fallback if not enough countries
                }

                // Shuffle first 3 options
                Collections.shuffle(neighborOptions);

                // Add "No Neighbor" always as 4th option
                neighborOptions.add("No Neighbor");

                // Step 3: Create and add quiz question
                List<String> correctNeighbors = (oneCorrectNeighbor != null) ?
                        Collections.singletonList(oneCorrectNeighbor) : new ArrayList<>();

                quizQuestions.add(new QuizQuestion(
                        country,
                        correctContinent,
                        continentOptions,
                        correctNeighbors,
                        neighborOptions
                ));
            }

            countryCursor.close();

            // Save and launch quiz
            QuizDataHolder.setQuizQuestions(quizQuestions);
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            startActivity(intent);
        });

        btnScoreboard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ScoreboardActivity.class);
            startActivity(intent);
        });

        return view;
    }
}