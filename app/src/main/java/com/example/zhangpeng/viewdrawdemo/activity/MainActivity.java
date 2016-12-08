package com.example.zhangpeng.viewdrawdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zhangpeng.viewdrawdemo.R;
import com.example.zhangpeng.viewdrawdemo.bean.PieData;
import com.example.zhangpeng.viewdrawdemo.view.PieView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PieView pieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }


    private void initView(){
        pieView = (PieView) findViewById(R.id.pie_view);

    }

    private void initData() {
        pieView.setStartAngle(10);
        ArrayList<PieData> pieDataList =new ArrayList<>();
        for(int i=0;i<5;i++){
            PieData pieData=new PieData("饼状图数据"+i,10*i);
            pieDataList.add(pieData);
        }
        pieView.setData(pieDataList);
    }
}
