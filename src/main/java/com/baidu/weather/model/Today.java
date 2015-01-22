package com.baidu.weather.model;

import us.codecraft.webmagic.MultiPageModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by edwardsbean on 15-1-11.
 */
public class Today implements MultiPageModel {
    public Today(String pageKey, String page) {
        this.pageKey = pageKey;
        this.page = page;
    }


    //用于标识属于同一个item,用于聚合
    private String pageKey;
    //一个item中的某个部分
    private String page;
    //item的所有部分
    private List<String> otherPages = Arrays.asList("today", "sun");

    @Override
    public String getPageKey() {
        return pageKey;
    }

    @Override
    public String getPage() {
        return page;
    }

    @Override
    public Collection<String> getOtherPages() {
        return otherPages;
    }

    private Map<String, Object> args;

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }

    @Override
    public MultiPageModel combine(MultiPageModel multiPageModel) {
        Today today = (Today) multiPageModel;
        this.getArgs().putAll(today.getArgs());
        return this;
    }

    @Override
    public String toString() {
        return "Today{" +
                "pageKey='" + pageKey + '\'' +
                ", page='" + page + '\'' +
                ", otherPages=" + otherPages +
                ", args=" + args +
                '}';
    }
}
