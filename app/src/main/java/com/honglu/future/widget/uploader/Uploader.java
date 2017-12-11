package com.honglu.future.widget.uploader;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class Uploader {
    public static void upload(final String url, final File file, final UploadProgressListener listener) {
        new CustomThreadAsyncTask<Void, Integer, Object>() {
            private long total;
            @Override
            protected Object doInBackground(Void... params) {
                byte[] response = null;
                UploadProgressListener listener = new UploadProgressListener() {
                    @Override
                    public void onSucceed(byte[] response) {
                    }
                    @Override
                    public void onProgress(long progressbyte) {
                        double per = (float) progressbyte / (float) total;
                        int progress = (int) (per * 100);
                        publishProgress(progress);
                    }
                    @Override
                    public void onError() {
                    }
                };
                ProcessMultiPartEntity entity = new ProcessMultiPartEntity(listener);
                entity.addPart("file", new FileBody(file));
                total += entity.getContentLength();
                try {
                    response = ApacheHttpUtils.executeRequest(url, entity, HttpPost.METHOD_NAME);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (values != null && values.length == 1) {
                    int progress = values[0];
                    if (listener != null) {
                        listener.onProgress(progress);
                    }
                }
            }

            @Override
            protected void onPostExecute(Object result) {
                if (result != null) {
                    listener.onSucceed((byte[]) result);
                } else {
                    listener.onError();
                }
            }
        }.execute();
    }

    /**
     * 上传用户头像
     * @param url   地址
     * @param file  文件
     * @param jsonParams    json格式
     * @param listener  监听callback
     */
    public static void uploadUserIcon(final String url, final File file, final String jsonParams, final UploadProgressListener listener) {
        new CustomThreadAsyncTask<Void, Integer, Object>() {
            private long total;
            @Override
            protected Object doInBackground(Void... params) {
                byte[] response = null;
                UploadProgressListener listener = new UploadProgressListener() {
                    @Override
                    public void onSucceed(byte[] response) {
                    }
                    @Override
                    public void onProgress(long progressbyte) {
                        double per = (float) progressbyte / (float) total;
                        int progress = (int) (per * 100);
                        publishProgress(progress);
                    }
                    @Override
                    public void onError() {
                    }
                };
                ProcessMultiPartEntity entity = new ProcessMultiPartEntity(listener);
                try {
                    entity.addPart("p",new StringBody(jsonParams, Charset.forName("UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                entity.addPart("Filedata", new FileBody(file));
                total += entity.getContentLength();
                try {
                    response = ApacheHttpUtils.executeRequest(url, entity, HttpPost.METHOD_NAME);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (values != null && values.length == 1) {
                    int progress = values[0];
                    if (listener != null) {
                        listener.onProgress(progress);
                    }
                }
            }

            @Override
            protected void onPostExecute(Object result) {
                if (result != null) {
                    listener.onSucceed((byte[]) result);
                } else {
                    listener.onError();
                }
            }
        }.execute();
    }



}
