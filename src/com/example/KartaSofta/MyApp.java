package com.example.KartaSofta;

import android.app.Application;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 16.03.13
 * Time: 19:21
 * To change this template use File | Settings | File Templates.
 */
public class MyApp extends Application {
    public static final String MAIN_XML_FILENAME = "main_xml.xml";
    public static final int FINISH_MAIN_XML = 100;
    public static final int FINISH_IMAGE = 101;
    public static final String PARAM_INTENT = "pending_intent";
    public static final String IMAGE_FILENAME = "image_filename";
    private HttpClient client;
    @Override
    public void onCreate()
    {
        super.onCreate();
        client = createClient();
    }
    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        shutDownClient();
    }
    @Override public void onTerminate()
    {
        super.onTerminate();
        shutDownClient();
    }
    private HttpClient createClient()
    {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params,HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(params,true);
        SchemeRegistry scheme = new SchemeRegistry();
        scheme.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),80));
        scheme.register(new Scheme("https", SSLSocketFactory.getSocketFactory(),443));
        ClientConnectionManager manager = new ThreadSafeClientConnManager(params,scheme);
        return new DefaultHttpClient(manager,params);
    }
    public HttpClient getClient()
    {
        return client;
    }
    private void shutDownClient()
    {
        if (client!=null&&client.getConnectionManager()!=null)
        {
            client.getConnectionManager().shutdown();
        }
    }

}
