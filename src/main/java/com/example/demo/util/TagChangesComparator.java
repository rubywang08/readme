package com.example.demo.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import com.example.demo.pojo.TagChanges;

public class TagChangesComparator implements Comparator<TagChanges> {
    @Override
    public int compare(TagChanges pO1, TagChanges pO2) {

        if (pO1 == null && pO2 == null) {
            return 0;
        }
        if (pO1 != null && pO2 == null) {
            return 1;
        } else if (pO1 == null && pO2 != null) {
            return -1;
        }
        if (isNullOrEmpty(pO1.getTagName()) && isNullOrEmpty(pO2.getTagName())) {
            return 0;
        }
        if (!isNullOrEmpty(pO1.getTagName()) && isNullOrEmpty(pO2.getTagName())) {
            return 1;
        } else if (isNullOrEmpty(pO1.getTagName()) && !isNullOrEmpty(pO2.getTagName())) {
            return -1;
        }
        List<Integer> o1 = getIntvalueOfTagname(pO1.getTagName());
        List<Integer> o2 = getIntvalueOfTagname(pO2.getTagName());
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
