package com.app.gradepoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class addLevelAdapter extends RecyclerView.Adapter<addLevelAdapter.addLeveHolder> {
    private ArrayList<addLevelModel> addleveList;

    private OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void  onItemclick(int position);
        void  onDeleteClick(int position);
        void  onEditClick(int position);
        void  onNextClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListener listner){
        mlistener=listner;
    }

    public static class addLeveHolder extends RecyclerView.ViewHolder{
           public TextView level,session,first,second;
           public ImageButton next,edit,delete;

        public addLeveHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            level=itemView.findViewById(R.id.addlevellevel);
            session=itemView.findViewById(R.id.addlevelsession);
            first=itemView.findViewById(R.id.addlevelfirst);
            second=itemView.findViewById(R.id.addlevelsecond);
            next=itemView.findViewById(R.id.addlevelnext);
            edit=itemView.findViewById(R.id.addleveledit);
            delete=itemView.findViewById(R.id.addleveldelete);


            // Setting the row clicks and the other clicks within the row
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemclick(position);

                        }
                    }

                }
            });
                // handle click for delete
                // needs to call a dialog asking the user for confirmation of delete
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

            // to handle click for the next
            next.setOnClickListener(new View.OnClickListener(){
                @Override

                public void onClick(View view){
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onNextClick(position);
                        }
                    }
                }
            });

            // handle click for the edit button
            // Should send the data to be edited to the dialog
            // add button should be changed to edit
            // data should be returned as edited to the actual place/database

            edit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });


        }


    }
    public addLevelAdapter(ArrayList<addLevelModel>addLevelModelArrayList){
        addleveList=addLevelModelArrayList;
    }
    @NonNull
    @Override
    public addLeveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.levetemp,parent,false);
    addLeveHolder alh =new addLeveHolder(v,mlistener);
        return alh;
    }

    @Override
    public void onBindViewHolder(@NonNull addLeveHolder holder, int position) {
        addLevelModel currentModel=addleveList.get(position);
        holder.delete.setImageResource(currentModel.getDelete());
        holder.edit.setImageResource(currentModel.getEdit());
        holder.first.setText(currentModel.getFirst());
        holder.second.setText(currentModel.getSecond());
        holder.level.setText(currentModel.getLevel()+"");
        holder.session.setText(currentModel.getSession()+"");
        holder.edit.setImageResource(currentModel.getEdit());
        holder.next.setImageResource(currentModel.getNext());

    }

    @Override
    public int getItemCount() {
        return addleveList.size();
    }


}
