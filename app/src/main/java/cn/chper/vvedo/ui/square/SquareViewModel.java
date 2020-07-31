package cn.chper.vvedo.ui.square;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import cn.chper.vvedo.bean.VideoBean;

public class SquareViewModel extends ViewModel {

    private MutableLiveData<ArrayList<VideoBean>> mVideos;

    public SquareViewModel() {
        mVideos = new MutableLiveData<>();
        mVideos.setValue(new ArrayList<>());
    }

    public void setVideos(ArrayList<VideoBean> videos) { mVideos.setValue(videos); }

    public LiveData<ArrayList<VideoBean>> getVideos() {
        return mVideos;
    }

}