package com.example.KartaSofta;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 17.03.13
 * Time: 20:10
 * To change this template use File | Settings | File Templates.
 */
public class F2 extends ListFragment {
    private Category category;
    SimpleAdapter adapter;
    List<HashMap<String,Object>> adapterData;
    private final String LOG_TAG = "F2_LOG";
    private FragmentInterface fragmentInterface;
    public String[] testArray;
    public F2(Context context)
    {
        testArray = new String[30];
        final int[] to = {R.id.image_layout_image,R.id.image_layout_title,R.id.image_layout_description};
        String[] from = {"image","title","description"};
        adapterData = new ArrayList<HashMap<String, Object>>();
        adapter = new SimpleAdapter(context,adapterData,R.layout.image_layout,from,to);

        Log.d(LOG_TAG, " adapter was created") ;
    }
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
    public void setCategory(Category category)
    {
        this.category = category;
    }
    public void setAdapterData(List<HashMap<String,Object>> data)
    {
        Log.d(LOG_TAG,data.size() + " : data.size. " + data.get(0).size() + " : 0 size");
        final int[] to = {R.id.image_layout_image,R.id.image_layout_title,R.id.image_layout_description};
        adapterData.addAll(data);
        //adapterData = new ArrayList<HashMap<String, Object>>(data);
        //fragmentInterface.setF2Data(adapterData);
        if (data.size()>0&&data.get(0).size()>=3)
        {
            String[] from = {"image","title","description"};
            Log.d(LOG_TAG,adapterData.size() + " size");
            //adapter = new SimpleAdapter(getActivity(),adapterData,R.layout.image_layout,from,to);

            setListAdapter(adapter);
            Log.d(LOG_TAG,adapter.getCount() + " : adapter count");
            /*HashMap<String,Object> map = null;
            try {
                map = (HashMap<String,Object>)adapter.getItem(0);
            } catch (ClassCastException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //TODO: catch class cast exception
            if (map!=null)
            {
                Log.d(LOG_TAG,map.toString());
            } */
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        /*adapterData = new ArrayList<HashMap<String,Object>>();
        final int[] to = {R.id.image_layout_image,R.id.image_layout_title,R.id.image_layout_description};
        final String[] from  = {"image","title","description"};
        adapter = new SimpleAdapter(getActivity(),adapterData,R.layout.image_layout,from,to);
        */
        View v = inflater.inflate(R.layout.f2,null);
        PendingIntent pi =  (getActivity().createPendingResult(1,new Intent(),0));
        Intent intent1 = new Intent(getActivity(),DownloadService.class);
        intent1.putExtra(MyApp.PARAM_INTENT,pi);
        if (category!=null)
        {
            ArrayList<HashMap<String,Object>> maps = new ArrayList<HashMap<String, Object>>();
            List<Offer> allOffers = XmlParser.offerParse(getActivity());
            List<String> imageNames = new ArrayList<String>();
            List<String> imageUrls = new ArrayList<String>();
            for (Offer offer : allOffers)
            {
                if (offer.categoryId.equals(category.getCategoryId()))
                {
                    HashMap<String,Object> map = new HashMap<String, Object>();
                    String imageName = "";
                    String imageUrl = "";
                    if (offer.picture!=null&&offer.picture.contains(".jpg"))
                    {
                        imageName = offer.id + ".jpg";
                        imageUrl = offer.picture;
                    }
                    if (offer.picture!=null&&offer.picture.contains(".png"))
                    {
                        imageName = offer.id + ".png";
                        imageUrl = offer.picture;
                    }
                    File file = new File(getActivity().getFilesDir().getPath() + "/" + imageName) ;
                    if (!imageName.isEmpty()&&file.exists())
                    {
                        map.put("image",file.getPath());
                    }
                    else
                    {
                        map.put("image",null);
                    }
                    map.put("title",offer.name);
                    map.put("description",offer.description);
                    map.put("id",offer.url);   //Записываем в данные адаптера урл для открытия в браузере при клике, на экране он не отображается.
                    map.put("filename",offer.id);
                    maps.add(map);
                    imageNames.add(imageName);
                    imageUrls.add(imageUrl);
                }
            }
            setAdapterData(maps);
            String[] urls = new String[imageUrls.size()];
            String[] names = new String[imageNames.size()];
            intent1.putExtra("urls",imageUrls.toArray(urls));
            intent1.putExtra("names",imageNames.toArray(names));
            getActivity().startService(intent1);
        }
        return v;
    }
    @Override
    public void onListItemClick(ListView l,View v,int position,long id)
    {
        Uri uri = Uri.parse(adapterData.get(position).get("id").toString());
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(browserIntent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("F2_LOG"," on activity result");
        //adapter = fragmentInterface.getAdapter();
        //adapterData = fragmentInterface.getF2Data();
        Log.d(LOG_TAG,testArray.length + " testarray.lenght");
        Log.d(LOG_TAG,adapterData.size() + " adapter data size");
        switch (resultCode)
        {
            case MyApp.FINISH_IMAGE:
                String imageName = data.getStringExtra(MyApp.IMAGE_FILENAME);
                //Log.d(LOG_TAG," adapter " + getListAdapter().getCount());
//                Log.d(LOG_TAG," . " + fragmentInterface.getF2Data().size() + " fragment interface adapterData.size");
                if (adapter!=null&&adapterData!=null)
                {
                    String idOfOffer = imageName.substring(0,imageName.length()-4);

                    Log.d(LOG_TAG,idOfOffer + " : idOfOffer");
                    for (int i = 0; i < adapterData.size(); i++)
                    {
                        Log.d(LOG_TAG,adapterData.get(i).get("filename").toString() + " getid()");
                        if (idOfOffer.endsWith(adapterData.get(i).get("filename").toString()))
                        {
                            adapterData.get(i).remove("image");
                            adapterData.get(i).put("image",imageName);
                            Log.d(LOG_TAG," egegei : " + imageName);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }

}
