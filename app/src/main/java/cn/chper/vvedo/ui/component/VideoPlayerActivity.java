package cn.chper.vvedo.ui.component;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.ArrayList;

import cn.chper.vvedo.R;
import cn.chper.vvedo.api.ApiServiceImpl;
import cn.chper.vvedo.api.SimpleResponse;
import cn.chper.vvedo.bean.VideoBean;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoPlayer;

    private ImageView ivResumePlay;

    private TextView tvVideoNickname;

    private TextView tvVideoDescription;

    private ImageView ivVideoAvatar;

    private ImageView ivLike;

    private TextView tvVideoLikecount;

    private ArrayList videos;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().hide();
        if (getIntent().getExtras() == null || getIntent().getExtras().getParcelableArrayList("videos") == null) {
            finish();
            return;
        }
        videos = getIntent().getExtras().getParcelableArrayList("videos");
        index = getIntent().getExtras().getInt("index");
        videoPlayer = findViewById(R.id.video_player);
        videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoPlayer.start();
            }
        });
        ivResumePlay = findViewById(R.id.iv_resume_play);
        tvVideoNickname = findViewById(R.id.tv_video_nickname);
        tvVideoDescription = findViewById(R.id.tv_video_description);
        tvVideoLikecount = findViewById(R.id.tv_video_likecount);
        ivVideoAvatar = findViewById(R.id.iv_video_avatar);
        ivLike = findViewById(R.id.iv_like);
        final AnimatorSet[] ivLikeAnimation = {null};
        GestureDetector gestureDetector = new GestureDetector(VideoPlayerActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (videoPlayer.isPlaying()) {
                    videoPlayer.pause();
                    ivResumePlayAction(true);
                }
                else {
                    videoPlayer.start();
                    ivResumePlayAction(false);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                ivLike.setX(e.getX() - ivLike.getWidth() / 2);
                ivLike.setY(e.getY() - ivLike.getHeight() / 2);

                if (ivLikeAnimation[0] != null) {
                    if (ivLikeAnimation[0].isRunning()) ivLikeAnimation[0].cancel();
                    ivLikeAnimation[0] = null;
                }

                ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ivLike, "scaleX", 0.0f, 1.5f, 1.0f);
                scaleXAnimator.setInterpolator(new DecelerateInterpolator());
                scaleXAnimator.setDuration(256);

                ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ivLike, "scaleY", 0.0f, 1.5f, 1.0f);
                scaleYAnimator.setInterpolator(new DecelerateInterpolator());
                scaleYAnimator.setDuration(256);

                ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(ivLike, "alpha", 0.0f, 0.9f);
                fadeInAnimator.setInterpolator(new DecelerateInterpolator());
                fadeInAnimator.setDuration(256);

                ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(ivLike, "alpha", 0.9f, 0.0f);
                fadeOutAnimator.setInterpolator(new DecelerateInterpolator());
                fadeOutAnimator.setDuration(256);
                fadeOutAnimator.setStartDelay(1024);

                ivLikeAnimation[0] = new AnimatorSet();
                ivLikeAnimation[0].playTogether(scaleXAnimator, scaleYAnimator, fadeInAnimator, fadeOutAnimator);

                ivLikeAnimation[0].start();

                ApiServiceImpl.instance.api.likeVideo(((VideoBean) videos.get(index)).getId()).enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        if (response.isSuccessful()) {
                            tvVideoLikecount.setText(String.valueOf(((Double) response.body().data.get("likecount")).intValue()));
                        }
                        else {
                            Toast.makeText(VideoPlayerActivity.this, "点赞失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        Toast.makeText(VideoPlayerActivity.this, "点赞失败！", Toast.LENGTH_SHORT).show();
                    }
                });

                return super.onDoubleTap(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getY() - e2.getY() > 50) {
                    loadVideo(index + 1);
                }
                if (e2.getY() - e1.getY() > 50) {
                    loadVideo(index - 1);
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        videoPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        loadVideo(index);
    }

    void ivResumePlayAction(boolean visible) {
        if (!visible && ivResumePlay.getAlpha() == 0.0f) return;
        float scaleFrom = visible ? 1.5f : 1.0f, scaleTo = visible ? 1.0f : 1.5f;
        float alphaFrom = visible ? 0.0f : 1.0f, alphaTo = visible ? 1.0f : 0.0f;

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(ivResumePlay, "scaleX", scaleFrom, scaleTo);
        scaleXAnimator.setInterpolator(new DecelerateInterpolator());
        scaleXAnimator.setDuration(256);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(ivResumePlay, "scaleY", scaleFrom, scaleTo);
        scaleYAnimator.setInterpolator(new DecelerateInterpolator());
        scaleYAnimator.setDuration(256);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(ivResumePlay, "alpha", alphaFrom, alphaTo);
        alphaAnimator.setInterpolator(new DecelerateInterpolator());
        alphaAnimator.setDuration(256);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
        animatorSet.start();
    }

    void loadVideo(int index) {
        if (index < 0) {
            Toast.makeText(VideoPlayerActivity.this, "已经是第一条视频了！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (index >= videos.size()) {
            Toast.makeText(VideoPlayerActivity.this, "已经是最后一条视频了！", Toast.LENGTH_SHORT).show();
            return;
        }
        this.index = index;
        VideoBean video;
        video = (VideoBean) videos.get(index);
        videoPlayer.setVideoURI(Uri.parse(video.getFeedurl()));
        videoPlayer.start();
        ivResumePlayAction(false);
        tvVideoNickname.setText(video.getNickname());
        tvVideoDescription.setText(video.getDescription());
        tvVideoLikecount.setText(String.valueOf(video.getLikecount()));
        Glide.with(this).load(video.getAvatar())
                .centerCrop()
                .circleCrop()
                .into(ivVideoAvatar);
        ApiServiceImpl.instance.api.getVideoLikes(video.getId()).enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                if (response.isSuccessful()) {
                    tvVideoLikecount.setText(String.valueOf(((Double) response.body().data.get("likecount")).intValue()));
                }
                else {
                    Toast.makeText(VideoPlayerActivity.this, "获取点赞数失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(VideoPlayerActivity.this, "获取点赞数失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
