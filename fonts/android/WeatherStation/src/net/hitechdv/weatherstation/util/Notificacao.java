package net.hitechdv.weatherstation.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import net.hitechdv.weatherstation.R;
import net.hitechdv.weatherstation.activity.WeatherStationActivity;

public class Notificacao {

	public static void mostraNotificacao(String titulo, String mensagem, Context context) {

		long tempoDefinido = System.currentTimeMillis();

		Notification notification = new Notification(R.drawable.ico, titulo, tempoDefinido);

		Intent intent = new Intent(context, WeatherStationActivity.class);
		intent.putExtra("alerta_recebido", mensagem);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, titulo, mensagem, pendingIntent);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		notification.defaults = Notification.DEFAULT_ALL;

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);
    }

}
