package com.yyp.sidebar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yyp.sidebar.adapter.MainAdapter;
import com.yyp.sidebar.model.User;
import com.yyp.sidebar.util.CharacterParser;
import com.yyp.sidebar.util.PinyinComparator;
import com.yyp.sidebar.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {

    SideBar main_sidebar;
    TextView select_letter;
    EditText search_text;

    RecyclerView main_data_list;
    MainAdapter adapter;

    // 汉字转换成拼音的类
    private CharacterParser characterParser;
    // 根据拼音来排列列表里面的数据类
    private PinyinComparator pinyinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    public void initView(){
        search_text = findViewById(R.id.search_edit_text);
        main_sidebar = findViewById(R.id.main_sidebar);
        select_letter = findViewById(R.id.main_show_select_letter);
        main_data_list = findViewById(R.id.main_data_list);

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        setListen();
        // 初始化 recycleView、adapter
        main_data_list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(this);
        main_data_list.setAdapter(adapter);
        // 加载数据并进行监听
        adapter.refresh(loadData());
        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String name) {
                Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setListen(){
        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                // 过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        main_sidebar.setTextDialog(select_letter);
        main_sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 处理选中的字母
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    main_data_list.smoothScrollToPosition(position);
                }
            }
        });
    }

    /**
     * 加载排序后的数据
     */
    public ArrayList<User> loadData(){
        ArrayList<User> mSortList = new ArrayList<>();

        for(int i=0; i<30; i++){
            User sortModel = new User();
            String name;
            switch (i%12){
                case 0:
                    name = "安静";
                    break;
                case 1:
                    name = "爸";
                    break;
                case 2:
                    name = "Linda";
                    break;
                case 3:
                    name = "赵小宇";
                    break;
                case 4:
                    name = "郑辉";
                    break;
                case 5:
                    name = "汤小米";
                    break;
                case 6:
                    name = "王安";
                    break;
                case 7:
                    name = "李亮";
                    break;
                case 8:
                    name = "黄辉";
                    break;
                default:
                    name = "贾亮";
                    break;
            }

            sortModel.setName(name);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(name);
            // 首字母转为大写
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是大写英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }

        // 根据a-z进行排序源数据
        Collections.sort(mSortList, pinyinComparator);

        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新列表
     * @param filterStr
     */
    private void filterData(String filterStr) {
        ArrayList<User> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = loadData();
        } else {
            filterDateList.clear();
            for (User sortModel : loadData()) {
                String name = sortModel.getName();
                /* 搜索关键字类型
                   1、汉字：数据中包含该关键字，就显示
                   2、字母：数据的拼音以该关键字开始，就显示
                */
                if (name.toUpperCase().contains(filterStr.toUpperCase())
                        || characterParser.getSelling(name).toUpperCase()
                        .startsWith(filterStr.toUpperCase())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.refresh(filterDateList);
    }
}
