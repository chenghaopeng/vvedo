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
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.net.URI;

import cn.chper.vvedo.R;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoPlayer;

    private ImageView ivResumePlay;

    private TextView tvVideoNickname;

    private TextView tvVideoDescription;

    private ImageView ivVideoAvatar;

    private ImageView ivLike;

    private TextView tvVideoLikecount;

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
        tvVideoNickname = findViewById(R.id.tv_video_nickname);
        tvVideoNickname.setText(video.getString("nickname"));
        tvVideoDescription = findViewById(R.id.tv_video_description);
        tvVideoDescription.setText(video.getString("description"));
        tvVideoLikecount = findViewById(R.id.tv_video_likecount);
        tvVideoLikecount.setText(String.valueOf(video.getInt("likecount")));
        ivVideoAvatar = findViewById(R.id.iv_video_avatar);
        Glide.with(this).load(video.getString("avatar"))
                .centerCrop()
                .circleCrop()
                .into(ivVideoAvatar);
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

                return super.onDoubleTap(e);
            }
        });
        videoPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    void ivResumePlayAction(boolean visible) {
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

}
