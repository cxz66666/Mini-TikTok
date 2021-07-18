package net.zjueva.minitiktok.net;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//最最最基础的get请求http封装
public class SimpleGetHttp {
    public byte[]getUrlBytes(String urlSpec) throws IOException{
        URL url=new URL(urlSpec);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        try{
            ByteArrayOutputStream out=new ByteArrayOutputStream();
            InputStream in=connection.getInputStream();
            if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage()+" with "+urlSpec);
            }
            int bytesRead=0;
            byte []buffer=new byte[4096];
            while((bytesRead=in.read(buffer))>0){
                out.write(buffer,0,bytesRead);
            }
            out.close();
            return out.toByteArray();
        }
        finally {
            connection.disconnect();
        }
    }
    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }
}
