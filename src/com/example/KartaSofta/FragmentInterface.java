package com.example.KartaSofta;

import android.app.Fragment;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ag
 * Date: 22.03.13
 * Time: 11:03
 * To change this template use File | Settings | File Templates.
 */
public interface FragmentInterface {
    public F2 getSecondFragment();
    public List<Category> getCategories(String parentId);
    public void setCategories(Map<String,List<Category>> categories);
    public String getParentId();
    public void setParentId(String parentId);
    public void increaseIndicator();
    public void decreaseIndicator();
    public int getIndicator();
    public boolean isLoaded();
    public void set_isLoaded(boolean value);
}
