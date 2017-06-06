package domon.cn.coustomerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import domon.cn.coustomerview.View.NumberProgressBar;

public class MainActivity extends AppCompatActivity {
    private NumberProgressBar mNumberProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumberProgressBar = (NumberProgressBar) findViewById(R.id.my_pb);
    }
}
