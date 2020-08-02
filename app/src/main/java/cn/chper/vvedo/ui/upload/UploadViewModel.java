package cn.chper.vvedo.ui.upload;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UploadViewModel extends ViewModel {

    private static MutableLiveData<Uri> mUri = new MutableLiveData<>();

    public static LiveData<Uri> getUri() {
        return mUri;
    }

    public static void setUri(Uri uri) { mUri.setValue(uri); }

}