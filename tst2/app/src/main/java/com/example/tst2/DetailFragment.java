package com.example.tst2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_detail, container, false);
        return view;
    }

    public void Refresh(Bundle bundle) {
        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(bundle.getString("title"));
        TextView source = (TextView)view.findViewById(R.id.source);
        source.setText("来源：" + bundle.getString("source"));
        TextView time = (TextView)view.findViewById(R.id.time);
        time.setText("时间：" + bundle.getString("time"));
        TextView content = (TextView)view.findViewById(R.id.detail);
        content.setText(bundle.getString("content"));
    }
}
