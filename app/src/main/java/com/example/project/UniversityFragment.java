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
import static com.example.project.LoginStu.TEXT;

public class UniversityFragment extends Fragment {
    TextView txtMesajlar,twuniversitystu;
    Connection connection;
    String sonuc;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "sharedPrefs";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_stu_university,container,false);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        twuniversitystu = (TextView) view.findViewById(R.id.twuniversitystu);
        SharedPreferences shp = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = shp.getString(TEXT,"No name defined");
        twuniversitystu.setText(text);
        twuniversitystu.setVisibility(View.GONE);
        listele();
        return view;
    }
    public void listele() {
        txtMesajlar.setText("");
        GirisKontrolUSF baglan = new GirisKontrolUSF();
        baglan.execute("listele");
    }
    public class GirisKontrolUSF extends AsyncTask<String,String,String>
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
                        String sorgu="SELECT        Student.number, Department.dname, Faculty.name, University.uni_name, University.country, University.telephone\n" +
                                "FROM            Student INNER JOIN\n" +
                                "                         Department ON Student.erasmusdepartment_id = Department.department_id INNER JOIN\n" +
                                "                         Faculty ON Department.faculty_id = Faculty.faculty_id INNER JOIN\n" +
                                "                         University ON Faculty.university_id = University.university_id\n" +
                                "WHERE        (Student.email = '"+twuniversitystu.getText()+"')";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+="Numara : "+rs.getString("number")+" \nDepartman Adı : "+rs.getString("dname")+" \nFakülte Adı : "+
                                    rs.getString("name")+" \nÜniversite Adı : "+
                                    rs.getString("uni_name")+" \nÜlke : "+
                                    rs.getString("country")+" \nTelefon : "+
                                    rs.getString("telephone")
                                    +"\n"+(char)10+(char)13+(char)10+(char)13;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("number")+" "+rs.getString("dname")+" "+
                                        rs.getString("name")+" "+
                                        rs.getString("uni_name")+" "+
                                        rs.getString("country")+" "+
                                        rs.getString("telephone")
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

    //private String mailgoster() {
    //Intent intent = getActivity().getIntent();
    //String email = intent.getStringExtra("EXTRA_MESSAGE");
    //new LoginStu.userLogin.execute(new String[]{"userid"});
    //return email;
    //}

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