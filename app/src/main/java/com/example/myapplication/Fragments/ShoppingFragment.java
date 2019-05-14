package com.example.myapplication.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Interface.IShoppingDataLoadListener;
import com.example.myapplication.Model.ShoppingItem;
import com.example.myapplication.R;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingFragment extends Fragment implements IShoppingDataLoadListener {

    IShoppingDataLoadListener iShoppingDataLoadListener;

    CollectionReference shoppingItemRef;

    Unbinder unbinder;

    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    @BindView(R.id.chip_basket)
    Chip chip_basket;
    @OnClick(R.id.chip_basket)
    void basketChipClick(){
        setSelectedChip(chip_basket);
        loadShopingItem(chip_basket.getText().toString());
    }

    private void loadShopingItem(String itemMenu) {
        shoppingItemRef = FirebaseFirestore.getInstance().collection("Shopping")
                .document(itemMenu)
                .collection("Items");
        //get Data
        shoppingItemRef.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iShoppingDataLoadListener.onShoppingDataLoadFailed(e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<ShoppingItem> shoppingItems = new ArrayList<>();
                    for (DocumentSnapshot itemSnapShot:task.getResult())
                    {
                        ShoppingItem shoppingItem = itemSnapShot.toObject(ShoppingItem.class);
                        shoppingItems.add(shoppingItem);
                    }
                    iShoppingDataLoadListener.onShoppingDataLoadSucces(shoppingItems);
                }
            }
        });

    }

    private void setSelectedChip(Chip chip_basket) {
        //Set Color
        for(int i=0;i<chipGroup.getChildCount();i++)
        {
            Chip chipItem = (Chip)chipGroup.getChildAt(i);
            if(chipItem.getId() != chipItem.getId()) // If not selected
            {
                chipItem.setChipBackgroundColorResource(android.R.color.darker_gray);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));
            }
            else //if selected
                chipItem.setChipBackgroundColorResource(android.R.color.holo_orange_dark);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));
        }
    }

    public ShoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_shopping, container, false);

        unbinder = ButterKnife.bind(this,itemView);
        iShoppingDataLoadListener = this;

        return itemView;
    }

    @Override
    public void onShoppingDataLoadSucces(List<ShoppingItem> shoppingItemList) {

    }

    @Override
    public void onShoppingDataLoadFailed(String message) {
        Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
    }
}
