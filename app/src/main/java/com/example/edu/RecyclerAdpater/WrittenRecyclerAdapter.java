package com.example.edu.RecyclerAdpater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edu.R;
import com.example.edu.model.LikesModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class WrittenRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<LikesModel> likesModels = new ArrayList<>();
    String auth;
    List<String> uidList = new ArrayList<>();


    public WrittenRecyclerAdapter(Context context) {

        this.context = context;
        auth = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseDatabase.getInstance().getReference().child("group").orderByChild("uid/").equalTo(auth).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                likesModels.clear();
                uidList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    likesModels.add(dataSnapshot1.getValue(LikesModel.class));
                    String uidKey = dataSnapshot1.getKey();
                    uidList.add(uidKey);
                }
                notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_myboard, parent, false);

        return new CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((CustomViewHolder)holder).tvTitle.setText(likesModels.get(position).groupName);
        ((CustomViewHolder)holder).tvShortTitle.setText(likesModels.get(position).groupShortTitle);
        ((CustomViewHolder)holder).tvCurrentMembers.setText(Integer.toString(likesModels.get(position).favCount));
        ((CustomViewHolder)holder).tvLimit.setText(Integer.toString(likesModels.get(position).groupLimit));


        ((CustomViewHolder)holder).itemView.findViewById(R.id.ivClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupDelete(position);
            }
        });

    }

    private void groupDelete(int position){

        FirebaseDatabase.getInstance().getReference().child("group").child(uidList.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "그룹을 삭제하였습니다.", Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return likesModels.size();
    }

    private class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvShortTitle, tvCurrentMembers, tvLimit;
        ImageView iv, ivClear;

        public CustomViewHolder(View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iv);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvShortTitle = itemView.findViewById(R.id.tvShortTitle);
            tvCurrentMembers = itemView.findViewById(R.id.tvCurrentMembers);
            tvLimit = itemView.findViewById(R.id.tvLimit);
            ivClear = itemView.findViewById(R.id.ivClear);

        }
    }
}
