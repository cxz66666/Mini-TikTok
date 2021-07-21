package net.zjueva.minitiktok.model;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

//单例模式 是PersonPhoto Model的单例
public class PersonPhotoModelLab {
    private static PersonPhotoModelLab sPersonPhotoModel;
    private List<PersonPhotoModel>mItems;

    public static PersonPhotoModelLab get(Context context){
        if(sPersonPhotoModel==null){
            sPersonPhotoModel=new PersonPhotoModelLab(context);
        }
        return sPersonPhotoModel;
    }
    private PersonPhotoModelLab(Context context){
        mItems=new ArrayList<>();
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/05/28/d9564eb150951819844b03de30fed060.th.png",123));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/06/14/7784577815f81c09c5ac4d67a684647d.th.jpg",456));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/16/image-20210716223318836.th.png",790));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/15/_LCP0N5HTJ8URDAYVQ.th.png",1003));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/02/064D9A76DC3AEEF4275D8EFB2147128C.th.jpg",2341));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/13/image-20210713211705571.th.png",2341));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/02/image-20210702212418948.th.png",6413));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/05/28/d9564eb150951819844b03de30fed060.th.png",123));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/06/14/7784577815f81c09c5ac4d67a684647d.th.jpg",456));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/16/image-20210716223318836.th.png",790));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/15/_LCP0N5HTJ8URDAYVQ.th.png",1003));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/02/064D9A76DC3AEEF4275D8EFB2147128C.th.jpg",2341));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/13/image-20210713211705571.th.png",2341));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/02/image-20210702212418948.th.png",6413));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/05/28/d9564eb150951819844b03de30fed060.th.png",123));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/06/14/7784577815f81c09c5ac4d67a684647d.th.jpg",456));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/16/image-20210716223318836.th.png",790));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/15/_LCP0N5HTJ8URDAYVQ.th.png",1003));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/02/064D9A76DC3AEEF4275D8EFB2147128C.th.jpg",2341));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/13/image-20210713211705571.th.png",2341));
        mItems.add(new PersonPhotoModel("https://pic.raynor.top/images/2021/07/02/image-20210702212418948.th.png",6413));
    }
    public List<PersonPhotoModel>getItems(){
        return mItems;
    }

}
