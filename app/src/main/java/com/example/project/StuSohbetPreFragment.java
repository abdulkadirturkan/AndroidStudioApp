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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static android.content.Context.MODE_PRIVATE;
import static com.example.project.currentStudentFragment.STUNO;

public class StuSohbetPreFragment extends Fragment implements View.OnClickListener {
    EditText txtMesaj;
    TextView txtMesajlar,twOgrenciBilgi;
    Button btnGonder;
    //private String text;
    Connection connection;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "sharedPrefsSohbet";
    public static final String STUSOHBETCOORD = "stu_sohbet_coord";
    String sonuc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stu_sohbet_pre,container,false);
        btnGonder = (Button) view.findViewById(R.id.btnGonder);
        btnGonder.setOnClickListener(this);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        txtMesaj = (EditText) view.findViewById(R.id.txtMesaj);
        twOgrenciBilgi = (TextView) view.findViewById(R.id.twOgrenciBilgi);
        SharedPreferences shp = getContext().getSharedPreferences("sharedPrefsStu", MODE_PRIVATE);
        String stu = shp.getString(STUNO,"No name defined");
        twOgrenciBilgi.setText(stu);
        listele();
        return view;
    }
    public void listele(){
        txtMesajlar.setText("");
        GirisKontrolSSPF baglan = new GirisKontrolSSPF();
        baglan.execute("listele");
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==btnGonder.getId())
        {
            saveData();
            StuSohbetFragment stuSohbetFragment = new StuSohbetFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,stuSohbetFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public void saveData() {
        SharedPreferences shp = getActivity().getSharedPreferences("sharedPrefsSohbet", MODE_PRIVATE);
        SharedPreferences.Editor editor =shp.edit();
        editor.putString(STUSOHBETCOORD,txtMesaj.getText().toString());
        editor.apply();
    }

    public class GirisKontrolSSPF extends AsyncTask<String,String,String>
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
                if(connection == null){
                    Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                    z = "On Internet Connection";
                }
                else
                {
                    if (strings[0].equals("listele"))
                    {
                        String sorgu="SELECT name, surname, email FROM Coordinator";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("name")+" "+rs.getString("surname")+" \nMail : "+
                                    rs.getString("email")
                                    +"\n"+(char)10;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("name")+" "+rs.getString("surname")+" \nMail : "+
                                        rs.getString("email")
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
