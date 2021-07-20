package net.zjueva.minitiktok.net;

import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.zjueva.minitiktok.activity.MainActivity;
import net.zjueva.minitiktok.utils.Constant;
import net.zjueva.minitiktok.model.PostResultMessage;
import net.zjueva.minitiktok.model.PostResultMessageResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import okhttp3.internal.Util;

public class ResultMessageGetHttp {
    public static List<PostResultMessage> getData(String studentId){
        String urlStr;
        if(studentId!=null){
            urlStr =String.format("%svideo?student_id=%s", Constant.base_url,studentId);
        } else{
            urlStr =String.format("%smessages", Constant.base_url);
        }
        PostResultMessageResponse result=null;
        try {
            URL url=new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("token", Constant.token);  //设置header中的token

            if (conn.getResponseCode() == 200){
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                result = new Gson().fromJson(reader, new TypeToken<PostResultMessageResponse>() {
                }.getType());
                reader.close();
                in.close();

            } else{
                throw new IOException("没找到欸QAQ");
            }

        } catch (Exception e){
            e.printStackTrace();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(MainActivity.this,"网络异常"+e.toString(),Toast.LENGTH_SHORT).show();
//                }
//            });

        }
        if(result!=null&&result.mItems!=null){
            return result.mItems;  //拿到了值
        }
        return  null;
    }

}
