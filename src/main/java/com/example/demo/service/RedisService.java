package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.example.demo.pojo.TagChanges;
import com.example.demo.util.TagNameComparator;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate           stringRedisTemplate;



    /**
     * 
     * add a new tag
     *
     * @param key
     *            is tagName
     * @param value
     *            is tagChanges
     */
    public void add(String key, Object value) {
        ValueOperations<Object, Object> ops = this.redisTemplate.opsForValue();
        boolean bExistent = this.stringRedisTemplate.hasKey(key);
        if (bExistent) {
            System.out.println("this key is already existed!");
        } else {
            ops.set(key, value);
        }
    }



    public void update(String key, Object value) {
        ValueOperations<Object, Object> ops = this.redisTemplate.opsForValue();
        boolean bExistent = this.stringRedisTemplate.hasKey(key);
        if (bExistent) {
            ops.getAndSet(key, value);
        } else {
            System.out.println("this key is not existed!");
        }
    }



    /**
     * 
     * get tagChanges by tagName
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }



    /**
     * 
     * get multi tagChanges by tagNames
     *
     * @param keys
     * @return
     */
    public List<Object> get(Set<Object> keys) {
        return this.redisTemplate.opsForValue().multiGet(keys);
    }



    /**
     * 
     * return all tagNames
     *
     * @return
     */
    public Set<Object> getAllKeys() {
        return redisTemplate.keys("*");
        // return this.jedisPool.getResource().keys("*");
    }



    /**
     * 
     * delete a tag by tagName
     *
     * @param key
     */
    public void del(String key) {
        this.redisTemplate.delete(key);
    }



    /**
     * 
     * search Keys by condition
     *
     * @return
     */
    public Set<Object> seachKeys(String tagNameprefix) {
        return redisTemplate.keys(tagNameprefix + "*");
    }



    public String doCalculate(Map<String, Object> models) {
        String targetTag = (String) models.get("targetTag");
        List<String> sourceTags = (List<String>) models.get("sourceTags");
        if (targetTag == null || targetTag.trim().isEmpty()) {
            return null;
        }
        if (sourceTags == null || sourceTags.isEmpty()) {
            return null;
        }
        TagChanges target = (TagChanges) get(targetTag);
        if (target == null) {
            return null;
        }
        List<Object> sources = get(new HashSet<>(sourceTags));
        List<String> enhTagNames = new ArrayList<>();
        List<String> hunterTagNames = new ArrayList<>();
        List<String> bobaTagNames = new ArrayList<>();
        List<String> pwTagNames = new ArrayList<>();
        List<String> vTagNames = new ArrayList<>();
        for (Object object : sources) {
            TagChanges source = (TagChanges) object;
            if (isNullOrEmpty(source.getEnhTag()) || isNullOrEmpty(source.getHunterTag())
                    || isNullOrEmpty(source.getBobaTag()) || isNullOrEmpty(source.getPwTag())
                    || isNullOrEmpty(source.getvTag())) {
                return null;
            }
            if (!source.isValid()) {
                continue;
            }
            enhTagNames.add(source.getEnhTag());
            hunterTagNames.add(source.getHunterTag());
            bobaTagNames.add(source.getBobaTag());
            pwTagNames.add(source.getPwTag());
            vTagNames.add(source.getvTag());
        }
        TagNameComparator comparator = new TagNameComparator();
        Optional<String> optionalEnhTag = enhTagNames.stream().sorted((t1, t2) -> comparator.compare(t2, t1))
                .findFirst();
        String endEnhTag = optionalEnhTag.isPresent() ? optionalEnhTag.get() : null;
        Optional<String> optionalHunterTag = hunterTagNames.stream().sorted((t1, t2) -> comparator.compare(t2, t1)).findFirst();
        String endHunterTag = optionalHunterTag.isPresent() ? optionalHunterTag.get() : null;
        Optional<String> optionalBobaTag = bobaTagNames.stream().sorted((t1, t2) -> comparator.compare(t2, t1)).findFirst();
        String endbobaTag = optionalBobaTag.isPresent() ? optionalBobaTag.get() : null;
        Optional<String> optionalPwTag = pwTagNames.stream().sorted((t1, t2) -> comparator.compare(t2, t1)).findFirst();
        String endPwTag = optionalPwTag.isPresent() ? optionalPwTag.get() : null;
        Optional<String> optionalVTag = vTagNames.stream().sorted((t1, t2) -> comparator.compare(t2, t1)).findFirst();
        String endVTag = optionalVTag.isPresent() ? optionalVTag.get() : null;

        String startEnhTag = target.getEnhTag();
        String startHunterTag = target.getHunterTag();
        String startBobaTag = target.getBobaTag();
        String startPwTag = target.getPwTag();
        String startVTag = target.getvTag();

        List<Object> mergedTags = new ArrayList<>();
        List<Object> newEndTags = new ArrayList<>();

        if (target.getTagName().equals(startEnhTag)) {
            mergedTags.add(startEnhTag);
            newEndTags.add(startEnhTag);
            List<Object> hunterTags = seachKeys("tag-b").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> bobaTags = seachKeys("tag-f").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> pwtags = seachKeys("tag-p").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> vTags = seachKeys("tag-v").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            calculateMergedTags(endHunterTag, startHunterTag, mergedTags, newEndTags, hunterTags);
            calculateMergedTags(endbobaTag, startBobaTag, mergedTags, newEndTags, bobaTags);
            calculateMergedTags(endPwTag, startPwTag, mergedTags, newEndTags, pwtags);
            calculateMergedTags(endVTag, startVTag, mergedTags, newEndTags, vTags);
        } else if (target.getTagName().equals(startHunterTag)) {
            mergedTags.add(startHunterTag);
            List<Object> enhTags = seachKeys("tag-e").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> bobaTags = seachKeys("tag-f").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> pwtags = seachKeys("tag-p").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> vTags = seachKeys("tag-v").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            calculateMergedTags(endEnhTag, startEnhTag, mergedTags, newEndTags, enhTags);
            calculateMergedTags(endbobaTag, startBobaTag, mergedTags, newEndTags, bobaTags);
            calculateMergedTags(endPwTag, startPwTag, mergedTags, newEndTags, pwtags);
            calculateMergedTags(endVTag, startVTag, mergedTags, newEndTags, vTags);

        } else if (target.getTagName().equals(startBobaTag)) {
            mergedTags.add(startBobaTag);
            List<Object> enhTags = seachKeys("tag-e").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> hunterTags = seachKeys("tag-b").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> pwtags = seachKeys("tag-p").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> vTags = seachKeys("tag-v").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            calculateMergedTags(endEnhTag, startEnhTag, mergedTags, newEndTags, enhTags);
            calculateMergedTags(endHunterTag, startHunterTag, mergedTags, newEndTags, hunterTags);
            calculateMergedTags(endPwTag, startPwTag, mergedTags, newEndTags, pwtags);
            calculateMergedTags(endVTag, startVTag, mergedTags, newEndTags, vTags);

        } else if (target.getTagName().equals(startPwTag)) {
            mergedTags.add(startPwTag);
            List<Object> enhTags = seachKeys("tag-e").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> hunterTags = seachKeys("tag-b").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> bobaTags = seachKeys("tag-f").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> vTags = seachKeys("tag-v").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            calculateMergedTags(endEnhTag, startEnhTag, mergedTags, newEndTags, enhTags);
            calculateMergedTags(endHunterTag, startHunterTag, mergedTags, newEndTags, hunterTags);
            calculateMergedTags(endbobaTag, startBobaTag, mergedTags, newEndTags, bobaTags);
            calculateMergedTags(endVTag, startVTag, mergedTags, newEndTags, vTags);
        } else if (target.getTagName().equals(startVTag)) {
            mergedTags.add(startVTag);
            List<Object> enhTags = seachKeys("tag-e").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> hunterTags = seachKeys("tag-b").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> bobaTags = seachKeys("tag-f").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            List<Object> pwtags = seachKeys("tag-p").stream()
                    .sorted((t1, t2) -> (comparator.compare((String) t1, (String) t2))).collect(Collectors.toList());
            calculateMergedTags(endEnhTag, startEnhTag, mergedTags, newEndTags, enhTags);
            calculateMergedTags(endHunterTag, startHunterTag, mergedTags, newEndTags, hunterTags);
            calculateMergedTags(endbobaTag, startBobaTag, mergedTags, newEndTags, bobaTags);
            calculateMergedTags(endPwTag, startPwTag, mergedTags, newEndTags, pwtags);
        }
        Map<String, List<Object>> result = new HashMap<>();
        result.put("mergedTags", mergedTags);
        result.put("newEndTags", newEndTags);
        return JSON.toJSONString(result);
    }



    private void calculateMergedTags(String endTag, String startTag, List<Object> mergedTags, List<Object> newEndTags,
            List<Object> tagsOfeachTeam) {
        int h1 = tagsOfeachTeam.indexOf(startTag);
        if (h1 == -1) {
            startTag = endTag;
            h1 = tagsOfeachTeam.indexOf(startTag);
        }
        int h2 = tagsOfeachTeam.indexOf(endTag);
        if (h1 < h2) {
            mergedTags.addAll(tagsOfeachTeam.subList(h1 + 1, h2 + 1));
            newEndTags.add(endTag);
        } else {
            newEndTags.add(startTag);
        }
    }



    public boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;

    }
}
