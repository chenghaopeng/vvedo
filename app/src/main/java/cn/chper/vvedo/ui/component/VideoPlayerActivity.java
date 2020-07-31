package cn.chper.vvedo.ui.component;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import cn.chper.vvedo.R;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoPlayer;

    private Bundle video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (getIntent().getExtras() == null || getIntent().getExtras().getBundle("video") == null) {
            finish();
            return;
        }
        video = getIntent().getExtras().getBundle("video");
        videoPlayer = findViewById(R.id.video_player);
        videoPlayer.setVideoURI(Uri.parse(video.getString("feedurl")));
        videoPlayer.start();
    }

}
