package com.yyp.sidebar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.yyp.sidebar.adapter.MainAdapter;
import com.yyp.sidebar.model.User;
import com.yyp.sidebar.widget.SideBar;

import java.util.ArrayList;

public class MainActivity extends Activity {

    SideBar main_sidebar;
    TextView select_letter;

    RecyclerView main_data_list;
    MainAdapter adapter;
    ArrayList<User> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    public void initView(){
        main_sidebar = findViewById(R.id.main_sidebar);
        select_letter = findViewById(R.id.main_show_select_letter);
        main_data_list = findViewById(R.id.main_data_list);

        setListen();

        main_data_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(this);
        main_data_list.setAdapter(adapter);

        loadData();
    }

    public void setListen(){
        main_sidebar.setTextDialog(select_letter);
        main_sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 处理选中的字母
            }
        });
    }

    /**
     * 加载数据
     */
    public void loadData(){
        for (int i=0;i<30;i++){
            User user;
            if(i<5){
                user = new User("李", "李"+i);
            }else if(i<20){
                user = new User("王", "王"+i);
            }else{
                user = new User("杨", "杨"+i);
            }
            data.add(user);
        }

        adapter.refresh(data);
    }
}
