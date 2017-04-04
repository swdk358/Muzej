package rs.etf.nikola.muzej;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rs.etf.nikola.muzej.utility.Exhibit;
import rs.etf.nikola.muzej.utility.Museum;

public class DemoExhibitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_exhibit);

        setTitle(R.string.activity_demo_exhibit_title);

        String exhibitName = getIntent().getParcelableExtra("exhibitName");

        Exhibit exhibit = Museum.instance.getExhibitByName(exhibitName);
        if(exhibit == null)
            finish();


    }
}
