package com.honglu.future.widget.uploader;

import android.text.TextUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class ApacheHttpUtils {
    public static String sUserAgent;
    public static void setUserAgent(String userAgent){
        sUserAgent = userAgent;
    }

    public static byte[] tryGet(String serverUrl, int failedTryCount) {
        failedTryCount--;
        try {
            return (failedTryCount > 0 ) ? ApacheHttpUtils.getFromServer(serverUrl) : null;
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
            tryGet(serverUrl, failedTryCount);
        }
        catch (IOException e) {
            e.printStackTrace();
            tryGet(serverUrl, failedTryCount);
        }
        return null;
    }

    public static byte[] tryPost(String serverUrl, byte[] bytes, int failedTryCount) {
        failedTryCount--;
        try {
            return (failedTryCount > 0 ) ? ApacheHttpUtils.postToServer(serverUrl, bytes) : null;
        }
        catch (ClientProtocolException e) {
            e.printStackTrace();
            tryPost(serverUrl, bytes, failedTryCount);
        }
        catch (IOException e) {
            e.printStackTrace();
            tryPost(serverUrl, bytes, failedTryCount);
        }
        return null;
    }

    public static byte[] postToServer(String url, byte[] content) throws ClientProtocolException,
            IOException {
        return executeRequest(url, new ByteArrayEntity(content), HttpPost.METHOD_NAME);
    }

    public static byte[] getFromServer(String url) throws ClientProtocolException, IOException {
        return executeRequest(url, null, HttpGet.METHOD_NAME);
    }

    public static byte[] executeRequest(String url, HttpEntity postEntity, String methodName)
            throws IOException {
        HttpUriRequest request = null;
        HttpClient client;
        byte[] resultBytes = null;
        try {
            if (HttpPost.METHOD_NAME.equals(methodName)) {
                request = new HttpPost(url);
                if (postEntity != null) {
                    HttpPost post = ((HttpPost) request );
                    post.setEntity(postEntity);
                }
            } else if (HttpGet.METHOD_NAME.equals(methodName)) {
                request = new HttpGet(url);
            }
            client = createHttpClient();
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resultEntity = response.getEntity();
                if (resultEntity != null) {
                    resultBytes = EntityUtils.toByteArray(resultEntity);
                }
            }
        }
        catch (Exception e) {}
        finally {
            if (request != null) {
                request.abort();
            }
        }
        return resultBytes;
    }

    private static HttpClient createHttpClient() {
        HttpParams httpParameters = new BasicHttpParams();
        if (!TextUtils.isEmpty(sUserAgent)){
            httpParameters.setParameter("http.useragent", sUserAgent);
        }
        int timeoutConnection = 30000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 30000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        return new DefaultHttpClient(httpParameters);
    }
}


