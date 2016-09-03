package app.example.com.harsh.unifyidtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String x = getIntent().getStringExtra("X");
        String y = getIntent().getStringExtra("X");
        String z = getIntent().getStringExtra("X");
        String avg = getIntent().getStringExtra("AVG");

        TextView X =(TextView) findViewById(R.id.X);
        TextView Y =(TextView) findViewById(R.id.Y);
        TextView Z =(TextView) findViewById(R.id.Z);
        TextView AVG =(TextView) findViewById(R.id.avg);

        X.setText(x);
        Y.setText(y);
        Z.setText(z);
        AVG.setText(avg);

    }

}
