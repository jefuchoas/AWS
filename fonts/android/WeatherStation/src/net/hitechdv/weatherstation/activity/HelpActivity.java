package net.hitechdv.weatherstation.activity;

import android.app.Activity;
import android.os.Bundle;

import net.hitechdv.weatherstation.R;

public class HelpActivity extends Activity {
	
	protected void carregaTelaPrincipal() {
		setContentView(R.layout.help);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carregaTelaPrincipal();
    }
}
