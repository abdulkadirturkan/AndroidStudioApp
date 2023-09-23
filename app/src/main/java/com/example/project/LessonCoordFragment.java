package com.example.project;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.apache.poi.ss.formula.functions.T;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static android.content.Context.MODE_PRIVATE;
import static com.example.project.currentStudentFragment.STUNO;

public class LessonCoordFragment extends Fragment implements View.OnClickListener {
    EditText txtDersID, txtIDGetir,etDersAl;
    TextView txtMesajlar,twOgrenciBilgi,twSemesterBilgi,display_message,twNumberOne,txtMesajlarKBU,txtMesajlarErasmus,display_messageKBU,twLessonOne,txtMesajlarEslenik;
    Button btnSave, btnGuncelle, btnSil, btnGetir,btnUpdate,btnSec;
    String sonuc, dersID, ID,AKTS,number1,AKTSKBU,DersErasmus,DersKBU,lesson1,eslenikDers;
    Connection connection,con1;
//SQL DEĞİŞİCEK
    //update değişicek
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_coord, container, false);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        //somestır sekmesi arayüzü
        Spinner mySpinner = (Spinner) view.findViewById(R.id.spinner2);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.somestir));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        //arayüz bitiş
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarKBU = (TextView) view.findViewById(R.id.txtMesajlarKBU);
        txtMesajlarKBU.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarErasmus = (TextView) view.findViewById(R.id.txtMesajlarErasmus);
        txtMesajlarErasmus.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarEslenik = (TextView) view.findViewById(R.id.txtMesajlarEslenik);
        txtMesajlarEslenik.setMovementMethod(new ScrollingMovementMethod());
        //txtIDGetir = (EditText) view.findViewById(R.id.txtIDGetir);
        twOgrenciBilgi = (TextView) view.findViewById(R.id.twOgrenciBilgi);
        twSemesterBilgi = (TextView) view.findViewById(R.id.twSemesterBilgi);
        twNumberOne = (TextView) view.findViewById(R.id.twNumberOne);
        twNumberOne.setVisibility(View.GONE);
        twLessonOne = (TextView) view.findViewById(R.id.twLessonOne);
        twLessonOne.setVisibility(View.GONE);
        display_message = (TextView) view.findViewById(R.id.display_message);
        display_messageKBU = (TextView) view.findViewById(R.id.display_messageKBU);
        //txtDeptID = (EditText) view.findViewById(R.id.txtDeptID);
        txtDersID = (EditText) view.findViewById(R.id.txtDersID);
        etDersAl = (EditText) view.findViewById(R.id.etDersAl);
        btnSec = (Button) view.findViewById(R.id.btnSec);
        btnSec.setOnClickListener(this);
        btnGuncelle = (Button) view.findViewById(R.id.btnGuncelle);
        btnGuncelle.setOnClickListener(this);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spinnerValue = mySpinner.getSelectedItem().toString();
                twSemesterBilgi.setText(spinnerValue);
                listele1();
                listele();
            }
        });
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnSil = (Button) view.findViewById(R.id.btnSil);
        //btnGetir = (Button) view.findViewById(R.id.btnGetir);
        btnSil.setOnClickListener(this);
        //btnGetir.setOnClickListener(this);
        //btnNotGirisi = (Button) view.findViewById(R.id.btnNotGirisi);
        //btnNotGirisi.setOnClickListener(this);
        SharedPreferences shp = getContext().getSharedPreferences("sharedPrefsStu", MODE_PRIVATE);
        String stu = shp.getString(STUNO,"No name defined");
        twOgrenciBilgi.setText(stu);
        twSemesterBilgi.setVisibility(View.GONE);
        //twOgrenciBilgi.setVisibility(View.GONE);

        //SQL SORGUSU
        //Eşlenik Dersleri Gösterme
        //SELECT * FROM Lesson WHERE equivalent IN (SELECT equivalent FROM Lesson GROUP BY equivalent HAVING COUNT(*) > 1)

        //SQL SORGUSU
        //Alınan Derslerin Çıkarılmış hali
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent FROM Lesson LEFT JOIN Taken_Lesson ON Lesson.lesson_id = Taken_Lesson.lesson_id
        //WHERE Taken_Lesson.lesson_id IS NULL

        //SQL SORGUSU
        //Alınan Derslerin Çıkarılmış haliyle Eşlenik Ders Gösterme
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent FROM  Lesson LEFT JOIN Taken_Lesson ON Lesson.lesson_id = Taken_Lesson.lesson_id
        // WHERE equivalent IN (SELECT equivalent FROM Lesson GROUP BY equivalent HAVING COUNT(*) > 1 )

        //SQL SORGUSU
        //Alınan Derslerin Çıkarılmış haliyle Girilen Departmanın Eşlenik Dersini Gösterme
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent
        //FROM  Lesson LEFT JOIN Taken_Lesson ON Lesson.lesson_id = Taken_Lesson.lesson_id
        //WHERE department_id=61 AND equivalent IN (SELECT equivalent FROM Lesson GROUP BY equivalent HAVING COUNT(*) > 1 )

        //SQL SORGUSU
        //Belirli Bir Öğrenciden Alınan Derslerin Çıkarılmış hali
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent
        //FROM  Lesson EXCEPT
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent
        //FROM  Lesson LEFT JOIN Taken_Lesson ON Lesson.lesson_id = Taken_Lesson.lesson_id
        //WHERE Taken_Lesson.student_id = 3

        //SQL SORGUSU
        //Belirli Bir Öğrenciden Alınan Derslerin Çıkarılmış halinin Departman hali
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent
        //FROM  Lesson EXCEPT
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent
        //FROM  Lesson INNER JOIN Department ON Lesson.department_id=Department.department_id INNER JOIN Student ON Student.erasmusdepartment_id=Department.department_id LEFT JOIN Taken_Lesson ON Lesson.lesson_id = Taken_Lesson.lesson_id
        //WHERE Taken_Lesson.student_id = 6 OR Student.erasmusdepartment_id!=61

        //SONDAKİ
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent
        //FROM  Lesson EXCEPT
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent
        //FROM  Lesson LEFT JOIN Taken_Lesson
        //ON Lesson.lesson_id = Taken_Lesson.lesson_id
        //
        //WHERE Taken_Lesson.student_id = 3 OR Lesson.department_id != 61 ORDER BY equivalent ASC

        //Alınanların Eşleniği olanlar
        //SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent
        //FROM  Lesson LEFT JOIN Taken_Lesson ON Lesson.lesson_id = Taken_Lesson.lesson_id
        //WHERE Taken_Lesson.student_id = 3 AND equivalent IN (SELECT equivalent FROM Lesson GROUP BY equivalent HAVING COUNT(*) > 1 )

        listele1();
        //listele2();
        listele();
        return view;
    }
    public void listele2(){
        try{
            SqlConnection sqlConnection = new SqlConnection();
            con1 = sqlConnection.conclass();
            eslenikDers="";
            String eslenik="SELECT elesson_id,Lesson.name,Lesson.ects\n" +
                    "FROM Lesson_Equ INNER JOIN Lesson ON Lesson_Equ.elesson_id=Lesson.lesson_id INNER JOIN Department ON Lesson.department_id=Department.department_id\n" +
                    "WHERE Lesson_Equ.lesson_id="+etDersAl.getText()+" AND Lesson.department_id= (SELECT erasmusdepartment_id FROM Student WHERE student_id=(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+"))";
            Statement st15= con1.createStatement();
            ResultSet rs15= st15.executeQuery(eslenik);
            if(rs15.next())
            {
                eslenikDers+=rs15.getString("elesson_id")+" "+rs15.getString("name")+" "+rs15.getString("ects")+"\n";
                while (rs15.next())
                {
                    eslenikDers+=rs15.getString("elesson_id")+" "+rs15.getString("name")+" "+rs15.getString("ects")+"\n";
                }
                txtMesajlarEslenik.setText(eslenikDers);
            }
        }
        catch (Exception ex){
            //
        }

    }
    public void listele() {
        txtMesajlar.setText("");
        display_message.setText("");
        GirisKontrolLCFC baglan = new GirisKontrolLCFC();
        baglan.execute("listele");
    }
    public void listele1(){
        try {
            SqlConnection sqlConnection = new SqlConnection();
            con1 = sqlConnection.conclass();
            number1="";
            lesson1="";
            String number= "SELECT student_id FROM Student WHERE number = "+twOgrenciBilgi.getText();
            Statement st4 = con1.createStatement();
            ResultSet rs4 = st4.executeQuery(number);
            if(rs4.next())
            {
                number1+=rs4.getString("student_id");
            }
            String lesson="SELECT lesson_id FROM Lesson_Equ WHERE elesson_id="+txtDersID.getText();
            Statement st5= con1.createStatement();
            ResultSet rs5 = st5.executeQuery(lesson);
            if(rs5.next())
            {
                lesson1+=rs5.getString("lesson_id");
            }
            twNumberOne.setText(number1);
            twLessonOne.setText(lesson1);
        }
        catch (Exception ex) {
            //
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnSave.getId()) {
            listele1();
            GirisKontrolLCFC baglan = new GirisKontrolLCFC();
            //ID = txtIDGetir.getText().toString();
            dersID = txtDersID.getText().toString();
            baglan.execute("ekle");
            listele();
        } else if (v.getId() == btnGuncelle.getId()) {
            GirisKontrolLCFC baglan = new GirisKontrolLCFC();
            //ID = txtIDGetir.getText().toString();
            dersID = txtDersID.getText().toString();
            baglan.execute("guncelle");
            //baglan.execute("guncelleKBU");
            listele();
        } else if (v.getId() == btnSil.getId()) {
            listele1();
            GirisKontrolLCFC baglan = new GirisKontrolLCFC();
            //ID = txtIDGetir.getText().toString();
            baglan.execute("sil");
            listele();
        }
        else if (v.getId()==btnSec.getId()){
            listele2();
        }
    }
    public class GirisKontrolLCFC extends AsyncTask<String,String,String>
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
            display_message.setText(AKTS);
            display_messageKBU.setText(AKTSKBU);
            txtMesajlarErasmus.setText(DersErasmus);
            txtMesajlarKBU.setText(DersKBU);
            //twSemesterBilgi.setText(ID);
            txtDersID.setText(dersID);
            //txtDeptID.setText(deptID);
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
                        //String sqlEqu="SELECT MAX(equivalent) FROM Lesson LEFT JOIN Taken_Lesson\n" +
                          //      "ON Lesson.lesson_id = Taken_Lesson.lesson_id WHERE Taken_Lesson.student_id = "+txtIDGetir.getText();
                        //Toast.makeText(getActivity(),sqlEqu,Toast.LENGTH_LONG).show();
                        //String sqlDept="SELECT erasmusdepartment_id FROM Student WHERE student_id = "+txtIDGetir.getText();
                        //AKTS Toplam Hesabı
                        String aktsToplam="SELECT SUM(Lesson.ects) AS ECTS FROM Semester_Lesson INNER JOIN Lesson ON Semester_Lesson.lesson_id = Lesson.lesson_id INNER JOIN Student ON Semester_Lesson.student_id = Student.student_id\n" +
                                "WHERE (Student.student_id = (SELECT student_id FROM Student WHERE number = "+twOgrenciBilgi.getText()+"))";
                        Statement st1=connection.createStatement();
                        ResultSet rs1=st1.executeQuery(aktsToplam);
                        AKTS="";
                        AKTSKBU="";
                        DersErasmus="";
                        DersKBU="";
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
                        String erasmusDersler="SELECT Lesson.name,Lesson.code\n" +
                                "FROM Semester_Lesson INNER JOIN Lesson ON Semester_Lesson.lesson_id=Lesson.lesson_id Inner JOIN Student ON \n" +
                                "Semester_Lesson.student_id=Student.student_id WHERE Student.student_id= (SELECT student_id FROM Student WHERE number ="+twOgrenciBilgi.getText()+")";
                        Statement st3=connection.createStatement();
                        ResultSet rs3=st3.executeQuery(erasmusDersler);
                        if (rs3.next())
                        {
                            DersErasmus+=rs3.getString("name")+" "+rs3.getString("code")+"\n";
                            basarili=true;
                            while (rs3.next())
                            {
                                DersErasmus+=rs3.getString("name")+" "+rs3.getString("code")+"\n";
                            }
                        }
                        String kbuDersler="SELECT Lesson.name,Lesson.code\n" +
                                "FROM Semester_Lesson_Kbu INNER JOIN Lesson ON Semester_Lesson_Kbu.lesson_id=Lesson.lesson_id Inner JOIN Student ON \n" +
                                "Semester_Lesson_Kbu.student_id=Student.student_id WHERE Student.student_id= (SELECT student_id FROM Student WHERE number ="+twOgrenciBilgi.getText()+")";
                        Statement st4=connection.createStatement();
                        ResultSet rs4= st4.executeQuery(kbuDersler);
                        if (rs4.next())
                        {
                            DersKBU+=rs4.getString("name")+" "+rs4.getString("code")+"\n";
                            basarili=true;
                            while (rs4.next())
                            {
                                DersKBU+=rs4.getString("name")+" "+rs4.getString("code")+"\n";
                            }
                        }

                        /*String sorgu="SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent\n" +
                                "FROM  Lesson EXCEPT SELECT Lesson.lesson_id, Lesson.name, code, Lesson.department_id, ects, semester, equivalent FROM  Lesson LEFT JOIN Taken_Lesson ON Lesson.lesson_id = Taken_Lesson.lesson_id\n" +
                                "WHERE Taken_Lesson.student_id =(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+") OR Lesson.department_id != (SELECT erasmusdepartment_id FROM Student WHERE student_id=(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+")) OR Lesson.equivalent < (SELECT MAX(equivalent) FROM Lesson LEFT JOIN Taken_Lesson ON Lesson.lesson_id = Taken_Lesson.lesson_id WHERE Taken_Lesson.student_id =(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+"))" +
                                "OR semester !="+twSemesterBilgi.getText();

                         */
                        String sorgu="SELECT Lesson.lesson_id, Lesson.name, code, semester, ects \n" +
                                "FROM Lesson EXCEPT SELECT Lesson.lesson_id, Lesson.name, code, semester, ects FROM Lesson LEFT JOIN Taken_Lesson ON Lesson.lesson_id = Taken_Lesson.lesson_id \n" +
                                "WHERE Taken_Lesson.student_id =(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+") OR Lesson.department_id!= (SELECT department_id FROM Student WHERE student_id=(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+")) " +
                                "OR semester !="+twSemesterBilgi.getText()+" OR Lesson.lesson_id=55 OR Lesson.lesson_id=56";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("lesson_id")+" "+rs.getString("name")+" "+
                                    rs.getString("code")+" "+rs.getString("semester")+" "+rs.getString("ects")
                                    +"\n"+(char)10;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("lesson_id")+" "+rs.getString("name")+" "+
                                        rs.getString("code")+" "+rs.getString("semester")+" "+rs.getString("ects")
                                        +"\n"+(char)10;
                            }
                            mesaj="Listelendi";
                            connection.close();
                        }
                        else
                        {
                            mesaj="Bu Somestıra Ait Veri Bulunamadı";
                            basarili=false;
                        }
                    }
                    else
                    if (strings[0].equals("ekle"))
                    {
                        String sorgu="insert into Semester_Lesson (lesson_id,student_id) "
                                +" values('"+ dersID+"','"
                                + twNumberOne.getText()+"')";
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        String sorguLesson="insert into Semester_Lesson_Kbu (lesson_id,student_id)" +
                                "values('"+twLessonOne.getText()+"','" +
                                twNumberOne.getText()+"')";
                        Statement st1=connection.createStatement();
                        st1.execute(sorguLesson);
                        String sorguDel="WITH cte AS (SELECT semesterlessonkbu_id, lesson_id, student_id, grade,ROW_NUMBER() OVER( PARTITION BY lesson_id, student_id, grade\n" +
                                "ORDER BY lesson_id, student_id, grade ) row_num FROM Semester_Lesson_Kbu ) DELETE FROM cte WHERE row_num > 1";
                        Statement st16=connection.createStatement();
                        st16.execute(sorguDel);
                        String sorguDel2="WITH cte AS (SELECT semesterlesson_id, lesson_id, student_id, grade,local_grade,ROW_NUMBER() OVER( PARTITION BY lesson_id, student_id, grade, local_grade\n" +
                                "ORDER BY lesson_id, student_id, grade, local_grade ) row_num FROM Semester_Lesson ) DELETE FROM cte WHERE row_num > 1";
                        Statement st17=connection.createStatement();
                        st17.execute(sorguDel2);
                        mesaj="Kayıt eklendi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("guncelle"))
                    {
                        String sorgu="update Semester_Lesson set lesson_id='"+ dersID+
                                "',student_id=(SELECT student_id FROM Student WHERE number='"+ twOgrenciBilgi.getText()+
                                "') where student_id=(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+")";
                        ;
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt guncellendi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("sil"))
                    {
                        String sorgu="delete from Semester_Lesson where lesson_id="+
                                dersID+"AND student_id=(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+")";

                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        String sorguLesson="delete from Semester_Lesson_Kbu where lesson_id="+
                                twLessonOne.getText()+"AND student_id=(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+")";
                        Statement st1=connection.createStatement();
                        st1.execute(sorguLesson);
                        mesaj="Kayıt silindi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("getir"))
                    {
                        String sorgu="select * from Semester_Lesson where student_id=(SELECT student_id FROM Student WHERE number="+twOgrenciBilgi.getText()+")";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            dersID=rs.getString("lesson_id");


                            basarili=true;

                            mesaj="Veriler başarıyla yüklendi";
                            connection.close();
                        }
                        else
                        {
                            mesaj="Bu ID'ye sahip mesaj bulunamadı";
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

    @SuppressLint("NewApi")
    public Connection connectionClass() {
        String ip = "192.168.0.11", port = "1433", db = "Proje_Erasmus", username = "project", password = "123456A";
        StrictMode.ThreadPolicy a = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(a);
        Connection connection = null;
        String connectURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + db + ";user=" + username + ";" + "password=" + password + ";";
            connection = DriverManager.getConnection(connectURL);
        } catch (Exception e) {
            Log.e("Error :", e.getMessage());
        }
        return connection;
    }
}
