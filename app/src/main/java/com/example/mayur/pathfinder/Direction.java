package com.example.mayur.pathfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mayur.pathfinder.Constant.ALTERNATIVE_PLACEHOLDER;
import static com.example.mayur.pathfinder.Constant.BASE_URL_ETA;
import static com.example.mayur.pathfinder.Constant.DESTINATION_PLACE_HOLDER;
import static com.example.mayur.pathfinder.Constant.DIRECTION_API_KEY;
import static com.example.mayur.pathfinder.Constant.KEY_PLACEHOLDER;
import static com.example.mayur.pathfinder.Constant.MODE_PLACE_HOLDER;
import static com.example.mayur.pathfinder.Constant.ORIGIN_PLACE_HOLDER;

public class Direction extends AppCompatActivity {

    EditText edtSource, edtDestination, edtMode;

    TextView txtTime, txtDistance, txtMode;

    Button btnSearch;

    ProgressBar progress;

    RadioGroup rgdmode;

    int rddriving;
    RadioButton rdwalking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);


        txtTime = findViewById(R.id.time);
        txtDistance = findViewById(R.id.dist);

        edtSource = findViewById(R.id.edtsrc);
        edtDestination = findViewById(R.id.edtdest);

        btnSearch = findViewById(R.id.btnserach);

        progress = findViewById(R.id.progress);

       // rddriving=findViewById(R.id.rddrving);
        rdwalking=findViewById(R.id.rdwalking);
        txtMode = findViewById(R.id.txtmode);

        rgdmode=findViewById(R.id.rgdmode);


    }

    public void OnSearchClick(View view) {

        progress.setVisibility(view.VISIBLE);

        String source = edtSource.getText().toString().trim();
        String destination = edtDestination.getText().toString().trim();

        if (TextUtils.isEmpty(source)) {
            edtSource.setError("Please Enter the source here");
            edtSource.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(destination)) {
            edtDestination.setError("Please Enter the destination here");
            edtDestination.requestFocus();
            return;
        }

        String mode;
        int modeid = rgdmode.getCheckedRadioButtonId();


         switch (modeid) {
            case R.id.rddrving:
                mode = "driving";
                break;

            case R.id.rdwalking:
                mode = "walking";
                break;

            default:
                mode = null;
                break;
        }


        String url = BASE_URL_ETA + ORIGIN_PLACE_HOLDER + source +
                DESTINATION_PLACE_HOLDER + destination + MODE_PLACE_HOLDER + mode + ALTERNATIVE_PLACEHOLDER + false +
                KEY_PLACEHOLDER + DIRECTION_API_KEY;

        DirectionEndPoints apiService = ApiClient.getClient().create(DirectionEndPoints.class);

        Call<DirectionResponse> callDirection = apiService.getDirectionData(url);

        callDirection.enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {

                progress.setVisibility(View.GONE);

                try {

                    DirectionResponse directionResponse = response.body();

                    ElementsItem element = directionResponse.getRows().get(0).getElements().get(0);

                    String distance = element.getDistance().getText();

                    String time = element.getDuration().getText();

                    txtDistance.setText("Distance in Km   : " + distance);
                    txtTime.setText("Time in Minutes : " + time);

                } catch (Exception e){

                    Toast.makeText(Direction.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }
}

