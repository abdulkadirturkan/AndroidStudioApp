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

public class currentStudentFragment  extends Fragment implements View.OnClickListener  {
    EditText txtIDGetir;
    TextView txtMesajlar,txtMesajlarAd,txtMesajlarS,txtMesajlarB;
    Button btnGetir;
    String sonuc,ID,sonucNo,sonucAdS,sonucS,sonucB;
    public static final String SHARED_PREFS = "sharedPrefsStu";
    public static final String STUNO = "stu_no";
    private String stuno;
    Connection connection;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_student,container,false);
        txtIDGetir=(EditText)view.findViewById(R.id.txtIDGetir);

        txtMesajlar=(TextView)view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarAd=(TextView)view.findViewById(R.id.txtMesajlarAd);
        txtMesajlarAd.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarS=(TextView)view.findViewById(R.id.txtMesajlarS);
        txtMesajlarS.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarB=(TextView)view.findViewById(R.id.txtMesajlarB);
        txtMesajlarB.setMovementMethod(new ScrollingMovementMethod());
        btnGetir=(Button)view.findViewById(R.id.btnGetir);
        btnGetir.setOnClickListener(this);

        listele();
        return view;

    }
    public void listele()
    {
        txtMesajlar.setText("");
        GirisKontrolO baglan=new GirisKontrolO();
        baglan.execute("listele");
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==btnGetir.getId())
        {
            //ogrenci nosuyla AktivasyonFragment a yönlendirme yapılacak..
            saveData();
            ActivationFragment activationFragment = new ActivationFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,activationFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public void saveData(){
        SharedPreferences shp = getActivity().getSharedPreferences("sharedPrefsStu",MODE_PRIVATE);
        SharedPreferences.Editor editor =shp.edit();
        editor.putString(STUNO,txtIDGetir.getText().toString());
        editor.apply();
    }
    public void loadData(){
        SharedPreferences shp = getActivity().getSharedPreferences("sharedPrefsStu",MODE_PRIVATE);
        stuno = shp.getString(STUNO,"");
    }
    public class GirisKontrolO extends AsyncTask<String,String,String>
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
            txtMesajlarAd.setText(sonucAdS);
            txtMesajlarS.setText(sonucS);
            txtMesajlarB.setText(sonucB);
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
                        String sorgu="Select Student.number, Student.name, Student.surname, Student.class, Department.dname From Student inner join Department ON Student.department_id = Department.department_id";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        sonucAdS="";
                        sonucS="";
                        sonucB="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("number")+"\n"+(char)10;
                            sonucAdS+=rs.getString("name")+" "+rs.getString("surname")+"\n"+(char)10;
                            sonucS+=rs.getString("class")+"\n"+(char)10;
                            sonucB+=rs.getString("dname")+"\n";

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("number")+"\n"+(char)10;
                                sonucAdS+=rs.getString("name")+" "+rs.getString("surname")+"\n"+(char)10;
                                sonucS+=rs.getString("class")+"\n"+(char)10;
                                sonucB+=rs.getString("dname")+"\n";
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
