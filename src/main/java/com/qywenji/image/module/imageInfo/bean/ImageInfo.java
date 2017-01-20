package com.qywenji.image.module.imageInfo.bean;

import javax.persistence.*;

/**
 * Created by CAI_GC on 2017/1/17.
 */
@Entity
@Table
public class ImageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String mdKey;

    private Long begin;

    private Long end;

    private String accessUrl;

    private String ctime;

    public ImageInfo() {
    }

    public ImageInfo(String mdKey, Long begin, Long end, String accessUrl,String ctime) {
        this.mdKey = mdKey;
        this.begin = begin;
        this.end = end;
        this.accessUrl = accessUrl;
        this.ctime = ctime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMdKey() {
        return mdKey;
    }

    public void setMdKey(String mdKey) {
        this.mdKey = mdKey;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public Long getBegin() {
        return begin;
    }

    public void setBegin(Long begin) {
        this.begin = begin;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }
}
