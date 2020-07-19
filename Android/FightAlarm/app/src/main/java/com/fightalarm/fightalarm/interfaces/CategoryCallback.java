package com.fightalarm.fightalarm.interfaces;

import com.fightalarm.fightalarm.models.Category;

import java.util.ArrayList;

public interface CategoryCallback {
    void onCallback(ArrayList<Category> categories);
}
