package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Adapter.MyViewPagerAdapter;
import com.example.myapplication.Common.Common;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    //Event
    @OnClick(R.id.btn_next_step)
    void nextClick() {
        Toast.makeText(this, ""+Common.currentField.getFieldID(), Toast.LENGTH_SHORT).show();
    }
    //Broadcast receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Common.currentField = intent.getParcelableExtra(Common.KEY_FIELD_STORE);
            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        setColorButton();

        //View
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);
                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setColorButton() {
        if(btn_next_step.isEnabled())
        {
            btn_next_step.setBackgroundResource(R.color.colorButton);
        }
        else
        {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }
        if(btn_previous_step.isEnabled())
        {
            btn_previous_step.setBackgroundResource(R.color.colorButton);
        }
        else
        {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Field");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }
}
