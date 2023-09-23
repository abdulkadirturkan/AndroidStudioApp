package com.example.project;

import android.annotation.SuppressLint;
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
import static com.example.project.currentStudentFragment.STUNO;

public class ActivationFragment extends Fragment implements View.OnClickListener {
    //Öğrenci otomaston yönlendirme arayüzü
    Button btnDersEslenik, btnNotYonlendirme,btnBelge,btnBos,btnMesaj;
    TextView txtMesajlar,twOgrenciBilgi;
    Connection connection;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "sharedPrefsStu";
    String sonuc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activation, container, false);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        twOgrenciBilgi = (TextView) view.findViewById(R.id.twOgrenciBilgi);
        btnDersEslenik = (Button) view.findViewById(R.id.btnDersEslenik);
        btnNotYonlendirme = (Button) view.findViewById(R.id.btnNotYonlendirme);
        btnBelge = (Button) view.findViewById(R.id.btnBelge);
        btnBos = (Button) view.findViewById(R.id.btnBos);
        btnMesaj = (Button) view.findViewById(R.id.btnMesaj);
        btnDersEslenik.setOnClickListener(this);
        btnNotYonlendirme.setOnClickListener(this);
        btnBelge.setOnClickListener(this);
        btnMesaj.setOnClickListener(this);
        btnBos.setOnClickListener(this);
        //twOgrenciBilgi ye sharedPrefden uygun texti gönderme
        SharedPreferences shp = getContext().getSharedPreferences("sharedPrefsStu", MODE_PRIVATE);
        String stu = shp.getString(STUNO,"No name defined");
        twOgrenciBilgi.setText(stu);
        //butonlara uygun setOnClickListener atama
        //listele ile seçilen ogrencinin verilerini gösterme txtMesajlarda
        listele();

        return view;
    }
     public void listele(){
         txtMesajlar.setText("");
         GirisKontrolAF baglan = new GirisKontrolAF();
         baglan.execute("listele");
     }
    @Override
    public void onClick(View v) {
        if (v.getId()==btnDersEslenik.getId())
        {
            //DersEşleniği
            LessonCoordFragment lessonCoordFragment = new LessonCoordFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,lessonCoordFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId() == btnNotYonlendirme.getId()) {
            //NotArayüzü
            NotFragment notFragment = new NotFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,notFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId() == btnBelge.getId()) {
            //Belge Arayüzü
            startActivity(new Intent(getActivity(), BelgeCoordActivity.class));
        }
        else if (v.getId() == btnMesaj.getId()) {
            //Mesaj Arayüzü
            SohbetFragment sohbetFragment = new SohbetFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,sohbetFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (v.getId() == btnBos.getId()) {
            currentStudentFragment currentStudentFragment = new currentStudentFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,currentStudentFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    //database i girme
    public class GirisKontrolAF extends AsyncTask<String,String,String>
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
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                connection = connectionClass();
                //String rsdText = shp.getString("text", null);
                if(connection == null){
                    Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                    z = "On Internet Connection";
                }
                else
                {
                    if (strings[0].equals("listele"))
                    {
                        String sorgu="Select Student.name, Student.surname, Student.email, Student.number, Department.dname, University.uni_name From Student inner join Department ON Student.erasmusdepartment_id = Department.department_id INNER JOIN Faculty ON Department.faculty_id = Faculty.faculty_id INNER JOIN\n" +
                                "University ON Faculty.university_id = University.university_id WHERE number="+twOgrenciBilgi.getText();
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("name")+" "+rs.getString("surname")+" \nMail : "+
                                    rs.getString("email")+" \nNumara : "+rs.getString("number")+" \nDepartman : "+rs.getString("dname")+" \nUniversite : "+rs.getString("uni_name")
                                    +"\n"+(char)10+(char)13+(char)10+(char)13;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("name")+" "+rs.getString("surname")+" \nMail : "+
                                        rs.getString("email")+" \nNumara : "+rs.getString("number")+" \nDepartman : "+rs.getString("dname")+" \nUniversite : "+rs.getString("uni_name")
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
//                    else
//                    if (strings[0].equals("guncelle"))
//                    {
//                        String sorgu="update Student set name='"+ isim+
//                                "',surname='"+ soyisim+
//                                "',email='"+ email+
//                                "',number='"+ numara+
//                                "',department_id='"+ departman;
//                        ;
//                        Statement st=connection.createStatement();
//                        st.execute(sorgu);
//                        mesaj="Kayıt guncellendi";
//                        connection.close();
//                    }
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
