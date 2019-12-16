package com.example.tst2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private TextView title1, source1, time1, title2, source2, time2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final News[] news = new News[2];
        news[0] = new News();
        news[0].Title = "习近平全面依法治国新理念新思想新战略十论";
        news[0].Content = "党的十八大以来，以习近平同志为核心的党中央提出一系列全面依法治国新理念新思想新战略，概括起来有十个方面的重要内容。习近平指出，这些新理念新思想新战略，是马克思主义法治思想中国化的最新成果，是全面依法治国的根本遵循，必须长期坚持、不断丰富发展。在第六个国家宪法日之际，新华社《学习进行时》为您梳理。";
        news[0].Source = "新华网";
        news[0].Time = "2019-12-04";

        news[1] = new News();
        news[1].Title = "医疗扶贫，照亮贫困家庭的明天";
        news[1].Content = "国家卫生健康委员会相关负责人介绍，未来将深入推进县医院能力建设、“县乡一体、乡村一体”机制建设和乡村医疗卫生机构标准化建设，到2019年底基本消除乡村医疗卫生机构和人员“空白点”，到2020年全面完成解决基本医疗有保障存在的突出问题，不断提高贫困群众的健康获得感和满意度。";
        news[1].Source = "新华网";
        news[1].Time = "2019-12-02";

        title1 = (TextView)findViewById(R.id.textView);
        source1 = (TextView)findViewById(R.id.textView2);
        time1 = (TextView)findViewById(R.id.textView3);
        title1.setText(news[0].Title);
        source1.setText("来源：" + news[0].Source);
        time1.setText("时间：" + news[0].Time);

        title2 = (TextView)findViewById(R.id._textView);
        source2 = (TextView)findViewById(R.id._textView2);
        time2 = (TextView)findViewById(R.id._textView3);
        title2.setText(news[1].Title);
        source2.setText("来源：" + news[1].Source);
        time2.setText("时间：" + news[1].Time);

        RelativeLayout lyout1 = (RelativeLayout)findViewById(R.id.layout1);
        lyout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration mConfiguration = MainActivity.this.getResources().getConfiguration();
                if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {
                    DetailFragment fragment = (DetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", news[0].Title);
                    bundle.putString("source", news[0].Source);
                    bundle.putString("content", news[0].Content);
                    bundle.putString("time", news[0].Time);
                    fragment.Refresh(bundle);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", news[0].Title);
                    bundle.putString("source", news[0].Source);
                    bundle.putString("content", news[0].Content);
                    bundle.putString("time", news[0].Time);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                }
            }
        });
        RelativeLayout lyout2 = (RelativeLayout)findViewById(R.id.layout2);
        lyout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration mConfiguration = MainActivity.this.getResources().getConfiguration();
                if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {
                    DetailFragment fragment = (DetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", news[1].Title);
                    bundle.putString("source", news[1].Source);
                    bundle.putString("content", news[1].Content);
                    bundle.putString("time", news[1].Time);
                    fragment.Refresh(bundle);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", news[1].Title);
                    bundle.putString("source", news[1].Source);
                    bundle.putString("content", news[1].Content);
                    bundle.putString("time", news[1].Time);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    title1.setTextColor(Color.parseColor("#0000FF"));
                    source1.setTextColor(Color.parseColor("#0000FF"));
                    time1.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    title2.setTextColor(Color.parseColor("#0000FF"));
                    source2.setTextColor(Color.parseColor("#0000FF"));
                    time2.setTextColor(Color.parseColor("#0000FF"));
                }
                break;
        }
    }
}
