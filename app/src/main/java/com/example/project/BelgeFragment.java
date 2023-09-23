package com.example.project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static android.app.appsearch.AppSearchResult.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.example.project.LoginStu.TEXT;

public class BelgeFragment extends AppCompatActivity {
    EditText editTextPdf, editTextPdfName,editTextPdfName1;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button btnPDFUpload,btnGeriDonBelge;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    TextView twTranskript1,twLA1,twRS1,twOgrenciBilgi,twNumberOne;
    Connection con1;
    String transkript1,recognitionSheet1,learningAggrement1,number1;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Nullable
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_belge_student);
        //View view = inflater.inflate(R.layout.fragment_belge_student,container,false);
        twTranskript1 = (TextView) findViewById(R.id.twTranskript1);
        radioGroup = findViewById(R.id.radioGroup);
        twLA1 = (TextView) findViewById(R.id.twLA1);
        twRS1 = (TextView) findViewById(R.id.twRS1);
        editTextPdf = (EditText) findViewById(R.id.editTextPdf);
        twNumberOne = (TextView) findViewById(R.id.twNumberOne);
        twNumberOne.setVisibility(View.GONE);
        twOgrenciBilgi = (TextView) findViewById(R.id.twOgrenciBilgi);
        SharedPreferences shp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = shp.getString(TEXT,"No name defined");
        twOgrenciBilgi.setText(text);
        twOgrenciBilgi.setVisibility(View.GONE);
        //editTextPdfName = (EditText) findViewById(R.id.editTextPdfName);
        //editTextPdfName1 = (EditText) findViewById(R.id.editTextPdfName1);
        btnPDFUpload = (Button) findViewById(R.id.btnPDFUpload);
        btnGeriDonBelge = (Button) findViewById(R.id.btnGeriDonBelge);
        //btnGeriDonBelge a geri dönme ekle
        btnGeriDonBelge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BelgeFragment.this, studentActivity.class));
            }
        });

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploadPDF_");
        btnPDFUpload.setEnabled(false);
        editTextPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectPDF();
            }
        });
        listele1();
        twOgrenciBilgi.setText(text);
        //return view;
    }
    public void checkButton(View v){
        int radioId=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
        Toast.makeText(this,"Selected Radio Button: "+radioButton.getText(),Toast.LENGTH_SHORT).show();
    }
    public void listele2(){
        try {
            SqlConnection sqlConnection = new SqlConnection();
            con1 = sqlConnection.conclass();
            String sorgu="update Document set "+radioButton.getText()+"='EVET' WHERE student_id =(SELECT student_id FROM Student WHERE(email ='"+twOgrenciBilgi.getText()+"'))";
            ;
            Statement st=con1.createStatement();
            st.execute(sorgu);

        }
        catch (Exception ex) {
            //
        }
    }
    public void listele1(){
        try {
            SqlConnection sqlConnection = new SqlConnection();
            con1 = sqlConnection.conclass();
            String transkript = "SELECT transkript FROM Document WHERE student_id =(SELECT student_id FROM Student WHERE email = '"+twOgrenciBilgi.getText()+"')";
            Statement st1 = con1.createStatement();
            ResultSet rs1 = st1.executeQuery(transkript);
            transkript1 = "";
            recognitionSheet1 = "";
            learningAggrement1 = "";
            number1="";

            if (rs1.next())
            {
                transkript1+=rs1.getString("transkript");
            }
            twTranskript1.setText(transkript1);
            String recognitionSheet = "SELECT recognition_sheet FROM Document WHERE student_id =(SELECT student_id FROM Student WHERE email = '"+twOgrenciBilgi.getText()+"')";
            Statement st2 = con1.createStatement();
            ResultSet rs2 = st2.executeQuery(recognitionSheet);
            if (rs2.next())
            {
                recognitionSheet1+=rs2.getString("recognition_sheet");
            }
            twRS1.setText(recognitionSheet1);
            String learningAggrement = "SELECT learning_aggrement FROM Document WHERE student_id =(SELECT student_id FROM Student WHERE email = '"+twOgrenciBilgi.getText()+"')";
            Statement st3 = con1.createStatement();
            ResultSet rs3 = st3.executeQuery(learningAggrement);
            if (rs3.next())
            {
                learningAggrement1+=rs3.getString("learning_aggrement");
            }
            twLA1.setText(learningAggrement1);
            String number= "SELECT number FROM Student WHERE (email = '"+twOgrenciBilgi.getText()+"')";
            Statement st4 = con1.createStatement();
            ResultSet rs4 = st4.executeQuery(number);
            if(rs4.next())
            {
                number1+=rs4.getString("number");
            }
            twNumberOne.setText(number1);
        }
        catch (Exception ex) {
            //
        }
    }
    private void selectPDF(){
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            btnPDFUpload.setEnabled(true);
            editTextPdf.setText(data.getDataString()
                    .substring(data.getDataString().lastIndexOf("/") +1));
            btnPDFUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listele2();
                    uploadPDFFileFirebase(data.getData());
                    listele1();
                }
            });
        }
    }

    private void uploadPDFFileFirebase(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Dosya Yükleniyor...");
        progressDialog.show();

        StorageReference reference= storageReference.child("uploadPDF_"+twNumberOne.getText()+"_"+radioButton.getText()+".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                                while(!uriTask.isComplete()) ;
                                Uri uri = uriTask.getResult();

                                putPDF putPDF = new putPDF(editTextPdf.getText().toString(), uri.toString());
                                databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                                Toast.makeText(BelgeFragment.this,"Dosya yüklendi",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress=(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("Dosya Yükleniyor..." +(int) progress+ "%");
            }
        });
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
