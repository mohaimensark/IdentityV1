package com.example.identity;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<String> values ;
    ArrayList<String> useridlist ;
    Context c ;

// Constructor
    public MyAdapter( Context c , ArrayList<String> Values, ArrayList<String> usreidlist) {
        this.c = c ;
        this.values = Values ;
        this.useridlist = usreidlist ;
    }
    @NonNull



//1
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(c).inflate(R.layout.rowlayout,parent,false) ;
        MyViewHolder VH = new MyViewHolder(v) ;
        return VH ;
    }


//2
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.rowID.setText(values.get(position))  ;
        holder.rowID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, OtherUserActivity.class);
                intent.putExtra("uid",useridlist.get(position)) ;
                c.startActivity(intent);
            }
        });
    }


//3
    @Override
    public int getItemCount() {
        return values.size() ;
    }



//4
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView rowID ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            rowID = (TextView) itemView.findViewById(R.id.rowID) ;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
