package com.example.KartaSofta;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivity extends Activity implements FragmentInterface {
    /**
     * Called when the activity is first created.
     */
    public Map<String,List<Category>> categories;
    private boolean _isLoaded = false;
    private String parentId = String.valueOf(-1);
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public boolean isLoaded()
    {
        return _isLoaded;
    }
    public void set_isLoaded(boolean value)
    {
        _isLoaded = value;
    }

    F2 imageFragment;
    F1 fragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setParentId(String.valueOf(-1)); //Задаем parentId специальным значением, показывающим, что у данного набора категорий его нет
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        fragment = new F1();
        imageFragment = new F2(getApplicationContext());
        ft.add(R.id.main_layout,fragment);
        ft.commit();
    }
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        boolean iv = fragment.isVisible();
        if (fragment.isVisible())
        {
            if (indicator == 0)
            {
                super.onBackPressed();
                return;
            }
            else
            {
                Category category = findCategory(getParentId());
                if (category!=null)
                {
                    List<Category> list =   getCategories(category.getParentId());
                    List<String> newAdapterData = new ArrayList<String>(list.size());
                    for (Category c : list)
                    {
                        newAdapterData.add(c.getCategoryName());
                    }
                    fragment.setAdapterData(newAdapterData,getApplicationContext());
                    decreaseIndicator();
                    setParentId(category.getParentId());
                }
            }
            /*ft.hide(fragment);
            fragment = new F2(getApplicationContext());
            ft.addToBackStack(null);
            ft.add(R.id.main_layout,imageFragment);*/
        }
        else
        {
            ft.hide(imageFragment);
            fragment = new F1();
            ft.add(R.id.main_layout,fragment);
            Category category = findCategory(getParentId());
            if (category!=null)
            {
                List<Category> list =   getCategories(category.getParentId());
                List<String> newAdapterData = new ArrayList<String>(list.size());
                for (Category c : list)
                {
                    newAdapterData.add(c.getCategoryName());
                }
                fragment.setAdapterData(newAdapterData,getApplicationContext());
                decreaseIndicator();
                setParentId(category.getParentId());
                Log.d(" DDD ","");
            }

        }
        ft.commit();
    }
    private Category findCategory(String id)
    {
        String[] keys = new String[categories.size()];
        categories.keySet().toArray(keys);
        for (int i = 0; i < categories.size(); i++)
        {
            for (int j = 0; j < categories.get(keys[i]).size(); j++)
            {
                if (categories.get(keys[i]).get(j).getCategoryId().equals(id))
                {
                    return categories.get(keys[i]).get(j);
                }
            }
        }
        return null;
    }
    private int indicator = 0;                                                                 //Показывает категория это или подкатегория.
    public void increaseIndicator()
    {
        indicator++;
    }
    public void decreaseIndicator()
    {
        indicator--;
    }
    public int getIndicator()
    {
        return indicator;
    }
    public List<Category> getCategories(String parentId)
    {
        return categories.get(parentId);
    }
    public void setCategories(Map<String,List<Category>> categories)
    {
        this.categories = categories;
    }
    @Override
    public void onStart()
    {
        super.onStart();
    }
    public F2 getSecondFragment()
    {
        return imageFragment;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d("ACTIVITY_LOG","!!!!!!!!!!!!!!!!!!!!!!");
        fragment.onActivityResult(requestCode,resultCode,data);
        if (resultCode == MyApp.FINISH_IMAGE)
        {
            imageFragment.onActivityResult(requestCode,resultCode,data);
        }
    }
}
