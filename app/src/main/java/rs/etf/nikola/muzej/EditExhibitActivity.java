package rs.etf.nikola.muzej;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import rs.etf.nikola.muzej.utility.Museum;
import rs.etf.nikola.muzej.utility.ShowpieceAdapter;

public class EditExhibitActivity extends CreateExhibitActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String exhibitName = getIntent().getExtras().getString("exhibitName");

        exhibit = Museum.getExhibitByName(exhibitName);
        if(exhibit == null)
            finish();

        ShowpieceAdapter adapter = new ShowpieceAdapter(exhibit, this);
        recyclerView.setAdapter(adapter);

        ((EditText) findViewById(R.id.inputNazivIzlozbe)).setText(exhibit.getName());

        Button button = (Button) findViewById(R.id.kreirajIzlozbu);
        button.setText("Izmeni Izložbu");
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Museum.saveToDisk();
                finish();
            }
        });
    }
}
