package com.app.gradepoint;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class editUserProfile extends AppCompatActivity {
EditText name,emailPhone,department,faculty,duration,regno,entryMode,country,state;
Button update;
Cursor res;
SharedPreferences sp;
int userid;
DatabaseHelper db=new DatabaseHelper(this);
    @Override
    protected void onCreate (Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.profile);
        name=findViewById(R.id.proname);
        emailPhone=findViewById(R.id.proemailphone);
        department=findViewById(R.id.prodepartment);
        faculty=findViewById(R.id.profaculty);
        duration=findViewById(R.id.produration);
        regno=findViewById(R.id.proregno);
        entryMode=findViewById(R.id.proentrymode);
        country=findViewById(R.id.procountry);
        state=findViewById(R.id.prostate);
        update=findViewById(R.id.proupdate);
        getValue();
        res=db.getUser(userid+"");
        while(res.moveToNext()){
            name.setText(res.getString(1)+" ");
            emailPhone.setText(res.getString(2)+" ");
            department.setText(res.getString(3)+" ");
            faculty.setText(res.getString(4)+" ");
            duration.setText(res.getInt(5)+"");
            regno.setText(res.getString(6)+" ");
            entryMode.setText(res.getString(7)+" ");
            country.setText(res.getString(8)+" ");
            state.setText(res.getString(9)+" ");
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()==true){
                if(db.updateUser(userid+"",name.getText().toString().trim(),emailPhone.getText().toString(),department.getText().toString().trim(),faculty.getText().toString().trim(),Integer.parseInt(duration.getText().toString().trim()),regno.getText().toString().trim(),entryMode.getText().toString().trim(),country.getText().toString().trim(),state.getText().toString().trim())==true){
                    Toast.makeText(getBaseContext(),"User updated",Toast.LENGTH_SHORT).show();
                }
                }else{
                    Toast.makeText(getBaseContext(),"Empty fields detected",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    // can object be stored in an array
    public boolean check(){
         boolean check=false;
         if(name.getText().toString().trim().isEmpty()){
             name.setError("Empty field");
             check=false;
         }else if(emailPhone.getText().toString().trim().isEmpty()){
             emailPhone.setError("Empty field");
             check=false;
         }else if(department.getText().toString().trim().isEmpty()){
             department.setError("Empty field");
             check=false;
         }else if(faculty.getText().toString().trim().isEmpty()){
             faculty.setError("Empty field");
             check =false;
         }else if(duration.getText().toString().trim().isEmpty()){
             duration.setError("Empty field");
             check=false;
         }else if(regno.getText().toString().trim().isEmpty()){
             regno.setError("Empty field");
             check=false;
         }else if(entryMode.getText().toString().trim().isEmpty()){
             entryMode.setError("Empty field");
             check=false;
         }else if(country.getText().toString().trim().isEmpty()){
             country.setError("Empty field");
             check=false;
         }else if(state.getText().toString().trim().isEmpty()){
             state.setError("Empty field");
             check=false;
         }else{
             check=true;
         }
        return check;
    }
    public void getValue(){
       sp=getSharedPreferences("userdetails",MODE_PRIVATE) ;
       userid=sp.getInt("userid",-1);
    }
}
