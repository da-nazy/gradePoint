package com.app.gradepoint;

public class addLevelModel {



    private int level;


    private String session;
    private int edit;
    private int delete;
    private int next;
    private String first;
    private String second;
    private int sesID;


    public addLevelModel(int level, String session, int edit, int delete, int next, String first, String second,int sesID ) {
        this.level = level;
        this.session = session;
        this.edit = edit;
        this.delete = delete;
        this.next = next;
        this.first= first;
        this.second= second;
        this.sesID=sesID;
    }



    public void setLevel(int level) {
        this.level = level;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getFirst(){
        return first;
    }
    public String getSecond(){
        return second;
    }
    public int getLevel() {
        return level;
    }

    public String getSession() {
        return session;
    }

    public int getEdit() {
        return edit;
    }

    public int getDelete() {
        return delete;
    }

    public int getNext() {
        return next;
    }
    public int getSesID(){
        return sesID;
    }


}
