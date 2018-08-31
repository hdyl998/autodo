package com.example.test1.fragmentadd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.test1.fragmentadd.adapter.BaseViewHolder;
import com.example.test1.fragmentadd.adapter.SuperAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatActivity mContext;


    ListView listView;
    SuperAdapter adapter;

    public final static List<String> listStrings = new ArrayList<>();

    static {
        listStrings.add("test");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        listView = findViewById(R.id.listView);


        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccessibility();
            }
        });


        listView.setAdapter(adapter = new SuperAdapter<String>(mContext, listStrings, R.layout.fragment1) {
            @Override
            protected void onBind(BaseViewHolder holder, String item, int position) {
                holder.setText(R.id.textView, item);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
    }


    private static final String TAG = "MainActivity";


    private void openAccessibility() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
    }
}
