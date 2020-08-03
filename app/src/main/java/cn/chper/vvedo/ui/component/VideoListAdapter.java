package cn.chper.vvedo.ui.component;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

import cn.chper.vvedo.R;
import cn.chper.vvedo.bean.VideoBean;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.VideoItemViewHolder> {

    private ArrayList<VideoBean> videos;

    private final VideoClickListener videoClickListener;

    private int viewHolderCount = 0;

    VideoListAdapter(ArrayList<VideoBean> videos, VideoClickListener videoClickListener) {
        this.videos = videos;
        this.videoClickListener = videoClickListener;
    }

    @NonNull
    @Override
    public VideoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.video_list_item, parent, false);
        VideoItemViewHolder videoItemViewHolder = new VideoItemViewHolder(view);
        VideoBean video = videos.get(viewHolderCount);
        Glide.with(view.getContext())
                .load(video.getFeedurl())
                .centerCrop()
                .into(videoItemViewHolder.imgVideo);
        viewHolderCount++;
        return videoItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.videos.size();
    }

    public class VideoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgVideo;

        private ImageView imgVideoPlay;

        public VideoItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgVideo = itemView.findViewById(R.id.img_video);
            imgVideoPlay = itemView.findViewById(R.id.img_video_play);
            imgVideo.setOnClickListener(this);
            imgVideoPlay.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (videoClickListener != null) videoClickListener.onVideoClick(getAdapterPosition());
        }

    }

    public interface VideoClickListener {
        void onVideoClick(int clickedItemIndex);
    }

}
