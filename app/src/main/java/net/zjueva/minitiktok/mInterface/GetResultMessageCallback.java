package net.zjueva.minitiktok.mInterface;

import java.util.List;

public interface GetResultMessageCallback<T> {
    void setData(List<T> item);
}
