package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapter.MyTimeSlotAdapter;
import com.example.myapplication.Common.Common;
import com.example.myapplication.Common.SpacesItemDecoration;
import com.example.myapplication.Interface.ITimeSlotLoadListener;
import com.example.myapplication.Model.TimeSlot;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import dmax.dialog.SpotsDialog;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {

    //Variable
    DocumentReference slotFieldDoc;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;
    Calendar selected_date;

    @BindView(R.id.recycler_time_slot)
    RecyclerView recycler_time_slot;
    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0); //add current date
            loadAvailableTimeOfEachField(Common.currentAdmin.getAdminId(),
                    simpleDateFormat.format(date.getTime()));
        }
    };

    private void loadAvailableTimeOfEachField(String adminId, final String bookDate) {
        dialog.show();

        slotFieldDoc = FirebaseFirestore.getInstance()
                .collection("AllField")
                .document(Common.field)
                .collection("Branch")
                .document(Common.currentField.getFieldID())
                .collection("Admin")
                .document(Common.currentAdmin.getAdminId());

        //Get information of Slot Field
        slotFieldDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) //if field available
                    {
                        //Get information of booking
                        //if not created, return empty
                        CollectionReference date = FirebaseFirestore.getInstance()
                                .collection("AllField")
                                .document(Common.field)
                                .collection("Branch")
                                .document(Common.currentField.getFieldID())
                                .collection("Admin")
                                .document(Common.currentAdmin.getAdminId())
                                .collection(bookDate);

                        date.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if(querySnapshot.isEmpty())//kalau kosong belom di booking
                                        iTimeSlotLoadListener.onTimeSlotLoadEmpty();
                                    else
                                    {
                                        //jika lapangan sudah di booking
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for(QueryDocumentSnapshot document:task.getResult())
                                            timeSlots.add(document.toObject(TimeSlot.class));
                                        iTimeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iTimeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                            }
                        });
                    }
                }
            }
        });
    }


    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot,new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy" ); //28_03_2019 contoh nya

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();

        selected_date = Calendar.getInstance();
        selected_date.add(Calendar.DATE,0); //Init current date

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_three,container,false);
        unbinder = ButterKnife.bind(this, itemView);

        init(itemView);
        return  itemView;

    }

    private void init(View itemView) {
        recycler_time_slot.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycler_time_slot.setLayoutManager(gridLayoutManager);
        recycler_time_slot.addItemDecoration(new SpacesItemDecoration(8));

        //Calendar
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,2); //2 days left

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(itemView,R.id.calendarView)
                .range(startDate,endDate)
                .datesNumberOnScreen(1)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
             if(selected_date.getTimeInMillis() != date.getTimeInMillis())
             {
                 selected_date = date; //kode ini gak akan load tanggal yang sama dengan tanggal yang dipilih
                 loadAvailableTimeOfEachField(Common.currentAdmin.getAdminId(),
                         simpleDateFormat.format(date.getTime()));
             }
            }
        });
    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(),timeSlotList);
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycler_time_slot.setAdapter(adapter);

        dialog.dismiss();
    }
}





















