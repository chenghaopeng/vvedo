package cn.chper.vvedo.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import cn.chper.vvedo.R;
import cn.chper.vvedo.api.ApiService;
import cn.chper.vvedo.api.ApiServiceImpl;
import cn.chper.vvedo.api.SimpleResponse;
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
                        if (response.isSuccessful()) {
                            ApiServiceImpl.instance.token = (String) response.body().data.get("token");
                            ApiServiceImpl.instance.username = username;
                            Toast.makeText(getContext(), "注册/登录成功，欢迎" + username + "！", Toast.LENGTH_SHORT).show();
                            edt_username.setVisibility(View.INVISIBLE);
                            edt_password.setVisibility(View.INVISIBLE);
                            btn_login.setVisibility(View.INVISIBLE);
                            txt_username.setVisibility(View.VISIBLE);
                            txt_username.setText(username);
                        }
                        else {
                            Toast.makeText(getContext(), "请求失败！", Toast.LENGTH_SHORT).show();
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
            edt_username.setVisibility(View.INVISIBLE);
            edt_password.setVisibility(View.INVISIBLE);
            btn_login.setVisibility(View.INVISIBLE);
            txt_username.setVisibility(View.VISIBLE);
            txt_username.setText(ApiServiceImpl.instance.username);
        }
        return root;
    }
}