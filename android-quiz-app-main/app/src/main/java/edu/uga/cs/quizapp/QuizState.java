package edu.uga.cs.quizapp;

public class QuizState {
    public static int finalScore = 0;

    public static void resetScore() {
        finalScore = 0;
    }

    public static void incrementScore() {
        finalScore++;
    }

    public static int getScore() {
        return finalScore;
    }
}