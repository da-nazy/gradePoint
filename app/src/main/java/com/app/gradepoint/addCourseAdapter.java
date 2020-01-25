package com.app.gradepoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class addCourseAdapter extends RecyclerView.Adapter<addCourseAdapter.addCourseViewHolder> {
    // variable to contain list
    private ArrayList<addCourseModel>addCourseList;
    // Need a viewHolder
    private  OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
       this.mListener=listener;
    }
    public static class addCourseViewHolder extends RecyclerView.ViewHolder{

            public ImageButton delete,edit;
            public TextView cCode;
            public TextView cGrade;
            public TextView cCreditLoad;


        public addCourseViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            delete=itemView.findViewById(R.id.addCourseDelete);
            cCode=itemView.findViewById(R.id.addCourseCode);
            cGrade=itemView.findViewById(R.id.addCourseGrade);
            cCreditLoad=itemView.findViewById(R.id.addCourseLoad);
            edit=itemView.findViewById(R.id.addleveledit);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                     if(listener!=null){
                         int position=getAdapterPosition();
                         if(position!=RecyclerView.NO_POSITION){
                             listener.onDeleteClick(position);
                         }
                     }
                }
            });

            edit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onEditClick(position);
                    }
                }
            });
        }
    }

     public addCourseAdapter(ArrayList<addCourseModel> addCourseModelArrayList){
        addCourseList=addCourseModelArrayList;
     }
    @NonNull
    @Override
    public addCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.addcourse,parent,false);
       addCourseViewHolder ach=new addCourseViewHolder(v,mListener);
       return ach;
    }

    @Override
    public void onBindViewHolder(@NonNull addCourseViewHolder holder, int position) {
      // pass the information to our view
        addCourseModel currentModel=addCourseList.get(position);
        holder.delete.setImageResource(currentModel.getDeleteImage());
        holder.cCode.setText(currentModel.getCourseCode());
        holder.cGrade.setText(getGrade(Integer.parseInt(currentModel.getCourseGrade())));
        holder.cCreditLoad.setText(currentModel.getCourseCreditLoad());
        holder.edit.setImageResource(currentModel.getEdit());
    }

    @Override
    public int getItemCount() {
        return addCourseList.size();
    }

    // To determine course grade using score
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
}


