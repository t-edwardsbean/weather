package com.baidu.weather.model;

/**
 * Created by edwardsbean on 15-1-20.
 */
public class SugType {
    private int id;
    private String type;
    private String typeName;

    public SugType(String type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "SugType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
