package com.example.project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.example.project.LoginStu.TEXT;

public class ProfileFragment extends Fragment implements View.OnClickListener  {
    TextView txtMesajlar,twmail,twnumber,twsurname,twdept;
    ImageView twpicture;
    Button btnGuncelle;
    Connection connection;
    String sonuc,soyisim,numara,email,departman;
    //DatabaseReference databaseRef;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
    Context _context;
    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    private static final String TAG = "ProfileFragment";
    public static final String SHARED_PREFS = "sharedPrefs";
    private static final int REQUEST_READ_PERMISSION = 120;
    private static final String USERS = "Users";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil,container,false);
        //Intent intent = Intent.getIntent();
        //LoginStu loginStu = (LoginStu) getActivity();
        //String email= loginStu.
        //Set Preference
        SharedPreferences shp = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = shp.getString(TEXT,"No name defined");
        //Toast.makeText(getActivity(),text,Toast.LENGTH_LONG).show();
        //SharedPreferences.Editor prefsEditor;
        //prefsEditor = shp.edit();
//strVersionName->Any value to be stored
        String rsdText = shp.getString("text", null);
        if (rsdText != null) {
            String name = shp.getString(TEXT, "No name defined");//"No name defined" is the default value.
            //Toast.makeText(getActivity(),name,Toast.LENGTH_LONG).show();
        }
        //SharedPreferences myPrefs;

        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        twsurname = (TextView) view.findViewById(R.id.twsurname);
        twmail = (TextView) view.findViewById(R.id.twmail);
        twnumber = (TextView) view.findViewById(R.id.twnumber);
        twdept = (TextView) view.findViewById(R.id.twdept);
        //twprofil = (TextView) view.findViewById(R.id.twprofil);
        //Bundle bundle = this.getArguments();
        //String data = bundle.getString("sharedPrefs");
        //twprofil.setText(data);
        //twprofil.setVisibility(View.GONE);
        //twmail.setVisibility(View.GONE);
        twmail.setText(text);
        twmail.setVisibility(View.GONE);
        twsurname.setVisibility(View.GONE);
        twnumber.setVisibility(View.GONE);
        twdept.setVisibility(View.GONE);
        twpicture = (ImageView) view.findViewById(R.id.twpicture);
        btnGuncelle = (Button) view.findViewById(R.id.btnGuncelle);
        btnGuncelle.setOnClickListener(this);
        //shp = _context.getSharedPreferences("UserInfo", MODE_PRIVATE);
        //String userid = shp.getString("UserId", "none");
        //twmail.setText(userid);
       // pref = _context.getSharedPreferences("Giris", Context.MODE_PRIVATE);
        // editor = pref.edit();
        //String email= pref.getString("saved_Email","");
        //twmail.setText(email);
        //database = FirebaseDatabase.getInstance();
        //userRef = database.getReference(USERS);
        //DatabaseReference oneRef = userRef.child("Users").child("0PyFHN0RkSdPN2FI0IWljniELL33");
     /*   twpicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,User.SELECT_PICTURE);
            }
        }); */
        listele();
        return view;
    }
    public void listele() {
        txtMesajlar.setText("");
        GirisKontrolPFC baglan = new GirisKontrolPFC();
        baglan.execute("listele");
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == btnGuncelle.getId()) {
            GirisKontrolPFC baglan = new GirisKontrolPFC();
            //isim = twprofil.getText().toString();
            soyisim = twsurname.getText().toString();
            email = twmail.getText().toString();
            numara = twnumber.getText().toString();
            departman = twdept.getText().toString();
            baglan.execute("guncelle");
            listele();
        }
    }
    public class GirisKontrolPFC extends AsyncTask<String,String,String>
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
                //String rsdText = shp.getString("text", null);
                if(connection == null){
                    Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_LONG).show();
                    z = "On Internet Connection";
                }
                else
                {
                    if (strings[0].equals("listele"))
                    {
                        String sorgu="Select name, surname, email, number, Department.dname From Student inner join Department ON Student.department_id = Department.department_id WHERE email='"+twmail.getText()+"'";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("name")+" "+rs.getString("surname")+" \nMail : "+
                                    rs.getString("email")+" \nNumara : "+rs.getString("number")+" \nDepartman : "+rs.getString("dname")
                                    +"\n"+(char)10+(char)13+(char)10+(char)13;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("name")+" "+rs.getString("surname")+" "+
                                        rs.getString("email")+" "+rs.getString("number")+" "+rs.getString("dname")
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
//                    else
//                    if (strings[0].equals("guncelle"))
//                    {
//                        String sorgu="update Student set name='"+ isim+
//                                "',surname='"+ soyisim+
//                                "',email='"+ email+
//                                "',number='"+ numara+
//                                "',department_id='"+ departman;
//                        ;
//                        Statement st=connection.createStatement();
//                        st.execute(sorgu);
//                        mesaj="Kayıt guncellendi";
//                        connection.close();
//                    }
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
    /*
    public void checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
        // [END check_current_user]
    }
    public void getUserProfile() {
        // [START get_user_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        // [END get_user_profile]
    }
    public void updateProfile() {
        // [START update_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Test User")
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
        // [END update_profile]
    }

*/
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
