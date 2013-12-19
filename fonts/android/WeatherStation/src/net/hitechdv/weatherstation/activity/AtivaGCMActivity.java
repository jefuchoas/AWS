package net.hitechdv.weatherstation.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import net.hitechdv.weatherstation.util.GCM;
import net.hitechdv.weatherstation.R;

public class AtivaGCMActivity extends Activity {
	
	private Button botaoAtivarDesativar;
	private boolean gcmAtivo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gcm);
		botaoAtivarDesativar = (Button) findViewById(R.id.botao_ativar_desativar);
		gcmAtivo = GCM.isAtivo(getApplicationContext());
		defineLabelBotao();
	}
	
	public void ativaDesativaGCM(View view) {
		if (GCM.isAtivo(getApplicationContext())) {
			GCM.desativa(getApplicationContext());
			gcmAtivo = false;
			Toast.makeText(getApplicationContext(), "Serviço de alertas desativado!", Toast.LENGTH_LONG).show();
		} else {
			GCM.ativa(getApplicationContext());
			gcmAtivo = true;
			Toast.makeText(getApplicationContext(), "Serviço de alertas ativado!", Toast.LENGTH_LONG).show();
		}
		defineLabelBotao();
	}

	private void defineLabelBotao() {
		if (gcmAtivo) {
			botaoAtivarDesativar.setText("Desativar alertas");
		} else {
			botaoAtivarDesativar.setText("Ativar alertas");
		}
	}

}
