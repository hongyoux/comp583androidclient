package comp680team7.com.clienthighscore;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by greatkiller on 4/22/2018.
 */

public class GameCreateActivity extends AppCompatActivity {
    private TextInputLayout publishedDateFieldLayout;
    private TextInputEditText publishedDateField;
    private TextInputEditText gameName;
    private TextInputEditText publisherName;
    private Date publisherDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_create);
        publishedDateFieldLayout = findViewById(R.id.datePublishedLayout);
        publishedDateField = findViewById(R.id.datePublishedEditText);
        gameName = findViewById(R.id.gameName);
        publisherName = findViewById(R.id.gamePublisher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.createGameToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.WHITE);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(GameCreateActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar newCalendar = Calendar.getInstance();
                        newCalendar.set(year, month, dayOfMonth);
                        publisherDate = newCalendar.getTime();
                        publishedDateField.setText(new SimpleDateFormat("MM/dd/yyy").format(newCalendar.getTime()));
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        };
        publishedDateFieldLayout.setOnClickListener(clickListener);
        publishedDateField.setOnClickListener(clickListener);
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
                saveNewGame();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNewGame() {
        if(fieldsValid()) {
            Call<ResponseBody> call = MainActivity.SERVICE.addGame(gameName.getText().toString(), publisherName.getText().toString(), publisherDate.getTime());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Snackbar.make(publishedDateFieldLayout, "Error saving game. Please try again", Snackbar.LENGTH_LONG).show();
                }
            });
        }

    }

    private boolean fieldsValid() {
        return publisherDate != null && gameName.getText().length() > 0 && publisherName.getText().length() > 0;
    }
}
