package com.example.project;

import android.annotation.SuppressLint;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MufredatFragment extends Fragment implements View.OnClickListener {
    EditText et_name, et_code, et_deptId, et_ects, et_semester, txtIDGetir;
    TextView txtMesajlar;
    Button saveMufredat, btnGuncelle, btnSil, btnGetir;
    String sonuc, isim, kod, deptId, ects, semester, ID;
    Connection connection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mufredat, container, false);
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_code = (EditText) view.findViewById(R.id.et_code);
        et_deptId = (EditText) view.findViewById(R.id.et_deptId);
        et_ects = (EditText) view.findViewById(R.id.et_ects);
        et_semester = (EditText) view.findViewById(R.id.et_semester);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        txtIDGetir = (EditText) view.findViewById(R.id.txtIDGetir);
        saveMufredat = (Button) view.findViewById(R.id.saveMufredat);
        saveMufredat.setOnClickListener(this);
        btnGuncelle = (Button) view.findViewById(R.id.btnGuncelle);
        btnGuncelle.setOnClickListener(this);
        btnSil = (Button) view.findViewById(R.id.btnSil);
        btnGetir = (Button) view.findViewById(R.id.btnGetir);
        btnSil.setOnClickListener(this);
        btnGetir.setOnClickListener(this);

        listele();

        return view;
    }

    public void listele() {
        txtMesajlar.setText("");
        GirisKontrolM baglan = new GirisKontrolM();
        baglan.execute("listele");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnGetir.getId()) {
            GirisKontrolM baglan = new GirisKontrolM();
            ID = txtIDGetir.getText().toString();
            baglan.execute("getir");
        } else if (v.getId() == saveMufredat.getId()) {
            GirisKontrolM baglan = new GirisKontrolM();
            isim = et_name.getText().toString();
            kod = et_code.getText().toString();
            deptId = et_deptId.getText().toString();
            ects = et_ects.getText().toString();
            semester = et_semester.getText().toString();
            baglan.execute("ekle");
            listele();
        } else if (v.getId() == btnGuncelle.getId()) {
            GirisKontrolM baglan = new GirisKontrolM();
            isim = et_name.getText().toString();
            kod = et_code.getText().toString();
            deptId = et_deptId.getText().toString();
            ects = et_ects.getText().toString();
            semester = et_semester.getText().toString();
            baglan.execute("guncelle");
            listele();
        } else if (v.getId() == btnSil.getId()) {
            GirisKontrolM baglan = new GirisKontrolM();
            ID = txtIDGetir.getText().toString();
            baglan.execute("sil");
            listele();
        }
    }
    public class GirisKontrolM extends AsyncTask<String,String,String>
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
            et_name.setText(isim);
            et_code.setText(kod);
            et_deptId.setText(deptId);
            et_ects.setText(ects);
            et_semester.setText(semester);
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
                        String sorgu="Select * From Lesson";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("lesson_id")+" "+rs.getString("name")+" "+
                                    rs.getString("department_id")+" "+rs.getString("ects")+" "+rs.getString("semester")
                                    +"\n"+(char)10;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("lesson_id")+" "+rs.getString("name")+" "+
                                        rs.getString("department_id")+" "+rs.getString("ects")+" "+rs.getString("semester")
                                        +"\n"+(char)10;
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
                    if (strings[0].equals("ekle"))
                    {
                        String sorgu="insert into Lesson (name,code,department_id,ects,semester) "
                                +" values('"+ isim+"','"+ kod
                                +"','"+ deptId
                                +"','" + ects
                                +"','"+ semester+"')";
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt eklendi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("guncelle"))
                    {
                        String sorgu="update Lesson set name='"+ isim+
                                "',code='"+ kod+
                                "',department_id='"+ deptId+
                                "',ects='"+ ects+
                                "',semester='"+ semester+
                                "' where lesson_id="+
                                ID;
                        ;
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt guncellendi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("sil"))
                    {
                        String sorgu="delete from Lesson where Lesson_id="+
                                ID;

                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt silindi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("getir"))
                    {
                        String sorgu="select * from Lesson where lesson_id="+ID;
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            isim=rs.getString("name");
                            kod=rs.getString("code");
                            deptId=rs.getString("department_id");
                            ects=rs.getString("ects");
                            semester=rs.getString("semester");


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
