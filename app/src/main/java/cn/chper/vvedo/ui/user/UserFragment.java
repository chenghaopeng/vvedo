package cn.chper.vvedo.ui.user;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;

import cn.chper.vvedo.R;
import cn.chper.vvedo.api.ApiService;
import cn.chper.vvedo.api.ApiServiceImpl;
import cn.chper.vvedo.api.SimpleResponse;
import cn.chper.vvedo.bean.VideoBean;
import cn.chper.vvedo.ui.component.VideoList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private UserViewModel userViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        Button btn_login = root.findViewById(R.id.btn_login);
        EditText edt_username = root.findViewById(R.id.edt_username);
        EditText edt_password = root.findViewById(R.id.edt_password);
        TextView txt_username = root.findViewById(R.id.txt_username);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                ApiServiceImpl.instance.api.login(new ApiService.LoginForm(username, password)).enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        if (response.isSuccessful() && response.body().code == 0) {
                            ApiServiceImpl.instance.token = (String) response.body().data.get("token");
                            ApiServiceImpl.instance.username = username;
                            Toast.makeText(getContext(), "注册/登录成功，欢迎" + username + "！", Toast.LENGTH_SHORT).show();
                            edt_username.setVisibility(View.GONE);
                            edt_password.setVisibility(View.GONE);
                            btn_login.setVisibility(View.GONE);
                            txt_username.setVisibility(View.VISIBLE);
                            txt_username.setText(username);
                            getVideoList();
                        }
                        else {
                            Toast.makeText(getContext(), "注册/登录失败，请检查用户名密码是否正确且完整！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        if (ApiServiceImpl.instance.token != null) {
            edt_username.setVisibility(View.GONE);
            edt_password.setVisibility(View.GONE);
            btn_login.setVisibility(View.GONE);
            txt_username.setVisibility(View.VISIBLE);
            txt_username.setText(ApiServiceImpl.instance.username);
            getVideoList();
        }
        userViewModel.getVideos().observe(getViewLifecycleOwner(), new Observer<ArrayList<VideoBean>>() {
            @Override
            public void onChanged(ArrayList<VideoBean> videoBeans) {
                VideoList videoList = new VideoList();
                Bundle args = new Bundle();
                ArrayList tmp = new ArrayList();
                tmp.addAll(videoBeans);
                args.putParcelableArrayList("videos", tmp);
                videoList.setArguments(args);
                getChildFragmentManager().beginTransaction().replace(R.id.user_video_list, videoList).commit();
            }
        });
        return root;
    }

    private void getVideoList() {
        ApiServiceImpl.instance.api.getMyVideos(ApiServiceImpl.instance.token).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful() && response.body().code == 0) {
                    ArrayList<VideoBean> videos = new ArrayList<>();
                    for (LinkedTreeMap video : (ArrayList<LinkedTreeMap>) response.body().data.get("videos")) {
                        VideoBean videoBean = new VideoBean();
                        videoBean.setId(((Double) video.get("id")).intValue());
                        videoBean.setLikecount(((Double) video.get("likecount")).intValue());
                        videoBean.setFeedurl((String) video.get("feedurl"));
                        videoBean.setAvatar((String) video.get("avatar"));
                        videoBean.setDescription((String) video.get("description"));
                        videoBean.setNickname((String) video.get("nickname"));
                        videos.add(videoBean);
                    }
                    userViewModel.setVideos(videos);
                }
                else {
                    Toast.makeText(getContext(), "请求自己的视频列表失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(getContext(), "请求自己的视频列表失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}