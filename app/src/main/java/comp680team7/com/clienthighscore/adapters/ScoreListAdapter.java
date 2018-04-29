package comp680team7.com.clienthighscore.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comp680team7.com.clienthighscore.OnListItemSelectedListener;
import comp680team7.com.clienthighscore.R;
import comp680team7.com.clienthighscore.models.Score;

/**
 * Created by greatkiller on 3/11/2018.
 */

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {
    private List<Score> scores = new ArrayList<>();

    private OnListItemSelectedListener selectionListener;

    public ScoreListAdapter(OnListItemSelectedListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public void addScores(List<Score> scores) {
        this.scores.clear();
        this.scores.addAll(scores);
        notifyDataSetChanged();
    }

    public Score getScoreAt(int index) {
        return scores.get(index);
    }

    @Override
    public ScoreListAdapter.ScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ScoreListAdapter.ScoreViewHolder holder, int position) {
        Score score = scores.get(position);

        holder.userName.setText(String.valueOf(score.getUserId()));
        holder.score.setText(String.valueOf(score.getScore()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView score;

        public ScoreViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectionListener.onItemSelected(getAdapterPosition());
                }
            });
            userName = itemView.findViewById(R.id.userName);
            score = itemView.findViewById(R.id.score);
        }
    }
}
