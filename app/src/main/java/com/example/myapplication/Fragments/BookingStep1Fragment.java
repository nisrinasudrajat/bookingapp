package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapter.MyFieldAdaptor;
import com.example.myapplication.Common.SpacesItemDecoration;
import com.example.myapplication.Interface.IAllFieldLoadListener;
import com.example.myapplication.Interface.IBranchLoadListener;
import com.example.myapplication.Model.Field;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllFieldLoadListener, IBranchLoadListener {

    //Variable
    CollectionReference allFieldRef;
    CollectionReference branchRef;

    IAllFieldLoadListener iAllFieldLoadListener;
    IBranchLoadListener iBranchLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_field)
    RecyclerView recycler_field;

    Unbinder unbinder;

    AlertDialog dialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allFieldRef = FirebaseFirestore.getInstance().collection("AllField");
        iAllFieldLoadListener = this;
        iBranchLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_one,container,false);
        unbinder = ButterKnife.bind(this, itemView);

        initView();
        loadAllField();

        return itemView;
    }

    private void initView() {
        recycler_field.setHasFixedSize(true);
        recycler_field.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_field.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllField() {
        allFieldRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Please choose field");
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllFieldLoadListener.onAllFieldLoadSuccess(list);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllFieldLoadListener.onAllFieldLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllFieldLoadSuccess(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position>0)
                {
                    loadBranchOfField(item.toString());
                }
                else
                    recycler_field.setVisibility(View.GONE);
            }
        });
    }

    private void loadBranchOfField(String fieldName) {
        dialog.show();

        branchRef = FirebaseFirestore.getInstance()
                .collection("AllField")
                .document(fieldName)
                .collection("Branch");

        branchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Field> list = new ArrayList<>();
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Field field =  documentSnapshot.toObject(Field.class);
                        field.setFieldID(documentSnapshot.getId());
                        list.add(field);
                    }
                    iBranchLoadListener.onBranchLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllFieldLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBranchLoadSuccess(List<Field> fieldList) {
        MyFieldAdaptor adapter = new MyFieldAdaptor(getActivity(),fieldList);
        recycler_field.setAdapter(adapter);
        recycler_field.setVisibility(View.VISIBLE);


        dialog.dismiss();
    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
