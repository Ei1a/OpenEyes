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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.openeyes.R;
import com.example.openeyes.bean.PersonalCount;
import com.example.openeyes.bean.RegisterEvent;
import com.example.openeyes.model.PersonalCountDao;
import com.example.openeyes.model.VideoDatabase;

import org.greenrobot.eventbus.EventBus;

public class FragmentRegister extends Fragment {

    private EditText editTextCount;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private TextView buttonSignUp;
    private ConstraintLayout constraintLayout;

    private PersonalCountDao dao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initView(view);
        intData();
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
        editTextCount = (EditText) root.findViewById(R.id.edit_count_sign_up);
        editTextPassword = (EditText) root.findViewById(R.id.edit_password_sign_up);
        editTextConfirmPassword = (EditText) root.findViewById(R.id.edit_confirm_password_sign_up);
        buttonSignUp = (TextView) root.findViewById(R.id.button_sign_up);
        constraintLayout = (ConstraintLayout) root.findViewById(R.id.constraint_layout_sign_up);
    }

    /*
     * 初始化数据
     */
    private void intData(){
        dao = VideoDatabase.getInstance(getActivity().getApplicationContext()).getPersonalCountDao();
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
        editTextConfirmPassword.setOnFocusChangeListener(listener);

        /*
         * 监听注册按钮
         */
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = editTextCount.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordConfirm = editTextConfirmPassword.getText().toString();

                if(count.equals("")){
                    //未输入账号
                    Toast.makeText(getContext(), "请输入账号", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    //未输入密码
                    Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                }else if(passwordConfirm.equals("")){
                    //未确认密码
                    Toast.makeText(getContext(), "请确认密码", Toast.LENGTH_SHORT).show();
                }else{
                    //输入完整, 检测两次密码输入是否一致
                    if(password.equals(passwordConfirm)){
                        //一致，判断账号是否以注册过
                        if(dao.queryCount(count) == null){
                            //未注册，导入数据库
                            dao.insert(new PersonalCount(count, password, 0));
                            EventBus.getDefault().post(new RegisterEvent(count, password));
                        }else{
                            //已注册
                            Toast.makeText(getContext(), "账号已注册", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //不一致
                        Toast.makeText(getContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
