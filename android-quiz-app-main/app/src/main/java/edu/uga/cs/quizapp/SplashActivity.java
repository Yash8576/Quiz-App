package edu.uga.cs.quizapp;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private boolean dbInitialized = false;  // flag to track init per app run

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (savedInstanceState != null) {
            dbInitialized = savedInstanceState.getBoolean("DB_INITIALIZED", false);
        }

        if (!dbInitialized) {
            Log.d("SPLASH_INIT", "DB not initialized yet. Loading now...");
            new LoadDataTask().execute();
            dbInitialized = true;
        } else {
            Log.d("SPLASH_INIT", "DB already initialized in this app session.");
            launchSplashFragment();  // directly show UI
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("DB_INITIALIZED", dbInitialized);
    }

    private void launchSplashFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new SplashFragment())
                .commit();
    }

    // AsyncTask definition
    private class LoadDataTask extends AsyncTask<Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            QuizDBHelper.getInstance(SplashActivity.this).getReadableDatabase(); // triggers DB creation if needed
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            launchSplashFragment();
        }
    }
}