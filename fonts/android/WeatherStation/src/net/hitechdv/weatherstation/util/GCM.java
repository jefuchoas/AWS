package net.hitechdv.weatherstation.util;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

import net.hitechdv.weatherstation.rest.WeatherStationREST;

public class GCM {
    private static WeatherStationREST wsREST = new WeatherStationREST();


    public static void ativa(Context context) {
		
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		
		final String regId = GCMRegistrar.getRegistrationId(context);
		
		if (regId.equals("")) {
			GCMRegistrar.register(context, Constantes.SENDER_ID);
            Log.i(Constantes.TAG, "Serviço de alertas ativado.");
		} else {
			Log.i(Constantes.TAG, "O serviço de alertas já está ativo. ID: " + regId);
            try {
                wsREST.inserirDevice(regId);
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
	}
	
	public static void desativa(Context context) {
			GCMRegistrar.unregister(context);
			Log.i(Constantes.TAG, "Serviço de alertas desativado.");
	}
	
	public static boolean isAtivo(Context context) {
		return GCMRegistrar.isRegistered(context);
	}
}
