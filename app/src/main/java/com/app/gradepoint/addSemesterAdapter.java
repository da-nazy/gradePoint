package com.app.gradepoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class addSemesterAdapter extends RecyclerView.Adapter<addSemesterAdapter.addSemesterHolder> {
     private ArrayList<addSemesterModel> addSemesterList;

     private OnItemClickListener mListener;

     public interface  OnItemClickListener{
         void onItemClick(int position);
         void onNextClick(int position);
         void onDeleteClick(int position);
         void onEditClick(int position);
     }

     public void setOnItemClickListner(OnItemClickListener listener){
         mListener=listener;
     }
    public static class addSemesterHolder extends RecyclerView.ViewHolder{
        private ImageButton edit;
        private ImageButton delete;
        private ImageButton next;
        private TextView semester;

        public addSemesterHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            edit= itemView.findViewById(R.id.asEdit);
            delete=itemView.findViewById(R.id.asDelete);
            next=itemView.findViewById(R.id.asNext);
            semester=itemView.findViewById(R.id.asSemester);

            //
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // for edit button click
            edit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener!=null){
                        int position=getAdapterPosition();
                         if(position!=RecyclerView.NO_POSITION){
                             listener.onEditClick(position);
                         }

                    }
                }
            });

            // for nextClick
            next.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onNextClick(position);
                        }

                    }
                }
            });

            // for delete button click

            delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                     if(listener!=null){
                          int position=getAdapterPosition();
                          if(position!=RecyclerView.NO_POSITION){
                              listener.onDeleteClick(position);
                          }

                     }
                }
            });

        }
    }

   public addSemesterAdapter (ArrayList<addSemesterModel> addSemesterModelArrayList){
        addSemesterList=addSemesterModelArrayList;

   }
    @NonNull
    @Override
    public addSemesterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.semester,parent,false);
       addSemesterHolder ash=new addSemesterHolder(v,mListener);
       return ash;
    }

    @Override
    public void onBindViewHolder(@NonNull addSemesterHolder holder, int position) {
        addSemesterModel currentSemsterModel=addSemesterList.get(position);
        holder.edit.setImageResource(currentSemsterModel.getEdit());
        holder.delete.setImageResource(currentSemsterModel.getDelete());
        holder.next.setImageResource(currentSemsterModel.getNext());
        holder.semester.setText(currentSemsterModel.getSemester()+" ");
    }

    @Override
    public int getItemCount() {
        return addSemesterList.size();
    }
}
