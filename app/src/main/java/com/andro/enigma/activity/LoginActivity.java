package com.andro.enigma.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.enigma.R;
import com.andro.enigma.database.*;
import com.andro.enigma.database.Package;
import com.andro.enigma.helper.Helper;
import com.andro.enigma.parser.JsonParser;
import com.andro.enigma.settings.MySettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    TextView txtTitle, txtUsername, txtPassword;
    EditText editUsername, editPassword;
    Button buttonLogin;
    String username, password;

    Bundle getPackage;

    AppCompatActivity activity;

    boolean loginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;
        loginSuccess = false;
        //getPackage = getIntent().getExtras();

        initializeView();
        fillData();
        Helper.inicActionBarUp(this,getResources().getString(R.string.title_activity_login));

    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent returnIntent = new Intent();
        if(loginSuccess){
            returnIntent.putExtra("result","success");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }else{
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initializeView(){
        txtTitle = (TextView) findViewById(R.id.text_title);
        txtUsername = (TextView) findViewById(R.id.text_username);
        txtPassword = (TextView) findViewById(R.id.text_password);

        editUsername = (EditText) findViewById(R.id.edittext_username);
        editPassword = (EditText) findViewById(R.id.edittext_password);

        buttonLogin = (Button) findViewById(R.id.button_login);
    }

    public void fillData(){
        txtTitle.setText(getResources().getString(R.string.title_activity_login));
        txtUsername.setText(getResources().getString(R.string.username));
        txtPassword.setText(getResources().getString(R.string.password));

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            username = editUsername.getText().toString();
            password = editPassword.getText().toString();

            if(Helper.isBlank(username) || Helper.isBlank(password)){
                Toast.makeText(LoginActivity.this, "Invalid input!", Toast.LENGTH_SHORT).show();
            }else{
                new JSONParse().execute();
            }
            }
        });
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JsonParser jsonParser = new JsonParser();

            JSONObject jsonObject = jsonParser.getJSONFromUrl(Helper.HOME_URL +
                    "/service/login?seckey=EnIgmAAEIOU" +
                    "&username=" + Helper.md5(username) +
                    "&password=" + Helper.md5(password));
            Log.d("USERNAME",Helper.HOME_URL +
                    "/service/login?seckey=EnIgmAAEIOU" +
                    "&username=" + Helper.md5(username) +
                    "&password=" + Helper.md5(password));
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            String userId = "0";
            try {
                userId = result.getString("user_id");
                String userName = result.getString("username");
                Log.d("USERNAME",userName);
                Log.d("USERNAME",userId);
            }catch (JSONException jex){
                jex.printStackTrace();
            }
            if(userId.equalsIgnoreCase("0")){
                Toast.makeText(activity, activity.getResources().getString(R.string.login_not_registered), Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences sharedpreferences = getSharedPreferences("Enigma user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("userId", userId);
            editor.commit();
            loginSuccess = true;
        }
    }

}
