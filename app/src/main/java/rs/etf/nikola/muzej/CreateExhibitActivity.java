package rs.etf.nikola.muzej;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import rs.etf.nikola.muzej.utility.Exhibit;
import rs.etf.nikola.muzej.utility.Museum;
import rs.etf.nikola.muzej.utility.MyAdapter;
import rs.etf.nikola.muzej.utility.Showpiece;

public class CreateExhibitActivity extends AppCompatActivity {
    private final Exhibit exhibit = new Exhibit();

    private static final int ACTIVITY_REQUEST_CODE = 1;

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
                startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
            }
        });

        EditText editText = (EditText) findViewById(R.id.inputNazivIzlozbe);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String name = v.getText().toString();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(!name.isEmpty() && !Museum.doesNameExist(name)) {
                    exhibit.setName(name);
                    findViewById(R.id.kreirajIzlozbu).setEnabled(true);
                    return true;
                }
                else {
                    findViewById(R.id.kreirajIzlozbu).setEnabled(false);
                    v.setText(R.string.invalid_name);
                    return false;
                }
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        findViewById(R.id.kreirajIzlozbu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Museum.instance.add(exhibit);
                Museum.saveToDisk();
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Showpiece showpiece = data.getParcelableExtra("showpiece");
                exhibit.add(showpiece);

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.showpieceList);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }
}