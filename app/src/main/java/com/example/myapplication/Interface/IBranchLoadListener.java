package com.example.myapplication.Interface;
import com.example.myapplication.Model.Field;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Field> fieldList);
    void onBranchLoadFailed(String message);
}
