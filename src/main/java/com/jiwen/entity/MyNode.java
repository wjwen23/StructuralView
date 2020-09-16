package com.jiwen.entity;

import java.util.ArrayList;
import java.util.List;

public class MyNode {
    private String className;
    private int index;
    private List<MyNode> children = new ArrayList<>();

    public MyNode(int index) {
        this.index = index;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<MyNode> getChildren() {
        return children;
    }

    public void setChildren(List<MyNode> children) {
        this.children = children;
    }
}
