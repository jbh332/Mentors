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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter_Likes extends RecyclerView.Adapter<RecyclerAdapter_Likes.ViewHolder> {

    Context context;
    private String uid;
    List<LikesModel> likesModels = new ArrayList<>();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    List<String> uidList = new ArrayList<>();

    public RecyclerAdapter_Likes(final Context context) {

        this.context = context;

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("group").orderByChild("userFavorites/" + uid).equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        likesModels.clear();
                        uidList.clear();
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            likesModels.add(item.getValue(LikesModel.class));
                            String uidKey = item.getKey();
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
    public RecyclerAdapter_Likes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_likeboard, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter_Likes.ViewHolder holder, final int position) {

        holder.tvTitle.setText(likesModels.get(position).groupName);
        holder.tvShortTitle.setText(likesModels.get(position).groupShortTitle);
        holder.tvCurrentMembers.setText(Integer.toString(likesModels.get(position).favCount));
        holder.tvLimit.setText(Integer.toString(likesModels.get(position).groupLimit));

        holder.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClearClicked(FirebaseDatabase.getInstance().getReference().child("group").child(uidList.get(position)));
                Toast.makeText(view.getContext(), "관심목록에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("group").orderByChild("userFavorites/" + uid).equalTo(true)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                likesModels.clear();
                                uidList.clear();
                                for (DataSnapshot item : dataSnapshot.getChildren()) {
                                    likesModels.add(item.getValue(LikesModel.class));
                                    String uidKey = item.getKey();
                                    uidList.add(uidKey);
                                }
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });


    }
    private void onClearClicked(DatabaseReference postRef) {

        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                LikesModel likesModel = mutableData.getValue(LikesModel.class);
                if (likesModel == null) {
                    return Transaction.success(mutableData);
                }

                if (likesModel.favorites.containsKey(auth.getCurrentUser().getUid())) { // 해당 유저(본인)이 입력되어있다면
                    likesModel.favCount = likesModel.favCount - 1;
                    likesModel.favorites.remove(auth.getCurrentUser().getUid()); // 제거
                } else {

                }

                mutableData.setValue(likesModel);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return likesModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv, ivClear;
        TextView tvTitle, tvShortTitle, tvCurrentMembers, tvLimit;


        public ViewHolder(View itemView) {
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
