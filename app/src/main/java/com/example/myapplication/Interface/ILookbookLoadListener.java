package com.example.myapplication.Interface;

import com.example.myapplication.Model.Banner;
import com.example.myapplication.Model.Banner;

import java.util.List;

public interface ILookbookLoadListener {
    void onLookbookLoadSucces(List<Banner> banners);
    void onLookbookLoadFailed(String message);
}
