package com.example.project;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.example.project.LoginStu.TEXT;
import static com.example.project.currentStudentFragment.STUNO;

public class BelgeCoordActivity extends AppCompatActivity {
    Button btnIndir,btnYukle,btnGeriDonBelge;
    //private DrawerLayout drawer;
    EditText etOgrenciNo,etOgrenciExt;
    TextView twOgrenciBilgi,twTranskript,twLA,twRS;
    RadioGroup radioGroup;
    RadioButton radioButton,radio_one,radio_two,radio_three;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference storageReference1;
    StorageReference ref;
    DatabaseReference databaseReference;
    SharedPreferences shp;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "sharedPrefsStu";
    Connection connection,con1;
    String transkript1,recognitionSheet1,learningAggrement1;
    @Nullable
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_belge_coord);

        etOgrenciNo = (EditText) findViewById(R.id.etOgrenciNo);
        //etOgrenciExt = (EditText) view.findViewById(R.id.etOgrenciExt);
        twOgrenciBilgi = (TextView) findViewById(R.id.twOgrenciBilgi);
        SharedPreferences shp = getSharedPreferences("sharedPrefsStu", MODE_PRIVATE);
        String stu = shp.getString(STUNO,"No name defined");
        twOgrenciBilgi.setText(stu);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup = findViewById(R.id.radioGroup);
        twOgrenciBilgi = (TextView) findViewById(R.id.twOgrenciBilgi);
        twTranskript = (TextView) findViewById(R.id.twTranskript);
        twLA = (TextView) findViewById(R.id.twLA);
        twRS = (TextView) findViewById(R.id.twRS);
        btnGeriDonBelge = (Button)findViewById(R.id.btnGeriDonBelge);
        btnGeriDonBelge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BelgeCoordActivity.this, CoordActivity.class));
            }
        });
        btnIndir = (Button) findViewById(R.id.btnIndir);
        btnIndir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });
        btnYukle = (Button) findViewById(R.id.btnYukle);
        storageReference1= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploadPDFCoord_");
        btnYukle.setEnabled(false);
        etOgrenciNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });
        //checkButton();
        listele1();
        //editTextPdfName = (EditText) findViewById(R.id.editTextPdfName);
        //editTextPdfName1 = (EditText) findViewById(R.id.editTextPdfName1);
        //btnPDFUpload = (Button) findViewById(R.id.btnPDFUpload);
        //btnGeriDonBelge = (Button) findViewById(R.id.btnGeriDonBelge);
        //btnGeriDonBelge a geri dönme ekle
        /*btnGeriDonBelge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BelgeCoordActivity.this, studentActivity.class));
            }
        });

         */

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploadPDF_");
        listele1();
        //twOgrenciBilgi.setText(text);
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
            String sorgu="update Document set "+radioButton.getText()+"='EVET' WHERE student_id =(SELECT student_id FROM Student WHERE(number ='"+twOgrenciBilgi.getText()+"'))";
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
            String transkript = "SELECT transkript FROM Document WHERE student_id =(SELECT student_id FROM Student WHERE number = '"+twOgrenciBilgi.getText()+"')";
            Statement st1 = con1.createStatement();
            ResultSet rs1 = st1.executeQuery(transkript);
            transkript1 = "";
            recognitionSheet1 = "";
            learningAggrement1 = "";

            if (rs1.next())
            {
                transkript1+=rs1.getString("transkript");
            }
            twTranskript.setText(transkript1);
            String recognitionSheet = "SELECT recognition_sheet FROM Document WHERE student_id =(SELECT student_id FROM Student WHERE number = '"+twOgrenciBilgi.getText()+"')";
            Statement st2 = con1.createStatement();
            ResultSet rs2 = st2.executeQuery(recognitionSheet);
            if (rs2.next())
            {
                recognitionSheet1+=rs2.getString("recognition_sheet");
            }
            twRS.setText(recognitionSheet1);
            String learningAggrement = "SELECT learning_aggrement FROM Document WHERE student_id =(SELECT student_id FROM Student WHERE number = '"+twOgrenciBilgi.getText()+"')";
            Statement st3 = con1.createStatement();
            ResultSet rs3 = st3.executeQuery(learningAggrement);
            if (rs3.next())
            {
                learningAggrement1+=rs3.getString("learning_aggrement");
            }
            twLA.setText(learningAggrement1);
        }
        catch (Exception ex) {
            //
        }
    }
    public void download(){

        storageReference=firebaseStorage.getInstance().getReference();
        ref=storageReference.child("uploadPDF_" + twOgrenciBilgi.getText()+"_"+ radioButton.getText()+".pdf");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                String pdfFile=twOgrenciBilgi.getText()+"_"+ radioButton.getText().toString();
                downloadFiles(BelgeCoordActivity.this,pdfFile,".pdf",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BelgeCoordActivity.this,"Belge Bulunamadı",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void downloadFiles(Context context, String fileName, String fileExtension, String destinationDirectory, String url){

        DownloadManager downloadManager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request);
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
            btnYukle.setEnabled(true);
            etOgrenciNo.setText(data.getDataString()
                    .substring(data.getDataString().lastIndexOf("/") +1));
            btnYukle.setOnClickListener(new View.OnClickListener() {
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

        StorageReference reference= storageReference1.child("uploadPDFCoord" + twOgrenciBilgi.getText()+"_"+ radioButton.getText()+".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask= taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();
                        String pdfFile=twOgrenciBilgi.getText()+"_"+ radioButton.getText().toString();
                        putPDF putPDF = new putPDF(pdfFile, uri.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(putPDF);
                        Toast.makeText(BelgeCoordActivity.this,"Dosya yüklendi",Toast.LENGTH_LONG).show();
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
