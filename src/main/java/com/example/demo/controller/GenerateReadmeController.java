package com.example.demo.controller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.pojo.TagChanges;
import com.example.demo.service.RedisService;
import com.example.demo.util.ReadAndWriteFileUtil;
import com.example.demo.util.TagChangesComparator;
import com.example.demo.util.TagNameComparator;

@Controller
public class GenerateReadmeController {

    @Autowired
    private RedisService         redisService;
    @Autowired
    private ReadAndWriteFileUtil fileUtil;



    @GetMapping("/newTag")
    public String newTagForm(Model model) {
        model.addAttribute("newTag", new TagChanges());
        List<Object> enhTags = redisService.seachKeys("tag-e").stream()
                .sorted((t1, t2) -> (((String) t2).compareTo((String) t1))).collect(Collectors.toList());
        List<Object> hunterTags = redisService.seachKeys("tag-b").stream()
                .sorted((t1, t2) -> (((String) t2).compareTo((String) t1))).collect(Collectors.toList());
        List<Object> bobaTags = redisService.seachKeys("tag-f").stream()
                .sorted((t1, t2) -> (((String) t2).compareTo((String) t1))).collect(Collectors.toList());
        List<Object> pwtags = redisService.seachKeys("tag-p").stream()
                .sorted((t1, t2) -> (((String) t2).compareTo((String) t1))).collect(Collectors.toList());
        List<Object> vTags = redisService.seachKeys("tag-v").stream()
                .sorted((t1, t2) -> (((String) t2).compareTo((String) t1))).collect(Collectors.toList());
        model.addAttribute("enhTags", enhTags);
        model.addAttribute("hunterTags", hunterTags);
        model.addAttribute("bobaTags", bobaTags);
        model.addAttribute("pwTags", pwtags);
        model.addAttribute("vTags", vTags);
        return "newForm";
    }



    @PostMapping("/newTag")
    public String doAdd(@Valid TagChanges newTag, BindingResult result, Model model) {
        // 回填form表单
        model.addAttribute("newTag", newTag);
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                System.out.println(error.getDefaultMessage());
            }
            return "newForm";
        }
        redisService.add(newTag.getTagName(), newTag);
        return "newForm";
    }



    @GetMapping("/tagMap")
    public String getTagsByTeam(Model model) {
        List<TagChanges> enhTags = getAndSortTags("tag-e");
        List<TagChanges> hunterTags = getAndSortTags("tag-b");
        List<TagChanges> bobaTags = getAndSortTags("tag-f");
        List<TagChanges> pwTags = getAndSortTags("tag-p");
        List<TagChanges> vTags = getAndSortTags("tag-v");

        model.addAttribute("enhTags", enhTags);
        model.addAttribute("hunterTags", hunterTags);
        model.addAttribute("bobaTags", bobaTags);
        model.addAttribute("pwTags", pwTags);
        model.addAttribute("vTags", vTags);
        model.addAttribute("newTag", new TagChanges());
        return "tagMap";

    }



    private List<TagChanges> getAndSortTags(String tagName) {
        List<Object> tags = redisService.get(redisService.seachKeys(tagName));
        List<TagChanges> enhTags = (List) tags;
        // Collections.sort(enhTags,
        // Comparator.comparing(TagChanges::getTagName));
        Collections.sort(enhTags, new TagChangesComparator());
        return enhTags;
    }



    @PostMapping("/generateReadme")
    public String generateReadme(HttpServletResponse response, @ModelAttribute TagChanges newTag, Model model) {
        final String inputTagNames = newTag.getTagName().trim();
        if (inputTagNames == null || inputTagNames.isEmpty()) {
            model.addAttribute("newTag", new TagChanges());
            return "newForm";
        }
        Set<String> names = Stream.of(inputTagNames.split(",")).collect(Collectors.toSet());
        Set<Object> set = new HashSet<Object>();
        set.addAll(names);
        List<Object> tagChanges = redisService.get(set);
        TagNameComparator comparator = new TagNameComparator();
        List<Object> sortedTagChanges = tagChanges.stream().sorted(
                (t1, t2) -> (comparator.compare(((TagChanges) t1).getTagName(), ((TagChanges) t2).getTagName())))
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (Object object : sortedTagChanges) {
            TagChanges tag = (TagChanges) object;
            if (tag == null) {
                continue;
            }
            sb.append("=================TagName: " + tag.getTagName() + "\r\n" + "\r\n");
            if (tag.getDbChanges() != null && !tag.getDbChanges().isEmpty()) {
                sb.append("DBchanges: \r\n" + tag.getDbChanges() + "\r\n" + "\r\n");
            }
            if (tag.getWcsChanges() != null && !tag.getWcsChanges().isEmpty()) {
                sb.append("WCSchanges: \r\n" + tag.getWcsChanges() + "\r\n" + "\r\n");
            }
            if (tag.getEndecaChanges() != null && !tag.getEndecaChanges().isEmpty()) {
                sb.append("EndecaChanges: \r\n" + tag.getEndecaChanges() + "\r\n" + "\r\n");
            }
            if (tag.getEnvClientChanges() != null && !tag.getEnvClientChanges().isEmpty()) {
                sb.append("EnvClientChanges: \r\n" + tag.getEnvClientChanges() + "\r\n" + "\r\n");
            }
            if (tag.getContentKeyChanges() != null && !tag.getContentKeyChanges().isEmpty()) {
                sb.append("ContentKeyChanges: \r\n" + tag.getContentKeyChanges() + "\r\n" + "\r\n");
            }
            sb.append("Based on tags: \r\n" + tag.getEnhTag() + " " + tag.getHunterTag() + " " + tag.getBobaTag() + " "
                    + tag.getPwTag() + " " + tag.getvTag() + "\r\n" + "\r\n");
            if (tag.getComments() != null && !tag.getComments().isEmpty()) {
                sb.append("Comments: \r\n" + tag.getComments() + "\r\n" + "\r\n");
            }
        }
        fileUtil.downloadFileFromString(response, sb.toString());
        return null;
    }



    @PostMapping("/updateTag")
    public String updateTag(@ModelAttribute TagChanges updateTag, Model model) {
        redisService.update(updateTag.getTagName(), updateTag);
        // 回填form表单
        model.addAttribute("updateTag", updateTag);
        return "newForm";
    }

}
