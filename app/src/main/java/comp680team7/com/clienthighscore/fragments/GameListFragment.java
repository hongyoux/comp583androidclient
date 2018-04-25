package comp680team7.com.clienthighscore.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import comp680team7.com.clienthighscore.GameListAdapter;
import comp680team7.com.clienthighscore.R;
import comp680team7.com.clienthighscore.models.Game;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameListFragment.OnListItemSelectedListener} interface
 * to handle interaction events.
 * Use the {@link GameListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameListFragment extends ListFragment {
    private static final String ARG_PARAM1 = "mGameList";

    private OnListItemSelectedListener mListener;

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

        final GameListAdapter adapter = new GameListAdapter();
        gameListView.setAdapter(adapter);
        gameListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.addGames(this.getArguments().<Game>getParcelableArrayList(ARG_PARAM1));

        return rootView;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mListener.onGameSelected(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListItemSelectedListener) {
            mListener = (OnListItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListItemSelectedListener {
        void onGameSelected(int position);
    }
}
