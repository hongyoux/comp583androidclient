package comp680team7.com.clienthighscore.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import comp680team7.com.clienthighscore.OnListItemSelectedListener;
import comp680team7.com.clienthighscore.R;
import comp680team7.com.clienthighscore.models.Game;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder> {
    private List<Game> games = new ArrayList<>();

    private OnListItemSelectedListener selectionListener;

    public GameListAdapter(OnListItemSelectedListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public void addGames(List<Game> games) {
        this.games.clear();
        this.games.addAll(games);
        notifyDataSetChanged();
    }

    public Game getGameAt(int index) {
        return games.get(index);
    }

    @Override
    public GameListAdapter.GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_row, parent, false));
    }

    @Override
    public void onBindViewHolder(GameListAdapter.GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.gameTitle.setText(game.getName());
        holder.gamePublisher.setText(game.getPublisher());
        holder.gameReleaseDate.setText(new SimpleDateFormat("MMMM dd, yyyy").format(game.getReleaseDate()));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {
        public TextView gameTitle;
        public TextView gamePublisher;
        public TextView gameReleaseDate;

        public GameViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectionListener.onItemSelected(getAdapterPosition());
                }
            });
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gamePublisher = itemView.findViewById(R.id.gamePublisher);
            gameReleaseDate = itemView.findViewById(R.id.releaseDate);
        }
    }
}
