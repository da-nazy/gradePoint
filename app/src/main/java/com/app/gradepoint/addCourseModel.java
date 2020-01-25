package com.app.gradepoint;

public class addCourseModel {
    private int deleteImage;
    private String  courseCode;



    private int courseId;
    private int sessionId;
    private int userId;
    private int semesterType;
    private String  courseGrade;
    private String  courseCreditLoad;
    private String courseName;
    private int  editImage;



    public addCourseModel(int deleteImage, String courseCode, String courseGrade, String courseCreditLoad,int editImage,String courseName,int courseId,int sessionId,int userId,int semesterType) {
        this.deleteImage = deleteImage;
        this.courseCode = courseCode;
        this.courseGrade = courseGrade;
        this.courseCreditLoad = courseCreditLoad;
        this.editImage=editImage;
        this.courseName=courseName;
        this.courseId=courseId;
        this.sessionId=sessionId;
        this.userId=userId;
        this.semesterType=semesterType;
    }

    public int getCourseId() {
        return courseId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public int getUserId() {
        return userId;
    }

    public int getSemesterType() {
        return semesterType;
    }

    public void setCourseGrade(String courseGrade) {
        this.courseGrade = courseGrade;
    }

    public void setCourseCreditLoad(String courseCreditLoad) {
        this.courseCreditLoad = courseCreditLoad;
    }

    public void setCourseName(String courseName){
        this.courseName=courseName;
    }

    public void setCourseCode(String courseCode){
        this.courseName=courseCode;
    }

    public String getCourseName(){
        return courseName;
    }
    public int getEdit(){
        return editImage;
    }
    public int getDeleteImage() {
        return deleteImage;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseGrade() {
        return courseGrade;
    }

    public String getCourseCreditLoad() {
        return courseCreditLoad;
    }


}
