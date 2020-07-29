package cn.chper.vvedo.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.chper.vvedo.bean.VideoBean;

public class UserViewModel extends ViewModel {

    private MutableLiveData<ArrayList<VideoBean>> mVideos;

    public UserViewModel() {
        mVideos = new MutableLiveData<>();
        mVideos.setValue(new ArrayList<>());
    }

    public void setVideos(ArrayList<VideoBean> videos) { mVideos.setValue(videos); }

    public LiveData<ArrayList<VideoBean>> getVideos() {
        return mVideos;
    }

}