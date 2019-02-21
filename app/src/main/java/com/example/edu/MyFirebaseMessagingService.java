package com.example.edu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.example.edu.RecyclerAdpater.ChatRecyclerAdapter;
import com.example.edu.model.ChatModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) { // remoteMessage는 푸쉬알람이 도착했다고 알려주는 기능을 한다
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title").toString();
            String text = remoteMessage.getData().get("text").toString();
            sendNotification(title, text);

            String uid;
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ChatRecyclerAdapter.chatModels.clear();
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                ChatRecyclerAdapter.chatModels.add(item.getValue(ChatModel.class));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }


        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendNotification(String title, String text) {
//        오레오 버전 push 코드 // 그대로 쓰면 사용 불가 // 이해 후 코드 변경해야 사용 가능
//         NotificationManager notifManager = (NotificationManager) getSystemService  (Context.NOTIFICATION_SERVICE);
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel mChannel = new NotificationChannel(
//                    title, text, importance);
//            notifManager.createNotificationChannel(mChannel);
//        }
//
//        NotificationCompat.Builder builder =
//                new NotificationCompat.Builder(getApplicationContext(), title);
//
//        Intent notificationIntent = new Intent(getApplicationContext()
//                , MainActivity.class);
//
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        int requestID = (int) System.currentTimeMillis();
//
//        PendingIntent pendingIntent
//                = PendingIntent.getActivity(getApplicationContext()
//                , requestID
//                , notificationIntent
//                , PendingIntent.FLAG_UPDATE_CURRENT);
//
//        builder.setContentTitle(title) // required
//                .setContentText(text)  // required
//                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
//                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
//                .setSound(RingtoneManager
//                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setSmallIcon(android.R.drawable.btn_star)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources()
//                        , R.mipmap.ic_launcher))
//                .setBadgeIconType(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent);
//        notifManager.notify(0, builder.build());



        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                        .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH) //헤드업이 뜨려면 좌측 코드가 있어야 한다
                        .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(title,
                    text,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//        MainActivity.adapter.notifyDataSetChanged();
//        ChatRecyclerAdapter.chatModels.clear();

    }



}
