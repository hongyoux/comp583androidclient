package comp680team7.com.clienthighscore.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import comp680team7.com.clienthighscore.GameListAdapter;
import comp680team7.com.clienthighscore.MainActivity;
import comp680team7.com.clienthighscore.OnListItemSelectedListener;
import comp680team7.com.clienthighscore.R;
import comp680team7.com.clienthighscore.models.Game;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameListFragment extends Fragment implements OnListItemSelectedListener {
    private static final String ARG_PARAM1 = "mGameList";

    RecyclerView gameListView;

    public GameListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment GameListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameListFragment newInstance(ArrayList<String> param1) {
        GameListFragment fragment = new GameListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game_list, container, false);

        gameListView = (RecyclerView) rootView.findViewById(R.id.gameListView);

        final GameListAdapter adapter = new GameListAdapter(this);
        gameListView.setAdapter(adapter);
        gameListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Call<ArrayList<Game>> games = MainActivity.SERVICE.getGames();
        games.enqueue(new Callback<ArrayList<Game>>() {
            @Override
            public void onResponse(Call<ArrayList<Game>> call, Response<ArrayList<Game>> response) {
                if (response.isSuccessful()) {
                    adapter.addGames(response.body());
                } else {
                    Snackbar.make(gameListView, "Issue Retrieving Game List", Snackbar.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Game>> call, Throwable t) {

            }
        });



        return rootView;
    }

    private GameListAdapter getGameAdapter() {
        return (GameListAdapter) gameListView.getAdapter();
    }

    @Override
    public void onGameSelected(int position) {
        Snackbar.make(gameListView, "Selected " + getGameAdapter().getGameAt(position).getName(), Snackbar.LENGTH_LONG).show();
    }
}
