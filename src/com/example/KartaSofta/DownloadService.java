package com.example.KartaSofta;

import android.app.*;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.text.LoginFilter;
import android.util.Log;
import android.util.StringBuilderPrinter;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 16.03.13
 * Time: 19:58
 * To change this template use File | Settings | File Templates.
 */
public class DownloadService extends IntentService {

    final String LOG_TAG = "INTENT SERVICE";
    public DownloadService()
    {
        super("download_service");
    }
    public void onCreate()
    {
        super.onCreate();
        Log.d(LOG_TAG,"service.onCreate()");
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        PendingIntent pi = intent.getParcelableExtra(MyApp.PARAM_INTENT);
        String mainUrl = intent.getStringExtra("mainUrl");
        if (mainUrl==null)
        {
            Log.d(LOG_TAG,"  ____________1")   ;
            String[] urls = intent.getStringArrayExtra("urls");
            String[] names = intent.getStringArrayExtra("names");
            for (String s : names)
            {
                Log.d(LOG_TAG, "names : " + s);
            }
           Thread thread = new Thread(null,new ServiceWorker(urls,names,pi));
           thread.start();
        }
        else
        {
            Log.d(LOG_TAG,"_________________2")   ;
            Thread thread = new Thread(null,new XmlDownloader(mainUrl,pi));
            thread.start();
        }
    }
    class XmlDownloader implements Runnable
    {
        private String mainUrl;
        private PendingIntent pi;
        public XmlDownloader(String mainUrl,PendingIntent pi)
        {
            this.pi = pi;
            this.mainUrl = mainUrl;
        }
        public void run()
        {
            MyApp application = (MyApp)getApplication();
            Log.d(LOG_TAG," my app service is started");
            try {
                Log.d(LOG_TAG," in try ");
                HttpClient client = application.getClient();
                Log.d(LOG_TAG," client");
                HttpGet get = new HttpGet(mainUrl);
                Log.d(LOG_TAG,mainUrl + " main url");
                Log.d(LOG_TAG," get");
                Log.d(LOG_TAG, client.getParams().toString() + " : httpparams");
                HttpResponse response = client.execute(get);
                Log.d(LOG_TAG," response");
                File file = new File(DownloadService.this.getFilesDir().getPath() + "/" + MyApp.MAIN_XML_FILENAME);
                Log.d(LOG_TAG, " file");
                Log.d(LOG_TAG, file.exists() + " : file exists?");
                if (!file.exists())
                {
                    Log.d(LOG_TAG,"!file.exists()");
                    downloadXml(file,response);
                    get.abort();
                    Intent intent = new Intent();
                    pi.send(DownloadService.this,MyApp.FINISH_MAIN_XML,intent);
                    return;
                }
                else
                {
                    if (XmlParser.NeedUpdate(getApplicationContext(),response.getEntity().getContent()))
                    {
                        response = client.execute(get);
                        downloadXml(file, response);
                        get.abort();
                        pi.send(DownloadService.this, MyApp.FINISH_MAIN_XML, new Intent());
                        return;
                    }
                }
                pi.send(DownloadService.this,MyApp.FINISH_MAIN_XML,new Intent());
            }
            catch (IOException e)
            {
                Log.d(LOG_TAG," IO EXCEPTION") ;
                e.printStackTrace();
            }
            catch (PendingIntent.CanceledException e)
            {
                Log.d(LOG_TAG, " canceled exception e");
                e.printStackTrace();
            }
        }
        public boolean downloadXml(File file,HttpResponse response)
        {
            try {

                file.createNewFile();
                InputStream stream = response.getEntity().getContent();
                //FileInputStream stream = (FileInputStream)response.getEntity().getContent();
                FileOutputStream outputStream = new FileOutputStream(file);
                int c;
                while ((c = stream.read()) != -1)
                {
                    outputStream.write(c);
                }
                Log.d(LOG_TAG,c + " : XML DOWNLOADED!");
                stream.close();
                outputStream.close();
                response.getEntity().consumeContent();
                return true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return false;
        }
    }
    class ServiceWorker implements Runnable
    {
        private PendingIntent pi;
        private String[] urls;
        private String[] names;
        public ServiceWorker(String[] urls,String[] names,PendingIntent pi)
        {
            this.pi = pi;
            this.urls = urls;
            this.names = names;
        }
        public void run()
        {
            MyApp application = (MyApp)getApplication();
            HttpClient client = application.getClient();
            Log.d(LOG_TAG, "urls length + : " + urls.length);
            try {
            for (int i = 0; i < urls.length; i++)
            {
                Log.d(LOG_TAG,"download of image started") ;
                if (urls[i]!=null&&!urls[i].isEmpty())
                {
                HttpGet get = new HttpGet(urls[i]);
                    Log.d(LOG_TAG, " urls not null or empty");

                HttpResponse response = client.execute(get);
                String filename = names[i];
                File image = new File(DownloadService.this.getFilesDir().getPath() + "/" + filename);
                    Log.d(LOG_TAG, " before image downloading" + filename  + " _ " + image.exists());

                if (!filename.isEmpty()&&!image.exists())
                {
                    Log.d(LOG_TAG, " image file name : " + image.getPath());
                    image.createNewFile();
                    InputStream stream = response.getEntity().getContent();
                    FileOutputStream outputStream = new FileOutputStream(image);
                    int c;
                    while ((c = stream.read()) != -1)
                    {
                        outputStream.write(c);
                    }
                    stream.close();
                    outputStream.close();
                    get.abort();
                    Intent intent = new Intent();
                    intent.putExtra(MyApp.IMAGE_FILENAME,image.getPath());
                    Log.d(LOG_TAG, " image sent");
                    pi.send(DownloadService.this, MyApp.FINISH_IMAGE, intent);
                }
                }
            }
            }   catch (IOException e)
            {
                Log.d(LOG_TAG, "IO exception caught");
                e.printStackTrace();
            }
            catch (PendingIntent.CanceledException e)
            {
                Log.d(LOG_TAG, " canceledException caught");
                e.printStackTrace();
            }
        }
    }
    public void onDestroy()
    {
        super.onDestroy();
        Log.d(LOG_TAG,"onDestroy");
    }
}
