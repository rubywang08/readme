package com.example.demo.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.example.demo.pojo.TagChanges;
import com.example.demo.service.RedisService;

@RestController
public class SearchTagsController {

    @Autowired
    private RedisService redisService;



    @PostMapping("/getTag")
    public String getTag(@RequestParam(value = "tagName") String tagName) {
        TagChanges tagChanges = (TagChanges) redisService.get(tagName);
        return JSON.toJSONString(tagChanges);
    }



    @PostMapping("/deleteTag")
    public Boolean deleteTag(@RequestParam(value = "tagName") String tagName) {
        redisService.del(tagName);
        return true;
    }



    @GetMapping("/allTags")
    public Set<Object> getAllTagNames() {
        return redisService.getAllKeys();
    }



    @PostMapping("/calculateMergedTags")
    public String calculateMergedTags(@RequestBody Map<String, Object> models) {
        System.out.println(models);
        return redisService.doCalculate(models);
    }

}
