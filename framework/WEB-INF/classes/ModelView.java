package etu1800.framework;

import java.util.HashMap;

public class ModelView {
    String View;
    HashMap<String, Object> data = new HashMap<>();

    public void addItem(String key, Object value) {
        data.put(key, value);
    }

    public String getView() {
        return View;
    }

    public void setView(String view) {
        View = view;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

}
