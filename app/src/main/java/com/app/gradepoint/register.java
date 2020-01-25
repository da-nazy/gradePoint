package com.app.gradepoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;

public class register extends AppCompatActivity {
    int spinView;

Button signin;
Spinner direct_entry;
TextInputLayout textInputLayout;
ScrollView scrollView;
EditText name,emailPhone,department,faculty,duration,registrationNo,directEntry;
String entry_mode;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        scrollView=findViewById(R.id.scrollView2);


        bindView();
        db=new DatabaseHelper(getApplicationContext());
        // for Spinner Entry mode
        direct_entry=findViewById(R.id.spinnerde);
        textInputLayout=findViewById(R.id.dewrapper);
        getSupportActionBar().setTitle("REGISTER");
        String deitems[]={"Entry Mode:","Regular","Direct Entry"};

        ArrayAdapter ade=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,deitems);
        direct_entry.setAdapter(ade);
        direct_entry.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){


                    public void onItemSelected(AdapterView<?> parent,View view, int position, long id){
                        if(direct_entry.getSelectedItemPosition()==2){
                            textInputLayout.setVisibility(View.VISIBLE);
                            spinView=1;
                            //scrollView.notify();
                        }else{
                            textInputLayout.setVisibility(View.GONE);
                            spinView=2;
                        }

                    }


                    public void onNothingSelected(AdapterView<?> parent) {
                        customToast("Nothing was selected");
                    }


                });


            signin=findViewById(R.id.regSignIn);

            signin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent=new Intent(register.this,login.class);
                    startActivity(intent);
                }
            });
    }
    public void next(View view){
        /**
         *  Intent i=new Intent(register.this,levelList.class);
         *         startActivity(i);
         */
        if(check()){
            customToast("Well done codeblooded");
           if( db.addUser(name.getText().toString().trim(),emailPhone.getText().toString().trim(),department.getText().toString().trim(),faculty.getText().toString(),
                    Integer.parseInt(duration.getText().toString()),registrationNo.getText().toString(),entry_mode)==true){
             Intent intent=new Intent(register.this,login.class);
                startActivity(intent);
            }
        }
    }

public void bindView(){
        name=findViewById(R.id.name);
        emailPhone=findViewById(R.id.email);
        department=findViewById(R.id.dept);
        faculty=findViewById(R.id.faculty);
        duration=findViewById(R.id.duration);
        registrationNo=findViewById(R.id.regNo);
        directEntry=findViewById(R.id.directentry);
}
    // Check for creating account
    // creating a customToast
    public void customToast(String value){
        Toast.makeText(getApplicationContext(),value,Toast.LENGTH_LONG).show();
    }

    public boolean check(){
        boolean valid=false;

       if(emailPhone.getText().toString().trim().isEmpty()){
           // empty input
           emailPhone.setError("Empty value");
           valid=false;
       }else{
           // non empty input
           // check for email
           if(Patterns.EMAIL_ADDRESS.matcher(emailPhone.getText().toString().trim()).matches()){
              // Matches an email address
               valid=true;
           }else{
               // doesn't match an email address check for number
               // trim() to remove space
               // first convert string to character array
               // check if character array is upt to eleven
               // check if character is an integer
               char[] phone=emailPhone.getText().toString().trim().toCharArray();
               int phoneNumber[]=new int[phone.length];
               if(phone.length==11){
                   // phone is upto eleven digits
                   for(int a=0; a<phone.length; a++){
                       // convert from char to int
                       // check if not int and trow an error
                       // then convert from charArray to int Array
                       try{
                         phoneNumber[a]=Integer.parseInt(phone[a]+"");
                           valid=true;
                       }catch(Exception e){
                           e.printStackTrace();
                          // customToast("Not a valid Phone Number");
                           emailPhone.setError("Not a valid Phone Number");
                         valid=false;
                       }
                   }
               }else{
                   // phone not up to eleven digits
                   //customToast("Not up to eleven");
                   emailPhone.setError("Eleven digits required");
                   valid=false;
               }
           }
       }

       if(name.getText().toString().trim().isEmpty()){
           valid=false;
           name.setError("Name not valid");
       }else if(registrationNo.getText().toString().trim().isEmpty()){
            valid=false;
            registrationNo.setError("Invalid registration Number");
        }else if(faculty.getText().toString().trim().isEmpty()){
            valid=false;
            faculty.setError("Invalid Faculty");
        }else if(department.getText().toString().trim().isEmpty()){
           valid=false;
           department.setError("Department not valid");
       }else if(duration.getText().toString().trim().isEmpty()){
           duration.setError("Duration not valid");
           valid=false;
       }else if(direct_entry.getSelectedItemPosition()==0){
           customToast("Invalid mode of entry");
           valid=false;
       }else if(direct_entry.getSelectedItemPosition()==1){
              // selected Regular as mode of entry
           entry_mode="REGULAR";
             valid=true;
          }else if(spinView==1){
           if(directEntry.getText().toString().trim().isEmpty()){
               directEntry.setError("Invalid GP");
               valid=false;
           }else{
               entry_mode=directEntry.getText().toString();
               valid=true;
           }
       }else{
           valid=true;
       }




       return valid;

    }



}
