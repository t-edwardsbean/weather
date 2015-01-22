package com.baidu.weather.model;

/**
 * Created by edwardsbean on 15-1-20.
 */
public class SugTypeInfo {
    private int id;
    private String type;
    private String suggestion;
    private String info;

    @Override
    public String toString() {
        return "SugTypeInfo{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", suggestion='" + suggestion + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SugTypeInfo that = (SugTypeInfo) o;

        if (!suggestion.equals(that.suggestion)) return false;
        if (!type.equals(that.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + suggestion.hashCode();
        return result;
    }

    public SugTypeInfo(String type, String suggestion) {
        this.type = type;
        this.suggestion = suggestion;
    }

    public SugTypeInfo(String type, String suggestion, String info) {
        this.type = type;
        this.suggestion = suggestion;
        this.info = info;
    }

    public SugTypeInfo(int id, String type, String suggestion, String info) {
        this.id = id;
        this.type = type;
        this.suggestion = suggestion;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
