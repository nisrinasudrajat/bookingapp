package com.example.myapplication.Interface;

import com.example.myapplication.Model.Banner;
import com.example.myapplication.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSucces(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
