package cn.chper.vvedo.ui.component;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

import cn.chper.vvedo.R;
import cn.chper.vvedo.bean.VideoBean;

public class VideoList extends Fragment implements VideoListAdapter.VideoClickListener {

    private ArrayList<VideoBean> videos;

    private RecyclerView rcyVideoList;

    private VideoListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_video_list, container, false);
        this.rcyVideoList = root.findViewById(R.id.rcy_video_list);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        this.rcyVideoList.setLayoutManager(staggeredGridLayoutManager);
        Bundle args = getArguments();
        if (args != null) {
            videos = (ArrayList) args.getParcelableArrayList("videos");
        }
        else {
            videos = new ArrayList<>();
        }
        adapter = new VideoListAdapter(videos, this);
        this.rcyVideoList.setAdapter(adapter);
        return root;
    }

    public void onVideoClick(int clickedItemIndex) {

    }
}
