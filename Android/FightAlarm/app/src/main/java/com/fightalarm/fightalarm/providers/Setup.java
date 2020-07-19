package com.fightalarm.fightalarm.providers;

import com.fightalarm.fightalarm.models.Category;
import com.fightalarm.fightalarm.models.Event;

public class Setup {

    private Event selectedEvent;
    private Integer selected;


    private Category category;

    public Setup() {
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

}
