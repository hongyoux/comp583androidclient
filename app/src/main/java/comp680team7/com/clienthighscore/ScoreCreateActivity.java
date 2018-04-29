package comp680team7.com.clienthighscore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by greatkiller on 4/22/2018.
 */

public class ScoreCreateActivity extends AppCompatActivity implements IPickResult {
    public static String GAME_ID = "gameId";
    public static String USER_ID = "userId";

    private Integer gameId;
    private Integer userId;
    private AutoCompleteTextView scoreEdit;
    private ImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_create);

        gameId = getIntent().getIntExtra(GAME_ID, -1);
        userId = getIntent().getIntExtra(USER_ID, -1);

        scoreEdit = findViewById(R.id.newScore);
        imageView = findViewById(R.id.imageView);

        findViewById(R.id.selectImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoCompleteTextView score = findViewById(R.id.newScore);
                score.clearListSelection();
                PickImageDialog.build(new PickSetup()).show(ScoreCreateActivity.this);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.createScoreToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_game_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                processSaveClick();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String imageFilePath;
    private void processSaveClick() {
        if(fieldsValid()) {

            createRequestAndUpload(new File(imageFilePath));
        }

    }

    private boolean fieldsValid() {
        return scoreEdit.getText().length() > 0 ;
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.
//            Bitmap imgBitmap = r.getBitmap();
//            imageView.setImageBitmap(imgBitmap);
//            startOCR(imgBitmap);
            imageFilePath = r.getPath();
            //Changed to use actual bitmap as getBitmap returns a degraded thumbnail and will effect OCR
            Bitmap bitmap = BitmapFactory.decodeFile(r.getPath());
            imageView.setImageBitmap(r.getBitmap());
            startOCR(bitmap);
            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Snackbar.make(scoreEdit, "Error picking file. Please try again", Snackbar.LENGTH_LONG).show();
        }
    }

    private void createRequestAndUpload(File file) {
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        uploadToBucket(body);
    }

    private void uploadToBucket(MultipartBody.Part body) {
        Call call = MainActivity.SERVICE.uploadImage(body);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()) {
                    try {
                        createNewScore(((ResponseBody) response.body()).string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Snackbar.make(imageView, "Issue uploading image", Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
                System.out.println("FAILURE!!");
            }
        });
    }

    private void createNewScore(String publicImageUrl) {
        Call<ResponseBody> call = MainActivity.SERVICE.addScore(gameId, userId,
                Integer.valueOf(scoreEdit.getText().toString()), publicImageUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    finish();
                } else {
                    Snackbar.make(imageView, "Issue saving score", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snackbar.make(scoreEdit, "Error saving score. Please try again", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void startOCR(Bitmap imgBitmap) {
        TextRecognizer ocr= new TextRecognizer.Builder(this).build();

        if (ocr.isOperational()) {
            Frame imgFrame = new Frame.Builder().setBitmap(imgBitmap).build();

            SparseArray<TextBlock> textBlocks = ocr.detect(imgFrame);

            ArrayList<String> possibleScores = new ArrayList<>();
            for (int i = 0; i < textBlocks.size(); i++) {
                possibleScores.add(textBlocks.get(textBlocks.keyAt(i)).getValue());

            }

            updateScoreEdit(possibleScores);
        }
    }

    private void updateScoreEdit(ArrayList<String> possibleScores) {

        ArrayList<String> newS = new ArrayList<>();
        for (String s : possibleScores) {
            String[] tmpS = s.split("[ \n]");
            for (String token : tmpS) {
                if (token.matches("[\\d,]*")) {
                    newS.add(token);
                }
            }
        }
        AutoCompleteTextView score = findViewById(R.id.newScore);
        score.setThreshold(0);
        score.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.select_dialog_item, newS));
        if(!newS.isEmpty()) {
            score.setText(newS.get(0));
            score.showDropDown();
        }
    }
}
