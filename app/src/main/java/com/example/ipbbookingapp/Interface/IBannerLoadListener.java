package com.example.ipbbookingapp.Interface;

import com.example.ipbbookingapp.Models.Banner;
import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSucces(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
