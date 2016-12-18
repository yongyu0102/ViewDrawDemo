package com.example.zhangpeng.viewdrawdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zhangpeng.viewdrawdemo.R;
import com.example.zhangpeng.viewdrawdemo.bean.PieData;
import com.example.zhangpeng.viewdrawdemo.view.CheckView;
import com.example.zhangpeng.viewdrawdemo.view.PieView;
import com.example.zhangpeng.viewdrawdemo.view.RemoteControlMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private PieView pieView;
    private Button btnChecked;
    private Button btnUnchecked;
    private CheckView checkView;
    private RemoteControlMenu remoteMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        btnChecked.setOnClickListener(this);
        btnUnchecked.setOnClickListener(this);
        checkView.setOnClickListener(this);
        remoteMenu.setListener(new RemoteControlMenu.MenuListener() {
            @Override
            public void onCenterCliched() {
                Toast.makeText(MainActivity.this,"Center",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpCliched() {
                Toast.makeText(MainActivity.this,"onUpCliched",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCliched() {
                Toast.makeText(MainActivity.this,"onRightCliched",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownCliched() {
                Toast.makeText(MainActivity.this,"onDownCliched",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLeftCliched() {
                Toast.makeText(MainActivity.this,"onLeftCliched",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView(){
        pieView = (PieView) findViewById(R.id.pie_view);
        btnChecked = (Button) findViewById(R.id.btn_checked);
        btnUnchecked = (Button) findViewById(R.id.btn_unchecked);
        checkView = (CheckView) findViewById(R.id.check_view);
        remoteMenu = (RemoteControlMenu) findViewById(R.id.remote_menu);

    }

    private void initData() {
        pieView.setStartAngle(10);
        ArrayList<PieData> pieDataList =new ArrayList<>();
        for(int i=0;i<5;i++){
            PieData pieData=new PieData("饼状图数据"+i,10*i);
            pieDataList.add(pieData);
        }
        pieView.setData(pieDataList);
        checkView.setAnimDuration(1000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checked :
                checkView.check();
                break;
            case R.id.btn_unchecked :
                checkView.unCheck();
                break;
            case R.id.check_view :
                break;
            default:
                break;
        }
    }
}
