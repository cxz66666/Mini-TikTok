package net.zjueva.minitiktok.model;


//用于详情页面的图片链接和点赞数 model
public class PersonPhotoModel {
    public PersonPhotoModel(String url, int likenumber) {
        this.url = url;
        this.likenumber = likenumber;
    }

    private String url;
    private int likenumber;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLikenumber() {
        return likenumber;
    }

    public void setLikenumber(int likenumber) {
        this.likenumber = likenumber;
    }
}
