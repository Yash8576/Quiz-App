package edu.uga.cs.quizapp;

import java.util.List;

public class QuizQuestion {
    public String country;
    public String correctContinent;
    public List<String> continentOptions;
    public List<String> correctNeighbors;
    public List<String> neighborOptions;

    public String selectedContinent = null;
    public String selectedNeighbor = null;

    public QuizQuestion(String country,
                        String correctContinent,
                        List<String> continentOptions,
                        List<String> correctNeighbors,
                        List<String> neighborOptions) {
        this.country = country;
        this.correctContinent = correctContinent;
        this.continentOptions = continentOptions;
        this.correctNeighbors = correctNeighbors;
        this.neighborOptions = neighborOptions;
    }
}