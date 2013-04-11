package com.example.kartasofta;

import android.content.Context;
import android.util.Log;
import org.apache.commons.io.input.BOMInputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 17.03.13
 * Time: 20:36
 * To change this template use File | Settings | File Templates.
 */
public class XmlParser {
    private static final String LOG_TAG = "XmlParser_Log";
    public static boolean NeedUpdate(Context context,InputStream stream)
    {

        String oldDate = "";
        String newDate = "";
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new FileInputStream(new File(context.getFilesDir().getPath() + "/" + MyApp.MAIN_XML_FILENAME)),Charset.forName("utf-8").name());
            boolean whileNeedBreak = false;
            while (parser.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (parser.getEventType())
                {
                    case XmlPullParser.START_TAG:
                if (parser.getName().equals("yml_catalog"))
                {

                    for (int i = 0; i < parser.getAttributeCount(); i++)
                    {
                        if (parser.getAttributeName(i).equals("date"))
                        {
                            oldDate = parser.getAttributeValue(i);
                            break;
                        }
                    }
                    whileNeedBreak = true;

                }
                        break;
                    default:
                        break;
                }
                if (whileNeedBreak)
                {
                    break;
                }
                parser.next();
            }
            parser.setInput(new InputStreamReader(stream));
            whileNeedBreak = false;
            while (parser.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (parser.getEventType())
                {
                    case XmlPullParser.START_TAG:
                if (parser.getName().equals("yml_catalog"))
                {
                    for (int i = 0; i < parser.getAttributeCount(); i++)
                    {
                        if (parser.getAttributeName(i).equals("date"))
                        {
                            newDate = parser.getAttributeValue(i);
                            break;
                        }
                    }
                    whileNeedBreak = true;

                }
                        break;
                    default:
                        break;
                }
                if (whileNeedBreak)
                {
                    break;
                }
                parser.next();
            }
            Log.d(LOG_TAG, "debug point");
            return !oldDate.equals(newDate);
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }
    public static Map<String,List<Category>> categoryParse(Context context)
    {
        List<Category> dirtyList = new ArrayList<Category>();   //Список всех категорий в файле, не разобран.
        Map<String,List<Category>> allCategoriesMap = new HashMap<String, List<Category>>();                 //Коллекция, в которую будут записаны списки категорий. Ключ - parentId.
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new FileInputStream(new File(context.getFilesDir().getPath() + "/" + MyApp.MAIN_XML_FILENAME)),Charset.forName("utf-8").name());
            while (parser.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (parser.getEventType())
                {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("category"))
                        {
                            String parentId = String.valueOf(-1);
                            String categoryId = parser.getAttributeValue(0);
                            if (parser.getAttributeCount()>1)
                            {
                                parentId = parser.getAttributeValue(1);
                            }
                            Category c = new Category(parser.nextText(),categoryId,parentId);
                            dirtyList.add(c);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    default:
                        break;
                }
                parser.next();
            }
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (Category c : dirtyList)
        {
            if (!allCategoriesMap.containsKey(c.getParentId()))
            {
                List<Category> cleanedList = new ArrayList<Category>();
                cleanedList.add(c);
                allCategoriesMap.put(c.getParentId(),cleanedList);
            }
            else
            {
                allCategoriesMap.get(c.getParentId()).add(c);
            }
        }
        return allCategoriesMap;
    }
    public static List<Offer> offerParse(Context context)
    {
        List<Offer> result = new ArrayList<Offer>();
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
          //  InputStreamReader reader = new InputStreamReader(new FileInputStream(new File(context.getFilesDir().getPath() + "/" + MyApp.MAIN_XML_FILENAME)));
            // BufferedReader stream = new BufferedReader(new FileInputStream(new File(context.getFilesDir().getPath() + "/" + MyApp.MAIN_XML_FILENAME)));

            parser.setInput(new InputStreamReader((new FileInputStream(new File(context.getFilesDir().getPath() + "/" + MyApp.MAIN_XML_FILENAME)))));

            while (parser.getEventType()!=XmlPullParser.END_DOCUMENT)
            {
                switch (parser.getEventType())
                {
                    case XmlPullParser.START_TAG:

                        if (parser.getName().equals("offer"))
                        {
                            String id = parser.getAttributeValue(0);
                            parser.nextTag();
                            String _model = "";
                            String _price = "";
                            String _url = "";
                            String _picture = "";
                            String _categoryId = "";
                            String _currencyId = "";
                            String str = parser.getName();
                            Log.d(" ! "," " );
                            while (!parser.getName().equals("offer")){
                                boolean isNeededTag = false;
                                if (parser.getName().equals("model"))
                                {
                                    _model = parser.nextText();
                                    isNeededTag = true;
                                }
                                if (parser.getName().equals("price"))
                                {
                                    _price = parser.nextText();
                                    isNeededTag = true;
                                }
                                if (parser.getName().equals("url"))
                                {
                                    _url = parser.nextText();
                                    isNeededTag = true;
                                }
                                if (parser.getName().equals("picture"))
                                {
                                    _picture = parser.nextText();
                                    isNeededTag = true;

                                }
                                if (parser.getName().equals("categoryId"))
                                {
                                    _categoryId = parser.nextText();
                                    isNeededTag = true;

                                }
                                if (parser.getName().equals("currencyId"))
                                {
                                    _currencyId = parser.nextText();
                                    isNeededTag = true;

                                }
                                if (!isNeededTag)
                                {
                                  //  Log.d(LOG_TAG,parser.getEventType() + " parser event type!!!!!");
                                    parser.nextText();
                                    //parser.nextTag();

                                }
                                parser.nextTag();
                            }
                            Offer offer = new Offer(id,_url,_categoryId,_currencyId,_picture,_price,_model,"");
                            result.add(offer);
                        }
                        break;
                }
                parser.next();
            }
        }
        catch (XmlPullParserException e)
        {

            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {

            e.printStackTrace();
        }
        catch (IOException e)
        {

            e.printStackTrace();
        }
        return result;

    }
}
