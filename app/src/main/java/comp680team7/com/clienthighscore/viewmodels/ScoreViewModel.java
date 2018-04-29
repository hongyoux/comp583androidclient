package comp680team7.com.clienthighscore.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import comp680team7.com.clienthighscore.models.Score;
import comp680team7.com.clienthighscore.repos.ScoreRepository;

public class ScoreViewModel extends ViewModel{
    private MutableLiveData<ArrayList<Score>> scores;
    private ScoreRepository scoreRepo;

//    @Inject
//    public ScoreViewModel(ScoreRepository scoreRepo) {
//        this.scoreRepo = scoreRepo;
//    }

    public void init(Integer gameId) {
        if (this.scores != null){
//            scoreRepo = new ScoreRepository();
            return;
        }
        scores = ScoreRepository.getScore(gameId);
    }

    public LiveData<ArrayList<Score>> getScores() {
        return scores;
    }

    public void addScore(Integer gameId, Integer userId, Integer score) {
        ScoreRepository.addScore(gameId, userId, score);

        ArrayList<Score> tmpScores = scores.getValue();
        tmpScores.add(new Score(0, score, gameId, userId, "fakeurl"));
        scores.setValue(tmpScores);

    }

}
