package cdd.equationgenerater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int op_min = bundle.getInt("op_min"), op_max = bundle.getInt("op_max"),
                num_max = bundle.getInt("num_max"), cnt = bundle.getInt("cnt");

        Log.d("Intent", op_min + ", " + op_max + ", " + num_max + ", " + cnt);
        List<elem> elems = new ArrayList<>();
        for (int i = 1; i <= cnt; ++i) {
            elems.add(calculate.equation(op_min, op_max, num_max));
        }
        Log.d("elem.size", Integer.toString(elems.size()));
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        elemAdapter adapter = new elemAdapter(elems);
        recyclerView.setAdapter(adapter);
    }
}
