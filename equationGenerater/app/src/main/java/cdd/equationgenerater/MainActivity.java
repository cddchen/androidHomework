package cdd.equationgenerater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calculate.random.setSeed(new Date().getTime());
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ArrayList<String> init = new ArrayList<>();
        init.add("该难度下<font color=\"red\">5</font>道只包含<font color=\"red\">一</font>个加或减运算符的题目");
        init.add("该难度下<font color=\"red\">10</font>道只包含<font color=\"red\">一</font>个加或减运算符的题目");
        init.add("该难度下<font color=\"red\">5</font>道只包含<font color=\"red\">二</font>个加或减运算符的题目");
        init.add("该难度下<font color=\"red\">10</font>道只包含<font color=\"red\">二</font>个加或减运算符的题目");
        init.add("该难度下<font color=\"red\">5</font>道只包含<font color=\"red\">四</font>个加或减运算符的题目");
        init.add("该难度下<font color=\"red\">10</font>道只包含<font color=\"red\">四</font>个加或减运算符的题目");
        final TextView txt_ofDiffculty = (TextView)findViewById(R.id.txt_statusOfDiffculty);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar_diffculty);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_ofDiffculty.setText(Html.fromHtml(init.get(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(5);
        txt_ofDiffculty.setText(Html.fromHtml(init.get(5)));

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "生成成功！", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("op_min", 1);
                bundle.putInt("op_max", 4);
                bundle.putInt("num_max", 20);
                bundle.putInt("cnt", 20);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
