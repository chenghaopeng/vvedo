package cn.chper.vvedo.ui.upload;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.iceteck.silicompressorr.SiliCompressor;

import java.io.File;
import java.util.HashMap;

import cn.chper.vvedo.R;
import cn.chper.vvedo.api.ApiServiceImpl;
import cn.chper.vvedo.api.SimpleResponse;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment {

    private static final int REQUEST_VIDEO_CAPTURE = 100;

    private VideoView vvRecord;

    private Button btnRecord;

    private Button btnUpload;

    private UploadViewModel uploadViewModel;

    private boolean isUploading = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        uploadViewModel =
                ViewModelProviders.of(this).get(UploadViewModel.class);
        View root = inflater.inflate(R.layout.fragment_upload, container, false);
        vvRecord = root.findViewById(R.id.vv_record);
        btnRecord = root.findViewById(R.id.btn_record);
        btnUpload = root.findViewById(R.id.btn_upload);
        vvRecord.setOnCompletionListener(view -> {
            vvRecord.start();
        });
        btnRecord.setOnClickListener(view -> {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        });
        btnUpload.setOnClickListener(view -> {
            if (ApiServiceImpl.instance.token == null || ApiServiceImpl.instance.token.isEmpty()) {
                Toast.makeText(getContext(), "请先登录！", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = uploadViewModel.getUri().getValue();
            if (uri == null || uri.equals(Uri.EMPTY)) {
                Toast.makeText(getContext(), "请先录制一个视频！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isUploading) {
                Toast.makeText(getContext(), "正在上传，请稍候...", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] filePathColumn = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            cursor.close();
            Toast.makeText(getContext(), "开始压缩并上传...", Toast.LENGTH_SHORT).show();
            new Thread(() -> {
                isUploading = true;
                String compressed = null;
                try {
                    compressed = SiliCompressor.with(getContext()).compressVideo(path, path.substring(0, path.lastIndexOf("/")));
                }
                catch (Exception e) {
                    isUploading = false;
                    return;
                }
                HashMap<String, RequestBody> map = new HashMap<>();
                File video = new File(compressed);
                map.put("video" + video.getName() + "\"", RequestBody.create(MediaType.parse("video/mpeg4"), video));
                map.put("token", RequestBody.create(MediaType.parse("text/plain"), ApiServiceImpl.instance.token));
                ApiServiceImpl.instance.api.uploadVideo(map).enqueue(new Callback<SimpleResponse>() {
                    @Override
                    public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                        isUploading = false;
                        if (response.isSuccessful()) {
                            uploadViewModel.setUri(Uri.EMPTY);
                            Toast.makeText(getContext(), "上传成功！", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), "上传失败！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SimpleResponse> call, Throwable t) {
                        isUploading = false;
                        Toast.makeText(getContext(), "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
        uploadViewModel.getUri().observe(this, uri -> {
            if (uri == null || uri.equals(Uri.EMPTY)) {
                vvRecord.pause();
            }
            else{
                vvRecord.setVideoURI(uri);
                vvRecord.start();
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            uploadViewModel.setUri(videoUri);
        }
    }
}