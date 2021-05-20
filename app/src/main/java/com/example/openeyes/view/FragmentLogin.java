package com.example.openeyes.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.example.openeyes.bean.LoginEvent;
import com.example.openeyes.bean.PersonalCount;
import com.example.openeyes.bean.RegisterEvent;
import com.example.openeyes.model.PersonalCountDao;
import com.example.openeyes.model.VideoDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FragmentLogin extends Fragment {

    private EditText editTextCount;
    private EditText editTextPassword;
    private TextView buttonSignIn;
    private ConstraintLayout constraintLayout;

    private PersonalCountDao dao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    /*
     * 初始化View
     */
    private void initView(View root){
        //注册EventBus
        EventBus.getDefault().register(this);
        /*
         * 得到View引用
         */
        editTextCount = (EditText) root.findViewById(R.id.edit_count_sign_in);
        editTextPassword = (EditText) root.findViewById(R.id.edit_password_sign_in);
        buttonSignIn = (TextView) root.findViewById(R.id.button_sign_in);
        constraintLayout = (ConstraintLayout) root.findViewById(R.id.constraint_layout_sign_in);
    }

    /*
     * 初始化数据
     */

    private void initData(){
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

        /*
         * 监听登陆按钮点击
         */
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String count = editTextCount.getText().toString();
                String password = editTextPassword.getText().toString();

                //检查格式
                if(count.equals("")){
                    //未输入账号
                    Toast.makeText(getContext(), "请输入账号", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")) {
                    //未输入密码
                    Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                }else{
                    //查询账号是否存在
                    if(dao.queryCount(count) == null){
                        //不存在
                        Toast.makeText(getContext(), "账号不存在", Toast.LENGTH_SHORT).show();
                    }else{
                        //存在，查询密码是否正确
                        if(dao.confirmPassword(count, password) == null){
                            //不正确
                            Toast.makeText(getContext(), "密码不正确", Toast.LENGTH_SHORT).show();
                        }else{
                            //正确
                            //更新最近登陆状态
                            dao.insert(new PersonalCount(count, password, 1));
                            //通知已登录
                            EventBus.getDefault().post(new LoginEvent(true, count, password));
                            getActivity().finish();
                        }
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterEvent(RegisterEvent event){
        editTextCount.setText(event.getCount());
        editTextPassword.setText(event.getPassword());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
