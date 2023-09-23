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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class UniversityCoordFragment extends Fragment  implements View.OnClickListener {
    EditText etCountry, etName, etCity, etPhone, txtIDGetir;
    TextView txtMesajlar;
    Button saveUniversity,btnGuncelle,btnSil,btnGetir;
    String sonuc,ulke,isim,sehir,telefon,ID;
    Connection connection;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_university_coord, container, false);
        etCountry = view.findViewById(R.id.edit_text_country);
        etName = view.findViewById(R.id.edit_text_name);
        etCity = view.findViewById(R.id.edit_text_city);
        etPhone = view.findViewById(R.id.edit_text_phone);
        txtMesajlar=(TextView)view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());

        saveUniversity = view.findViewById(R.id.saveUniversity);
        saveUniversity.setOnClickListener(this);
        btnGuncelle=(Button)view.findViewById(R.id.btnGuncelle);
        btnSil=(Button)view.findViewById(R.id.btnSil);
        btnGetir=(Button)view.findViewById(R.id.btnGetir);

        btnGuncelle.setOnClickListener(this);
        btnSil.setOnClickListener(this);
        btnGetir.setOnClickListener(this);
        //dept = view.findViewById(R.id.dept);
        //fact = view.findViewById(R.id.fact);
        //      db = new DBHelper(getActivity());
        //databaseUniversity = FirebaseDatabase.getInstance().getReference("university");
      /*  fact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacultyCoordFragment facultyCoordFragment = new FacultyCoordFragment();
                //FragmentManager manager = getChildFragmentManager();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,facultyCoordFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

       */
    /*    dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DepartmentFragment departmentFragment = new DepartmentFragment();
                //FragmentManager manager = getChildFragmentManager();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,departmentFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

     */

/*        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        final UniversityAdapter adapter = new UniversityAdapter();
        recyclerView.setAdapter(adapter);

        universityViewModel = new ViewModelProvider(this).get(TableUniversityViewModel.class);
        universityViewModel.getAllUniversities().observe(getActivity(), new Observer<List<TableUniversity>>() {
            @Override
            public void onChanged(List<TableUniversity> tableUniversities) {
                Toast.makeText(getActivity(), "onChanged", Toast.LENGTH_SHORT).show();
            }
        });
        */
        listele();

        return view;
    }

    public void listele()
    {
        txtMesajlar.setText("");
        GirisControl baglan=new GirisControl();
        baglan.execute("listele");
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==btnGetir.getId())
        {
            GirisControl baglan=new GirisControl();
            ID=txtIDGetir.getText().toString();
            baglan.execute("getir");
        }else
        if (v.getId()==saveUniversity.getId())
        {
            GirisControl baglan=new GirisControl();
            ulke=etCountry.getText().toString();
            isim=etName.getText().toString();
            sehir=etCity.getText().toString();
            telefon=etPhone.getText().toString();
            baglan.execute("ekle");
            listele();
        }else
        if (v.getId()==btnGuncelle.getId())
        {
            GirisControl baglan=new GirisControl();
            ulke=etCountry.getText().toString();
            isim=etName.getText().toString();
            sehir=etCity.getText().toString();
            telefon=etPhone.getText().toString();
            baglan.execute("guncelle");
            listele();
        }else
        if (v.getId()==btnSil.getId())
        {
            GirisControl baglan=new GirisControl();
            ID=txtIDGetir.getText().toString();
            baglan.execute("sil");
            listele();

        }
    }
    public class GirisControl extends AsyncTask<String,String,String>
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
            etCountry.setText(ulke);
            etName.setText(isim);
            etCity.setText(sehir);
            etPhone.setText(telefon);
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
                        String sorgu="Select uni_name,country From University";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("uni_name")+"  "+rs.getString("country")
                                    +"\n"+(char)10;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("uni_name")+"  "+rs.getString("country")
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
                        String sorgu="insert into University (country,uni_name,city,telephone) "
                                +" values('"+ ulke+"','"+ isim
                                +"','" + sehir+"','"+ telefon+"')";
                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt eklendi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("guncelle"))
                    {
                        String sorgu="update University set country='"+ ulke+
                                "',uni_name='"+ isim+
                                "',city='"+ sehir+
                                "',telephone='"+ telefon+
                                "' where university_id="+
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
                        String sorgu="delete from University where university_id="+
                                ID;

                        Statement st=connection.createStatement();
                        st.execute(sorgu);
                        mesaj="Kayıt silindi";
                        connection.close();
                    }
                    else
                    if (strings[0].equals("getir"))
                    {
                        String sorgu="select * from University where university_id="+ID;
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            ulke=rs.getString("country");
                            isim=rs.getString("uni_name");
                            sehir=rs.getString("city");
                            telefon=rs.getString("telephone");


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
