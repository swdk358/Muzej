package rs.etf.nikola.muzej;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import rs.etf.nikola.muzej.utility.ExhibitAdapter;

public class MainActivity extends AppCompatActivity {
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.exhibitList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ExhibitAdapter adapter = new ExhibitAdapter(this);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.dodajIzlozbu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateExhibitActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.exhibitList);

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.exportMuseum:
                exportMuseum();
                return true;
            case R.id.importMuseumDisk:
                importMuseumDisk();
                return true;
            case R.id.importMuseumNet:
                importMuseumNet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void importMuseumNet() {
        ImportNetDialogFragment newFragment = ImportNetDialogFragment.newInstance();
        newFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.exhibitList);

                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(layoutManager);

                ExhibitAdapter adapter = new ExhibitAdapter(MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
        newFragment.show(getFragmentManager(), "ImportMuseumNet");
    }

    private void importMuseumDisk() {
        ImportDiskDialogFragment newFragment = ImportDiskDialogFragment.newInstance();
        newFragment.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.exhibitList);

                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(layoutManager);

                ExhibitAdapter adapter = new ExhibitAdapter(MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
        newFragment.show(getFragmentManager(), "ImportMuseumDisk");
    }

    private void exportMuseum() {
        ExportDialogFragment newFragment = ExportDialogFragment.newInstance();
        newFragment.show(getFragmentManager(), "ExportMuseum");
    }
}
