package com.example.vkinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.example.vkinfo.utils.NetWorkUtils.generateURL;
import static com.example.vkinfo.utils.NetWorkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {
    private EditText searchField;
    private Button searchButton;
    private static TextView result;
    private static TextView errorMassage;
    private static ProgressBar loadingIndicator;

    private static void showResultTextView(){
        result.setVisibility(View.VISIBLE);
        errorMassage.setVisibility(View.INVISIBLE);
    }
    private static void showErrorTextView(){
        result.setVisibility(View.INVISIBLE);
        errorMassage.setVisibility(View.VISIBLE);
    }

    static class VkQuerryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String response = null;
            try {
                response = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {

            String firstName = null;
            String lastName = null;
            if (response!=null && !response.equals("")){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    String resultingString ="";
                    for (int i = 0; i <jsonArray.length() ; i++) {
                        JSONObject userInfo = jsonArray.getJSONObject(i);
                        firstName = userInfo.getString("first_name");
                        lastName = userInfo.getString("last_name");
                        resultingString += "Имя: " + firstName + "\n" + "Фамилия: " + lastName + "\n\n";
                    }

                    result.setText(resultingString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                showResultTextView();
            }else{
               showErrorTextView();
            }
            loadingIndicator.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchField = findViewById(R.id.et_search_field);
        searchButton = findViewById(R.id.b_search_vk);
        result = findViewById(R.id.tv_result);
        errorMassage = findViewById(R.id.tv_error_massage);
        loadingIndicator = findViewById(R.id.pb_loading_indicator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL generatedURL = generateURL(searchField.getText().toString());

                new VkQuerryTask().execute(generatedURL);
            }
        };
        searchButton.setOnClickListener(onClickListener);
    }
}
