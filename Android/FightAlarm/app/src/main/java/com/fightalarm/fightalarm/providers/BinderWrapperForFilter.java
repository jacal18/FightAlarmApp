package com.fightalarm.fightalarm.providers;

import android.os.Binder;

import com.fightalarm.fightalarm.models.Event;
import com.fightalarm.fightalarm.models.Filter;

public class BinderWrapperForFilter extends Binder {
    private final Filter filter;

    public BinderWrapperForFilter(Filter filter) {
        this.filter = filter;
    }

    public Filter getData() {
        return filter;
    }
}
