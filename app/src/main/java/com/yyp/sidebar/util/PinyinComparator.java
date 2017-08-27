package com.yyp.sidebar.util;

import com.yyp.sidebar.model.User;

import java.util.Comparator;

/**
 * 根据ABCDEFG...对数据排序
 */
public class PinyinComparator implements Comparator<User> {

    public int compare(User o1, User o2) {
        // 升序排序
        if (o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}


