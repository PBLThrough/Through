package jm.through.form;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import jm.through.R;
import jm.through.send.SendActivity;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new FormAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(FormActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), SendActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
