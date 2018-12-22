package com.example.dmytro.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import history_tab_classes.ArchiveObject;
import history_tab_classes.HistoryObject;

public class StatisticActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    Spinner chartChooseSpinner;
    int radioButtonIndex=0;

    MenuItem statisticCartSaveAsImage;

    BarChart barChart ;
    LineChart lineChart;
    ArrayList<HistoryObject> listHistory;
    ArrayList<ArchiveObject> listArchive;
    Controller controller=new Controller();
    ArrayList<BarEntry> entries = new ArrayList<>();//chart entries
    ArrayList<String> labels = new ArrayList<>();//chart labels

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setTitle(R.string.StatActivName);
        statisticCartSaveAsImage = (MenuItem) findViewById(R.id.saveimage_cart);

        barChart = (BarChart) findViewById(R.id.chart);
        lineChart = (LineChart) findViewById(R.id.linechart);

        listHistory =controller.loadHistory(getApplicationContext());
        listArchive =controller.loadArchive(getApplicationContext());



        radioGroup= (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // find which radio button is selected
                if(checkedId == R.id.radioButtonArchive) {
                    radioButtonIndex=0;

                } else if(checkedId == R.id.radioButtonHistory) {
                    radioButtonIndex=1;


                } else {

                }
            }
        });

        chartChooseSpinner = (Spinner) findViewById(R.id.spinnerChart);
        chartChooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                switch (position) {
                    case 0:
                        if(radioButtonIndex==0) ArchiveCharts(0);  else HistoryCharts(0);
                        break;
                    case 1:
                        if(radioButtonIndex==0) ArchiveCharts(1); else HistoryCharts(1);
                        break;
                    case 2:
                       if(radioButtonIndex==0) ArchiveCharts(2); else HistoryCharts(2);
                        break;
                    case 3:
                        if(radioButtonIndex==0) ArchiveCharts(3); else HistoryCharts(3);
                        break;
                    case 4:
                        if(radioButtonIndex==0) ArchiveCharts(4); else HistoryCharts(4);
                        break;
                    case 5:
                        if(radioButtonIndex==0) ArchiveCharts(5); else HistoryCharts(5);
                        break;
                    case 6:
                        if(radioButtonIndex==0) BuildAllArchivedChart(); else BuildAllHistoryChart();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        ArrayAdapter<?> cartChooseList= ArrayAdapter.createFromResource(this, R.array.statisticList, android.R.layout.simple_spinner_item);
        cartChooseList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        chartChooseSpinner.setAdapter(cartChooseList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.statistic_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveimage_cart:
                saveImageDialog();
                break;
            }
        return super.onOptionsItemSelected(item);
    }


    void saveImageDialog()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alert.setTitle(R.string.StatSaveChartTitle); //Set Alert dialog title here
        alert.setMessage(R.string.StatSaveChartMsg); //Message here
        alert.setView(input);
        alert.setPositiveButton(R.string.DialogOkBtn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                if(barChart.getVisibility()==View.VISIBLE)barChart.saveToGallery(value+".jpg", 85);
                else lineChart.saveToGallery(value+".jpg",85);
            }
        });

        alert.setNegativeButton(R.string.DialogCancelBtn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }

