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

public class oldStudentFragment extends Fragment {
    TextView txtMesajlar;
    String sonuc;
    Connection connection;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_old_student,container,false);
        txtMesajlar=(TextView)view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        listele();
        return view;

    }
    public void listele()
    {
        txtMesajlar.setText("");
        GirisKontrolOSFR baglan=new GirisKontrolOSFR();
        baglan.execute("listele");
    }
    public class GirisKontrolOSFR extends AsyncTask<String,String,String>
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
                        String sorgu="SELECT Student_grad.name, Student_grad.surname, Student_grad.number, University.uni_name,Student_grad.class,CASE WHEN grade = 4.00 THEN 'AA'\n" +
                                "WHEN grade >= 3.75 THEN 'AB'\n" +
                                "WHEN grade >= 3.33 THEN 'BA'\n" +
                                "WHEN grade >= 3.0 THEN 'BB'\n" +
                                "WHEN grade >= 2.75 THEN 'BC'\n" +
                                "WHEN grade >= 2.33 THEN 'CB'\n" +
                                "WHEN grade >= 2.00 THEN 'CC'\n" +
                                "WHEN grade >= 1.75 THEN 'CD'\n" +
                                "WHEN grade >= 1.33 THEN 'DC'\n" +
                                "WHEN grade >= 1.00 THEN 'DD'\n" +
                                "ELSE 'FF'\n" +
                                "END AS QuantityText\n" +
                                "FROM Student_grad INNER JOIN Department ON Student_grad.erasmusdepartment_id = Department.department_id INNER JOIN\n" +
                                "Faculty ON Department.faculty_id = Faculty.faculty_id INNER JOIN University ON Faculty.university_id = University.university_id";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("name")+" "+rs.getString("surname")+" "+rs.getString("number")+" "+
                                    rs.getString("uni_name")+" "+
                                    rs.getString("QuantityText")+" "+rs.getString("class")
                                    +"\n"+(char)10;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("name")+" "+rs.getString("surname")+" "+rs.getString("number")+" "+
                                        rs.getString("uni_name")+" "+
                                        rs.getString("QuantityText")+" "+rs.getString("class")
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
