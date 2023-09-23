package com.example.project;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
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

import com.github.barteksc.pdfviewer.PDFView;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class OgrenciBelgeFragment extends Fragment implements View.OnClickListener  {
    private static final String FILE_NAME = "example.doc";
    TextView txtMesajlar;
    EditText txtIDGetir;
    Button btnGetir,btnLoad;
    String sonuc,name1,country1,lesson1,kbulesson1,notdonusum1,AKTS,AKTSKBU,dersErasmusSayi1;
    Date currentTime;
    Connection connection,con1;
    private File filePath = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ogrenci_belge, container, false);
        txtIDGetir = (EditText) view.findViewById(R.id.txtIDGetir);
        txtMesajlar = (TextView) view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        currentTime = Calendar.getInstance().getTime();
        btnGetir = (Button) view.findViewById(R.id.btnGetir);
        btnGetir.setOnClickListener(this);
        btnLoad = (Button) view.findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        //PDFView pdfView = view.findViewById(R.id.pdfView);
        /*filePath = new File(getContext().getExternalFilesDir(null), "Test.docx");

        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

       /* pdfView.fromAsset("kbuSağlıksigortası.pdf")
                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .pageSnap(false) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(false) // toggle night mode
                .load();
*/
        //listele();
        return view;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == btnGetir.getId()) {
            try {
                SqlConnection sqlConnection = new SqlConnection();
                con1=sqlConnection.conclass();
                String name = "Select Student.name, Student.surname FROM Student WHERE number="+txtIDGetir.getText().toString();
                String country = "Select University.uni_name, University.country From Student INNER JOIN Department ON Student.erasmusdepartment_id = Department.department_id INNER JOIN Faculty ON Department.faculty_id=Faculty.faculty_id INNER JOIN University ON Faculty.university_id=University.university_id WHERE number="+txtIDGetir.getText().toString();
                String lesson = "SELECT Lesson.name,Lesson.code,ects,\n" +
                        "                                CASE WHEN grade >= 90 THEN '10'\n" +
                        "                                WHEN grade >= 86 THEN '9'\n" +
                        "                                WHEN grade >= 81 THEN '8'\n" +
                        "                                WHEN grade >= 70 THEN '7'\n" +
                        "                                WHEN grade >= 60 THEN '6'\n" +
                        "                                WHEN grade >= 50 THEN '5'\n" +
                        "                                WHEN grade >= 40 THEN '4'\n" +
                        "                                WHEN grade >= 30 THEN '3'\n" +
                        "                                WHEN grade >= 20 THEN '2'\n" +
                        "                                WHEN grade >= 10 THEN '1'\n" +
                        "                                WHEN grade >= 0 THEN '0'\n" +
                        "                                ELSE '-'\n" +
                        "                                END AS QuantityText1,\n" +
                        "                                CASE WHEN grade >= 90 THEN 'A'\n" +
                        "                                WHEN grade >= 86 THEN 'B'\n" +
                        "                                WHEN grade >= 81 THEN 'B'\n" +
                        "                                WHEN grade >= 76 THEN 'C'\n" +
                        "                                WHEN grade >= 70 THEN 'C'\n" +
                        "                                WHEN grade >= 65 THEN 'D'\n" +
                        "                                WHEN grade >= 60 THEN 'D'\n" +
                        "                                WHEN grade >= 57 THEN 'D'\n" +
                        "                                WHEN grade >= 54 THEN 'E'\n" +
                        "                                WHEN grade >= 50 THEN 'E'\n" +
                        "                                WHEN grade >= 0 THEN 'F'\n" +
                        "                                ELSE '-'\n" +
                        "                                END AS QuantityText\n" +
                        "                                FROM Semester_Lesson INNER JOIN Lesson ON Semester_Lesson.lesson_id=Lesson.lesson_id Inner JOIN Student ON Semester_Lesson.student_id=Student.student_id WHERE Student.student_id= (SELECT student_id FROM Student WHERE number ="+txtIDGetir.getText().toString()+")";

                String kbulesson= "SELECT semesterlessonkbu_id,code,Lesson.name,Lesson.ects,Semester_Lesson_Kbu.grade,\n" +
                        "CASE WHEN grade >= 90 THEN 'AA'\n" +
                        "WHEN grade >= 86 THEN 'AB'\n" +
                        "WHEN grade >= 81 THEN 'BA'\n" +
                        "WHEN grade >= 76 THEN 'BB'\n" +
                        "WHEN grade >= 70 THEN 'BC'\n" +
                        "WHEN grade >= 65 THEN 'CB'\n" +
                        "WHEN grade >= 60 THEN 'CC'\n" +
                        "WHEN grade >= 57 THEN 'CD'\n" +
                        "WHEN grade >= 54 THEN 'DC'\n" +
                        "WHEN grade >= 50 THEN 'DD'\n" +
                        "ELSE 'FF'\n" +
                        "END AS QuantityText\n" +
                        "FROM Semester_Lesson_Kbu INNER JOIN Lesson ON Semester_Lesson_Kbu.lesson_id = Lesson.lesson_id\n" +
                        "WHERE student_id=(SELECT student_id FROM Student WHERE number="+txtIDGetir.getText().toString()+")";
                String notdonusum = "SELECT letter, equivilant, mindegree, maxdegree, gradewithfour FROM Grade_Transformation WHERE (maxdegree IS NOT NULL)";
                String aktsToplam="SELECT SUM(Lesson.ects) AS ECTS FROM Semester_Lesson INNER JOIN Lesson ON Semester_Lesson.lesson_id = Lesson.lesson_id INNER JOIN Student ON Semester_Lesson.student_id = Student.student_id WHERE (Student.student_id = (SELECT student_id FROM Student WHERE number = "+txtIDGetir.getText().toString()+"))";
                String aktsKBUToplam="SELECT SUM(Lesson.ects) AS ECTS FROM Semester_Lesson_Kbu INNER JOIN Lesson ON Semester_Lesson_Kbu.lesson_id = Lesson.lesson_id INNER JOIN Student ON Semester_Lesson_Kbu.student_id = Student.student_id\n" +
                        "WHERE (Student.student_id = (SELECT student_id FROM Student WHERE number = "+txtIDGetir.getText().toString()+"))";
                String dersErasmusSayi="SELECT COUNT(semesterlesson_id) AS Erasmus FROM Semester_Lesson WHERE student_id=( SELECT student_id FROM Student WHERE number = "+txtIDGetir.getText().toString()+")";

                Statement st1 = con1.createStatement();
                ResultSet rs1 = st1.executeQuery(name);
                AKTS="";
                AKTSKBU="";
                name1 = "";
                country1= "";
                lesson1= "";
                kbulesson1="";
                notdonusum1="";
                dersErasmusSayi1="";
                if (rs1.next())
                {
                    name1+=rs1.getString("name")+" "+rs1.getString("surname")+(char)1;
                }
                Statement st2 = con1.createStatement();
                ResultSet rs2 = st2.executeQuery(country);
                if (rs2.next())
                {
                    country1+=rs2.getString("uni_name")+" "+rs2.getString("country")+(char)1;
                }
                Statement st3 = con1.createStatement();
                ResultSet rs3 = st3.executeQuery(lesson);
                if (rs3.next())
                {
                    lesson1+=rs3.getString("code")+" "+rs3.getString("name")+" "+rs3.getString("ects")+" "+rs3.getString("QuantityText1")+" "+rs3.getString("QuantityText")+"\n"+(char)10;
                    while(rs3.next())
                    {
                        lesson1+=rs3.getString("code")+" "+rs3.getString("name")+" "+rs3.getString("ects")+" "+rs3.getString("QuantityText1")+" "+rs3.getString("QuantityText")+"\n"+(char)10;
                    }
                }
                Statement st4 = con1.createStatement();
                ResultSet rs4 = st4.executeQuery(kbulesson);
                if (rs4.next())
                {
                    kbulesson1+=rs4.getString("code")+" "+rs4.getString("name")+" "+rs4.getString("ects")+" "+rs4.getString("grade")+" "+rs4.getString("QuantityText")+"\n"+(char)10;
                    while(rs4.next())
                    {
                        kbulesson1+=rs4.getString("code")+" "+rs4.getString("name")+" "+rs4.getString("ects")+" "+rs4.getString("grade")+" "+rs4.getString("QuantityText")+"\n"+(char)10;
                    }
                }
                Statement st5 = con1.createStatement();
                ResultSet rs5 = st5.executeQuery(notdonusum);
                if (rs5.next())
                {
                    notdonusum1+=rs5.getString("letter")+"   "+rs5.getString("equivilant")+"  "+rs5.getString("mindegree")+"-"+rs5.getString("maxdegree")+"  "+rs5.getString("gradewithfour")+"\n";
                    while(rs5.next())
                    {
                        notdonusum1+=rs5.getString("letter")+"   "+rs5.getString("equivilant")+"  "+rs5.getString("mindegree")+"-"+rs5.getString("maxdegree")+"  "+rs5.getString("gradewithfour")+"\n";
                    }
                }
                Statement st6=con1.createStatement();
                ResultSet rs6=st6.executeQuery(aktsToplam);

                if (rs6.next())
                {
                    AKTS+=rs6.getString("ECTS");
                    while (rs6.next())
                    {
                        AKTS+=rs6.getString("ECTS");
                    }
                }
                Statement st7=con1.createStatement();
                ResultSet rs7=st7.executeQuery(aktsKBUToplam);
                if (rs7.next())
                {
                    AKTSKBU+=rs7.getString("ECTS");
                    while (rs7.next())
                    {
                        AKTSKBU+=rs7.getString("ECTS");
                    }
                }
                Statement st8=con1.createStatement();
                ResultSet rs8=st8.executeQuery(dersErasmusSayi);
                if (rs8.next())
                {
                    dersErasmusSayi1+=rs8.getString("Erasmus");
                    while (rs8.next())
                    {
                        dersErasmusSayi1+=rs8.getString("Erasmus");
                    }
                }
            }
            catch (Exception ex){
                //
            }
            String text = "                    MÜHENDİSLİK FAKÜLTESİ DEKANLIĞINA \nBölümümüz öğrencilerinden "+txtIDGetir.getText().toString()+" numaralı "+name1+", 2021-2022 Akademik yılı Güz yarıyılını Erasmus programı kapsamında "+country1+"’da tamamlamış olup, program süresince almış olduğu derslerin dönüşümünü yapmak üzere Bölümümüze başvurmuştur." +
                    " "+currentTime+" tarihinde toplanmış olan kurulumuz ilgili öğrencinin durumunu incelemiş ve ders eşleştirme ve sayıştırma işlemini gerçekleştirmiştir.\n\n" +
                    "Yapılan çalışmalara göre: \n" +
                    "1.	Öğrencinin yurt dışında almış olduğu derslerin bir kısmını başarmış olduğuna,\n" +
                    "2.	Alınmış olan toplam "+AKTSKBU+" ECTS kredilik derslerden başarılmış olan derslerin toplam ECTS kredisinin "+AKTSKBU+" olduğuna,\n" +
                    "3.	Dönüştürme işlemi sonucunda, Karabük Üniversitesi, Bilgisayar Mühendisliği Bölümü,  4.Sınıf 2.Öğretim %30 İngilizce programında Güz Yarıyılında alması gereken dersler yerine," +
                    "öğrencinin yurt dışında alıp başarılı olduğu, ekli listede gösterilen "+dersErasmusSayi1+" derse karşı düşen "+dersErasmusSayi1+" dersin ve Bahar Yarıyılında alması gereken dersler yerine, öğrencinin yurt dışında alıp başarılı olduğu, ekli listede gösterilen 1 derse karşı düşen 1" +
                    "dersin başarılı sayılmasının uygunluğuna ve konunun Dekanlık makamına arzına karar verilmiştir.\n" +
                    "Dr. Öğr. Üyesi Yüksel ÇELİK	\tDoç. Dr. İlhami Muharrem ORAK\n" +
                    "Bölüm Başkanı	Bölüm            Erasmus Koordinatörü\n\n\n" +
                    "EKLER: \n"+
                    "EK-1: Öğrencinin ilgili üniversiteden getirdiği ders ve not dökümü\n" +
                    "EK-2: Öğrenci yurt dışına çıkmadan önce hazırlanmış olan ders sayıştırma belgesi\n\n\n\n\n\n\n\n\n\n\n\n\n" +
                    "2021-2022 ACADEMIC YEAR 1.TERM\n" +
                    "ERASMUS OUT-GOING STUDENT GRADE CONVERSION CHART\n" +
                    "____________________________\n" +
                    ""+country1+"\n" +
                    "Code   Course Title    ECTS LocalGrade LetterGrade\n" +
                    ""+lesson1+"\n" +
                    "Equivalent Course(s) in KBU\n" +
                    "Code    Name     ECTS   Local Credit Local Grade\n" +
                    ""+kbulesson1+"\n" +
                    "Student’s:\n"
                    +" Name and Surname         : "+name1+"\n"
                    +" Number	               : "+txtIDGetir.getText().toString()+"\n"
                    +" Faculty/Institute	    : Faculty of Engineering\n"
                    +" Department/Program 	    :  Computer Engineering\n\n" +
                    "Head of Department \t\t\t\tDepartmental Erasmus Coordinator " +
                    "Dr. Öğr. Üyesi Yüksel ÇELİK\t\t\tDoç. Dr. İlhami M. ORAK\n\n\n" +
                    "Faculty/ Institute/ Dean/Director \n" +
                    "Prof. Dr. Refik POLAT (Vekaleten)\n\n\n\n\n"
                    +"We confirm that this proposed grade conversion is approved\n"
                    +"Chart for Grade Conversion\n" +
                    ""+notdonusum1+"\n" +
                    "Failing Grades:\n" +
                    "1) F1: is given to students who do not fulfill the requirements for attendance, and who do not have right to take the final or makeup examination, coefficient is     zero.\n" +
                    "2) F2: is given to students who attend the classes regularly, but do not take the final or makeup examinations, coefficient is zero.\n" +
                    "3) K: is given to students who fail the course. It is for non-credit courses\n" +
                    "4) G: Pass (Out of Grade)\n\n\n\n\n" +
                    "Başarısız notlar\n" +
                    "1) F1 notu: Devamsız, genel ve bütünleme sınavlarına girme hakkı olmayan öğrencilere verilir, katsayısı sıfırdır. Tek başına başarı notu olmamasına rağmen devamsız olan öğrencilere de ara sınav notu olarak F1 notu verilir.\n" +
                    "2) F2 notu: Devam eden, ancak genel veya bütünleme sınavına girmeyen öğrencilere verilir, katsayısı sıfırdır. Tek başına başarı notu olmamasına rağmen ara sınava girmeyen öğrencilere de ara sınav notu olarak F2 notu verilir.\n" +
                    "3) F3 notu: Devam eden, genel ve bütünleme sınavına giren, ancak sınav notu %50’nin veya başarı notu %60’ın altında kalan öğrencilere verilir, katsayısı sıfırdır. Uygulamalarda başarısızlığı nedeniyle genel veya bütünleme sınavına girme hakkı tanınmayan öğrencilere de bu not verilir.\n" +
                    "4) K notu: Kalan öğrenciler için verilir. Kredisiz dersler içindir.\n" +
                    "5) Geçer (Kredisiz dersler için) \n" ;
            FileOutputStream fos = null;

            try {
                fos = getContext().openFileOutput(FILE_NAME, MODE_PRIVATE);
                fos.write(text.getBytes());

                txtIDGetir.getText().clear();
                Toast.makeText(getActivity(), "Saved to " + getContext().getFilesDir() + "/" + FILE_NAME,
                        Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            /*try {
                XWPFDocument xwpfDocument = new XWPFDocument();
                XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
                XWPFRun xwpfRun = xwpfParagraph.createRun();

                xwpfRun.setText(txtIDGetir.getText().toString());
                xwpfRun.setFontSize(24);

                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                xwpfDocument.write(fileOutputStream);

                if (fileOutputStream!=null){
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                xwpfDocument.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }*/
        }
        else if (v.getId()==btnLoad.getId()){
            FileInputStream fis = null;

            try {
                fis = getContext().openFileInput(FILE_NAME);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String text;

                while ((text = br.readLine()) != null) {
                    sb.append(text).append("\n");
                }

                txtMesajlar.setText(sb.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void listele() {
        txtMesajlar.setText("");
        GirisKontrolOBF baglan = new GirisKontrolOBF();
        baglan.execute("listele");
    }
    public class GirisKontrolOBF extends AsyncTask<String,String,String>
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
                        String sorgu="Select Student.name, Student.surname WHERE number="+txtIDGetir.getText().toString();
                        //String sorgu1="Select name From Student WHERE number="+txtIDGetir.getText().toString()+"";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        if (rs.next())
                        {
                            sonuc+=rs.getString("name")+" "+rs.getString("surname")
                                    +"\n"+(char)10+(char)13+(char)10+(char)13;

                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("name")+" "+rs.getString("surname")
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
/*String sorgu= "MÜHENDİSLİK FAKÜLTESİ DEKANLIĞINA /nBölümümüz öğrencilerinden"+getNo+"numaralı "+getName+", 2021-2022 Akademik yılı Güz yarıyılını Erasmus programı kapsamında "+getUni+", "+getCountry+"’da tamamlamış olup, program süresince almış olduğu derslerin dönüşümünü yapmak üzere Bölümümüze başvurmuştur." +
        " 14/03/2022 tarihinde toplanmış olan kurulumuz ilgili öğrencinin durumunu incelemiş ve ders eşleştirme ve sayıştırma işlemini gerçekleştirmiştir." +
        "Yapılan çalışmalara göre: " +
        "1.	Öğrencinin yurt dışında almış olduğu derslerin bir kısmını başarmış olduğuna," +
      "2.	Alınmış olan toplam 35 ECTS kredilik derslerden başarılmış olan derslerin toplam ECTS kredisinin 35 olduğuna," +
      "3.	Dönüştürme işlemi sonucunda, Karabük Üniversitesi, Bilgisayar Mühendisliği Bölümü,  3.Sınıf 1.Öğretim %100 İngilizce programında Güz Yarıyılında alması gereken dersler yerine," +
      "öğrencinin yurt dışında alıp başarılı olduğu, ekli listede gösterilen 5 derse karşı düşen 6 dersin ve Bahar Yarıyılında alması gereken dersler yerine, öğrencinin yurt dışında alıp başarılı olduğu, ekli listede gösterilen 1 derse karşı düşen 1" +
      "dersin başarılı sayılmasının uygunluğuna ve konunun Dekanlık makamına arzına karar verilmiştir." +
      "Dr. Öğr. Üyesi Yüksel ÇELİK	Doç. Dr. İlhami Muharrem ORAK" +
      "Bölüm Başkanı	Bölüm Erasmus Koordinatörü" +
      "EKLER: "+
      "EK-1: Öğrencinin ilgili üniversiteden getirdiği ders ve not dökümü" +
      "EK-2: Öğrenci yurt dışına çıkmadan önce hazırlanmış olan ders sayıştırma belgesi" +
"2021-2022 ACADEMIC YEAR 1.TERM" +
"ERASMUS OUT-GOING STUDENT GRADE CONVERSION CHART" +
""+getStuSeasonDers+""+getStuDers+"Student’s:"
+"Name and Surname : "+getName+""
+"Number	              : "+getNo+""
+"Faculty/Institute	             : Faculty of Engineering"
+"Department/Program	: Computer Engineering"
+"We confirm that this proposed grade conversion is approved"
+"Chart for Grade Conversion"
+getGradeTransformation+"";
*/

/*ders dönüşümü dublicateleri yok sayıyor...
SELECT name,code FROM Lesson WHERE lesson_id IN (SELECT MAX(Lesson_Equ.lesson_id)
FROM Semester_Lesson INNER JOIN
Lesson ON Semester_Lesson.lesson_id = Lesson.lesson_id INNER JOIN
Lesson_Equ ON Lesson.lesson_id = Lesson_Equ.elesson_id INNER JOIN Student ON Semester_Lesson.student_id=Student.student_id
WHERE Student.number=2012010206011
GROUP BY Semester_Lesson.student_id,Lesson.name
HAVING   COUNT(*) = 5 OR COUNT(*) = 1)*/