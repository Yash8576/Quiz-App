package edu.uga.cs.quizapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QuizDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "QuizDB.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_COUNTRY = "country";
    public static final String TABLE_NEIGHBOR = "neighbor";
    public static final String TABLE_QUIZ = "quiz";

    private static QuizDBHelper instance;
    private final Context context;

    // 🔒 Private constructor to enforce singleton
    private QuizDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context.getApplicationContext(); // safe reference
    }

    // ✅ Singleton access method
    public static synchronized QuizDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB_INIT", "Database is being created and tables are being initialized.");

        db.execSQL("CREATE TABLE country (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE, continent TEXT);");
        db.execSQL("CREATE TABLE neighbor (country_name TEXT, neighbor_name TEXT);");
        db.execSQL("CREATE TABLE quiz (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, score INTEGER);");

        Log.d("DB_INIT", "Inserting countries from CSV...");
        insertCountriesFromCSV(db);

        Log.d("DB_INIT", "Inserting neighbors from CSV...");
        insertNeighborsFromCSV(db);
    }

    private void insertCountriesFromCSV(SQLiteDatabase db) {
        try {
            InputStream is = context.getAssets().open("country_continent.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 2) {
                    String country = tokens[0].trim();
                    String continent = tokens[1].trim();

                    db.execSQL("INSERT INTO " + TABLE_COUNTRY + " (name, continent) VALUES (?, ?);",
                            new Object[]{country, continent});
                    Log.d("DB_INIT", "Inserted country: " + country + " -> " + continent);
                }
            }
            reader.close();
        } catch (Exception e) {
            Log.e("DB_INIT", "Error reading country_continent.csv", e);
        }
    }

    private void insertNeighborsFromCSV(SQLiteDatabase db) {
        try {
            InputStream is = context.getAssets().open("country_neighbors.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 2) {
                    String country = tokens[0].trim();
                    for (int i = 1; i < tokens.length; i++) {
                        String neighbor = tokens[i].trim();
                        db.execSQL("INSERT INTO " + TABLE_NEIGHBOR + " (country_name, neighbor_name) VALUES (?, ?);",
                                new Object[]{country, neighbor});
                        Log.d("DB_INIT", "Inserted neighbor: " + country + " -> " + neighbor);
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            Log.e("DB_INIT", "Error reading country_neighbors.csv", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEIGHBOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        onCreate(db);
        Log.d("DB_INIT", "Database upgraded from version " + oldVersion + " to " + newVersion);
    }

    public void insertQuizScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        db.execSQL("INSERT INTO " + TABLE_QUIZ + " (date, score) VALUES (?, ?);",
                new Object[]{date, score});

        Log.d("DB_INSERT", "Inserted quiz score: " + score + " on " + date);
    }
}