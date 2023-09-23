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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticFragment extends Fragment implements View.OnClickListener {
    Button btnBasari,btnUni,button3;
    TextView txtMesajlar,txtMesajlarN,txtMesajlarK;
    String sonuc,ID,uni,chart,notsonuc,kisisonuc;
    ListView data;
    float[] grade;
    Connection connection,con1;
    AnyChartView anyChartView,anyChartView1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic,container,false);
        txtMesajlar=(TextView)view.findViewById(R.id.txtMesajlar);
        txtMesajlar.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarN=(TextView)view.findViewById(R.id.txtMesajlarN);
        txtMesajlarN.setMovementMethod(new ScrollingMovementMethod());
        txtMesajlarK=(TextView)view.findViewById(R.id.txtMesajlarK);
        txtMesajlarK.setMovementMethod(new ScrollingMovementMethod());
        btnBasari=(Button)view.findViewById(R.id.btnBasari);
        btnUni=(Button)view.findViewById(R.id.btnUni);
        button3=(Button)view.findViewById(R.id.button3);
        btnBasari.setOnClickListener(this);
        btnUni.setOnClickListener(this);
        button3.setOnClickListener(this);
        AnyChartView anyChartView1 = view.findViewById(R.id.any_chart_view);
        //AnyChartView anyChartView1 = view.findViewById(R.id.any_chart_view1);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

       /* Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Lodz", 3.030));
        data.add(new ValueDataEntry("Lomza", 2.798));
        data.add(new ValueDataEntry("Lublin", 3.036));
        data.add(new ValueDataEntry("Münih", 3.055));
        data.add(new ValueDataEntry("Lucian Blaga", 3.310));
        data.add(new ValueDataEntry("Hradec Králové", 2.993));
        data.add(new ValueDataEntry("VGTV", 2.890));
        data.add(new ValueDataEntry("Viyana", 2.755));
        Column column = cartesian.column(data);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title("Başarılara göre Üniversiteler");
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Universiteler");
        cartesian.yAxis(0).title("Basari");
        anyChartView.setChart(cartesian);

        */

         //Yıllara göre Başarı
        Cartesian cartesian1 = AnyChart.column();
        List<DataEntry> data1 = new ArrayList<>();
        data1.add(new ValueDataEntry("2013",2.520));
        data1.add(new ValueDataEntry("2014",3.155));
        data1.add(new ValueDataEntry("2015",2.720));
        data1.add(new ValueDataEntry("2016",3.290));
        data1.add(new ValueDataEntry("2017",2.970));
        data1.add(new ValueDataEntry("2018",3.047));
        data1.add(new ValueDataEntry("2019",2.799));
        data1.add(new ValueDataEntry("2020",2.965));
        Column column1 = cartesian1.column(data1);
        column1.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        cartesian1.animation(true);
        cartesian1.title("Yıllara Göre Başarılar");
        cartesian1.yScale().minimum(0d);
        cartesian1.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
        cartesian1.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian1.interactivity().hoverMode(HoverMode.BY_X);
        cartesian1.xAxis(0).title("Yıllar");
        cartesian1.yAxis(0).title("Basari");
        anyChartView1.setChart(cartesian1);



        return view;
    }
    /*public void setupPieChart(){
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for (int i=0; i<uni;i++){
            dataEntries.add(new ValueDataEntry(uni,grade[i]));
        }
        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }*/
    public void listele()
    {
        txtMesajlar.setText("");
        GirisKontrolS baglan=new GirisKontrolS();
        baglan.execute("listele");
    }
    public void yillistele()
    {
        txtMesajlar.setText("");
        GirisKontrolS baglan=new GirisKontrolS();
        baglan.execute("yilgetir");
    }
    @Override
    public void onClick(View v) {
        if (v.getId()==btnUni.getId())
        {
            GirisKontrolS baglan=new GirisKontrolS();
            baglan.execute("getir");
            listele();
        }
        else if (v.getId()==button3.getId())
        {
            GirisKontrolS baglan=new GirisKontrolS();
            baglan.execute("yilgetir");
            yillistele();
        }
        else if (v.getId()==btnBasari.getId())
        {
            /*try {
                SqlConnection sqlConnection = new SqlConnection();
                con1 = sqlConnection.conclass();
                String name = "Select University.uni_name, ROUND(AVG(Student_grad.grade),3) AS grade From Student_grad inner join Department ON Student_grad.erasmusdepartment_id = Department.department_id inner join Faculty ON " +
                        "Department.faculty_id = Faculty.faculty_id inner join University ON Faculty.university_id = University.university_id GROUP BY uni_name ORDER BY uni_name";
                Statement st1 = con1.createStatement();
                ResultSet rs1 = st1.executeQuery(name);
                chart = "";

                while(rs1.next()){
                    List<DataEntry> data = new ArrayList<>();
                    Map<String,String> data1=new HashMap<String,String>();
                    data1.put("uni_name",rs1.getString("uni_name"));
                    data1.put("grade",rs1.getString("grade"));
                    data.add(data1);
                }
            }
            catch (Exception ex){
            //
        }*/
        }
    }

    private class GirisKontrolS extends AsyncTask<String,String,String> {
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
            txtMesajlarN.setText(notsonuc);
            txtMesajlarK.setText(kisisonuc);
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
                        String sorgu="SELECT University.uni_name,COUNT(Student_grad.number) AS\n" +
                                "kisi, (CASE WHEN AVG(Student_grad.grade)>=3.33 THEN 'AA'\n" +
                                "WHEN AVG(Student_grad.grade)>=3.0 THEN 'BA'\n" +
                                "WHEN AVG(Student_grad.grade)>=2.75 THEN 'BB'\n" +
                                "WHEN AVG(Student_grad.grade)>=2.33 THEN 'BC'\n" +
                                "WHEN AVG(Student_grad.grade)>=2.00 THEN 'CB'\n" +
                                "WHEN AVG(Student_grad.grade)>=1.75 THEN 'CC'\n" +
                                "WHEN AVG(Student_grad.grade)>=1.33 THEN 'CD'\n" +
                                "WHEN AVG(Student_grad.grade)>=1.00 THEN 'DC' ELSE 'FF' END ) AS grade\n" +
                                "FROM Student_grad INNER JOIN Department ON Student_grad.erasmusdepartment_id = Department.department_id INNER JOIN Faculty ON Department.faculty_id = Faculty.faculty_id INNER JOIN University ON Faculty.university_id = University.university_id\n" +
                                "GROUP BY University.uni_name\n";
                        //String sorgu="Select University.uni_name, ROUND(AVG(Student_grad.grade),3) AS grade From Student_grad inner join Department ON Student_grad.erasmusdepartment_id = Department.department_id inner join Faculty ON Department.faculty_id = Faculty.faculty_id inner join University ON Faculty.university_id = University.university_id GROUP BY uni_name ORDER BY uni_name";
                        Statement st=connection.createStatement();
                        ResultSet rs=st.executeQuery(sorgu);
                        sonuc="";
                        notsonuc="";
                        kisisonuc="";
                        if (rs.next())
                        {
                            sonuc+="Universite\n";
                            sonuc+=rs.getString("uni_name")+"\n";
                            notsonuc+="NOT\n";
                            notsonuc+=rs.getString("grade")+"\n";
                            kisisonuc+="Kisi\n";
                            kisisonuc+=rs.getString("kisi")+"\n";
                            basarili=true;
                            while(rs.next())
                            {
                                sonuc+=rs.getString("uni_name")+"\n";
                                notsonuc+=rs.getString("grade")+"\n";
                                kisisonuc+=rs.getString("kisi")+"\n";
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
                    else if (strings[0].equals("yilgetir"))
                    {
                        String sorgu1="SELECT Student_grad.class,COUNT(Student_grad.number) AS\n" +
                                "kisi, (CASE WHEN AVG(Student_grad.grade)>=3.33 THEN 'AA'\n" +
                                "WHEN AVG(Student_grad.grade)>=3.0 THEN 'BA'\n" +
                                "WHEN AVG(Student_grad.grade)>=2.75 THEN 'BB'\n" +
                                "WHEN AVG(Student_grad.grade)>=2.33 THEN 'BC'\n" +
                                "WHEN AVG(Student_grad.grade)>=2.00 THEN 'CB'\n" +
                                "WHEN AVG(Student_grad.grade)>=1.75 THEN 'CC'\n" +
                                "WHEN AVG(Student_grad.grade)>=1.33 THEN 'CD'\n" +
                                "WHEN AVG(Student_grad.grade)>=1.00 THEN 'DC' ELSE 'FF' END ) AS grade\n" +
                                "FROM Student_grad \n" +
                                "GROUP BY Student_grad.class";
                        Statement st1=connection.createStatement();
                        ResultSet rs1=st1.executeQuery(sorgu1);
                        sonuc="";
                        notsonuc="";
                        kisisonuc="";
                        if (rs1.next())
                        {
                            sonuc+=" YIL      NOT     Kisi\n";
                            sonuc+=rs1.getString("class")+"    "+rs1.getString("grade")+"        "+rs1.getString("kisi")
                                    +"\n";

                            basarili=true;
                            while(rs1.next())
                            {
                                sonuc+=rs1.getString("class")+"    "+rs1.getString("grade")+"        "+rs1.getString("kisi")
                                        +"\n";
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
