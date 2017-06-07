package domon.cn.coustomerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import domon.cn.coustomerview.View.NumberProgressBar;
import domon.cn.coustomerview.View.OnProgressBarListener;
import domon.cn.coustomerview.View.RectView;

public class MainActivity extends AppCompatActivity implements OnProgressBarListener {
    private NumberProgressBar mNumberProgressBar;
    private RectView mRectView;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumberProgressBar = (NumberProgressBar) findViewById(R.id.my_pb);
        mRectView = (RectView) findViewById(R.id.my_rv);

        mNumberProgressBar.setOnProgressBarListener(this);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNumberProgressBar.incrementProgressBy(10);
                    }
                });
            }
        }, 1000, 1000);
    }

    @Override
    public void onProgressChange(int current, int max) {
        if (current == max) {
            mNumberProgressBar.setCurrentProgress(0);
        }
    }
}
