package com.example.edu.RecyclerAdpater;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edu.R;
import com.example.edu.ReservationActivity;
import com.example.edu.model.StudyRoomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReservationRecyclerAdapter extends RecyclerView.Adapter<ReservationRecyclerAdapter.ViewHolder> {
//    ArrayList<Boolean> data;
    Context context;
    List<StudyRoomModel.Day> studyRoomModels = new ArrayList<>();

    //초기화
    public ReservationRecyclerAdapter(Context context) {
//        this.data = data;
        this.context = context;

        FirebaseDatabase.getInstance().getReference()
                .child("studyroom").child("No101").child("Monday")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        studyRoomModels.clear();
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                           // studyRoomModels.add(item.getValue(StudyRoomModel.Day.class));
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //뷰홀더 생성후 리턴
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_room, parent, false);

        return new ViewHolder(view);
    }

    //뷰의 내용
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvRoom.setText((position + 9) + " : 00 ~ " + (position + 10) + " : 00");

        if (studyRoomModels.get(position).reservation) {
            holder.tvRoom.setTextColor(Color.GRAY);
            holder.tvRoom.setEnabled(false);
        } else {
            holder.tvRoom.setTextColor(Color.BLACK);
            holder.tvRoom.setEnabled(true);
        }

        final String tvRoom = holder.tvRoom.getText().toString();
        holder.tvRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener reservationListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //예약 실행
                        //요일은 dayOfWeek에서, 시간은 position으로 컨트롤.
                        Toast.makeText(context, ReservationActivity.sCurrentRoom + " " + ReservationActivity.sCurrentDayOfWeek + "요일 " + tvRoom + " " + " 예약 완료", Toast.LENGTH_SHORT).show();
                    }
                };

                new AlertDialog.Builder(context)
                        .setTitle(ReservationActivity.sCurrentRoom + " " + ReservationActivity.sCurrentDayOfWeek + "요일 " + tvRoom + " " + " 예약 하시겠습니까?")
                        .setPositiveButton("예약", reservationListener)
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }

    //생성할 수
    @Override
    public int getItemCount() {
        return studyRoomModels.size();
    }

    //뷰홀더
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoom;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRoom = itemView.findViewById(R.id.tvRoom);
        }
    }
}
