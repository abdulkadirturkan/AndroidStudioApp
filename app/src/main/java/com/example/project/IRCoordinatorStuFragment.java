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

public class IRCoordinatorStuFragment extends Fragment  implements View.OnClickListener {
    EditText et_name,et_lastname,et_mail,et_number,et_password,et_coordID,et_deptID,et_edeptID,et_cinsiyet,et_reg,et_telephone,txtIDGetir,et_class;
    Button btnGuncelle,btnSil,btnGetir,saveStud;
    TextView txtMesajlar;
    String sonuc,isim,soyisim,mail,cinsiyet,password,deptid,ID,number,edeptid,reg,coordid,telefon,sinif;
    Connection connection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ircoord_stu, container, false);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_lastname = (EditText)view.findViewById(R.id.et_lastname);
        et_mail = (EditText)view.findViewById(R.id.et_mail);
        et_number = (EditText)view.findViewById(R.id.et_number);
        et_password = (EditText)view.findViewById(R.id.et_password);
        et_telephone = (EditText)view.findViewById(R.id.et_telephone);
        et_coordID = (EditText)view.findViewById(R.id.et_coordID);
        et_deptID = (EditText)view.findViewById(R.id.et_deptID);
        et_edeptID = (EditText)view.findViewById(R.id.et_edeptID);
        et_cinsiyet = (EditText)view.findViewById(R.id.et_cinsiyet);
        et_class = (EditText)view.findViewById(R.id.et_class);
        et_reg = (EditText)view.findViewById(R.id.et_reg);
        txtIDGetir=(EditText)view.findViewById(R.id.txtIDGetir);
        btnGuncelle = (Button) view.findViewById(R.id.btnGuncelle);
        saveStud = (Button) view.findViewById(R.id.saveStud);
        btnGetir = (Button) view.findViewById(R.id.btnGetir);
        btnSil = (Button) view.findViewById(R.id.btnSil);
        btnGuncelle.setOnClickListener(this);
        btnSil.setOnClickListener(this);
        btnGetir.setOnClickListener(this);
        saveStud.setOnClickListener(this);

        listele();

        return view;
    }
    public void listele() {
        txtMesajlar.setText("");
        GirisKontrolCST baglan = new GirisKontrolCST();
        baglan.execute("listele");
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == btnGetir.getId()) {
            GirisKontrolCST baglan = new GirisKontrolCST();
            ID = txtIDGetir.getText().toString();
            baglan.execute("getir");
        } else if (v.getId() == saveStud.getId()) {
            GirisKontrolCST baglan = new GirisKontrolCST();
            isim = et_name.getText().toString();
            soyisim = et_lastname.getText().toString();
            mail = et_mail.getText().toString();
            number = et_number.getText().toString();
            password = et_password.getText().toString();
            sinif = et_class.getText().toString();
            telefon = et_telephone.getText().toString();
            coordid = et_coordID.getText().toString();
            deptid = et_deptID.getText().toString();
            edeptid = et_edeptID.getText().toString();
            cinsiyet = et_cinsiyet.getText().toString();
            reg = et_reg.getText().toString();
            baglan.execute("ekle");
            listele();

        } else if (v.getId() == btnGuncelle.getId()) {
            GirisKontrolCST baglan = new GirisKontrolCST();
            isim = et_name.getText().toString();
            soyisim = et_lastname.getText().toString();
            mail = et_mail.getText().toString();
            number = et_number.getText().toString();
            password = et_password.getText().toString();
            sinif = et_class.getText().toString();
            telefon = et_telephone.getText().toString();
            coordid = et_coordID.getText().toString();
            deptid = et_deptID.getText().toString();
            edeptid = et_edeptID.getText().toString();
            cinsiyet = et_cinsiyet.getText().toString();
            reg = et_reg.getText().toString();
            baglan.execute("guncelle");
            listele();
        } else if (v.getId() == btnSil.getId()) {
            GirisKontrolCST baglan = new GirisKontrolCST();
            ID = txtIDGetir.getText().toString();
            baglan.execute("sil");
            listele();
        }
    }
    public class GirisKontrolCST extends AsyncTask<String,String,String>
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
            et_number.setText(number);
            et_password.setText(password);
            et_telephone.setText(telefon);
            et_class.setText(sinif);
            et_coordID.setText(coordid);
            et_deptID.setText(deptid);
            et_edeptID.setText(edeptid);
            et_cinsiyet.setText(cinsiyet);
            et_reg.setText(reg);
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
                        String sorgu="Select number, name, surname, Department.dname From Student inner join Department ON Student.department_id = Department.department_id";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("number")+" "+rs.getString("name")+" "+
                                    rs.getString("surname")+" "+rs.getString("dname")
                                    +"\n"+(char)10+(char)13+(char)10+(char)13;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("number")+" "+rs.getString("name")+" "+
                                        rs.getString("surname")+" "+rs.getString("dname")
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
                        String sorgu="insert into Student (name,surname,email,number,password,coordinator_id,telephone,department_id,erasmusdepartment_id,gender,date,class) "
                                +" values('"+ isim+"','"+ soyisim
                                +"','" + mail+"','" + number+"','" + password+"','" +coordid+"','" +telefon+"','" +deptid+"','" +edeptid+"','" +cinsiyet+"','" +reg+"','" +sinif+"')";
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt eklendi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("guncelle"))
                    {
                        String sorgu="update Student set number='"+ number+
                                "',name='"+ isim+
                                "',surname='"+ soyisim+
                                "',email='"+ mail+
                                "',password='"+ password+
                                "',coordinator_id='"+ coordid+
                                "',department_id='"+ deptid+
                                "',erasmusdepartment_id='"+ edeptid+
                                "',telephone='"+ telefon+
                                "',gender='"+ cinsiyet+
                                "',class='"+ sinif+
                                "',date='"+ reg+
                                "' where student_id"+
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
                        String sorgu="delete from Student where student_id="+
                                ID;

                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt silindi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("getir"))
                    {
                        String sorgu="select * from Student where student_id="+ID;
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            number=rs.getString("number");
                            isim=rs.getString("name");
                            soyisim=rs.getString("surname");
                            mail=rs.getString("email");
                            telefon=rs.getString("telephone");
                            sinif=rs.getString("class");
                            password=rs.getString("password");
                            coordid=rs.getString("coordinator_id");
                            deptid=rs.getString("department_id");
                            edeptid=rs.getString("erasmusdepartment_id");
                            cinsiyet=rs.getString("gender");
                            reg=rs.getString("date");


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
