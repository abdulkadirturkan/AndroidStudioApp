package com.example.project;

import android.annotation.SuppressLint;
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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static android.content.Context.MODE_PRIVATE;
import static com.example.project.LoginCoord.COORDLOGIN;
import static com.example.project.currentStudentFragment.STUNO;

public class SohbetFragment extends Fragment implements View.OnClickListener {
    EditText txtBaslik,txtMesaj;
    TextView twOgrenciBilgi,twKoordinatorBilgi,txtReceived3,txtReceived2,txtReceived1,txtSend3,txtSend2,txtSend1;
    Button btnGonder;
    Connection connection,con1;
    SharedPreferences shp,shp1;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "sharedPrefsStu";
    String sonuc,message,received1,csend2,crec1,csend3,crec2,crec3;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sohbet,container,false);
        txtBaslik=(EditText)view.findViewById(R.id.txtBaslik);
        txtMesaj=(EditText)view.findViewById(R.id.txtMesaj);
        twOgrenciBilgi = (TextView) view.findViewById(R.id.twOgrenciBilgi);
        twKoordinatorBilgi = (TextView) view.findViewById(R.id.twKoordinatorBilgi);
        txtReceived3=(TextView)view.findViewById(R.id.txtReceived3);
        txtReceived3.setMovementMethod(new ScrollingMovementMethod());
        txtReceived2=(TextView)view.findViewById(R.id.txtReceived2);
        txtReceived2.setMovementMethod(new ScrollingMovementMethod());
        txtReceived1=(TextView)view.findViewById(R.id.txtReceived1);
        txtReceived1.setMovementMethod(new ScrollingMovementMethod());
        txtSend3=(TextView)view.findViewById(R.id.txtSend3);
        txtSend3.setMovementMethod(new ScrollingMovementMethod());
        txtSend2=(TextView)view.findViewById(R.id.txtSend2);
        txtSend2.setMovementMethod(new ScrollingMovementMethod());
        txtSend1=(TextView)view.findViewById(R.id.txtSend1);
        txtSend1.setMovementMethod(new ScrollingMovementMethod());
        btnGonder=(Button)view.findViewById(R.id.btnGonder);
        btnGonder.setOnClickListener(this);
        SharedPreferences shp = getContext().getSharedPreferences("sharedPrefsStu", MODE_PRIVATE);
        String stu = shp.getString(STUNO,"No name defined");
        twOgrenciBilgi.setText(stu);
        SharedPreferences shp1 = getContext().getSharedPreferences("sharedPrefsCoord", MODE_PRIVATE);
        String coord = shp1.getString(COORDLOGIN,"No name defined");
        twKoordinatorBilgi.setText(coord);
        twKoordinatorBilgi.setVisibility(View.GONE);
        listele();
        listele1();
        return view;
    }
    public void listele1(){
        try {
            SqlConnection sqlConnection = new SqlConnection();
            con1 = sqlConnection.conclass();
            String rec1 = "SELECT message FROM (SELECT message, ROW_NUMBER() OVER(ORDER BY chat_id DESC) AS ROW FROM Chat WHERE" +
                    " sender='"+twOgrenciBilgi.getText()+"' AND receiver ='"+twKoordinatorBilgi.getText()+"') AS TMP WHERE ROW = 1";
            Statement st1 = con1.createStatement();
            ResultSet rs1 = st1.executeQuery(rec1);
            crec1 = "";
            crec2 = "";
            crec3 = "";
            csend2 = "";
            csend3 = "";
            if (rs1.next())
            {
                crec1+=rs1.getString("message")+"\n";
            }
            txtReceived1.setText(crec1);
            String send1 = "SELECT message FROM (SELECT message, ROW_NUMBER() OVER(ORDER BY chat_id DESC) AS ROW FROM Chat WHERE" +
                    " sender='"+twKoordinatorBilgi.getText()+"' AND receiver ='"+twOgrenciBilgi.getText()+"') AS TMP WHERE ROW = 2";
            Statement st2 = con1.createStatement();
            ResultSet rs2 = st2.executeQuery(send1);
            if (rs2.next())
            {
                csend2+=rs2.getString("message")+"\n";
            }
            txtSend2.setText(csend2);
            String send2 = "SELECT message FROM (SELECT message, ROW_NUMBER() OVER(ORDER BY chat_id DESC) AS ROW FROM Chat WHERE" +
                    " sender='"+twKoordinatorBilgi.getText()+"' AND receiver ='"+twOgrenciBilgi.getText()+"') AS TMP WHERE ROW = 3";
            Statement st3 = con1.createStatement();
            ResultSet rs3 = st3.executeQuery(send2);
            if (rs3.next())
            {
                csend3+=rs3.getString("message")+"\n";
            }
            txtSend3.setText(csend3);
            String rec2 = "SELECT message FROM (SELECT message, ROW_NUMBER() OVER(ORDER BY chat_id DESC) AS ROW FROM Chat WHERE" +
                    " sender='"+twOgrenciBilgi.getText()+"' AND receiver ='"+twKoordinatorBilgi.getText()+"') AS TMP WHERE ROW = 2";
            Statement st4 = con1.createStatement();
            ResultSet rs4 = st4.executeQuery(rec2);
            if (rs4.next())
            {
                crec2+=rs4.getString("message")+"\n";
            }
            txtReceived2.setText(crec2);
            String rec3 = "SELECT message FROM (SELECT message, ROW_NUMBER() OVER(ORDER BY chat_id DESC) AS ROW FROM Chat WHERE" +
                    " sender='"+twOgrenciBilgi.getText()+"' AND receiver ='"+twKoordinatorBilgi.getText()+"') AS TMP WHERE ROW = 3";
            Statement st5 = con1.createStatement();
            ResultSet rs5 = st5.executeQuery(rec3);
            if (rs5.next())
            {
                crec3+=rs5.getString("message")+"\n";
            }
            txtReceived3.setText(crec3);
        }
            catch (Exception ex) {
                //
            }
    }
    public void listele(){
        //txtReceived3.setText("");
        //txtSend3.setText("");
        //txtReceived2.setText("");
        //txtSend2.setText("");
        //txtReceived1.setText("");
        //txtSend1.setText("");
        GirisKontrolSoF baglan = new GirisKontrolSoF();
        baglan.execute("listele");
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == btnGonder.getId()) {
            GirisKontrolSoF baglan = new GirisKontrolSoF();
            message = txtMesaj.getText().toString();
            baglan.execute("ekle");
            listele();
        }
    }
    public class GirisKontrolSoF extends AsyncTask<String,String,String>
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
            txtSend1.setText(sonuc);
            //txtReceived1.setText(received1);
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
                        String sorgu="SELECT TOP 1 message FROM Chat WHERE sender='"+twKoordinatorBilgi.getText()+"' AND receiver ='"+twOgrenciBilgi.getText()+"' ORDER BY chat_id DESC";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("message")
                                    +"\n";

                            basarili=true;
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
                        String sorgu="insert into Chat (sender,receiver,message) "
                                +" values('"+ twKoordinatorBilgi.getText()+"','"
                                + twOgrenciBilgi.getText()+"','"+ message+"')";
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt eklendi";
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
