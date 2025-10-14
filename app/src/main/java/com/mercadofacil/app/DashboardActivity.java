package com.mercadofacil.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    LineChart lineChart;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_dashboard);

        lineChart = findViewById(R.id.lineChart);
        bottomNav = findViewById(R.id.bottomNav);

        setupChart();
        setupBottomNav();
    }

    private void setupChart() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1, 18));
        entries.add(new Entry(2, 12));
        entries.add(new Entry(3, 25));
        entries.add(new Entry(4, 20));

        LineDataSet set = new LineDataSet(entries, "Vendas (ult. dias)");
        set.setLineWidth(2f);
        set.setCircleRadius(4f);

        LineData data = new LineData(set);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_dashboard: return true;
                case R.id.nav_lista:
                    startActivity(new Intent(this, ListaActivity.class));
                    return true;
                case R.id.nav_perfil:
                    startActivity(new Intent(this, PerfilActivity.class));
                    return true;
                case R.id.nav_analytics:
                    startActivity(new Intent(this, AnalyticsActivity.class));
                    return true;
            }
            return false;
        });
    }
}
