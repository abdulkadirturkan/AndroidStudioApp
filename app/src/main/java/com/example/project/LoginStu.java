package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
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

public class    LoginStu extends AppCompatActivity implements View.OnClickListener {
    // private static final String TAG = "EmailPassword";
    private EditText etEmail, etPassword;
    private Button button;
    private TextView register;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private String text;
    Connection connection;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
    ProfileFragment sessionManager;
    // private String email, password;
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize Firebase Auth

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_stu);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        //register = (TextView) findViewById(R.id.register);
        //register.setOnClickListener(this);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        shp = this.getSharedPreferences("UserInfo", MODE_PRIVATE);

        String userid = shp.getString("UserId", "none");
        //sharedPreferences = getSharedPreferences("Giris", Context.MODE_PRIVATE);
        //editor = sharedPreferences.edit();
        //sessionManager = new ProfileFragment(getApplicationContext());


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.button:
                saveData();
                new LoginStu.userLogin().execute("aa");
                break;
        }
    }
    public void saveData(){
        SharedPreferences shp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor =shp.edit();
        editor.putString(TEXT,etEmail.getText().toString());
        editor.apply();
    }
    public void loadData(){
        SharedPreferences shp = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        text = shp.getString(TEXT,"");
    }
   /* private void userLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(email.isEmpty()){
            etEmail.setError("Email Giriniz");
            etEmail.requestFocus();
            return;
        }
  /*      if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Geçerli Mail Adresi Giriniz");
            etEmail.requestFocus();
        }
        if(password.isEmpty()){
            etPassword.setError("Şifre Giriniz");
            etPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            etPassword.setError("6 haneden az karakter içeremez");
            etPassword.requestFocus();
            return;
        }

 /*       mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Profile ilerle
                    startActivity(new Intent(LoginStu.this,studentActivity.class));
                }else{
                    Toast.makeText(LoginStu.this,"Giriş Sağlanamadı! Lütfen email ve şifrenizi kontrol ediniz",Toast.LENGTH_LONG).show();
                }
            }
        });
    }*/
    public class userLogin extends AsyncTask<String, String, String>{

        String z= null;
        Boolean isSuccess = false;
       String userid = etEmail.getText().toString();
       String password = etPassword.getText().toString();

       @Override
       protected void onPreExecute() {

       }

       @Override
       protected void onPostExecute(String s) {
           if (isSuccess) {

               SharedPreferences.Editor edit = shp.edit();
               edit.putString("UserId", userid);
               edit.commit();
               Intent i = new Intent(LoginStu.this, studentActivity.class);
               startActivity(i);
               finish();
           }
       }

       @Override
       protected String doInBackground(String... strings) {
           String email= etEmail.getText().toString();
           //Intent intent = new Intent(LoginStu.this,studentActivity.class);
           //intent.putExtra("EXTRA_MESSAGE",email);
           String password= etPassword.getText().toString();
           //editor.putString("saved_Email",email);
           //editor.putString("saved_Password",password);
           //editor.commit();
           connection = connectionClass();
           if(connection == null){
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Toast.makeText(LoginStu.this,"Check Internet Connection",Toast.LENGTH_LONG).show();
                   }
               });
               z = "On Internet Connection";
           }
           else {
               try {
                   String sql = "SELECT * From Student WHERE email = '" + etEmail.getText() + "' AND password = '" + etPassword.getText() + "' ";
                   Statement statement = connection.createStatement();
                   ResultSet rs = statement.executeQuery(sql);

                   if (rs.next()) {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Toast.makeText(LoginStu.this, "Login Success", Toast.LENGTH_LONG).show();
                           }
                       });
                        z= "Success";
                       //sessionManager.createLoginSession(email,password);
                       startActivity(new Intent(LoginStu.this, studentActivity.class));
                       finish();
                   } else {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               Toast.makeText(LoginStu.this, "Check email or password", Toast.LENGTH_LONG).show();
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
