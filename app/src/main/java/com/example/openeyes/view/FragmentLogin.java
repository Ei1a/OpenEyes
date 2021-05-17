package com.example.openeyes.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.openeyes.R;
import com.example.openeyes.bean.LoginEvent;

import org.greenrobot.eventbus.EventBus;

public class FragmentLogin extends Fragment {

    private EditText editTextCount;
    private EditText editTextPassword;
    private TextView buttonSignIn;
    private ConstraintLayout constraintLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        initListener();
        return view;
    }

    /*
     * 初始化View
     */
    private void initView(View root){
        /*
         * 得到View引用
         */
        editTextCount = (EditText) root.findViewById(R.id.edit_count_sign_in);
        editTextPassword = (EditText) root.findViewById(R.id.edit_password_sign_in);
        buttonSignIn = (TextView) root.findViewById(R.id.button_sign_in);
        constraintLayout = (ConstraintLayout) root.findViewById(R.id.constraint_layout_sign_in);
    }

    /*
     * 初始化监听器
     */
    private void initListener(){
        /*
         * 监听点击空白处
         */
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                constraintLayout.requestFocus();
                return false;
            }
        });

        /*
         * 监听输入框焦点改变
         */
        View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //收起软键盘
                if(!hasFocus){
                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(manager != null){
                        manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        };
        editTextCount.setOnFocusChangeListener(listener);
        editTextPassword.setOnFocusChangeListener(listener);

        /*
         * 监听登陆按钮点击
         */
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LoginEvent(true));
                getActivity().finish();
            }
        });
    }
}
