package cn.chper.vvedo.ui.component;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import cn.chper.vvedo.R;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoPlayer;

    private ImageView ivResumePlay;

    private Bundle video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        getSupportActionBar().hide();
        if (getIntent().getExtras() == null || getIntent().getExtras().getBundle("video") == null) {
            finish();
            return;
        }
        video = getIntent().getExtras().getBundle("video");
        videoPlayer = findViewById(R.id.video_player);
        videoPlayer.setVideoURI(Uri.parse(video.getString("feedurl")));
        videoPlayer.start();
        videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoPlayer.start();
            }
        });
        ivResumePlay = findViewById(R.id.iv_resume_play);
        videoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoPlayer.isPlaying()) {
                    videoPlayer.pause();
                    ivResumePlayAction(true);
                }
                else {
                    videoPlayer.start();
                    ivResumePlayAction(false);
                }
            }
        });
    }

    void ivResumePlayAction(boolean visiable) {
        float scaleFrom = visiable ? 1.5f : 1.0f, scaleTo = visiable ? 1.0f : 1.5f;
        float alphaFrom = visiable ? 0.0f : 1.0f, alphaTo = visiable ? 1.0f : 0.0f;

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

}
