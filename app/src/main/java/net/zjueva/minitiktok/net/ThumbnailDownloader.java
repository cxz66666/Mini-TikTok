package net.zjueva.minitiktok.net;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG="ThumbnailDownloader";
    private static int MESSAGE_DOWNLOAD=0;

    //拿到主ui的handler
    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler=responseHandler;
    }
    private Boolean mHasQuit=false;
    private Handler mRequestHandler;//处理请求的handler
    private ConcurrentHashMap<T,String> mRequestMap=new ConcurrentHashMap<>();

    private Handler mResponseHandler;
   private DownloadListerner<T> mThumbnailDownListener;


   //回调接口
    public  interface  DownloadListerner<T>{
        void onThumbnailDownFinish(T target,Bitmap thumbnail);
    }

    public void setThumbnailDownListener(DownloadListerner<T> listerner){
        mThumbnailDownListener=listerner;
    }
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mRequestHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==MESSAGE_DOWNLOAD){
                    T target=(T)msg.obj;
                    handlRequest(target);
                }
            }
        };
    }

    @Override
    public boolean quit() {
        mHasQuit=true;
        return super.quit();
    }

    public void queueThumbnail(T target,String url){
        Log.i(TAG,"Got a url: "+url);
        if(url==null){
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target,url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD,target)
                    .sendToTarget();
        }
    }

    private void handlRequest(final T target){
        try{
            final String url=mRequestMap.get(target);
            if(url==null){
                return;
            }
            byte []bitmapBytes=new SimpleGetHttp().getUrlBytes(url);
            final Bitmap bitmap= BitmapFactory.decodeByteArray(bitmapBytes,0,bitmapBytes.length);
            Log.d(TAG,"create bitmap");
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(mRequestMap.get(target)!=url||mHasQuit){
                        return;
                    }
                    mRequestMap.remove(target);
                    mThumbnailDownListener.onThumbnailDownFinish(target,bitmap);
                }
            });
        }catch (IOException ioe){
            Log.d(TAG,"Error downloading");
        }
    }
}
