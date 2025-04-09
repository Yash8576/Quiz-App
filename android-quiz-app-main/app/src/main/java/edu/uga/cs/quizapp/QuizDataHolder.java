package edu.uga.cs.quizapp;

import java.util.List;

public class QuizDataHolder {
    private static List<QuizQuestion> quizQuestions;

    public static void setQuizQuestions(List<QuizQuestion> questions) {
        quizQuestions = questions;
    }

    public static List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public static void clear() {
        quizQuestions = null;
    }
}