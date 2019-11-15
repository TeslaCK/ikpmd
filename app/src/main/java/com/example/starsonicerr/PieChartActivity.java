package com.example.starsonicerr;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;


public class PieChartActivity extends Activity {

    private PieChart mChart;
    public static final int MAX_present = 24;

    public static int currentpresent = 0;

    protected void setupPieChart(){

        mChart = (PieChart) findViewById(R.id.chart);
        Description d = new Description();
        d.setText("Aanwezige deze week");
        d.setTextSize(20f);
        mChart.setDescription(d);
        mChart.setTouchEnabled(true);
        mChart.setDrawSliceText(true);
        mChart.getLegend().setEnabled(true);
        mChart.setTransparentCircleColor(Color.rgb(130, 130, 130));
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }

    private void setData(int aantal) {
        currentpresent = aantal;
        List<PieEntry> yValues = new ArrayList<>();
        List<PieEntry> xValues = new ArrayList<>();

        yValues.add(new PieEntry(aantal, 0));
        xValues.add(new PieEntry(aantal, "Behaalde present"));

        yValues.add(new PieEntry(24 - currentpresent, 1));
        xValues.add(new PieEntry(24 - aantal, "Resterende present"));

        //  http://www.materialui.co/colors
        ArrayList<Integer> colors = new ArrayList<>();
        if (currentpresent <10) {
            colors.add(Color.rgb(244,81,30));
        } else if (currentpresent < 16){
            colors.add(Color.rgb(235,0,0));
        } else if  (currentpresent < 20) {
            colors.add(Color.rgb(253,216,53));
        } else {
            colors.add(Color.rgb(67,160,71));
        }
        colors.add(Color.rgb(255,0,0));

        PieDataSet dataSet = new PieDataSet(yValues, "present");
        dataSet.setColors(colors);//colors);


        // PieDataSet set = new PieDataSet(xValues, "Election Results");

        PieData data = new PieData(dataSet);
        mChart.setData(data); // bind dataset aan chart.
        mChart.invalidate();  // Aanroepen van een redraw
        //Log.d("aantal =", ""+currentpresent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual);

        setupPieChart();
        setData(0);
        setData(currentpresent += MainActivity.countusers);

        // OTHER STUFF !

    }
}
