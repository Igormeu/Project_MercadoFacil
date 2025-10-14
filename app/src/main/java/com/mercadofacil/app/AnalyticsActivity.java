package com.mercadofacil.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsActivity extends AppCompatActivity {
    BarChart barChart;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_analytics);

        barChart = findViewById(R.id.barChart);
        pieChart = findViewById(R.id.pieChart);

        setupBar();
        setupPie();
    }

    private void setupBar() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 50));
        entries.add(new BarEntry(1, 120));
        entries.add(new BarEntry(2, 80));
        BarDataSet set = new BarDataSet(entries, "Vendas por semana");
        BarData data = new BarData(set);
        barChart.setData(data);
        barChart.invalidate();
    }

    private void setupPie() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "A"));
        entries.add(new PieEntry(30f, "B"));
        entries.add(new PieEntry(30f, "C"));
        PieDataSet set = new PieDataSet(entries, "Categorias");
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.invalidate();
    }
}