void HistoryCharts(int value ){
    barChart.setVisibility(View.VISIBLE);
    lineChart.setVisibility(View.INVISIBLE);
    int index=0;
    float chartValue=0;
    String name="";
    boolean histLineFlag=false;
    entries.clear();
    labels.clear();
    for(HistoryObject model : listHistory) {
        switch (value){
            case 0: chartValue=(float)model.getTemperature1(); name=getApplicationContext().getString(R.string.StatTemp1Name); histLineFlag=true; break;
            case 1: chartValue=(float)model.getTemperature2(); name=getApplicationContext().getString(R.string.StatTemp2Name); histLineFlag=true; break;
            case 2: chartValue=(float)model.getHumidity(); name=getApplicationContext().getString(R.string.StatHumidityName); histLineFlag=false; break;
            case 3: chartValue=(float)model.getLuminosity(); name=getApplicationContext().getString(R.string.StatLuminosityName); histLineFlag=false; break;
            case 4: chartValue=(float)model.getPressure(); name=getApplicationContext().getString(R.string.StatPressureName); histLineFlag=false; break;
            case 5: chartValue=(float)model.getAltitude(); name=getApplicationContext().getString(R.string.StatAltitudeName); histLineFlag=false; break;
        }
        entries.add(new BarEntry(chartValue,index));
        labels.add( model.getDatetime());
        index++;
    }
    buildChart(name,histLineFlag);
}

    void ArchiveCharts(int value){
        barChart.setVisibility(View.VISIBLE);
        lineChart.setVisibility(View.INVISIBLE);
        int index=0;
        float chartValue=0;
        String name="";
        boolean archLineFlag=false;
        entries.clear();
        labels.clear();
        for(ArchiveObject model : listArchive) {
            switch (value){
                case 0: chartValue=(float)model.getTemp1A(); name=getApplicationContext().getString(R.string.StatTemp1Name); archLineFlag=true;  break;
                case 1: chartValue=(float)model.getTemp2A(); name=getApplicationContext().getString(R.string.StatTemp2Name); archLineFlag=true; break;
                case 2: chartValue=(float)model.getHumiditiA(); name=getApplicationContext().getString(R.string.StatHumidityName); archLineFlag=false; break;
                case 3: chartValue=(float)model.getLuminosityA(); name=getApplicationContext().getString(R.string.StatLuminosityName); archLineFlag=false; break;
                case 4: chartValue=(float)model.getPressureA(); name=getApplicationContext().getString(R.string.StatPressureName); archLineFlag=false; break;
                case 5: chartValue=(float)model.getAltitudeA(); name=getApplicationContext().getString(R.string.StatAltitudeName);archLineFlag=false;  break;
            }
            entries.add(new BarEntry(chartValue,index));
            labels.add( model.getTimeperiodA());
            index++;
        }

       buildChart(name,archLineFlag);
    }


    void BuildAllHistoryChart(){
        barChart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.VISIBLE);
        int index=0;
        ArrayList<Entry> temp1List= new ArrayList<>();
        ArrayList<Entry> temp2List= new ArrayList<>();
        ArrayList<Entry> humidList= new ArrayList<>();
        ArrayList<Entry> luminList= new ArrayList<>();
        ArrayList<Entry> presList= new ArrayList<>();
        ArrayList<Entry> alttList= new ArrayList<>();
        ArrayList<String> values=new ArrayList<>();


        for(HistoryObject model : listHistory) {
            temp1List.add(new Entry((float) model.getTemperature1(), index));
            temp2List.add(new Entry((float) model.getTemperature2(), index));
            humidList.add(new Entry((float) model.getHumidity(), index));
            luminList.add(new Entry((float) model.getLuminosity(), index));
            presList.add(new Entry((float) model.getPressure(), index));
            alttList.add(new Entry((float) model.getAltitude(), index));
            values.add(model.getDatetime());
            index++;
        }
        buildAllCharts(temp1List,temp2List,humidList,luminList,presList,alttList,values);
    }

    void BuildAllArchivedChart(){
        barChart.setVisibility(View.INVISIBLE);
        lineChart.setVisibility(View.VISIBLE);
        int index=0;
        ArrayList<Entry> temp1List= new ArrayList<>();
        ArrayList<Entry> temp2List= new ArrayList<>();
        ArrayList<Entry> humidList= new ArrayList<>();
        ArrayList<Entry> luminList= new ArrayList<>();
        ArrayList<Entry> presList= new ArrayList<>();
        ArrayList<Entry> alttList= new ArrayList<>();
        ArrayList<String> values=new ArrayList<>();


        for(ArchiveObject model : listArchive) {
            temp1List.add(new Entry((float) model.getTemp1A(), index));
            temp2List.add(new Entry((float) model.getTemp2A(), index));
            humidList.add(new Entry((float) model.getHumiditiA(), index));
            luminList.add(new Entry((float) model.getLuminosityA(), index));
            presList.add(new Entry((float) model.getPressureA(), index));
            alttList.add(new Entry((float) model.getAltitudeA(), index));
            values.add(model.getTimeperiodA());
            index++;
        }
        buildAllCharts(temp1List,temp2List,humidList,luminList,presList,alttList,values);
    }



    void buildChart(String name,boolean LineFlag){

        Controller controller =new Controller();
       UserSetting settings=controller.loadSettings(getApplicationContext());
        barChart.clear();
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.animateXY(3000, 3000);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setDoubleTapToZoomEnabled(true);
        barChart.setDescription("HomeInformer");
        barChart.setDescriptionTextSize(12f);

        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);

        BarDataSet dataset= new BarDataSet(entries, name);
        dataset.setColor(Color.parseColor("#A870DF"));
        BarData Data = new BarData(labels,dataset);
        if(LineFlag) {
            LimitLine line = new LimitLine(settings.getCriticalTemp(),getApplicationContext().getString(R.string.StatCriticalTemp));
            line.setLineWidth(4f);
            line.setTextSize(12f);
            line.setLineColor(Color.parseColor("#EEBF70"));
            line.setTextColor(Color.parseColor("#5D1694"));
            barChart.getAxisLeft().addLimitLine(line);
        }
        barChart.setData(Data);
    }

    void buildAllCharts(ArrayList<Entry> temp1List,ArrayList<Entry> temp2List,ArrayList<Entry> humidList,//get all list fro building charts
                        ArrayList<Entry> luminList, ArrayList<Entry> presList,ArrayList<Entry> alttList,
                        ArrayList<String> values){ //get value headers for charts
        ArrayList<LineDataSet> lines = new ArrayList<LineDataSet> ();
        LineDataSet lDataSet1 = new LineDataSet(temp1List, getApplicationContext().getString(R.string.StatTemp1FullChart));
        lDataSet1.setFillAlpha(65);
        lDataSet1.setColor(Color.parseColor("#ffff00"));
        lDataSet1.setCircleColor(Color.parseColor("#21319c"));//Primary dark
        lDataSet1.setCircleColorHole(Color.parseColor("#FF4081"));//accent
        lDataSet1.setLineWidth(2f);
        lDataSet1.setCircleSize(5f);
        lines.add(lDataSet1);

        LineDataSet lDataSet2 = new LineDataSet(temp2List, getApplicationContext().getString(R.string.StatTemp2FullChart));
        lDataSet2.setColor(Color.parseColor("#aa00ff"));
        lDataSet2.setCircleColor(Color.parseColor("#21319c"));
        lDataSet2.setCircleColorHole(Color.parseColor("#FF4081"));
        lDataSet2.setLineWidth(2f);
        lDataSet2.setCircleSize(5f);
        lines.add(lDataSet2);

        LineDataSet lDataSet3 = new LineDataSet(humidList, getApplicationContext().getString(R.string.StatHumidityFullChart));
        lDataSet3.setColor(Color.parseColor("#1976d2"));
        lDataSet3.setCircleColor(Color.parseColor("#21319c"));
        lDataSet3.setCircleColorHole(Color.parseColor("#FF4081"));
        lDataSet3.setLineWidth(2f);
        lDataSet3.setCircleSize(5f);
        lines.add(lDataSet3);

        LineDataSet lDataSet4 = new LineDataSet(luminList, getApplicationContext().getString(R.string.StatLuminosityFullChart));
        lDataSet4.setColor(Color.parseColor("#00c853"));
        lDataSet4.setCircleColor(Color.parseColor("#21319c"));
        lDataSet4.setCircleColorHole(Color.parseColor("#FF4081"));
        lDataSet4.setLineWidth(2f);
        lDataSet4.setCircleSize(5f);
        lines.add(lDataSet4);

        LineDataSet lDataSet5 = new LineDataSet(presList, getApplicationContext().getString(R.string.StatPressureFullChart));
        lDataSet5.setColor(Color.parseColor("#f44336"));
        lDataSet5.setCircleColor(Color.parseColor("#21319c"));
        lDataSet5.setCircleColorHole(Color.parseColor("#FF4081"));
        lDataSet5.setLineWidth(2f);
        lDataSet5.setCircleSize(5f);
        lines.add(lDataSet5);

        LineDataSet lDataSet6 = new LineDataSet(alttList, getApplicationContext().getString(R.string.StatAltitudeFullChart));
        lDataSet6.setColor(Color.parseColor("#5d4037"));
        lDataSet6.setCircleColor(Color.parseColor("#21319c"));
        lDataSet6.setCircleColorHole(Color.parseColor("#FF4081"));
        lDataSet6.setLineWidth(2f);
        lDataSet6.setCircleSize(5f);
        lines.add(lDataSet6);

        lineChart.setData(new LineData(values, lines));
        lineChart.setDescription("HomeInformer");
        lineChart.setDrawGridBackground(false);

        lineChart.animateY(5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }
}
