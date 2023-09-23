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

public class CoordinatorFragment extends Fragment implements View.OnClickListener {
    EditText et_name,et_lastname,et_mail,et_uname,et_password,et_deptID,txtIDGetir;
    Button btnGuncelle,btnSil,btnGetir,saveKoord;
    TextView txtMesajlar;
    String sonuc,isim,soyisim,mail,uname,password,deptid,ID;
    Connection connection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ircoord_coord, container, false);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_lastname = (EditText)view.findViewById(R.id.et_lastname);
        et_mail = (EditText)view.findViewById(R.id.et_mail);
        et_uname = (EditText)view.findViewById(R.id.et_uname);
        et_password = (EditText)view.findViewById(R.id.et_password);
        et_deptID = (EditText)view.findViewById(R.id.et_deptID);
        txtIDGetir=(EditText)view.findViewById(R.id.txtIDGetir);
        btnGuncelle = (Button) view.findViewById(R.id.btnGuncelle);
        saveKoord = (Button) view.findViewById(R.id.saveKoord);
        btnGetir = (Button) view.findViewById(R.id.btnGetir);
        btnSil = (Button) view.findViewById(R.id.btnSil);
        btnGuncelle.setOnClickListener(this);
        btnSil.setOnClickListener(this);
        btnGetir.setOnClickListener(this);
        saveKoord.setOnClickListener(this);

        listele();

        return view;
    }

    public void listele() {
        txtMesajlar.setText("");
        GirisKontrolICF baglan = new GirisKontrolICF();
        baglan.execute("listele");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnGetir.getId()) {
            GirisKontrolICF baglan = new GirisKontrolICF();
            ID = txtIDGetir.getText().toString();
            baglan.execute("getir");
        } else if (v.getId() == saveKoord.getId()) {
            GirisKontrolICF baglan = new GirisKontrolICF();
            isim = et_name.getText().toString();
            soyisim = et_lastname.getText().toString();
            mail = et_mail.getText().toString();
            uname = et_uname.getText().toString();
            password = et_password.getText().toString();
            deptid = et_deptID.getText().toString();
            baglan.execute("ekle");
            listele();

        } else if (v.getId() == btnGuncelle.getId()) {
            GirisKontrolICF baglan = new GirisKontrolICF();
            isim = et_name.getText().toString();
            soyisim = et_lastname.getText().toString();
            mail = et_mail.getText().toString();
            uname = et_uname.getText().toString();
            password = et_password.getText().toString();
            deptid = et_deptID.getText().toString();
            baglan.execute("guncelle");
            listele();
        } else if (v.getId() == btnSil.getId()) {
            GirisKontrolICF baglan = new GirisKontrolICF();
            ID = txtIDGetir.getText().toString();
            baglan.execute("sil");
            listele();
        }

    }
    public class GirisKontrolICF extends AsyncTask<String,String,String>
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
            et_lastname.setText(soyisim);
            et_mail.setText(mail);
            et_uname.setText(uname);
            et_password.setText(password);
            et_deptID.setText(deptid);
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
                        String sorgu="Select username, name, surname, email, Department.dname From Coordinator inner join Department ON Coordinator.department_id = Department.department_id";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("username")+" "+rs.getString("name")+" "+
                                    rs.getString("surname")+" "+rs.getString("email")+" "+rs.getString("dname")
                                    +"\n"+(char)10+(char)13+(char)10+(char)13;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("username")+" "+rs.getString("name")+" "+
                                        rs.getString("surname")+" "+rs.getString("email")+" "+rs.getString("dname")
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
                    else
                    if (strings[0].equals("ekle"))
                    {
                        String sorgu="insert into Coordinator (name,surname,email,username,password,department_id) "
                                +" values('"+ isim+"','"+ soyisim
                                +"','" + mail+"','" + uname+"','" + password+"','" +deptid+"')";
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt eklendi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("guncelle"))
                    {
                        String sorgu="update Coordinator set name='"+ isim+
                                "',surname='"+ soyisim+
                                "',email='"+ mail+
                                "',username='"+ uname+
                                "',password='"+ password+
                                "',department_id='"+ deptid+
                                "' where coordinator_id"+
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
                        String sorgu="delete from Coordinator where coordinator_id="+
                                ID;

                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt silindi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("getir"))
                    {
                        String sorgu="select * from Coordinator where coordinator_id="+ID;
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            isim=rs.getString("name");
                            soyisim=rs.getString("surname");
                            mail=rs.getString("email");
                            uname=rs.getString("username");
                            password=rs.getString("password");
                            deptid=rs.getString("department_id");


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
