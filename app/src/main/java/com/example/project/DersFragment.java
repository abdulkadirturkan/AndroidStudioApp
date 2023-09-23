package com.example.project;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static android.content.Context.MODE_PRIVATE;
import static com.example.project.LoginStu.TEXT;

public class DersFragment extends Fragment {
    TextView txtMesajlar,textView,twBilgi,twDersAKTS;
    Button btnDegis;
    Connection connection;
    String sonuc,AKTS;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "sharedPrefs";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_ders,container,false);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        twBilgi = (TextView) view.findViewById(R.id.twBilgi);
        twDersAKTS = (TextView) view.findViewById(R.id.twDersAKTS);
        //btnDegis = (Button) view.findViewById(R.id.btnDegis);
        //btnDegis.setOnClickListener(this);
        textView = (TextView) view.findViewById(R.id.display_message);
        SharedPreferences shp = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = shp.getString(TEXT,"No name defined");
        textView.setText(text);
        textView.setVisibility(View.GONE);
        //**
        //***
        //ViewModel le veri çekme
        //***
        //**
        listele();
        return view;
    }
    public void mailgoster(String mail){
        Intent intent = getActivity().getIntent();
        String email = intent.getStringExtra("EXTRA_MESSAGE");
        textView.setText(email);
    }
    public void listele() {
        txtMesajlar.setText("");
        GirisKontrolLF baglan = new GirisKontrolLF();
        baglan.execute("listele");
    }
    /*@Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDegis:
                dersErasmusFragment dersErasmusFragment = new dersErasmusFragment();
                //FragmentManager manager = getChildFragmentManager();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,dersErasmusFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }*/
    public class GirisKontrolLF extends AsyncTask<String,String,String>
    {
        String z= null;
        String mesaj="";
        Boolean basarili=false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            if (basarili)
            {
                //Toast.makeText(MainActivity.this,"Giriş başarılı",Toast.LENGTH_LONG).show();
            }
            txtMesajlar.setText(sonuc);
            twDersAKTS.setText(AKTS);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                connection = connectionClass();
                if(connection == null){
                    Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                    z = "On Internet Connection";
                }
                else
                {
                    //AKTS Toplam Hesabı
                    String aktsToplam="SELECT SUM(Lesson.ects) AS ECTS FROM Semester_Lesson INNER JOIN Lesson ON Semester_Lesson.lesson_id=Lesson.lesson_id Inner JOIN Student ON Semester_Lesson.student_id=Student.student_id\n" +
                            "WHERE Student.email ='"+textView.getText()+"'";
                    Statement st1=connection.createStatement();
                    ResultSet rs1=st1.executeQuery(aktsToplam);
                    AKTS="";
                    if (rs1.next())
                    {
                        AKTS+=rs1.getString("ECTS")+"\n"+(char)10+(char)13+(char)10+(char)13;
                        basarili=true;
                        while (rs1.next())
                        {
                            AKTS+=rs1.getString("ECTS")+"\n"+(char)10+(char)13+(char)10+(char)13;
                        }
                    }
                    if (strings[0].equals("listele"))
                    {
                        String sorgu="SELECT        Lesson.name, Lesson.code, Lesson.ects, Lesson.semester, Semester_Lesson.grade\n" +
                                "FROM            Semester_Lesson INNER JOIN\n" +
                                "                         Lesson ON Semester_Lesson.lesson_id = Lesson.lesson_id INNER JOIN\n" +
                                "                         Student ON Semester_Lesson.student_id = Student.student_id\n" +
                                "WHERE        (Student.email = '"+textView.getText()+"')";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("name")+" "+rs.getString("code")+" "+
                                    rs.getString("ects")+" "+
                                    rs.getString("semester")+" "+
                                    rs.getString("grade")
                                    +"\n"+(char)10+(char)13+(char)10+(char)13;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("name")+" "+rs.getString("code")+" "+
                                        rs.getString("ects")+" "+
                                        rs.getString("semester")+" "+
                                        rs.getString("grade")
                                        +"\n"+(char)10+(char)13+(char)10+(char)13;
                            }
                            mesaj="Veriler başarıyla listelendi";
                            connection.close();
                        }
                        else
                        {
                            mesaj="Hiç bir yeni mesajınız yok";
                            basarili=false;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                basarili=false;
                mesaj="Bağlantı hatası: "+ex.getMessage();
            }
            return mesaj;
        }
    }

    //private String mailgoster() {
        //Intent intent = getActivity().getIntent();
        //String email = intent.getStringExtra("EXTRA_MESSAGE");
        //new LoginStu.userLogin.execute(new String[]{"userid"});
        //return email;
    //}

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
