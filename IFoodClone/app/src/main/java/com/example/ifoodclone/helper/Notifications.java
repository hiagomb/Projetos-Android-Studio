package com.example.ifoodclone.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.ifoodclone.R;
import com.example.ifoodclone.activity.PedidosActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Notifications extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if(message.getNotification()!= null){
            String titulo= message.getNotification().getTitle();
            String corpo= message.getNotification().getBody();
            enviar_notificacao(titulo, corpo);
        }
    }

    private void enviar_notificacao(String titulo, String corpo){
        String canal= "fcm_default_channel";
        Intent i= new Intent(this, PedidosActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_ONE_SHOT);
        //criando notificação
        NotificationCompat.Builder builder= new NotificationCompat.Builder(this, canal);
        builder.setContentTitle(titulo).setContentText(corpo).setSmallIcon(R.drawable.ic_baseline_list_alt_24)
        .setAutoCancel(true).setContentIntent(pendingIntent);

        NotificationManager notificationManager= (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel(canal, "canal",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, builder.build());

    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }
}
