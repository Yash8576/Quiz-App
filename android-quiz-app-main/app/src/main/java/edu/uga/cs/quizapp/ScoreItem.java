package edu.uga.cs.quizapp;

public class ScoreItem {
    private final String serialNo;
    private final String dateTime;
    private final String score;

    public ScoreItem(String serialNo, String dateTime, String score) {
        this.serialNo = serialNo;
        this.dateTime = dateTime;
        this.score = score;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "ScoreItem{" +
                "serialNo='" + serialNo + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
