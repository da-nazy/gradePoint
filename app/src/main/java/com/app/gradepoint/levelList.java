package com.app.gradepoint;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class levelList extends AppCompatActivity {
// for adrequest
    AdRequest adRequest;
    RecyclerView mrecycle;
    RecyclerView.LayoutManager layoutManager;
    //RecyclerView.Adapter adapter;
   FloatingActionButton fab;
    ArrayList<addLevelModel> leveList;
    addLevelAdapter alAdapter;
    EditText level,ses;
    Button cancle,add,yes,no;
    int levPos;
    String sessionYear;
    DatabaseHelper db;
    int userid;
    Cursor res,editses,ser;
    TextView tv;
   int sessionID;
   semesterList sm;
   // for calculation of total grades
    ImageButton imageButton;
    TextView head,body;
    String entryyear[];
    double grades[];
    private AdView mAdMobAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_list);
        sm=new semesterList();
        db=new DatabaseHelper(getBaseContext());
        leveList=new ArrayList<>();
        SharedPreferences sp=getSharedPreferences("userdetails", MODE_PRIVATE);
        userid=sp.getInt("userid",-1);
        // String t=sp.getString("test","failed");
        // Toast.makeText(this,userid+" "+t,Toast.LENGTH_LONG).show();
       populateSession();

        mrecycle=findViewById(R.id.levelRec);
        mrecycle.setHasFixedSize(true);
        layoutManager=new GridLayoutManager(this,2);
        alAdapter=new addLevelAdapter(leveList);
        mrecycle.setLayoutManager(layoutManager);
        mrecycle.setAdapter(alAdapter);
        fab=findViewById(R.id.addlevel);

        getSupportActionBar().setTitle("SESSIONS");

        mAdMobAdView = (com.google.android.gms.ads.AdView) findViewById(R.id.admoblevel);
        // mAdMobAdView.setBackgroundColor(Color.BLACK);
        MobileAds.initialize(this,"ca-app-pub-5845987937730369~6847789230");

      adRequest = new AdRequest.Builder().build();
        mAdMobAdView.loadAd(adRequest);

        mAdMobAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Toast.makeText(levelList.this, "onAdLoaded", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if(errorCode== AdRequest.ERROR_CODE_INTERNAL_ERROR)

                    Toast.makeText(levelList.this, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Toast.makeText(levelList.this, "onAdOpened", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onAdLeftApplication() {
                Toast.makeText(levelList.this, "onAdLeftApplication", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onAdClosed() {
                Toast.makeText(levelList.this, "onAdClosed", Toast.LENGTH_SHORT).show();



            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getBaseContext(),"working",Toast.LENGTH_LONG).show();
               // levDialog.setEdit("ADD"," "," ");
               // levDialog.show(getSupportFragmentManager(),"levelDialog");
                // TODO 1 is add for state
                showCutomDialog(1);
            }
        });
        alAdapter.setOnItemClickListner(new addLevelAdapter.OnItemClickListener(){
            @Override
            public void  onItemclick(int position){
                Toast.makeText(getBaseContext(),"You clicked on an item "+position,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onDeleteClick(int position) {
              // Toast.makeText(getBaseContext(),"You clicked on the delete button"+position,Toast.LENGTH_LONG).show();
                sessionID=leveList.get(position).getSesID();
                levPos=position;
                deleteDialog();
            }

            @Override
            public void onEditClick(int position) {
              //  Toast.makeText(getBaseContext(),"You clicked on the edit button"+position,Toast.LENGTH_LONG).show();
              //  levDialog.setEdit("EDIT",leveList.get(position).getLevel()+"",leveList.get(position).getSession());
                //levDialog.show(getSupportFragmentManager(),"levelDialog");
                // TODO 2 is edit for state
                sessionID=leveList.get(position).getSesID();
                levPos=position;
                showCutomDialog(2);
            }

            @Override
            public void onNextClick(int position) {
              //  Toast.makeText(getBaseContext(),"You clicked on the next  button"+position,Toast.LENGTH_LONG).show();
                sessionID=leveList.get(position).getSesID();
                sessionYear=leveList.get(position).getSession();
             Intent intent=new Intent(levelList.this,semesterList.class);
             startActivity(intent);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.multiplesession,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int menuId=item.getItemId();
        switch(menuId){
            // call result dialog
            case R.id.multiplesessionItem1 :mutSession();
                        displayTotalGrades();
                break;
            case R.id.multiplesessionItem2 :
                Intent intent=new Intent(levelList.this,editUserProfile.class);
                startActivity(intent);
        }
        return true;

    }

    private void deleteDialog(){
        // This handle warning dialog of the level
        ViewGroup viewGroup=findViewById(android.R.id.content);

        // Then we will inflate he custome alert dialog xml that we crated
        View rldView=LayoutInflater.from(this).inflate(R.layout.removelevelwarndialog,viewGroup,false);

        // Now we need an AlertDDialog.Builder object
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        // Setting the viw of the builder to our custom view taht we already inflated
        builder.setView(rldView);

        // Finally creating the alert dialog and displaying it
        final AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
        tv=rldView.findViewById(R.id.levelHeadings);
       tv.setText( leveList.get(levPos).getLevel()+" Level");
        yes=rldView.findViewById(R.id.rlwdYes);
        no=rldView.findViewById(R.id.rlwdNo);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Yes button clicked",Toast.LENGTH_LONG).show();
                //TODO need the id remove grade is overloaded with remove by sessionid and gradeId
                //leveList.remove(levPos);
                if(db.removeGrade(sessionID)==true){
                    if(db.removeSession(userid+"",leveList.get(levPos).getSesID()+" ")==true){
                        //TODO should include removal of semesters under a certain session
                        leveList.clear();
                        populateSession();
                        alAdapter.notifyDataSetChanged();
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getBaseContext(),"Error removing grades",Toast.LENGTH_SHORT).show();
                }




            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"no button clicked",Toast.LENGTH_LONG).show();
                alertDialog.dismiss();

            }
        });

    }

            private void showCutomDialog(int state){
        // before inflatin the custom alert dialog layout, we will get the currently active viewGroup
               // getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                final int mstate=state;
                 ViewGroup viewGroup=findViewById(android.R.id.content);

                // Then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(this).inflate(R.layout.leveldialog,viewGroup,false);

                // Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder=new AlertDialog.Builder(this);

                // Setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);


                // Finally creating the alert dialog and displaying it
                final AlertDialog alertDialog=builder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
             //   levelDialog.super.onDismiss(getDialog());
                alertDialog.show();
                // alertDialog view assignment

                level=dialogView.findViewById(R.id.dialogLevel);
                ses=dialogView.findViewById(R.id.dialogSession);
                cancle=dialogView.findViewById(R.id.cancleLevel);
                add=dialogView.findViewById(R.id.addLevelDialog);
                switch (state){
                    case 2:  try{
                        level.setText(leveList.get(levPos).getLevel()+"");
                        ses.setText(leveList.get(levPos).getSession()+"");
                        add.setText("Edit");

                    }catch (Exception e){
                        Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                    break;
                    case 1: level.setText(" ");
                            ses.setText(" ");
                            break;

                }

                   // This is used to add and update using an state

                add.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                    //Toast.makeText(getBaseContext(),"hello!",Toast.LENGTH_LONG).show();
                    // TODO get and update the level list
                        // This checks if the fields to get information from is empty or not.


                    switch (mstate){
                        case 1:
                            if(level.getText().toString().trim().isEmpty()){
                                level.setError("Empty field");

                            }else if (ses.getText().toString().trim().isEmpty()){
                                ses.setError("Empty field");
                            }else{
                               if(db.addSession(ses.getText().toString().trim(),Integer.parseInt(level.getText().toString().trim()),userid)==true){
                                   leveList.clear();
                                  populateSession();
                                   alAdapter.notifyDataSetChanged();
                                   alertDialog.dismiss();
                               }else{
                                   Toast.makeText(getBaseContext(),"Error inserting into database",Toast.LENGTH_LONG).show();
                               }
                              //  leveList.add(new addLevelModel(Integer.parseInt(level.getText().toString().trim()),ses.getText().toString(),R.drawable.ic_more,R.drawable.delete,R.drawable.next,"1st","2rnd"));

                            }

                            break;
                        case 2:
                            //leveList.add(levPos,new addLevelModel(Integer.parseInt(level.getText().toString().trim()),ses.getText().toString(),R.drawable.ic_more,R.drawable.delete,R.drawable.next,"1st","2rnd"));
                             //alAdapter.notifyDataSetChanged();
                            if(level.getText().toString().trim().isEmpty()){
                                level.setError("Empty field");
                            }else if (ses.getText().toString().trim().isEmpty()){
                                ses.setError("Empty field");
                            }else{
                                //leveList.get(levPos).setLevel(Integer.parseInt(level.getText().toString().trim()));
                                //leveList.get(levPos).setSession(ses.getText().toString());
                                if(db.editSession(ses.getText().toString().trim(),level.getText().toString(),leveList.get(levPos).getSesID()+"",userid+"")==true){
                                    leveList.clear();
                                    populateSession();
                                    alAdapter.notifyDataSetChanged();
                                    alertDialog.dismiss();
                                }else{
                                    Toast.makeText(getBaseContext(),"Not inserted",Toast.LENGTH_SHORT).show();
                                }



                            }

                            break;

                            default:
                                break;
                    }



                    }
                });

                cancle.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(getBaseContext(),"cancle clicked",Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    }
                });
            }

            public void populateSession() {
                // leveList.add(new addLevelModel(100,"2017-2018",R.drawable.ic_more,R.drawable.delete,R.drawable.next,"1st","2rnd"));
                res = db.getSession(userid + "");
                while (res.moveToNext()) {
                    leveList.add(new addLevelModel(res.getInt(2), res.getString(1), R.drawable.ic_more, R.drawable.delete, R.drawable.next, "1st", "2rnd", res.getInt(0)));

                }
            }

    public void onPause(){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("sessionDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("sessionId",sessionID);
        editor.putString("sessionYear",sessionYear);
        //editor.putString("test","worked for string");
        editor.apply();
        super.onPause();
    }


    public void multipleSession(){
        // Select all Id of a session with respect to a certain user
        // store id in an array
        // use the selected session id to get grades
        // LOOP one for grade and another for semester
        // Total grade result for a session should be in an array
        // The size of all array should be equivalent to size of total sessions id
        // calculate grades semester by semester and display result
        ser=db.getSessionId(userid+"");
      int sessionsID[]=new int [ser.getCount()];
      int creditSum[]=new int [ser.getCount()*2];
      int credit[]=new int [ser.getCount()*2];
      double grade[]=new double[ser.getCount()*2];
      double totalGrade[]=new double[ser.getCount()];
      double tempGrade[]=new double[ser.getCount()];
        ser=db.getSessionId(userid+"");
      String[] semester={"1","2"};
        double gradeTotal=0;
      int a=0;
      while (ser.moveToNext()){
          sessionsID[a]=ser.getInt(0);
          a++;
      }
      // looping through sessions
        for(int c=0;c<sessionsID.length;c++){
            for(int d=0;d<semester.length;d++){
                // res=db.getGrades(userid+"",sessionid+"",semester[a]+"");
                res=db.getGrades(userid+"",sessionsID[c]+"",semester[d]);
                while(res.moveToNext()){
                  //  Toast.makeText(getApplicationContext(), res.getCount()+"daniela",Toast.LENGTH_SHORT).show();
                    creditSum[a]+=res.getInt(1);
                    //Toast.makeText(getApplicationContext(),res.getInt(1)+"",Toast.LENGTH_SHORT).show();
                    credit[a]+=sm.getIntGrade(sm.getGrade(res.getInt(0)))*res.getInt(1);
                }
                try{
                    grade[a]=(double) (credit[a]/creditSum[a]);
                }catch(Exception e){grade[0]=0;}

               // Toast.makeText(getApplicationContext(),grade[a]+"",Toast.LENGTH_SHORT).show();
            }
        }
        // To Achieve a total grade
        // which is the addition of two grades in the array and dividing by 2
        // which will be divided by the total lenght by 2
        int d=0,u=0;
        for(int b=0;b<grade.length;b+=2){
            tempGrade[d]+=grade[b];
            d++;
        }
        for(int m=1; m<grade.length;m+=2){
            tempGrade[u]+=grade[m];
        }
        for(int q=0;q<tempGrade.length;q++){
            gradeTotal+=tempGrade[q]/2;
        }

        Toast.makeText(getApplicationContext(),gradeTotal+"GPA",Toast.LENGTH_SHORT).show();

    }

    public double mutSession(){
        double firstSemester=0,secondSemester=0;
        res=db.getAllSessionsGrade(userid+"");
        entryyear=new String[res.getCount()];
        grades=new double[res.getCount()];
        // for the total grade calculation
        double totalGrades=0;

        int a=0;
        while (res.moveToNext()){
            entryyear[a]=res.getString(0);
            if(res.getString(1)==null){
                firstSemester=0;
                // First semester grade might not have been calculated
            }else{
                firstSemester=Double.parseDouble(res.getString(1)+"");
            }
            if(res.getString(2)==null){
                // second semester grade might not have been calculated
                secondSemester=0;
            }else{
                secondSemester=Double.parseDouble(res.getString(2)+"");
            }
         grades[a]=(firstSemester+secondSemester)/2;
          a++;
        }
        for(int d=0; d<grades.length;d++){
            totalGrades+=grades[d];
        }
       return totalGrades/res.getCount();
    }
    public void displayTotalGrades(){
        ViewGroup viewGroup=findViewById(R.id.content);

        View totalGrade=LayoutInflater.from(this).inflate(R.layout.displaysessionsgrade,viewGroup,false);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(totalGrade);

        final AlertDialog alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
        imageButton=totalGrade.findViewById(R.id.mutSesExit);
        head=totalGrade.findViewById(R.id.mutsesHead);
        body=totalGrade.findViewById(R.id.mutsesBody);
        head.setText("Total GRADE");
          String mut=" ";
          for(int a=0; a<entryyear.length;a++){
              mut+=entryyear[a]+":"+grades[a]+"\n";
          }
          mut+="TOTAL GRADE :"+mutSession();
          body.setText(mut);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
