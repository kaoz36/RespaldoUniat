package com.university3dmx.basura;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 19/05/2015.
 */
public class Comment implements Serializable{

    private long id;
    private String comment;
    private ArrayList<String> algo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAlgo(ArrayList<String> algo) {
        this.algo = algo;
    }
}
