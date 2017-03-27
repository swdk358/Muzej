package rs.etf.nikola.muzej;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import rs.etf.nikola.muzej.utility.Exhibit;
import rs.etf.nikola.muzej.utility.Museum;
import rs.etf.nikola.muzej.utility.MyAdapter;

public class CreateExhibitActivity extends AppCompatActivity {
    Exhibit exhibit = new Exhibit();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exhibit);

        setTitle(R.string.activity_create_exhibit_title);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.showpieceList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        MyAdapter adapter = new MyAdapter<>(exhibit);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.text1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateExhibitActivity.this, CreateShowpieceActivity.class);
                startActivity(intent);
            }
        });

        EditText editText = (EditText) findViewById(R.id.inputNaziv);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                exhibit.setName(v.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                findViewById(R.id.kreirajIzlozbu).setEnabled(true);
                return true;
            }
        });

        findViewById(R.id.kreirajIzlozbu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Museum.instance.add(exhibit);
                finish();
            }
        });
    }
}
