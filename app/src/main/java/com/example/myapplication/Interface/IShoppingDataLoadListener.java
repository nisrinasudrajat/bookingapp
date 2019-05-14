package com.example.myapplication.Interface;

import com.example.myapplication.Model.ShoppingItem;

import java.util.List;

public interface IShoppingDataLoadListener {
    void onShoppingDataLoadSucces (List<ShoppingItem> shoppingItemList);
    void onShoppingDataLoadFailed (String message);
}
