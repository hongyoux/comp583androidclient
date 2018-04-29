package comp680team7.com.clienthighscore.viewmodels;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.util.List;

import comp680team7.com.clienthighscore.models.Score;
import comp680team7.com.clienthighscore.repos.ScoreRepository;

public class ScoreListViewModel extends ViewModel{

    private Integer gameId;
    private ScoreRepository scoreRepository = new ScoreRepository();

    private MutableLiveData<List<Score>> scoreListLiveData = new MutableLiveData<>();

    public void init(LifecycleOwner lifecycleOwner, Integer gameId) {
        this.gameId = gameId;
        scoreRepository.getScoreListLiveData().observe(lifecycleOwner, new Observer<List<Score>>() {
            @Override
            public void onChanged(@Nullable List<Score> scores) {
                scoreListLiveData.setValue(scores);
            }
        });
    }

    public void refreshScoreList() {
        scoreRepository.getScoreList(gameId);
    }

    public LiveData<List<Score>> scoresListLiveData() {
        return scoreListLiveData;
    }

}
