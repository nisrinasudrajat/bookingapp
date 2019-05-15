package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Adapter.MyViewPagerAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.Common.NonSwipeViewPager;
import com.example.myapplication.Model.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference adminRef;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    //Event
    @OnClick(R.id.btn_previous_step)
    void previousStep() {
        if(Common.step == 3 || Common.step > 0)
        {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
        }
    }

    @OnClick(R.id.btn_next_step)
    void nextClick() {
        if(Common.step < 3 || Common.step == 0)
        {
            Common.step++;
            if(Common.step == 1) //setelah pilih field
            {
                if(Common.currentField != null)
                    loadAdminByField(Common.currentField.getFieldID());
            }
            else if(Common.step == 2) //Pick timeslot
            {
                if(Common.currentAdmin != null)
                    loadTimeSlotOfEachDield(Common.currentAdmin.getAdminId());
            }
            viewPager.setCurrentItem(Common.step);
        }
    }

    private void loadTimeSlotOfEachDield(String adminId) {
        //Send Local Broadcast to Fragment step 3
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadAdminByField(String fieldID) {
        dialog.show();

        //Sekareng, select semua Admin dari Field
        ///AllField/Gelora/Branch/QG7AvbUYyQM1XUHA5I1H/Admin
        if(!TextUtils.isEmpty(Common.field))
        {
            adminRef = FirebaseFirestore.getInstance()
                    .collection("AllField")
                    .document(Common.field)
                    .collection("Branch")
                    .document(fieldID)
                    .collection("Admin");
            adminRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Admin> admins = new ArrayList<>();
                            for(QueryDocumentSnapshot adminSnapshot:task.getResult())
                            {
                                Admin admin = adminSnapshot.toObject(Admin.class);
                                admin.setPassword(""); //hapus password admin karena ini untuk client app
                                admin.setAdminId(adminSnapshot.getId());

                                admins.add(admin);
                            }
                            //Kirim Broadcast untuk BookingStep2Fragment untuk load Recycler
                            Intent intent = new Intent(Common.KEY_ADMIN_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_ADMIN_LOAD_DONE,admins);
                            localBroadcastManager.sendBroadcast(intent);

                            dialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            dialog.dismiss();
                        }
                    });
        }
    }

    //Broadcast receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP,0);
            if(step == 1)
                Common.currentField = intent.getParcelableExtra(Common.KEY_FIELD_STORE);
            else if(step == 2)
                Common.currentAdmin = intent.getParcelableExtra(Common.KEY_ADMIN_SELECTED);

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

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        setColorButton();

        //View
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4); //karena ada 4 fragment
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //show step
                stepView.go(i, true);
                if(i == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                //Set disable button next here
                btn_next_step.setEnabled(false);
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
        stepList.add("Admin");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }
}
