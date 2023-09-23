package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.EditText;
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
import static com.example.project.currentStudentFragment.STUNO;

public class NotFragment extends Fragment implements View.OnClickListener {
    TextView txtMesajlar,display_message,twOgrenciBilgi,txtMesajlarKod,txtMesajlarNot,txtMesajlarHarf,display_messageKBU,txtMesajlarKbu,txtMesajlarKbuKod,txtMesajlarKbuNot,txtMesajlarKbuHarf;
    EditText txtIDGetir,txtLesson,txtGrade;
    Button btnGetir,btnUpdate,btnKBUDers;
    Connection connection;
    String sonuc,ID,lessonID,grade,AKTS,sonucKod,sonucNot,sonucHarf,AKTSKBU,sonucKBU,sonucKBUKod,sonucKBUNot,sonucKBUHarf,KBULessonID;
    //update değişicek
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.fragment_not,container,false);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarKod = (TextView) view.findViewById(R.id.txtMesajlarKod);
        txtMesajlarKod.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarNot = (TextView) view.findViewById(R.id.txtMesajlarNot);
        txtMesajlarNot.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarHarf = (TextView) view.findViewById(R.id.txtMesajlarHarf);
        txtMesajlarHarf.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarKbu = (TextView) view.findViewById(R.id.txtMesajlarKbu);
        txtMesajlarKbu.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarKbuKod = (TextView) view.findViewById(R.id.txtMesajlarKbuKod);
        txtMesajlarKbuKod.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarKbuNot = (TextView) view.findViewById(R.id.txtMesajlarKbuNot);
        txtMesajlarKbuNot.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarKbuHarf = (TextView) view.findViewById(R.id.txtMesajlarKbuHarf);
        txtMesajlarKbuHarf.setMovementMethod(new ScrollingMovementMethod());
        display_message = (TextView) view.findViewById(R.id.display_message);
        display_messageKBU= (TextView) view.findViewById(R.id.display_messageKBU);
        //btnGetir = (Button) view.findViewById(R.id.btnGetir);
        //btnGetir.setOnClickListener(this);
        //txtIDGetir = (EditText) view.findViewById(R.id.txtIDGetir);
        txtLesson = (EditText) view.findViewById(R.id.txtLesson);
        txtGrade = (EditText) view.findViewById(R.id.txtGrade);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        btnKBUDers=(Button) view.findViewById(R.id.btnKBUDers);
        btnKBUDers.setOnClickListener(this);
        twOgrenciBilgi = (TextView) view.findViewById(R.id.twOgrenciBilgi);
        SharedPreferences shp = getContext().getSharedPreferences("sharedPrefsStu", MODE_PRIVATE);
        String stu = shp.getString(STUNO,"No name defined");
        twOgrenciBilgi.setText(stu);

        //twOgrenciBilgi.setVisibility(View.GONE);
        //**
        //***
        //ViewModel le veri çekme
        //***
        //**
        listele();
        return view;
    }
    public void listele() {
        txtMesajlar.setText("");
        display_message.setText("");
        display_messageKBU.setText("");
        GirisKontrolON baglan = new GirisKontrolON();
        baglan.execute("listele");
    }
    public void listeleKBU(){
        txtMesajlarKbu.setText("");
        txtMesajlarKbuKod.setText("");
        txtMesajlarKbuHarf.setText("");
        txtMesajlarKbuNot.setText("");
        GirisKontrolON baglan = new GirisKontrolON();
        baglan.execute("listeleKBU");
    }
    @Override
    public void onClick(View v) {
         if (v.getId() == btnUpdate.getId()) {
            GirisKontrolON baglan = new GirisKontrolON();
            //ID = txtIDGetir.getText().toString();
            lessonID = txtLesson.getText().toString();
            grade = txtGrade.getText().toString();
            baglan.execute("guncelle");
            listele();
            //baglan.execute("guncelleKBU");
            listeleKBU();
        }else if (v.getId() == btnKBUDers.getId()){
             GirisKontrolON baglan = new GirisKontrolON();
             baglan.execute("listeleKBU");
             listeleKBU();
         }
    }
    public class GirisKontrolON extends AsyncTask<String,String,String>
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
            //txtIDGetir.setText(ID);
            txtGrade.setText(grade);
            txtLesson.setText(lessonID);
            display_message.setText(AKTS);
            display_messageKBU.setText(AKTSKBU);
            txtMesajlarKod.setText(sonucKod);
            txtMesajlarNot.setText(sonucNot);
            txtMesajlarHarf.setText(sonucHarf);
            txtMesajlarKbu.setText(sonucKBU);
            txtMesajlarKbuKod.setText(sonucKBUKod);
            txtMesajlarKbuNot.setText(sonucKBUNot);
            txtMesajlarKbuHarf.setText(sonucKBUHarf);
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

                    if (strings[0].equals("listele"))
                    {

                        //AKTS Toplam Hesabı
                        String aktsToplam="SELECT SUM(Lesson.ects) AS ECTS FROM Semester_Lesson INNER JOIN Lesson ON Semester_Lesson.lesson_id = Lesson.lesson_id INNER JOIN Student ON Semester_Lesson.student_id = Student.student_id\n" +
                                "WHERE (Student.student_id = (SELECT student_id FROM Student WHERE number = "+twOgrenciBilgi.getText()+"))";
                        Statement st1=connection.createStatement();
                        ResultSet rs1=st1.executeQuery(aktsToplam);
                        AKTS="";
                        AKTSKBU="";
                        if (rs1.next())
                        {
                            AKTS+=rs1.getString("ECTS");
                            basarili=true;
                            while (rs1.next())
                            {
                                AKTS+=rs1.getString("ECTS");
                            }
                        }
                        String aktsKBUToplam="SELECT SUM(Lesson.ects) AS ECTS FROM Semester_Lesson_Kbu INNER JOIN Lesson ON Semester_Lesson_Kbu.lesson_id = Lesson.lesson_id INNER JOIN Student ON Semester_Lesson_Kbu.student_id = Student.student_id\n" +
                                "WHERE (Student.student_id = (SELECT student_id FROM Student WHERE number = "+twOgrenciBilgi.getText()+"))";
                        Statement st2=connection.createStatement();
                        ResultSet rs2=st2.executeQuery(aktsKBUToplam);
                        if (rs2.next())
                        {
                            AKTSKBU+=rs2.getString("ECTS");
                            basarili=true;
                            while (rs2.next())
                            {
                                AKTSKBU+=rs2.getString("ECTS");
                            }
                        }
                        //Not Database'i
                        String sorgu="SELECT semesterlesson_id,Lesson.name,Lesson.code, Student.number, grade,\n" +
                                "CASE WHEN grade >= 90 THEN 'A'\n" +
                                "WHEN grade >= 86 THEN 'B'\n" +
                                "WHEN grade >= 81 THEN 'B'\n" +
                                "WHEN grade >= 76 THEN 'C'\n" +
                                "WHEN grade >= 70 THEN 'C'\n" +
                                "WHEN grade >= 65 THEN 'D'\n" +
                                "WHEN grade >= 60 THEN 'D'\n" +
                                "WHEN grade >= 57 THEN 'D'\n" +
                                "WHEN grade >= 54 THEN 'E'\n" +
                                "WHEN grade >= 50 THEN 'E'\n" +
                                "WHEN grade >= 0 THEN 'F'\n" +
                                "ELSE '-'\n" +
                                "END AS QuantityText,\n" +
                                "CASE WHEN grade >= 90 THEN '10'\n" +
                                "WHEN grade >= 86 THEN '9'\n" +
                                "WHEN grade >= 81 THEN '8'\n" +
                                "WHEN grade >= 70 THEN '7'\n" +
                                "WHEN grade >= 60 THEN '6'\n" +
                                "WHEN grade >= 50 THEN '5'\n" +
                                "WHEN grade >= 40 THEN '4'\n" +
                                "WHEN grade >= 30 THEN '3'\n" +
                                "WHEN grade >= 20 THEN '2'\n" +
                                "WHEN grade >= 10 THEN '1'\n" +
                                "WHEN grade >= 0 THEN '0'\n" +
                                "ELSE '-'\n" +
                                "END AS QuantityText1\n" +
                                "FROM Semester_Lesson INNER JOIN Lesson ON Semester_Lesson.lesson_id=Lesson.lesson_id Inner JOIN Student ON Semester_Lesson.student_id=Student.student_id WHERE Student.student_id= (SELECT student_id FROM Student WHERE number = "+twOgrenciBilgi.getText()+")";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        sonucKod="";
                        sonucNot="";
                        sonucHarf="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("semesterlesson_id")+" "+rs.getString("name")+"\n"+(char)10;
                            sonucKod+=rs.getString("code")+"\n"+(char)10;
                            sonucNot+=rs.getString("grade")+"\n"+(char)10;
                            sonucHarf+=rs.getString("QuantityText")+" "+rs.getString("QuantityText1")+"\n"+(char)10;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("semesterlesson_id")+" "+rs.getString("name")+"\n"+(char)10;
                                sonucKod+=rs.getString("code")+"\n"+(char)10;
                                sonucNot+=rs.getString("grade")+"\n"+(char)10;
                                sonucHarf+=rs.getString("QuantityText")+" "+rs.getString("QuantityText1")+"\n"+(char)10;
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
                    else
                    if (strings[0].equals("guncelle"))
                    {
                        String sorgu="UPDATE Semester_Lesson SET grade ="+grade+
                        "WHERE student_id = (SELECT student_id FROM Student WHERE number = "+twOgrenciBilgi.getText()+") AND semesterlesson_id = "+lessonID;
                        Statement st=connection.createStatement();
                        st.execute(sorgu);

                        String sorgu1="UPDATE Semester_Lesson_Kbu\n" +
                                "SET grade = "+grade+
                                "WHERE (student_id = (SELECT student_id FROM Student WHERE (number = "+twOgrenciBilgi.getText()+"))) AND (lesson_id = (SELECT MIN(Lesson_Equ.lesson_id) AS Expr1\n" +
                                "FROM Lesson_Equ INNER JOIN Semester_Lesson ON Lesson_Equ.elesson_id = Semester_Lesson.lesson_id\n" +
                                "WHERE (Semester_Lesson.lesson_id = (SELECT lesson_id FROM Semester_Lesson AS Semester_Lesson_1 WHERE (semesterlesson_id = "+lessonID+")))))";
                        Statement st1=connection.createStatement();
                        st1.execute(sorgu1);
                        mesaj="Kayıt guncellendi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("listeleKBU"))
                    {
                        String sorgu="SELECT Lesson.name,Lesson.code,grade,\n" +
                                "CASE WHEN grade >= 90 THEN 'AA'\n" +
                                "WHEN grade >= 86 THEN 'AB'\n" +
                                "WHEN grade >= 81 THEN 'BA'\n" +
                                "WHEN grade >= 76 THEN 'BB'\n" +
                                "WHEN grade >= 70 THEN 'BC'\n" +
                                "WHEN grade >= 65 THEN 'CB'\n" +
                                "WHEN grade >= 60 THEN 'CC'\n" +
                                "WHEN grade >= 57 THEN 'CD'\n" +
                                "WHEN grade >= 54 THEN 'DC'\n" +
                                "WHEN grade >= 50 THEN 'DD'\n" +
                                "ELSE 'FF'\n" +
                                "END AS QuantityText ,\n" +
                                "CASE WHEN grade >= 90 THEN '4.00'\n" +
                                "WHEN grade >= 86 THEN '3.75'\n" +
                                "WHEN grade >= 81 THEN '3.33'\n" +
                                "WHEN grade >= 76 THEN '3.00'\n" +
                                "WHEN grade >= 70 THEN '2.75'\n" +
                                "WHEN grade >= 65 THEN '2.33'\n" +
                                "WHEN grade >= 60 THEN '2.00'\n" +
                                "WHEN grade >= 57 THEN '1.75'\n" +
                                "WHEN grade >= 54 THEN '1.33'\n" +
                                "WHEN grade >= 50 THEN '1.00'\n" +
                                "WHEN grade >= 0 THEN '0.00'\n" +
                                "ELSE '-'\n" +
                                "END AS QuantityText1\n" +
                                "FROM Semester_Lesson_Kbu INNER JOIN Lesson ON Semester_Lesson_Kbu.lesson_id=Lesson.lesson_id Inner JOIN Student ON Semester_Lesson_Kbu.student_id=Student.student_id\n" +
                                "WHERE Student.student_id= (SELECT student_id FROM Student WHERE number =  "+twOgrenciBilgi.getText()+")";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonucKBU="";
                        sonucKBUKod="";
                        sonucKBUNot="";
                        sonucKBUHarf="";
                        if (rs.next())
                        {
                            sonucKBU+=rs.getString("name")+"\n"+(char)10;
                            sonucKBUKod+=rs.getString("code")+"\n"+(char)10;
                            sonucKBUNot+=rs.getString("grade")+"\n"+(char)10;
                            sonucKBUHarf+=rs.getString("QuantityText")+" "+rs.getString("QuantityText1")+"\n"+(char)10;

                            basarili=true;
                            while(rs.next())
                            {
                                sonucKBU+=rs.getString("name")+"\n"+(char)10;
                                sonucKBUKod+=rs.getString("code")+"\n"+(char)10;
                                sonucKBUNot+=rs.getString("grade")+"\n"+(char)10;
                                sonucKBUHarf+=rs.getString("QuantityText")+" "+rs.getString("QuantityText1")+"\n"+(char)10;
                            }
                            mesaj="Veriler başarıyla listelendi";
                            connection.close();
                        }
                        else
                        {
                            mesaj="Hiç bir yeni mesajınız yok";
                            basarili=false;
                        }
                        connection.close();
                    }
                    else
                    if (strings[0].equals("guncelleKBU"))
                    {
                        String sorgu="UPDATE Semester_Lesson_Kbu\n" +
                                "SET grade = "+grade+
                                "WHERE (student_id = (SELECT student_id FROM Student WHERE (number = "+twOgrenciBilgi.getText()+"))) AND (lesson_id = (SELECT MIN(Lesson_Equ.lesson_id) AS Expr1\n" +
                                "FROM Lesson_Equ INNER JOIN Semester_Lesson ON Lesson_Equ.elesson_id = Semester_Lesson.lesson_id\n" +
                                "WHERE (Semester_Lesson.lesson_id = (SELECT lesson_id FROM Semester_Lesson AS Semester_Lesson_1 WHERE (semesterlesson_id = "+lessonID+")))))";
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt guncellendi";
                        connection.close();
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
