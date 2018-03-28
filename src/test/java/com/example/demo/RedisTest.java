package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.example.demo.pojo.TagChanges;
import com.example.demo.service.RedisService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisService redisService;



    @Before
    public void setUp() {

    }



    @Test
    public void get() {
        TagChanges tagchanges = new TagChanges();
        tagchanges.setTagName("test1");
        tagchanges.setDbChanges("323333");
        redisService.add(tagchanges.getTagName(), tagchanges);

        TagChanges tag = (TagChanges) redisService.get("test1");
        Assert.assertNotNull(tag);
    }



    @Test
    public void getAllKeys() {
        Set<Object> ss = redisService.getAllKeys();
        for (Object string : ss) {
            System.out.println(string);
        }
    }



    @Test
    public void getMulti() {
        Set<Object> keys = new HashSet<Object>();
        keys.add("test1");
        List<Object> ss = redisService.get(keys);
        for (Object string : ss) {
            System.out.println(string);
        }
    }



    @Test
    public void testDoCalculate() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("targetTag", "tag-e3.2.35");
        List<String> list = new ArrayList<String>();
        list.add("tag-v3.4.9.2");
        map.put("sourceTags", list);
        System.out.println(redisService.doCalculate(map));
    }

}
