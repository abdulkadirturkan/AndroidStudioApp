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
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static com.example.project.currentStudentFragment.STUNO;

public class BelgeCoordFragment extends Fragment {
    Button btnIndir,btnYukle;
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_belge_coord,container,false);
        etOgrenciNo = (EditText) view.findViewById(R.id.etOgrenciNo);
        //etOgrenciExt = (EditText) view.findViewById(R.id.etOgrenciExt);
        twOgrenciBilgi = (TextView) view.findViewById(R.id.twOgrenciBilgi);
        SharedPreferences shp = getContext().getSharedPreferences("sharedPrefsStu", MODE_PRIVATE);
        String stu = shp.getString(STUNO,"No name defined");
        twOgrenciBilgi.setText(stu);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.radio_one:

                        break;
                    case R.id.radio_two:
                        // Fragment 2
                        break;
                    case R.id.radio_three:
                        // Fragment 2
                        break;
                }
            }
        });
        //radioButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
        //String belgeCesidi = radioButton.getText().toString();
        //Toast.makeText(getActivity(),"Test : "+belgeCesidi,Toast.LENGTH_SHORT).show();
        //radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        radio_one = view.findViewById(R.id.radio_one);
        radio_two = view.findViewById(R.id.radio_two);
        radio_three = view.findViewById(R.id.radio_three);
        //int radioId=radioGroup.getCheckedRadioButtonId();
        //radioButton=  view.findViewById(radioId);
        //Toast.makeText(getActivity(),"Selected Radio Button: "+radioButton.getText(),Toast.LENGTH_SHORT).show();
//RadioButton ayarlanacak
        twTranskript = (TextView) view.findViewById(R.id.twTranskript);
        twLA = (TextView) view.findViewById(R.id.twLA);
        twRS = (TextView) view.findViewById(R.id.twRS);
        btnIndir = (Button) view.findViewById(R.id.btnIndir);
        btnIndir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });
        btnYukle = (Button) view.findViewById(R.id.btnYukle);
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
        return view;
    }
    public void checkButton(){
        int radioId=radioGroup.getCheckedRadioButtonId();
        radioButton= getView().findViewById(radioId);
        Toast.makeText(getActivity(),"Selected Radio Button: "+radioButton.getText(),Toast.LENGTH_SHORT).show();
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
                downloadFiles(getActivity(),pdfFile,".pdf",DIRECTORY_DOWNLOADS,url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Belge Bulunamadı",Toast.LENGTH_LONG).show();
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

                    uploadPDFFileFirebase(data.getData());
                }
            });
        }
    }
    private void uploadPDFFileFirebase(Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
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
                        Toast.makeText(getActivity(),"Dosya yüklendi",Toast.LENGTH_LONG).show();
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
