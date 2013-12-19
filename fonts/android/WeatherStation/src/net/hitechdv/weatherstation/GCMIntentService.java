package net.hitechdv.weatherstation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import net.hitechdv.weatherstation.rest.WeatherStationREST;
import net.hitechdv.weatherstation.util.Constantes;
import net.hitechdv.weatherstation.util.Notificacao;
import net.hitechdv.weatherstation.util.WakeLocker;

public class GCMIntentService extends GCMBaseIntentService {
    private WeatherStationREST wsREST = new WeatherStationREST();
	
	public GCMIntentService() {
        super("inserir_seu_numero_sender_id_aqui");
    }

	protected void onRegistered(Context context, String regId) {
		Log.i(Constantes.TAG, "Serviço de Alertas ativado.");
		String mensagem = "ID de registro no Serviço de Alertas: " + regId;
		Log.i(Constantes.TAG, mensagem);
        try {
            wsREST.inserirDevice(regId);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	protected void onError(Context context, String errorMessage) {
		Log.e(Constantes.TAG, "Erro: " + errorMessage);
	}

	protected void onMessage(Context context, Intent intent) {

        WakeLocker.acquire(getApplicationContext());
		String mensagem = intent.getExtras().getString("mensagem");
		Log.i(Constantes.TAG, "Alerta recebido: " + mensagem);
		if (mensagem != null && !"".equals(mensagem))
			Notificacao.mostraNotificacao("Alerta da Weather Station!", mensagem, context);

        WakeLocker.release();
	}

	protected void onUnregistered(Context context, String regId) {
		Log.i(Constantes.TAG, "Serviço de alertas desativado.");
        try {
            wsREST.removerDevice(regId);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}
