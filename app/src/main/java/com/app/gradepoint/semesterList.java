package com.app.gradepoint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import com.google.android.gms.ads.InterstitialAd;
public class semesterList extends AppCompatActivity {
    private InterstitialAd mInterstitialAd;
    private RecyclerView mRecycle;
   TextView sm;
    //private RecyclerView.Adapter adpater;
    addSemesterAdapter addsemesteradapter;
    FloatingActionButton fab;
    Button addsmdialog,canclesmdialog,yesem,nosem;
    EditText inputsmdialog;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<addSemesterModel> addsemesterList;
    Cursor res;
    DatabaseHelper db;
    int userid;
    int sessionid;
    int semesterNo;
    ImageButton sesExit;
    TextView sesHeader,sesBody;
    String sessionyear;

    // FIRST AND SECOND SEMESTER GLOBAL VARIABLES
    int creditSum[]=new int[2];
    int credit[]=new int[2];
    double grade[]=new double[2];
    double firstSemester=0,secondSemester=0;
    double totalSessionGrade=0;
    double totalGrade=0;
    private AdView mAdMobAdView;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester);
        db=new DatabaseHelper(this);
        addsemesterList=new ArrayList<>();
        addsemesterList.add(new addSemesterModel(1,R.drawable.ic_more,R.drawable.delete,R.drawable.next));
        addsemesterList.add(new addSemesterModel(2,R.drawable.ic_more,R.drawable.delete,R.drawable.next));
            fab=findViewById(R.id.semlistfab);
        mRecycle=findViewById(R.id.semesterRec);
        mRecycle.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
            addsemesteradapter=new addSemesterAdapter(addsemesterList);
            mRecycle.setLayoutManager(layoutManager);
            mRecycle.setAdapter(addsemesteradapter);
        getSupportActionBar().setTitle("SEMESTER");
        //ADS
        mAdMobAdView = (com.google.android.gms.ads.AdView) findViewById(R.id.admob);
        MobileAds.initialize(this,"ca-app-pub-5845987937730369~6847789230");
        for(int i=0; i<=1000;i++){
            adRequest = new AdRequest.Builder().build();
        }

        mAdMobAdView.loadAd(adRequest);

        // mAdMobAdView.setBackgroundColor(Color.BLACK);
        mAdMobAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Toast.makeText(semesterList.this, "onAdLoaded", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(errorCode== AdRequest.ERROR_CODE_INTERNAL_ERROR)

                    Toast.makeText(semesterList.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Toast.makeText(semesterList.this, "onAdOpened", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(semesterList.this, "onAdLeftApplication", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onAdClosed() {
                Toast.makeText(semesterList.this, "onAdClosed", Toast.LENGTH_SHORT).show();



            }
        });


            fab.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    // TODO STATE -1 FOR ADDING NEW SEMSTER
                    // And should not exceed 2 semesters

                    addsemester(-1);
                }
            });
            addsemesteradapter.setOnItemClickListner(new addSemesterAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Toast.makeText(getApplicationContext(),"You clicked on an item",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNextClick(int position) {
                    semesterNo=addsemesterList.get(position).getSemester();
                    Toast.makeText(getApplicationContext(),"You clicked on an next button",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(semesterList.this,courseList.class);
                    startActivity(intent);
                }

                @Override
                public void onDeleteClick(int position) {
                    Toast.makeText(getApplicationContext(),"You clicked on an delete button",Toast.LENGTH_LONG).show();
                    // -2 for delete
                    // TODO CREATE A DELTE PROMPT DIALOG FOR SEMESTER
                    deleteSemester(position);
                }

                @Override
                public void onEditClick(int position) {
                    Toast.makeText(getApplicationContext(),"You clicked on an edit button",Toast.LENGTH_LONG).show();
                    // TODO FOR EDIT STATE CLICK

                        addsemester(position);
                }
            });

        SharedPreferences sp=getSharedPreferences("userdetails", MODE_PRIVATE);
        userid=sp.getInt("userid",-2);
        SharedPreferences shap=getSharedPreferences("sessionDetails",MODE_PRIVATE);
        sessionid=shap.getInt("sessionId",-1);
        sessionyear=shap.getString("sessionYear","1");

       // Toast.makeText(getApplicationContext(),sessionid+"",Toast.LENGTH_SHORT).show();
       // Toast.makeText(getApplicationContext(),sessionyear+"",Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.singlesession,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int menuId=item.getItemId();
        switch(menuId){
            // call result dialog
            case R.id.sessionItem1:calcSessionsGrade();
                displaySessionGrade();
                break;
            case R.id.sessionItem2:
                Intent intent=new Intent(semesterList.this,editUserProfile.class);
                startActivity(intent);
                break;
        }
        return true;

    }
    private void addsemester( int state){
        final int myState=state;
        ViewGroup viewGroup =findViewById(android.R.id.content);
              //viewGroup.setPadding(20,10,20,10);
            //viewGroup.setMinimumWidth(viewGroup.getWidth()-50);

        // this will inflate the custom alert that will be created
        View semesterView= LayoutInflater.from(this).inflate(R.layout.semeterdialog,viewGroup,false);
        LinearLayout linearLayout;
        linearLayout=semesterView.findViewById(R.id.sd);
        //getLayoutParams().height=x;
        // requestLayout(); or invalidate();'
        // for changing the width of the LinearLayout
        linearLayout.getLayoutParams().width=linearLayout.getWidth()-100;
         linearLayout.invalidate();
        // Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        // Setting the view of the builder to our custom view that we already inflated
        builder.setView(semesterView);

        // Finally creating the alert dialog and displaying it
        final AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        alertDialog.show();
        addsmdialog=semesterView.findViewById(R.id.addsmdialog);
        canclesmdialog=semesterView.findViewById(R.id.canclesmdialog);
        inputsmdialog=semesterView.findViewById(R.id.inputsmdialog);
if(myState==-1){

}else{
    inputsmdialog.setText(addsemesterList.get(myState).getSemester()+"");
    addsmdialog.setText("EDIT");
}
        addsmdialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(myState==-1){
                    if(inputsmdialog.getText().toString().isEmpty()){
                        //empty
                    }else{
                        if(addsemesterList.size()>1){
                            Toast.makeText(getBaseContext(),"You can't add more than two semesters",Toast.LENGTH_SHORT).show();
                        }else{
                            //TODO SHOULD NOT ALLOW More than two semesters to be added
                            Toast.makeText(getBaseContext(),addsemesterList.size()+" ",Toast.LENGTH_SHORT).show();
                            addsemesterList.add(new addSemesterModel(Integer.parseInt(inputsmdialog.getText().toString().trim()),R.drawable.ic_more,R.drawable.delete,R.drawable.next));

                            addsemesteradapter.notifyDataSetChanged();
                        }

                    }
                }else
                {
                    if(inputsmdialog.getText().toString().isEmpty()){

                    }else{
                        addsemesterList.get(myState).setSemester(Integer.parseInt(inputsmdialog.getText().toString().trim()));
                        addsemesteradapter.notifyItemChanged(myState);
                    }
                }

                alertDialog.dismiss();
            }
        });
        canclesmdialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                alertDialog.dismiss();
            }
        });
        Toast.makeText(this, viewGroup.getWidth()+" ",Toast.LENGTH_LONG).show();

    }
    private void deleteSemester( int position){
    final int myposition=position;
        ViewGroup viewGroup=findViewById(R.id.content);

        // Then we will inflate the custom alert dialog xml that we created
        View warningView=LayoutInflater.from(this).inflate(R.layout.removesemdialog,viewGroup,false);

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setView(warningView);

        //Finally creatign the alert dialog and displaying it
        final AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();

        // Binding views
        sm=warningView.findViewById(R.id.semesterheader);
        sm.setText(addsemesterList.get(myposition).getSemester()+" SEMESTER");
        nosem=warningView.findViewById(R.id.noremsem);
        yesem=warningView.findViewById(R.id.yesremsem);

        nosem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //
                alertDialog.dismiss();
            }
        });

        yesem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
              //  addsemesterList.remove(myposition);
                semesterNo=myposition;
                if(db.removeSemester(userid+"",sessionid+"",semesterNo+"")==true) {
                    Toast.makeText(getBaseContext(), "All grades associated to semester dissolved", Toast.LENGTH_SHORT).show();
                }
                //  addsemesteradapter.notifyItemRemoved(myposition);
                alertDialog.dismiss();
            }
        });

    }
    public void onPause(){
        SharedPreferences sp=getApplicationContext().getSharedPreferences("semesterType",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("semesterNo",semesterNo);
        editor.apply();
        super.onPause();
    }
    public void sessiongrade(){
        // for first semester

        //int creditSum[a]=0;
     //   double grade=0;
        //int credit[a]=0;
        // for second semester


        // semester Array
        String [] semester={"1","2"};
        for(int a=0; a<semester.length;a++){

           // res=db.getGrades(userid+"",sessionid+"",semester[a]+"");
            res=db.getGrades(userid+"",sessionid+"",semester[a]);
            while(res.moveToNext()){
                Toast.makeText(getApplicationContext(), res.getCount()+"daniela",Toast.LENGTH_SHORT).show();
                creditSum[a]+=res.getInt(1);
                Toast.makeText(getApplicationContext(),res.getInt(1)+"",Toast.LENGTH_SHORT).show();
                credit[a]+=getIntGrade(getGrade(res.getInt(0)))*res.getInt(1);
            }
            try{
                grade[a]=(double) (credit[a]/creditSum[a]);
            }catch(Exception e){grade[0]=0;}

            Toast.makeText(getApplicationContext(),grade[a]+"",Toast.LENGTH_SHORT).show();
        }
        // Totall session grade for a semester



    }
    public String  getGrade(int score){
        String grade="";
        if(score>=70){
            grade="A";
        } else if (score >= 60 && score < 70) {
            grade="B";
        } else if (score>=50 && score <60){
            grade="C";
        }else if(score>=45 && score <50){
            grade="D";
        }else if(score>=0 && score<44){
            grade="F";
        }
        return grade;
    }
    public int getIntGrade(String a) {
        // converting from  grade to grade score in Integer

        int grade=0;
        switch (a) {
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
    public void  calcSessionsGrade(){
        res=db.getSessionsGrade(sessionid+"");

        while(res.moveToNext()){
            if(res.getString(0)==null){
                firstSemester=0;
            }else{
                firstSemester=Double.parseDouble(res.getString(0));
            }
            if(res.getString(1)==null){
                secondSemester=0;
            }else{
                secondSemester=Double.parseDouble(res.getString(1));
            }
            totalGrade=(double)(firstSemester+secondSemester)/2;
        }
    }
    public void displaySessionGrade(){
        ViewGroup viewGroup=findViewById(R.id.content);
        View sessionDialog=LayoutInflater.from(this).inflate(R.layout.dispsessiongrade,viewGroup,false);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(sessionDialog);
        final AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
        sesExit=sessionDialog.findViewById(R.id.sesExit);
        sesHeader=sessionDialog.findViewById(R.id.sesresultHead);
        sesBody=sessionDialog.findViewById(R.id.sesResutl);
        totalSessionGrade=(int) (grade[0]+grade[1])/2;
        sesBody.setText("First Semester="+firstSemester+"\n"+"Second Semester="+secondSemester+"\n"+"Total="+totalGrade);
        sesHeader.setText(sessionyear);
        sesExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
