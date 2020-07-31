package cn.chper.vvedo.ui.square;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

import cn.chper.vvedo.R;
import cn.chper.vvedo.api.ApiServiceImpl;
import cn.chper.vvedo.api.SimpleResponse;
import cn.chper.vvedo.bean.VideoBean;
import cn.chper.vvedo.ui.component.VideoList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SquareFragment extends Fragment {

    private SquareViewModel squareViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        squareViewModel =
                ViewModelProviders.of(this).get(SquareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_square, container, false);
        squareViewModel.getVideos().observe(getViewLifecycleOwner(), new Observer<ArrayList<VideoBean>>() {
            @Override
            public void onChanged(ArrayList<VideoBean> videoBeans) {
                VideoList videoList = new VideoList();
                Bundle args = new Bundle();
                ArrayList tmp = new ArrayList();
                tmp.addAll(videoBeans);
                args.putParcelableArrayList("videos", tmp);
                videoList.setArguments(args);
                getChildFragmentManager().beginTransaction().replace(R.id.square_video_list, videoList).commit();
            }
        });
        getVideoList();
        return root;
    }

    private void getVideoList() {
        ApiServiceImpl.instance.api.getMyVideos().enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<VideoBean> videos = new ArrayList<>();
                    for (LinkedTreeMap video : (ArrayList<LinkedTreeMap>) response.body().data.get("videos")) {
                        VideoBean videoBean = new VideoBean();
                        videoBean.setId(0);
                        videoBean.setLikecount(((Double) video.get("likecount")).intValue());
                        videoBean.setFeedurl((String) video.get("feedurl"));
                        videoBean.setAvatar((String) video.get("avatar"));
                        videoBean.setDescription((String) video.get("description"));
                        videoBean.setNickname((String) video.get("nickname"));
                        videos.add(videoBean);
                    }
                    squareViewModel.setVideos(videos);
                }
                else {
                    Toast.makeText(getContext(), "请求视频列表失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(getContext(), "请求视频列表失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}