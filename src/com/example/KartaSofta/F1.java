package com.example.kartasofta;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.kartasofta.catalog.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 16.03.13
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class F1 extends ListFragment {
    private final String LOG_TAG = "F1_LOG";
    private ArrayAdapter<String> adapter1;
    private final String url = "http://kartasofta.ru/xml_yandex/to_android.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (!fragmentInterface.isLoaded())
        {
        adapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,new ArrayList<String>());
        setListAdapter(adapter1);
        }
        PendingIntent pi =  (getActivity().createPendingResult(1,new Intent(),0));
        Intent intent1 = new Intent(getActivity(),DownloadService.class);
        intent1.putExtra(MyApp.PARAM_INTENT,pi);
        intent1.putExtra("mainUrl",url);
        if (!fragmentInterface.isLoaded())
        {
            getActivity().startService(intent1);
        }
        View v  = inflater.inflate(R.layout.f1, null);
        fragmentInterface.set_isLoaded(true);
        return v;
    }

    FragmentInterface fragmentInterface;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            fragmentInterface = (FragmentInterface)activity;
        } catch (ClassCastException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public void setAdapterData(List<String> data,Context context)
    {
        if (adapter1==null)
        {
//            getActivity().getApplicationContext().databaseList();
            adapter1 = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,new ArrayList<String>());
            setListAdapter(adapter1);
        }
        adapter1.clear();
        adapter1.addAll(data);
        adapter1.notifyDataSetChanged();
        //setListAdapter(adapter1);
    }
    @Override
    public void onListItemClick(ListView l,View v,int position,long id)
    {
        List<Category> list = fragmentInterface.getCategories(fragmentInterface.getParentId());
        String parentId = list.get(position).getCategoryId();
        List<Category> newDataList = fragmentInterface.getCategories(parentId);
        List<String> adapterDataList = new ArrayList<String>();
        if (newDataList!=null&&!newDataList.isEmpty())
        {
            for (Category c : newDataList)
            {
                adapterDataList.add(c.getCategoryName());
            }
            fragmentInterface.setParentId(parentId);
            adapter1.clear();
            adapter1.addAll(adapterDataList);
            adapter1.notifyDataSetChanged();
            fragmentInterface.increaseIndicator();
            //ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,adapterDataList);
            //setListAdapter(newAdapter);
        }
        else
        {

            F2 imageFragment = fragmentInterface.getSecondFragment();
            //F2 imageFragment = new F2(getActivity().getApplicationContext());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            //ft.hide(imageFragment);
            fragmentInterface.increaseIndicator();
            fragmentInterface.setParentId(parentId);
            ft.hide(this);
            ft.add(R.id.main_layout,imageFragment);
            imageFragment.setCategory(list.get(position));
            //ft.addToBackStack("imageFragment");
            ft.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("F1_LOG"," on activity result");
        switch (resultCode)
        {
            case MyApp.FINISH_MAIN_XML:
                Log.d("F1_LOG", "finish main");
                fragmentInterface.setCategories(XmlParser.categoryParse(getActivity()));

                List<Category> currentList = fragmentInterface.getCategories(fragmentInterface.getParentId());
                List<String> names = new ArrayList<String>(currentList.size());
                for (int i = 0; i < currentList.size(); i++)
                {
                    names.add(currentList.get(i).getCategoryName());
                    Log.d(LOG_TAG,currentList.get(i).getCategoryName());
                    Log.d(LOG_TAG,names.get(i));
                }
                adapter1.clear();
                adapter1.addAll(names);
                adapter1.notifyDataSetChanged();
                //adapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,names);

                break;
            default:
                break;
        }
    }
}