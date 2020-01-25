package com.app.gradepoint;

public class addSemesterModel {
    private int semester;
    private int edit;
    private int delete;
    private int next;
    public addSemesterModel(int semester,int edit,int delete,int next){
        this.semester=semester;
        this.edit=edit;
        this.delete=delete;
        this.next=next;
    }
    public int getSemester() {
        return semester;
    }
   public void setSemester(int semester){
        this.semester=semester;
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


}
