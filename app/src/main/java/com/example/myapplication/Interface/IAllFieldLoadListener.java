package com.example.myapplication.Interface;

import java.util.List;

public interface IAllFieldLoadListener {
    void onAllFieldLoadSuccess(List<String> areaNameList);
    void onAllFieldLoadFailed(String message);
}
