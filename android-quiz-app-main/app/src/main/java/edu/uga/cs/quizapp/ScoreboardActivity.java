package edu.uga.cs.quizapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScoreboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScoreAdapter scoreAdapter;
    private QuizDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        recyclerView = findViewById(R.id.recyclerViewScores);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ✅ Set empty adapter initially
        scoreAdapter = new ScoreAdapter(new ArrayList<>());
        recyclerView.setAdapter(scoreAdapter);

        dbHelper = QuizDBHelper.getInstance(this);

        new LoadScoresTask().execute();
    }

    // -----------------------------
    // AsyncTask to fetch scores in background
    // -----------------------------
    private class LoadScoresTask extends AsyncTask<Void, ArrayList<ScoreItem>> {

        @Override
        protected ArrayList<ScoreItem> doInBackground(Void... voids) {
            ArrayList<ScoreItem> scoresList = new ArrayList<>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = null;

            try {
                cursor = db.rawQuery("SELECT id, date, score FROM quiz ORDER BY id DESC", null);

                if (cursor.moveToFirst()) {
                    do {
                        String serialNo = String.valueOf(cursor.getInt(0));  // id
                        String dateTime = cursor.getString(1);  // date
                        String score = String.valueOf(cursor.getInt(2));  // score

                        scoresList.add(new ScoreItem(serialNo, dateTime, score));
                        Log.d("SCOREBOARD", "Score Loaded - ID: " + serialNo + ", Date: " + dateTime + ", Score: " + score);
                    } while (cursor.moveToNext());
                } else {
                    Log.d("SCOREBOARD", "No scores found in the database.");
                }
            } catch (Exception e) {
                Log.e("SCOREBOARD", "Error retrieving scores", e);
            } finally {
                if (cursor != null) cursor.close();
            }

            return scoresList;
        }

        @Override
        protected void onPostExecute(ArrayList<ScoreItem> scores) {
            scoreAdapter.updateScores(scores); // this method already exists in your adapter
            Log.d("SCOREBOARD", "Loaded " + scores.size() + " scores.");
        }
    }
}