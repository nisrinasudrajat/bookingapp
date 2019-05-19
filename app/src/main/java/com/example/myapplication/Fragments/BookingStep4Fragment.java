package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Common.Common;
import com.example.myapplication.Model.BookingInformation;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;
    AlertDialog dialog;

    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_field_name)
    TextView txt_field_name;
    @BindView(R.id.txt_booking_address_text)
    TextView txt_booking_address_text;
    @BindView(R.id.txt_field_name2)
    TextView txt_field_name2;

    @OnClick(R.id.btn_confirm)
    void confirmBooking(){
        dialog.show();

        //Process Timestamp
        //We will use TImestamp to filter all booking with date is greater today
        //For only display all future booking
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-"); //Split ex: 9:00 - 10:00
        //Get start time : get 9:00
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); // we get 9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim()); // we got 00

        Calendar bookingDateWithourHouse = Calendar.getInstance();
        bookingDateWithourHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
        bookingDateWithourHouse.set(Calendar.HOUR_OF_DAY,startHourInt);
        bookingDateWithourHouse.set(Calendar.MINUTE,startMinInt);

        //Create timestamp object and apply to BookingInformation
        Timestamp timestamp = new Timestamp(bookingDateWithourHouse.getTime());

        //Create booking information
        final BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setTimestamp(timestamp);
        bookingInformation.setDone(false); //selalu false, karena kita akan menggunakan fild ini untuk filter for display di user
        bookingInformation.setAdminId(Common.currentAdmin.getAdminId());
        bookingInformation.setAdminName(Common.currentAdmin.getName());
        bookingInformation.setCustomerName(Common.curretUser.getName());
        bookingInformation.setCustomerPhone(Common.curretUser.getPhoneNumber());
        bookingInformation.setFieldId(Common.currentField.getFieldID());
        bookingInformation.setFieldAddress(Common.currentField.getAdress());
        bookingInformation.setFieldName(Common.currentField.getName());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(bookingDateWithourHouse.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        //Submit to SlotField document
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllField")
                .document(Common.field)
                .collection("Branch")
                .document(Common.currentField.getFieldID())
                .collection("Admin")
                .document(Common.currentAdmin.getAdminId())
                .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        //Write Datea
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Here we can write an function to check
                        //If already exist an booking, we will prevent new booking

                        addToUserBooking(bookingInformation);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToUserBooking(final BookingInformation bookingInformation) {

        //First, create new Collectiom
        final CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(Common.curretUser.getPhoneNumber())
                .collection("Booking");
        //Check ig exist document in this collection
        userBooking.whereEqualTo("done",false) // If have any document with field done = false
        .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().isEmpty())
                        {
                            //Set data
                            userBooking.document()
                                    .set(bookingInformation)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if(dialog.isShowing())
                                                dialog.dismiss();

                                            addToCalendar(Common.bookingDate,
                                                    Common.convertTimeSlotToString(Common.currentTimeSlot));

                                            resetStaticData();
                                            getActivity().finish();//Close Activity
                                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if(dialog.isShowing())
                                                dialog.dismiss();
                                            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else
                        {
                            if(dialog.isShowing())
                                dialog.dismiss();

                            resetStaticData();
                            getActivity().finish();//Close Activity
                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void addToCalendar(Calendar bookingDate, String startDate) {
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-"); //Split ex: 9:00 - 10:00
        //Get start time : get 9:00
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); // we get 9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim()); // we got 00

        String[] endTimeConvert = convertTime[1].split(":");
        int endHourInt = Integer.parseInt(endTimeConvert[0].trim()); // we get 10
        int endMinInt = Integer.parseInt(endTimeConvert[1].trim()); // we got 00

        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY,startHourInt);//Set event start hour
        startEvent.set(Calendar.MINUTE,startMinInt); //Set event start min

        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY,endHourInt);//Set event end hour
        endEvent.set(Calendar.MINUTE,endMinInt); //Set event end min

        //After we have startEvent and endEvent, convert it to format String
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        String startEventTime = calendarDateFormat.format(startEvent.getTime());
        String endEventTime = calendarDateFormat.format(endEvent.getTime());

        addToDeviceCalendar (startEventTime,endEventTime,"Sports booking",
                new StringBuilder("Sports from ")
        .append(startTime)
        .append(" at ")
        .append(Common.field)
        .append(" - ")
        .append(Common.currentField.getName()).toString(),
                new StringBuilder("Address: ").append(Common.currentField.getAdress()).toString());
    }

    private void addToDeviceCalendar(String startEventTime, String endEventTime, String title , String description, String location) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
            Date start = calendarDateFormat.parse(startEventTime);
            Date end = calendarDateFormat.parse(endEventTime);

            ContentValues event = new ContentValues();

            //Put
            event.put(CalendarContract.Events.CALENDAR_ID, getCalendar(getContext()));
            event.put(CalendarContract.Events.TITLE,title);
            event.put(CalendarContract.Events.DESCRIPTION,description);
            event.put(CalendarContract.Events.EVENT_LOCATION,location);

            //Time
            event.put(CalendarContract.Events.DTSTART,start.getTime());
            event.put(CalendarContract.Events.DTEND,end.getTime());
            event.put(CalendarContract.Events.ALL_DAY,0);
            event.put(CalendarContract.Events.HAS_ALARM,1);

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE,timeZone);

            Uri calendars;
            if(Build.VERSION.SDK_INT >= 8)
                calendars = Uri.parse("content://com.android.calendar/events");
            else calendars = Uri.parse("content://calendar/events");

            getActivity().getContentResolver().insert(calendars,event);


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private String getCalendar(Context context) {
        //Get default calendar ID of Calendar of Gmail
        String gmailIdCalendar = "";
        String projection[]={"_id","calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        //Select all calendar
        Cursor managedCursor = contentResolver.query(calendars,projection,null,null,null);
        if(managedCursor.moveToFirst())
        {
            String calName;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do{
                calName = managedCursor.getString(nameCol);
                if(calName.contains("@gmail.com"))
                {
                    gmailIdCalendar = managedCursor.getString(idCol);
                    break; //exit as soon as have id
                }
            }while (managedCursor.moveToNext());
            managedCursor.close();
        }

        return gmailIdCalendar;
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentAdmin = null;
        Common.currentField = null;
        Common.bookingDate.add(Calendar.DATE,0);
    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append(" at ")
        .append(simpleDateFormat.format(Common.bookingDate.getTime())));

        txt_booking_address_text.setText(Common.currentField.getAdress());
        txt_field_name.setText(Common.field);
        txt_field_name2.setText(Common.currentField.getName());

    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply format for date display on Confirm
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false)
                .build();
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_four,container,false);
        unbinder = ButterKnife.bind(this, itemView);
        return itemView;
    }
}
