package com.app.gradepoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {
Button signup,login;
EditText email_phone,regNo;
Cursor res;
int id;
    String userDetails="userdetails";
DatabaseHelper db=new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cursor object creation

        setContentView(R.layout.activity_login);
        signup=findViewById(R.id.signUp);
        email_phone=findViewById(R.id.emailPhone);
        regNo=findViewById(R.id.regNum);
        login=findViewById(R.id.create);
        getSupportActionBar().setTitle("LOGIN");
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(check()==true){
               res=db.userLogin(email_phone.getText().toString().trim(),regNo.getText().toString().trim());
               if(res.moveToNext()){
                   Intent intent=new Intent(login.this,levelList.class);
                   id=res.getInt(0);
                 //  Toast.makeText(getBaseContext(),res.getInt(0)+"",Toast.LENGTH_LONG).show();
                   startActivity(intent);
               }else{
                   Toast.makeText(getBaseContext(),"User not found. Register!",Toast.LENGTH_LONG).show();
               }

                }



            }
        });
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
             
                Intent intent=new Intent(login.this,register.class);
                startActivity(intent);
            }
        });
    }

    public boolean check(){
        boolean valid=false;
        if(email_phone.getText().toString().isEmpty()){
            // empty input
            email_phone.setError("Empty value");
            valid=false;
        }else{
            // non empty input
            // check for email
            if(Patterns.EMAIL_ADDRESS.matcher(email_phone.getText().toString().trim()).matches()){
                // Matches an email address
                valid=true;
            }else{
                // doesn't match an email address check for number
                // trim() to remove space
                // first convert string to character array
                // check if character array is upt to eleven
                // check if character is an integer
                char[] phone=email_phone.getText().toString().trim().toCharArray();
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
                            email_phone.setError("Not a valid Phone Number");
                            valid=false;
                        }
                    }
                }else{
                    // phone not up to eleven digits
                    //customToast("Not up to eleven");
                    email_phone.setError("Eleven digits required");
                    valid=false;
                }
            }
        }
        if(regNo.getText().toString().trim().isEmpty()){
            valid=false;
            regNo.setError("Invalid registration Number");
        } else{
            valid=true;
        }
        return valid;
    }
    @Override
    public void onPause(){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(userDetails, MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("userid",id);
        editor.putString("test","worked for string");
        editor.apply();
        super.onPause();
    }
}
