package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginCoord extends AppCompatActivity implements View.OnClickListener  {
    private EditText etEmail, etPassword;
    private Button button;
    public static final String SHARED_PREFS = "sharedPrefsCoord";
    public static final String COORDLOGIN = "coord_login";
    Connection connection;
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase Auth

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_stu);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

    }
    public void saveData() {
        SharedPreferences shp = getSharedPreferences("sharedPrefsCoord", MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putString(COORDLOGIN,etEmail.getText().toString());
        editor.apply();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                saveData();
                new LoginCoord.userLogin().execute("");
                break;
        }
    }
    public class userLogin extends AsyncTask<String, String, String>{

        String z= null;
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected String doInBackground(String... strings) {
            connection = connectionClass();
            if(connection == null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginCoord.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                z = "On Internet Connection";
            }
            else {
                try {
                    String sql = "SELECT * From Coordinator WHERE email = '" + etEmail.getText() + "' AND password = '" + etPassword.getText() + "' ";
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);

                    if (rs.next()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginCoord.this, "Login Success", Toast.LENGTH_LONG).show();
                            }
                        });
                        z= "Success";
                        startActivity(new Intent(LoginCoord.this, CoordActivity.class));
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginCoord.this, "Check email or password", Toast.LENGTH_LONG).show();
                            }
                        });

                        etEmail.setText("");
                        etPassword.setText("");

                    }
                } catch (Exception e) {
                    isSuccess = false;
                    Log.e("SQL error : ", e.getMessage());
                }
            }
            return z;
        }
    }
    @SuppressLint("NewApi")
    public Connection connectionClass(){
        String ip = "192.168.0.11", port = "1433", db = "Proje_Erasmus", username = "project", password = "123456A";
        StrictMode.ThreadPolicy a = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(a);
        Connection connection= null;
        String connectURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + db + ";user=" + username + ";"+"password=" + password + ";";
            connection = DriverManager.getConnection(connectURL);
        } catch (Exception e) {
            Log.e("Error :", e.getMessage());
        }
        return connection;
    }
}
