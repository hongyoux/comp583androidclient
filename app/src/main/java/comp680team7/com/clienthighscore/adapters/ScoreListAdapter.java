package comp680team7.com.clienthighscore.adapters;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import comp680team7.com.clienthighscore.MainActivity;
import comp680team7.com.clienthighscore.OnListItemSelectedListener;
import comp680team7.com.clienthighscore.R;
import comp680team7.com.clienthighscore.models.Score;
import comp680team7.com.clienthighscore.models.User;

/**
 * Created by greatkiller on 3/11/2018.
 */

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder>
        implements Filterable{

    private SortedList<Score> scores;// = new ArrayList<>();
//    private SortedList<Score> filteredScores;
    private List<Score> filteredScores;

    private OnListItemSelectedListener selectionListener;

    public ScoreListAdapter(OnListItemSelectedListener selectionListener) {

        this.selectionListener = selectionListener;


        SortedList.Callback<Score> sortedListCallback = new SortedListAdapterCallback<Score>(this) {

            @Override
            public int compare(Score o1, Score o2) {
                return -1 * o1.getScore().compareTo(o2.getScore());
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Score oldItem, Score newItem) {
                return oldItem.getScore().equals(newItem.getScore());
            }

            @Override
            public boolean areItemsTheSame(Score item1, Score item2) {
                return item1.getScore().equals(item2.getScore());
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        };
        scores = new SortedList<Score>(Score.class, sortedListCallback);
//        filteredScores = new SortedList<Score>(Score.class, sortedListCallback);
        filteredScores = new ArrayList<>();
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
//        return new ScoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score_row, parent, false));
        return new ScoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.score_list_card_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ScoreListAdapter.ScoreViewHolder holder, int position) {
        Score score;
        if (!filteredScores.isEmpty()){
            score = filteredScores.get(position);
        }
        else {
            score = scores.get(position);
        }
        User user = MainActivity.CACHE_USERS.get(score.getUserId());
        if(user != null) {
            holder.userName.setText(user.getUserName());
        } else {
            holder.userName.setText("No Username");
        }

        holder.score.setText(String.valueOf(score.getScore()));
        if(score.getImageUrl().contains("localhost")) {
            score.setImageUrl(score.getImageUrl().replace("localhost", "10.0.2.2"));
        }
        Glide.with(holder.score.getContext()).load(score.getImageUrl()).into(holder.scoreView);
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
//                    filteredScores = scores;
//                    filteredScores.addAll(scores);
                } else {
//                    SortedList<Score> filteredList = new SortedList<>(Score.class, sortedListCallback);
                    List<Score> filteredList = new ArrayList<>();
                    for (int i = 0; i < scores.size(); i++) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        User compUser = MainActivity.CACHE_USERS.get(scores.get(i).getUserId());
                        if (compUser.getUserName().toLowerCase().contains(charString.toLowerCase())
                                || scores.get(i).getScore().toString().contains(charSequence)) {
                            Score tmpScore = scores.get(i);
                            filteredList.add(tmpScore);

                        }
                    }

//                    filteredScores = filteredList;
                    filteredScores.addAll(filteredList);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredScores;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
//                filteredScores = (SortedList<Score>) results.values; //TODO: CHANGE??
                filteredScores = (ArrayList) results.values; //TODO: CHANGE??
                notifyDataSetChanged();
            }
        };
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView score;
        public ImageView scoreView;

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
            scoreView = itemView.findViewById(R.id.scoreImage);
        }
    }
}
