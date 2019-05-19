package com.example.myapplication.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateUtils;

import com.example.myapplication.Adapter.HomeSliderAdapter;
import com.example.myapplication.Adapter.LookbookAdapter;
import com.example.myapplication.BookingActivity;
import com.example.myapplication.Common.Common;
import com.example.myapplication.Database.CartDatabase;
import com.example.myapplication.Database.DatabaseUtils;
import com.example.myapplication.Interface.IBannerLoadListener;
import com.example.myapplication.Interface.IBookingInfoLoadListener;
import com.example.myapplication.Interface.ICountItemInCartListener;
import com.example.myapplication.Interface.ILookbookLoadListener;
import com.example.myapplication.Interface.IBannerLoadListener;
import com.example.myapplication.Interface.ICountItemInCartListener;
import com.example.myapplication.Interface.ILookbookLoadListener;
import com.example.myapplication.Model.Banner;
import com.example.myapplication.Model.BookingInformation;
import com.example.myapplication.R;
import com.example.myapplication.R;
import com.example.myapplication.Service.PicassoImageLoadingService;
import com.facebook.accountkit.AccountKit;

import com.google.android.gms.common.util.DataUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment implements ILookbookLoadListener, IBannerLoadListener, ICountItemInCartListener,IBookingInfoLoadListener {


    private Unbinder unbinder;
    CartDatabase cartDatabase;

    AlertDialog dialog;

    @BindView(R.id.notification_badge)
    NotificationBadge notificationBadge;

    @BindView(R.id.layout_user_information)
    LinearLayout layout_user_information;
    @BindView(R.id.txt_userName)
    TextView txt_userName;
    @BindView(R.id.txt_identityNumber)
    TextView txt_identityNumber;
    @BindView(R.id.banner_slider)
    Slider banner_slider;
    @BindView(R.id.recycler_look_book)
    RecyclerView recyclerView_look_book;

    @BindView(R.id.card_booking_info)
    CardView card_booking_info;
    @BindView(R.id.txt_field_address)
    TextView txt_field_address;
    @BindView(R.id.txt_field_name)
    TextView txt_field_name;
    @BindView(R.id.txt_field_jenis)
    TextView txt_field_jenis;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_time_remain)
    TextView txt_time_remain;

    @OnClick(R.id.card_view_booking)
    void booking() {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }

    //FireStore
    CollectionReference bannerRef, lookbookRef;

    //Interface
    IBannerLoadListener iBannerLoadListener;
    ILookbookLoadListener iLookbookLoadListener;
    IBookingInfoLoadListener iBookingInfoLoadListener;

    public HomeFragment() {
        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        lookbookRef = FirebaseFirestore.getInstance().collection("Lookbook");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserBooking();
        countCartItem();
    }

    private void loadUserBooking() {
        CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(Common.curretUser.getPhoneNumber())
                .collection("Booking");


        //Get Current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        Timestamp todayTimeStamp = new Timestamp(calendar.getTime());

        //Select booking information from FIrebase with done = false and timestamp greater today
        userBooking
                .whereGreaterThanOrEqualTo("timestamp", todayTimeStamp)
                .whereEqualTo("done", false)
                .limit(1) //Only take one
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                    iBookingInfoLoadListener.onBookingInfoLoadSuccess(bookingInformation);
                                    break; //Exit loop as soon as
                                }
                            }
                        } else
                            iBookingInfoLoadListener.onBookingInfoLoadEmpty();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBookingInfoLoadListener.onBookingInfoLoadFailed(e.getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        cartDatabase = CartDatabase.getInstance(getContext());

        //Init
        Slider.init(new PicassoImageLoadingService());
        iBannerLoadListener = this;
        iLookbookLoadListener = this;
        iBookingInfoLoadListener = this;

        //Cek is Logged? Udah masuk apa belom
        if (AccountKit.getCurrentAccessToken() != null) {
            setUserInformation();
            loadBanner();
            loadLookBook();
            loadUserBooking();
            countCartItem();

        }

        return view;
    }

    private void countCartItem() {
        DatabaseUtils.countItemInCart(cartDatabase, this);
    }


    private void loadLookBook() {
        lookbookRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> lookbooks = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot bannerSnapShot : task.getResult()) {
                                Banner banner = bannerSnapShot.toObject(Banner.class);
                                lookbooks.add(banner);
                            }
                            iLookbookLoadListener.onLookbookLoadSucces(lookbooks);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLookbookLoadListener.onLookbookLoadFailed(e.getMessage());
            }
        });
    }

    private void loadBanner() {
        bannerRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> banners = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot bannerSnapShot : task.getResult()) {
                                Banner banner = bannerSnapShot.toObject(Banner.class);
                                banners.add(banner);
                            }
                            iBannerLoadListener.onBannerLoadSucces(banners);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBannerLoadListener.onBannerLoadFailed(e.getMessage());
            }
        });
    }

    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        txt_userName.setText(Common.curretUser.getName());
        txt_identityNumber.setText(Common.curretUser.getIdentityNumber());
    }

    @Override
    public void onLookbookLoadSucces(List<Banner> banners) {
        recyclerView_look_book.setHasFixedSize(true);
        recyclerView_look_book.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_look_book.setAdapter(new LookbookAdapter(getActivity(), banners));
    }

    @Override
    public void onLookbookLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerLoadSucces(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSliderAdapter(banners));
    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override

    public void onBookingInfoLoadEmpty() {
        card_booking_info.setVisibility(View.GONE);
    }

    @Override
    public void onBookingInfoLoadSuccess(BookingInformation bookingInformation) {
        txt_field_address.setText(bookingInformation.getFieldAddress());
        txt_field_jenis.setText(bookingInformation.getFieldName());
        txt_field_name.setText(Common.field);
        txt_time.setText(bookingInformation.getTime());
        String dateRemain = DateUtils.getRelativeTimeSpanString(
                Long.valueOf(bookingInformation.getTimestamp().toDate().getTime()),
                Calendar.getInstance().getTimeInMillis(), 0).toString();

        txt_time_remain.setText(dateRemain);

        card_booking_info.setVisibility(View.VISIBLE); //Don't forget it
    }

    @Override
    public void onBookingInfoLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartItemCountSuccess(int count) {
        notificationBadge.setText(String.valueOf(count));

    }

}