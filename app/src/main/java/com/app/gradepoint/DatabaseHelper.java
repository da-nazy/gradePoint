package com.app.gradepoint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name
    public static final String DATABASE_NAME="student.db";
    //Database table
    // USER TABLE
    public static final String USER_TABLE="user";
    // user columns
    public static final String COL_2="NAME";
    public static final String COL_3="EMAILPHONE";
    public static final String COL_4="DEPARTMENT";
    public static final String COL_5="FACULTY";
    public static final String COL_6="DURATION";
    public static final String COL_7="REGNO";
    public static final String COL_8="ENTRYMODE";
    public static final String COL_21="COUNTRY";
    public static final String COL_22="STATE";
    // SESSION TABLE
    public static final String SESSION_TABLE="session";
   // SESSION COLUMNS
   public static final String COL_9="ENTRYYEAR";
    public static final String COL_10="ENTRYLEVEL";
    public static final String COL_11="USERID";
    public static final String COL_19="FIRSTSEMESTER";
    public static final String COL_20="SECONDSEMESTER";

    // GRADE TABLE
    public static final String GRADE_TABLE="grade";
    // GRADE COLUMNS
    public static final String COL_12="USERID";
    public static final String COL_13="SESSIONID";
    public static final String COL_14="SEMESTERTYPE";
    public static final String COL_15="COURSECODE";
    public static final String COL_16="COURSENAME";
    public static final String COL_17="COURSEGRADE";
    public static final String COL_18="COURSECREDITLOAD";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
   // SQLiteDatabase db=getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // FOR USER TABLE
        db.execSQL("create table " + USER_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,EMAILPHONE TEXT UNIQUE,DEPARTMENT TEXT,FACULTY TEXT,DURATION INTEGER,REGNO TEXT UNIQUE,ENTRYMODE TEXT,COUNTRY TEXT,STATE TEXT)");
        // FOR SESSION
        db.execSQL("create table " + SESSION_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT,ENTRYYEAR TEXT,ENTRYLEVEL INTEGER ,USERID INTEGER,FIRSTSEMESTER TEXT,SECONDSEMESTER TEXT)");
        // FOR GRADE
        db.execSQL("create table " + GRADE_TABLE +"(ID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INTEGER,SESSIONID INTEGER,SEMESTERTYPE INTEGER,COURSECODE TEXT,COURSENAME TEXT,COURSEGRADE INTEGER, COURSECREDITLOAD INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SESSION_TABLE);
       db.execSQL("DROP TABLE IF EXISTS " + GRADE_TABLE);
        onCreate(db);
    }

    public boolean addUser(String name,String emailPhone,String department,String faculty,int duration,String regNumber, String entryMode){
        //TODO add users
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,emailPhone);
        contentValues.put(COL_4,department);
        contentValues.put(COL_5,faculty);
        contentValues.put(COL_6,duration);
        contentValues.put(COL_7,regNumber);
        contentValues.put(COL_8,entryMode);
        long result=db.insert(USER_TABLE,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }
    public boolean updateUser(String userid,String name,String emailPhone,String department,String faculty,int duration,String regNumber, String entryMode,String country,String state){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
       cv.put(COL_2,name);
       cv.put(COL_3,emailPhone);
       cv.put(COL_4,department);
       cv.put(COL_5,faculty);
       cv.put(COL_6,duration);
       cv.put(COL_7,regNumber);
       cv.put(COL_8,entryMode);
       cv.put(COL_21,country);
       cv.put(COL_22,state);
       long result=db.update(USER_TABLE,cv,"id=?",new String[]{userid});
       if(result==-1){
           return false;
        }else{
           return true;
        }

    }
    public boolean addSession(String entryYear,int entryLevel,int userId){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_9,entryYear);
        contentValues.put(COL_10,entryLevel);
        contentValues.put(COL_11,userId);
        long result=db.insert(SESSION_TABLE,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
        //TODO ADD SESSIONS ASSOCIATED TO A USER

    }
    public boolean addGrade(int userid,int sessionId,int semsterType,String courseCode,String courseName,int courseGrade,int courseCreditLoad){
        // TODO ADD GRADE ASSOCIATED TO A USER ,A SESSION, A SEMESTER
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_12,userid);
        contentValues.put(COL_13,sessionId);
        contentValues.put(COL_14,semsterType);
        contentValues.put(COL_15,courseCode);
        contentValues.put(COL_16,courseName);
        contentValues.put(COL_17,courseGrade);
        contentValues.put(COL_18,courseCreditLoad);
        long result=db.insert(GRADE_TABLE,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }
    public boolean updateGrade(String courseId,String courseCode,String courseName,int courseGrade,int courseCreditLoad){
        // TODO ADD GRADE ASSOCIATED TO A USER ,A SESSION, A SEMESTER
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(COL_15,courseCode);
        cv.put(COL_16,courseName);
        cv.put(COL_17,courseGrade);
        cv.put(COL_18,courseCreditLoad);
        long result=db.update(GRADE_TABLE,cv,"id=?",new String[]{courseId});
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }
    public Cursor getGrade(String userid,String sessionid,String semestertype){
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select * from grade where userid=? and sessionid=? and semestertype=?",new String []{userid,sessionid,semestertype});
        return res;
    }
    public Cursor getUser(String userid){
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+USER_TABLE+" where id=?",new String[]{userid});
        return res;
    }
    public Cursor getSession(String  id){
        // Select all session of a particular user
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select * from session where userid=?",new String []{id});
                return res;
    }
    public boolean updateFirstSemGrade(String id,String firstSem){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_19,firstSem);
        long result=db.update(SESSION_TABLE,cv,"id=?",new String[]{id});
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateSecondSemGrade(String id,String secondSem){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL_20,secondSem);
        long result=db.update(SESSION_TABLE,cv,"id=?",new String[]{id});
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }
    public Cursor getSessionId(String id){
        // Select * session id of a particular user
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select id from session where userid=?",new String[]{id});
        return res;
    }
    public boolean removeSession(String userId, String sessionId){

        //TODO REMOVE SESSION ASSOCIATED TO: A USER, GRADE
        // remove a session
        // drop grade with sessionId, and userId
        SQLiteDatabase db=getWritableDatabase();
        long result=db.delete(SESSION_TABLE,"userid=? and id=?",new String[]{userId,sessionId});
       // Cursor res=db.rawQuery( "delete from session where userid=? and id=?", new String[]{userId,sessionId});
       if( result==-1){
           return false;
        }else{
           return true;
        }
    }
    public boolean editSession(String entryYear,String entryLevel, String sessionId, String userId){
        // TODO UDPATE THE SESSION

        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_9,entryYear);
        contentValues.put(COL_10,entryLevel);
      long result= db.update(SESSION_TABLE,contentValues,"id= ? and userid=?",new String[]{sessionId,userId});
       // Cursor res=db.rawQuery( "update session set ENTRYYEAR=? ,ENTRYLEVEL=? where id=? and userid=?", new String[]{entryYear,entryLevel,sessionId,userId});
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor userLogin(String emailPhone,String regNumber){
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+USER_TABLE+" where EMAILPHONE=? and REGNO=?;",new String[]{emailPhone,regNumber});
    return res;
    }
    public boolean removeSemester(String userId,String sessionId,String semesterType){
        SQLiteDatabase db=getWritableDatabase();
        long result=db.delete(GRADE_TABLE,"userid=? and sessionid=? and semesterType=?",new String[]{ userId,sessionId,semesterType});
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public boolean removeGrade(String gradeId){
        // TODO REMOVE GRADE ASSOCIATED WITH : A USER, SESSION, SEMESTER

        SQLiteDatabase db=getWritableDatabase();
        long result=db.delete(GRADE_TABLE,"id=?",new String[]{gradeId});
        // Cursor res=db.rawQuery( "delete from session where userid=? and id=?", new String[]{userId,sessionId});
        if( result==-1){
            return false;
        }else{
            return true;
        }
    }
    public boolean removeGrade(int  sessionId){
        SQLiteDatabase db=getWritableDatabase();
        long result=db.delete(GRADE_TABLE,"sessionid=?",new String[]{String.valueOf(sessionId)});
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

        // Grade calculation for a singel sessioni
    public Cursor getGrades(String userid,String sessionid,String semestertype){
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select coursegrade,coursecreditload from grade where userid=? and sessionid=? and semestertype=?",new String []{userid,sessionid,semestertype});
        return res;
    }

    // Grade Calculation for multiple session
    public Cursor getSessionsGrade(String sessionid){
        SQLiteDatabase db=getWritableDatabase();
        Cursor  res=db.rawQuery("select firstsemester,secondsemester from session where id=?;",new String []{sessionid});
        return res;
    }

    public Cursor getAllSessionsGrade(String userid){
        SQLiteDatabase db=getWritableDatabase();
        Cursor res=db.rawQuery("select entryyear,firstsemester,secondsemester from session where userid=?",new String []{userid});
        return res;
    }
}
