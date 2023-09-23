package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
/*Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnConnect=(Button) findViewById(R.id.button);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView name=(TextView) findViewById(R.id.textView);
                SqlConnection c= new SqlConnection();
                connection= c.conclass();

                if(c !=null)
                {
                    try {
                        //  Connection connection= DriverManager.getConnection("");
                        String sqlStatement = "Select * from Student";
                        Statement smt = connection.createStatement();
                        ResultSet set = smt.executeQuery(sqlStatement);
                        while (set.next()) {
                            name.setText(set.getString(2));
                        }
                        connection.close();
                    }
                    catch (Exception e)
                    {
                        Log.e("Error:",e.getMessage());
                    }
                }
            }
        });
    }*/
ImageView twbaykus;
private Button btnStu, btnCoord, btnIRCoord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        btnStu = (Button)findViewById(R.id.btnStu);
        btnCoord = (Button)findViewById(R.id.btnCoord);
        btnIRCoord = (Button)findViewById(R.id.btnIRCoord);
        twbaykus = (ImageView)findViewById(R.id.twbaykus);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        ProfileFragment sessionManager;
        sessionManager = new ProfileFragment(getApplicationContext());
        sessionManager.checkLogin(); */

        //btnStu.setOnClickListener(this);
        //btnCoord.setOnClickListener(this);
        //btnIRCoord.setOnClickListener(this);

    }
    public void nextActivity(View v){
        Intent i = new Intent(this,LoginStu.class);
        startActivity(i);
    }
    public void nextActivity2(View v){
        Intent i = new Intent(this,LoginCoord.class);
        startActivity(i);
    }
    public void nextActivity3(View v){
        Intent i = new Intent(this,LoginIRCoord.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStu:
                startActivity(new Intent(this, LoginStu.class));
                break;
            case R.id.btnCoord:
                startActivity(new Intent(this, LoginCoord.class));
                break;
            case R.id.btnIRCoord:
                startActivity(new Intent(this, LoginIRCoord.class));
                break;
        }
    }
}