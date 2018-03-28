package com.example.demo.pojo;

import org.hibernate.validator.constraints.NotEmpty;

public class TagChanges {
    @NotEmpty(message = "Tag Name is required")
    private String  tagName;
    private String  dbChanges;
    private String  wcsChanges;
    private String  endecaChanges;
    private String  envClientChanges;
    private String  contentKeyChanges;
    private String  comments;
    private String  enhTag;
    private String  hunterTag;
    private String  bobaTag;
    private String  pwTag;
    private String  vTag;
    private Boolean valid;



    public String getTagName() {
        return tagName;
    }



    public void setTagName(String tagName) {
        this.tagName = tagName;
    }



    public String getDbChanges() {
        return dbChanges;
    }



    public void setDbChanges(String dbChanges) {
        this.dbChanges = dbChanges;
    }



    public String getWcsChanges() {
        return wcsChanges;
    }



    public void setWcsChanges(String wcsChanges) {
        this.wcsChanges = wcsChanges;
    }



    public String getEndecaChanges() {
        return endecaChanges;
    }



    public void setEndecaChanges(String endecaChanges) {
        this.endecaChanges = endecaChanges;
    }



    public String getEnvClientChanges() {
        return envClientChanges;
    }



    public void setEnvClientChanges(String envClientChanges) {
        this.envClientChanges = envClientChanges;
    }



    public String getContentKeyChanges() {
        return contentKeyChanges;
    }



    public void setContentKeyChanges(String contentKeyChanges) {
        this.contentKeyChanges = contentKeyChanges;
    }



    public String getComments() {
        return comments;
    }



    public void setComments(String comments) {
        this.comments = comments;
    }



    public String getEnhTag() {
        if ((enhTag == null || enhTag.trim().length() == 0) && getTagName() != null) {
            return getTagName().startsWith("tag-e") ? getTagName() : null;
        }
        return enhTag;
    }



    public void setEnhTag(String enhTag) {
        this.enhTag = enhTag;
    }



    public String getHunterTag() {
        if ((hunterTag == null || hunterTag.trim().length() == 0) && getTagName() != null) {
            return getTagName().startsWith("tag-b") ? getTagName() : null;
        }
        return hunterTag;
    }



    public void setHunterTag(String hunterTag) {
        this.hunterTag = hunterTag;
    }



    public String getBobaTag() {
        if ((bobaTag == null || bobaTag.trim().length() == 0) && getTagName() != null) {
            return getTagName().startsWith("tag-f") ? getTagName() : null;
        }
        return bobaTag;
    }



    public void setBobaTag(String bobaTag) {
        this.bobaTag = bobaTag;
    }



    public String getPwTag() {
        if ((pwTag == null || pwTag.trim().length() == 0) && getTagName() != null) {
            return getTagName().startsWith("tag-p") ? getTagName() : null;
        }
        return pwTag;
    }



    public void setPwTag(String pwTag) {
        this.pwTag = pwTag;
    }



    public String getvTag() {
        if ((vTag == null || vTag.trim().length() == 0) && getTagName() != null) {
            return getTagName().startsWith("tag-v") ? getTagName() : null;
        }
        return vTag;
    }



    public void setvTag(String vTag) {
        this.vTag = vTag;
    }



    public boolean isValid() {
        return valid == null ? true : valid;
    }



    public void setValid(Boolean pValid) {
        valid = pValid;
    }



    @Override
    public String toString() {
        return "TagChanges [tagName=" + tagName + ", dbChanges=" + dbChanges + ", wcsChanges=" + wcsChanges
                + ", endecaChanges=" + endecaChanges + ", envClientChanges=" + envClientChanges + ", contentKeyChanges="
                + contentKeyChanges + ", comments=" + comments + ", enhTag=" + enhTag + ", hunterTag=" + hunterTag
                + ", bobaTag=" + bobaTag + ", pwTag=" + pwTag + ", vTag=" + vTag + ", valid=" + valid + "]";
    }

}
