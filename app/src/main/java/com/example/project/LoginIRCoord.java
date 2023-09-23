package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginIRCoord extends AppCompatActivity implements View.OnClickListener  {
    private EditText etEmail, etPassword;
    private Button button;
    //private SqlConnection sqlConnection;
    Connection connection;
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase Auth

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ircoord);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        //sqlConnection = new SqlConnection();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                new LoginIRCoord.userLogin().execute("");
                break;
        }
    }
    public class userLogin extends AsyncTask<String, String, String> {

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
                        Toast.makeText(LoginIRCoord.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                    }
                });
                z = "On Internet Connection";
            }
            else {
                try {
                    String sql = "SELECT * From I_R_Coordinator WHERE username = '" + etEmail.getText() + "' AND password = '" + etPassword.getText() + "' ";
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sql);

                    if (rs.next()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginIRCoord.this, "Login Success", Toast.LENGTH_LONG).show();
                            }
                        });
                        z= "Success";
                        startActivity(new Intent(LoginIRCoord.this, IRCoordActivity.class));
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginIRCoord.this, "Check username or password", Toast.LENGTH_LONG).show();
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
