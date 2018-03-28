package com.example.demo.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class TagNameComparator implements Comparator<String> {

    @Override
    public int compare(String pO1, String pO2) {
        if (isNullOrEmpty(pO1) && isNullOrEmpty(pO2)) {
            return 0;
        }
        if (!isNullOrEmpty(pO1) && isNullOrEmpty(pO2)) {
            return 1;
        } else if (isNullOrEmpty(pO1) && !isNullOrEmpty(pO2)) {
            return -1;
        }
        List<Integer> o1 = getIntvalueOfTagname(pO1);
        List<Integer> o2 = getIntvalueOfTagname(pO2);
        int length = o1.size() > o2.size() ? o2.size() : o1.size();
        for (int i = 0; i < length; i++) {
            int result = o1.get(i).compareTo(o2.get(i));
            if (result != 0) {
                return result;
            }
        }
        Integer l1 = o1.size();
        Integer l2 = o2.size();
        return l1.compareTo(l2);
    }



    private List<Integer> getIntvalueOfTagname(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        String[] arr = str.split("\\.");
        List<Integer> r = new ArrayList<>();
        for (String s : arr) {
            String temp = p.matcher(s).replaceAll("").trim();
            r.add(Integer.valueOf(temp));
        }
        return r;
    }



    public boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;

    }
}
