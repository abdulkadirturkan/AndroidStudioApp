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
import java.sql.SQLException;
import java.sql.Statement;

public class DuyuruFragment extends Fragment implements View.OnClickListener {
    EditText txtBaslik,txtMesaj,txtIsim,txtIDGetir;
    TextView txtMesajlar;
    Button btnYeni,btnKaydet,btnGuncelle,btnSil,btnGetir;
    String vtadres,vtkuladi,vtparola,vtadi,sonuc,baslik,icerik,gonderen,ID;
    Connection connection;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duyuru,container,false);

        txtBaslik=(EditText)view.findViewById(R.id.txtBaslik);
        txtMesaj=(EditText)view.findViewById(R.id.txtMesaj);
        txtIsim=(EditText)view.findViewById(R.id.txtIsim);
        txtIDGetir=(EditText)view.findViewById(R.id.txtIDGetir);

        txtMesajlar=(TextView)view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());

        btnYeni=(Button)view.findViewById(R.id.btnYeni);
        btnKaydet=(Button)view.findViewById(R.id.btnKaydet);
        btnGuncelle=(Button)view.findViewById(R.id.btnGuncelle);
        btnSil=(Button)view.findViewById(R.id.btnSil);
        btnGetir=(Button)view.findViewById(R.id.btnGetir);

        btnYeni.setOnClickListener(this);
        btnKaydet.setOnClickListener(this);
        btnGuncelle.setOnClickListener(this);
        btnSil.setOnClickListener(this);
        btnGetir.setOnClickListener(this);

        vtadi="your db name";
        vtkuladi="db user name";
        vtparola="db password";
        vtadres="db address";

        listele();

        return view;
    }

    public void listele()
    {
        txtMesajlar.setText("");
        GirisKontrol baglan=new GirisKontrol();
        baglan.execute("listele");
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==btnGetir.getId())
        {
            GirisKontrol baglan=new GirisKontrol();
            ID=txtIDGetir.getText().toString();
            baglan.execute("getir");
        }else
        if (v.getId()==btnKaydet.getId())
        {
            GirisKontrol baglan=new GirisKontrol();
            baslik=txtBaslik.getText().toString();
            icerik=txtMesaj.getText().toString();
            gonderen=txtIsim.getText().toString();
            baglan.execute("ekle");
            listele();
        }else
        if (v.getId()==btnYeni.getId())
        {
            txtBaslik.setText("");
            txtMesaj.setText("");
            txtIsim.setText("");

        }else
        if (v.getId()==btnGuncelle.getId())
        {
            GirisKontrol baglan=new GirisKontrol();
            baslik=txtBaslik.getText().toString();
            icerik=txtMesaj.getText().toString();
            gonderen=txtIsim.getText().toString();
            baglan.execute("guncelle");
            listele();
        }else
        if (v.getId()==btnSil.getId())
        {
            GirisKontrol baglan=new GirisKontrol();
            ID=txtIDGetir.getText().toString();
            baglan.execute("sil");
            listele();
        }


    }
    public class GirisKontrol extends AsyncTask<String,String,String>
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
            txtBaslik.setText(baslik);
            txtMesaj.setText(icerik);
            txtIsim.setText(gonderen);
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
                        String sorgu="Select Notice.title, Notice.text, Coordinator.username From Notice inner join Coordinator ON Notice.coordinator_id = Coordinator.coordinator_id";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("title")+" "+rs.getString("text")+" "+
                                    rs.getString("username")
                                    +"\n\n"+(char)10;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("title")+" "+rs.getString("text")+" "+
                                        rs.getString("username")
                                        +"\n\n"+(char)10;
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
                        String sorgu="insert into Notice (title,text,coordinator_id) "
                                +" values('"+ baslik+"','"+ icerik
                                +"','" + gonderen+"')";
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt eklendi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("guncelle"))
                    {
                        String sorgu="update Notice set title='"+ baslik+
                                "',text='"+ icerik+
                                "',coordinator_id='"+ gonderen+
                                "' where notice_id="+
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
                        String sorgu="delete from Notice where notice_id="+
                                ID;

                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt silindi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("getir"))
                    {
                        String sorgu="select * from Notice where notice_id="+ID;
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            baslik=rs.getString("title");
                            icerik=rs.getString("text");
                            gonderen=rs.getString("coordinator_id");


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
