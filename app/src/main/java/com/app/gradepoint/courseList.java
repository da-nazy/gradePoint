package com.app.gradepoint;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class courseList extends AppCompatActivity {
    AdRequest adRequest;
    // This should contain the recyclerview that we created in our xml layout for addcourse
    private RecyclerView mRecyclerView;
    // for resultdialog
    ImageButton imageButton;
    TextView textView,texthead,tv;
    String semester;
    // This is basically the bridge  between our data (ArrayList) and the recyclerview
   // private RecyclerView.Adapter mAdapter;
    // This is responsible for arranging things in our list

    private RecyclerView.LayoutManager mLayoutManager;
    addCourseAdapter addCourse;
    ArrayList<addCourseModel> courseList;
    FloatingActionButton fab;
    Button noremovecourse,yesremovecourse;
    Button noaddcourse,yesaddcourse;
    EditText coursecode,courseGrade,courseName,courseCreditCode;
    Cursor res,ser,esr;
    String userid,sessionid,semestertype;
    DatabaseHelper db;
    SharedPreferences sp,ps,sh;
    private AdView mAdMobAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
            setContentView(R.layout.adcourselist);
         courseList=new ArrayList<>();
         db=new DatabaseHelper(this);
         getValues();
        populateGrade();
        mRecyclerView=findViewById(R.id.recyclerview);
        // This will change in size so change false;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        addCourse=new addCourseAdapter(courseList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(addCourse);
        fab=findViewById(R.id.addcoursefab);
        getSupportActionBar().setTitle("GRADES");

        mAdMobAdView = (com.google.android.gms.ads.AdView) findViewById(R.id.admobcourse);
        // mAdMobAdView.setBackgroundColor(Color.BLACK);
        //MobileAds.initialize(this,"ca-app-pub-5845987937730369~6847789230");
         adRequest = new AdRequest.Builder().build();
        mAdMobAdView.loadAd(adRequest);

        mAdMobAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Toast.makeText(courseList.this, "onAdLoaded", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(errorCode== AdRequest.ERROR_CODE_INTERNAL_ERROR)

                    Toast.makeText(courseList.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Toast.makeText(courseList.this, "onAdOpened", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(courseList.this, "onAdLeftApplication", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onAdClosed() {
                Toast.makeText(courseList.this, "onAdClosed", Toast.LENGTH_SHORT).show();



            }
        });

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                editDialog(-1);
            }
        });

        addCourse.setOnItemClickListener(new addCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getApplicationContext(),"you clicked on an item", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDeleteClick(int position) {

                Toast.makeText(getApplicationContext(),"you clicked on delete item", Toast.LENGTH_LONG).show();
                deleteDialog(position);
            }

            @Override
            public void onEditClick(int position) {

                Toast.makeText(getApplicationContext(),"you clicked on edit item", Toast.LENGTH_LONG).show();
                editDialog(position);
            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.menu1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int menuId=item.getItemId();
        switch(menuId){
            // call result dialog
            case R.id.item1 : resultDialog();
            calculateGP();
            break;
            case R.id.itme2:Intent intent=new Intent(courseList.this,editUserProfile.class);
                startActivity(intent);
        }
        return true;

    }
    private void editDialog(final int state){
        ViewGroup viewGroup=findViewById(android.R.id.content);

        View editView= LayoutInflater.from(this).inflate(R.layout.courseinputdialog,viewGroup,false);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setView(editView);
        yesaddcourse=editView.findViewById(R.id.yesaddcourse);
        noaddcourse=editView.findViewById(R.id.noaddcourse);

        coursecode=editView.findViewById(R.id.courseCode);
        courseGrade=editView.findViewById(R.id.coureGrade);
        courseName=editView.findViewById(R.id.coureName);
        courseCreditCode=editView.findViewById(R.id.courseCreditCode);


        final AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if(state==-1){
            // Adding new course

        }else{
              // Handle editing here
            // Initial population
            coursecode.setText(courseList.get(state).getCourseCode());
            courseGrade.setText(courseList.get(state).getCourseGrade());
            courseName.setText(courseList.get(state).getCourseName());
            courseCreditCode.setText(courseList.get(state).getCourseCreditLoad());

            // editing

            yesaddcourse.setText("EDIT");
        }
        alertDialog.show();

        noaddcourse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                alertDialog.dismiss();
            }
        });

        yesaddcourse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(state==-1){
                    // handing new adding
                    if(checkCourse()==true){
                      if(db.addGrade(Integer.parseInt(userid),Integer.parseInt(sessionid),Integer.parseInt(semestertype),coursecode.getText().toString(),courseName.getText().toString(),Integer.parseInt(courseGrade.getText().toString()),Integer.parseInt(courseCreditCode.getText().toString()))==true){
                          courseList.clear();
                          populateGrade();
                          addCourse.notifyDataSetChanged();
                          alertDialog.dismiss();
                      }
                        //courseList.add(new addCourseModel(R.drawable.delete,coursecode.getText().toString(),courseGrade.getText().toString().trim(),courseCreditCode.getText().toString().trim(),R.drawable.ic_more1,courseName.getText().toString().trim()));

                    }else{
                        Toast.makeText(getBaseContext(),"Empty field found",Toast.LENGTH_LONG).show();

                    }
                }else{
                    // handle editing
                     if(checkCourse()==true){
                         db.updateGrade(courseList.get(state).getCourseId()+"",coursecode.getText().toString().trim(),courseName.getText().toString().trim(),Integer.parseInt(courseGrade.getText().toString()),Integer.parseInt(courseCreditCode.getText().toString().trim()));
                         courseList.clear();
                         populateGrade();
                         addCourse.notifyDataSetChanged();
                         alertDialog.dismiss();
                     }else{
                         Toast.makeText(getBaseContext(),"Empty field found",Toast.LENGTH_LONG).show();

                     }
                }

            }
        });

    }


    private void deleteDialog(int state){
      final int mystate=state;
        ViewGroup viewGroup=findViewById(android.R.id.content);
        View deleteView=LayoutInflater.from(this).inflate(R.layout.removecoursedialog,viewGroup,false);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(deleteView);
        final AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        noremovecourse=deleteView.findViewById(R.id.noremcos);
        yesremovecourse=deleteView.findViewById(R.id.yesremcos);
        tv=deleteView.findViewById(R.id.rlwdHead);
        tv.setText(courseList.get(mystate).getCourseCode());
        noremovecourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        yesremovecourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.removeGrade(courseList.get(mystate).getCourseId()+"")==true){
                    courseList.clear();
                    populateGrade();
                    addCourse.notifyDataSetChanged();
                    alertDialog.dismiss();
                }


            }
        });


    }

    private void resultDialog(){
        ViewGroup  viewGroup=findViewById(android.R.id.content);
        View  resultView=LayoutInflater.from(this).inflate(R.layout.dispgrade,viewGroup,false);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
         builder.setView(resultView);
         final AlertDialog alertDialog=builder.create();
         alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
         alertDialog.show();
         imageButton=resultView.findViewById(R.id.reExit);
         textView=resultView.findViewById(R.id.smResutl);
         texthead=resultView.findViewById(R.id.resultHead);

         texthead.setText(semestertype+" Semester");
         textView.setText(semestertype+" Semester: "+calculateGP());
         switch (Integer.parseInt(semestertype)){
             case 1: db.updateFirstSemGrade(sessionid,calculateGP()+"");
             break;
             case 2: db.updateSecondSemGrade(sessionid,calculateGP()+"");
             break;
         }
         imageButton.setOnClickListener(new View.OnClickListener(){
             @Override
             public void onClick(View view){
                 alertDialog.dismiss();
             }
         });
    }
    // Check for empty fields
    public boolean checkCourse(){
        boolean isCourse;
        if(coursecode.getText().toString().isEmpty()){
            isCourse=false;
            coursecode.setError("Empty field");

        }else if(courseGrade.getText().toString().isEmpty()){
            isCourse=false;
            courseGrade.setError("Empty field");
        }else if(courseName.getText().toString().isEmpty()){
            isCourse=false;
            courseName.setError("Empty field");
        }else if(courseCreditCode.getText().toString().isEmpty()){
            isCourse=false;
            courseCreditCode.setError("Empty field");
        }else{
            isCourse=true;
        }
        return isCourse;
    }

    public double calculateGP( ){
        // 5: credit load should have a limit eg 24
        // 1: get the grade score for gp ranging from 5:a,4:b,3:c,2:d,1:f
        // 2:  multiply the grade score with creditLoad
        // 3: sum number 2
        // 4: divide number 3 with total credit load
        //int sumcreditCode = 0;
        int credit=0;
        double grade=0;
        int creditTest=0;
        for(int a =0; a<courseList.size();a++){
          creditTest+= Integer.parseInt(courseList.get(a).getCourseCreditLoad());
        }

        if(creditTest<25){
            //have exceeded the credit load for a semester
            for(int b=0; b<courseList.size();b++) {
                // Calculation can be done here
                // 1: get the grade score for gp ranging from 5:a,4:b,3:c,2:d,1:f

                // getGrade returns a string value
                //  program that converts the returned string to Intetger
                //getIntGrade(a);
                credit += getIntGrade(b) * Integer.parseInt(courseList.get(b).getCourseCreditLoad());
                grade = (double) credit / creditTest;
            }
            Toast.makeText(getApplicationContext(),grade+"",Toast.LENGTH_LONG).show();
    }else if(creditTest>24){
            // from 25 and above
            Toast.makeText(getApplicationContext(),"You have exceeded your credit load",Toast.LENGTH_LONG).show();
        }
        return grade;
    }
    public int getIntGrade(int a) {
        // converting from  grade to grade score in Integer

        int grade=0;
        switch (addCourse.getGrade(Integer.parseInt(courseList.get(a).getCourseGrade()))) {
            case "A":
                grade=5;
            break;
            case "B":
                grade=4;
            break;
            case "C":
                grade= 3;
            break;
            case "D":
                grade=2;
                break;
            case"F":
                grade=0;
                break;
            default: System.out.println("Error");
            break;

        }
        return grade;
    }
            public void populateGrade(){


                res=db.getGrade(userid,sessionid,semestertype);

                while(res.moveToNext()) {

                    courseList.add(new addCourseModel(R.drawable.delete, res.getString(4) + "", res.getInt(6) + "", res.getInt(7) + "", R.drawable.ic_more1, res.getString(5), res.getInt(0), res.getInt(2), res.getInt(1), res.getInt(3)));
                }
                //Toast.makeText(getApplicationContext(),userid+sessionid+semestertype,Toast.LENGTH_SHORT).show();


            }
         public void getValues(){
             sp=getSharedPreferences("userdetails", MODE_PRIVATE);
             userid=sp.getInt("userid",-1)+"";

             ps=getSharedPreferences("sessionDetails", MODE_PRIVATE);
             sessionid=ps.getInt("sessionId",-1)+"";

             sh=getSharedPreferences("semesterType",MODE_PRIVATE);
             semestertype=sh.getInt("semesterNo",-1)+"";
         }

}
