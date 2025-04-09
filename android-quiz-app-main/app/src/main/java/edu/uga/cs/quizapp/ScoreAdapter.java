package edu.uga.cs.quizapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private ArrayList<ScoreItem> scoreList;

    public ScoreAdapter(ArrayList<ScoreItem> scoreList) {
        this.scoreList = scoreList;
    }

    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScoreViewHolder holder, int position) {
        ScoreItem scoreItem = scoreList.get(position);
        holder.serialNo.setText(scoreItem.getSerialNo());
        holder.dateTime.setText(scoreItem.getDateTime());
        holder.score.setText(scoreItem.getScore());
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public void updateScores(ArrayList<ScoreItem> newScores) {
        scoreList.clear();
        scoreList.addAll(newScores);
        notifyDataSetChanged();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        public TextView serialNo, dateTime, score;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            serialNo = itemView.findViewById(R.id.serialNo);
            dateTime = itemView.findViewById(R.id.dateTime);
            score = itemView.findViewById(R.id.score);
        }
    }
}