package com.example.myapplication;

import android.arch.persistence.room.Database;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Adapter.MyCartAdapter;
import com.example.myapplication.Database.CartDatabase;
import com.example.myapplication.Database.CartItem;
import com.example.myapplication.Database.DatabaseUtils;
import com.example.myapplication.Interface.ICartItemLoadListener;
import com.example.myapplication.Interface.ICartItemUpdateListener;
import com.example.myapplication.Interface.ISumCartListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity implements ICartItemLoadListener, ICartItemUpdateListener, ISumCartListener {

    @BindView(R.id.recycler_cart)
    RecyclerView recycler_cart;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.btn_submit_cart)
    Button btn_submit_cart;

    CartDatabase cartDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ButterKnife.bind(CartActivity.this);
        cartDatabase = CartDatabase.getInstance(this);
        DatabaseUtils.getAllCart(cartDatabase, this);

        //View
        recycler_cart.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(linearLayoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
    }

    @Override
    public void onGetAllItemFromCartSuccess(List<CartItem> cartItemList) {
        //Setelah get cart dari DB, tampilkan recycler view
        MyCartAdapter adapter = new MyCartAdapter(this, cartItemList, this);
        recycler_cart.setAdapter(adapter);
    }

    @Override
    public void onCartItemUpdateSuccess() {
        DatabaseUtils.sumCart(cartDatabase, this);
    }

    @Override
    public void onSumCartSuccess(Long value) {
        txt_total_price.setText(new StringBuilder("Rp ").append(value));

    }
}
