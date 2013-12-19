package net.hitechdv.weatherstation.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.hitechdv.weatherstation.bd.Banco;
import net.hitechdv.weatherstation.bd.BancoConfiguracao;
import net.hitechdv.weatherstation.bd.DadosBanco;
import net.hitechdv.weatherstation.model.DadosDiarios;
import net.hitechdv.weatherstation.model.DadosMeteorologicos;
import net.hitechdv.weatherstation.rest.WeatherStationREST;
import net.hitechdv.weatherstation.R;
import net.hitechdv.weatherstation.util.GCM;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class WeatherStationActivity extends Activity {
	private static final int AJUDA = Menu.FIRST;
	private static final int SOBRE = Menu.FIRST+1;
    private static final int ALERTA = Menu.FIRST+2;
	
	TextView temp = null;
	TextView max = null;
	TextView min = null;
	TextView rain = null;
	TextView rainSum = null;
	TextView chuvaAgora = null;
	TextView vento = null;
	TextView direcao = null;
	TextView umidade = null;
	TextView orvalho = null;
	TextView coleta = null;
	TextView textoTemperatura = null;
	TextView textoAcumulada = null;
	TextView textoHoje = null;
	TextView textoGraus1 = null;
	TextView textoGraus2 = null;
	TextView textoGraus3 = null;
	TextView textoGraus4 = null;
	TextView textoMaxima = null;
	TextView textoMinima = null;
	TextView textoChuva = null;
	TextView textoMilimetros1 = null;
	TextView textoMilimetros2 = null;
	TextView textoKilometros = null;
	TextView textoVento = null;
	TextView textoAmbiente = null;
	TextView textoUmidade = null;
	TextView textoOrvalho = null;
	TextView textoPercentual = null;
	
	LinearLayout layout1 = null;
	LinearLayout layout2 = null;
	LinearLayout layout3 = null;
	LinearLayout layout4 = null;
	LinearLayout layout5 = null;
	
	DadosMeteorologicos dm = new DadosMeteorologicos();
	DadosDiarios dd = new DadosDiarios();
	List<DadosDiarios> ddList = new ArrayList<DadosDiarios>();
    String threeDaysRain = "";
	private Banco banco;
	DadosBanco dados = new DadosBanco();
	VerificaConexao async;
	int bt = 0;
	
	public void carregaTelaPrincipal() {
		
        setContentView(R.layout.main);
        
        textoTemperatura = (TextView)findViewById(R.id.labelTemperatura);        
        textoChuva = (TextView)findViewById(R.id.labelChuva);
        textoAmbiente = (TextView)findViewById(R.id.labelAmbiente);
        
        banco = new Banco(new BancoConfiguracao(this));
        
        ImageView btAtualizar = (ImageView) findViewById(R.id.imageLogo);
        
        exibirTitulos();
        
        btAtualizar.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		exibirTitulos();
        		bt = 1;
        		async.onPostExecute(async.doInBackground());        				
			}		
        });        
	}	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        async = (VerificaConexao) new VerificaConexao().execute();
    }
    
    @Override 
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, AJUDA, 0, "Ajuda");
		menu.add(0, SOBRE, 0, "Sobre");
        menu.add(0, ALERTA, 0, "Alerta");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case AJUDA: 
			Intent iAjuda = new Intent(WeatherStationActivity.this, HelpActivity.class);
            startActivity(iAjuda);
			return true;
		case SOBRE:
			Intent iSobre = new Intent(WeatherStationActivity.this, AboutActivity.class);
            startActivity(iSobre);
			return true;
        case ALERTA:
            Intent iAlerta = new Intent(WeatherStationActivity.this, AtivaGCMActivity.class);
            startActivity(iAlerta);
            return true;
		default: return super.onOptionsItemSelected(item);
		}
	}
    
    public void toast (String msg){
        Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
    }
    
    private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(getApplicationContext(), message, duration);
		toast.show();
	}
    
    public void showMessage(String titulo, String mensagem){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(WeatherStationActivity.this);
    	dialog.setTitle(titulo);
    	dialog.setMessage(mensagem);
    	dialog.setNeutralButton("OK", null);
    	dialog.show();
    }
    
    public float arredondar(float a, int x) {			
		float b, c, d;			
		d = (float)Math.pow(10, x);			
		b = (float)(a * d);	                
        c = (float)(Math.rint(b)) / d;	        
        return c;
    }
    
    public Float formatarFloat(String num, String formato) {		
        Float fNum = Float.parseFloat(num);
        NumberFormat formatador = new DecimalFormat(formato);
        String novoNum = formatador.format(fNum);
        novoNum = novoNum.replace(",", ".");
        Float retorno = Float.parseFloat(novoNum);        
        return retorno;
    }
    
    private void exibirTitulos() {
    	textoTemperatura.setText("Temperatura");
    	textoChuva.setText("Chuva");
    	textoAmbiente.setText("Ambiente");    	
    }
    
    private void preencherTela(String temperatura, String maxima, String minima, String ch, String chA, String rSum, String wind, String direction, String dew, String humy, String ultimaColeta) {
    	temp.setText(temperatura);
    	max.setText(maxima);
    	min.setText(minima);
    	rain.setText(ch);
    	chuvaAgora.setText(chA);
    	rainSum.setText(rSum);
    	vento.setText(wind);
    	direcao.setText(direction);
    	orvalho.setText(dew);
    	umidade.setText(humy);
    	coleta.setText(ultimaColeta);    	
    	textoGraus1.setText("ºC");
    	textoGraus2.setText("ºC");
    	textoGraus3.setText("ºC");
    	textoGraus4.setText("ºC");
    	textoMaxima.setText("Max");
    	textoMinima.setText("Min");    	
    	textoMilimetros1.setText("mm");
    	textoMilimetros2.setText("mm");
    	textoHoje.setText("Hoje");
    	textoAcumulada.setText("Chuva em 3 dias");
    	textoVento.setText("Vento");
    	textoKilometros.setText("Km/h");    	
    	textoUmidade.setText("Umidade");
    	textoOrvalho.setText("Orvalho");
    	textoPercentual.setText("%");
    	}
    
   private void limparTela() {
    	temp.setText("");
    	max.setText("");
    	min.setText("");
    	rain.setText("");
    	chuvaAgora.setText("");
    	rainSum.setText("");
    	vento.setText("");
    	direcao.setText("");
    	orvalho.setText("");
    	umidade.setText("");
    	coleta.setText("");
    	textoTemperatura.setText("");
    	textoGraus1.setText("");
    	textoGraus2.setText("");
    	textoGraus3.setText("");
    	textoGraus4.setText("");
    	textoMaxima.setText("");
    	textoMinima.setText("");
    	textoChuva.setText("");
    	textoMilimetros1.setText("");
    	textoMilimetros2.setText("");
    	textoHoje.setText("");
    	textoAcumulada.setText("");
    	textoVento.setText("");
    	textoKilometros.setText("");
    	textoAmbiente.setText("");
    	textoUmidade.setText("");
    	textoOrvalho.setText("");
    	textoPercentual.setText("");
    	}    
      
  	public void insert(String temperatura, String maxima, String minima, String ch, String chA, String rSum, String wind, String direction, String dew, String humy, String data, String hora) {
  		ContentValues cv = new ContentValues();
  		cv.put("anemometer", wind);
	    cv.put("temperature", temperatura);
	    cv.put("humidity", humy);        
	    cv.put("windvane", direction);
	    cv.put("data", data);
	    cv.put("hora", hora);
	    cv.put("moisture", dew);
	    cv.put("intensity", chA);
	    cv.put("tempMinima", minima);        
	    cv.put("tempMaxima", maxima);
	    cv.put("chAcumulada", ch);
	    cv.put("chuvaTresDias", rSum);
	    banco.get().insert("dados", null, cv);
      }  	
  	
  	public void update(String id, String temperatura, String maxima, String minima, String ch, String chA, String rSum, String wind, String direction, String dew, String humy, String data, String hora) {
  		Banco bd = new Banco(new BancoConfiguracao(this));
  		bd.open();
        ContentValues cv = new ContentValues();
        cv.put("anemometer", wind);
  	    cv.put("temperature", temperatura);
  	    cv.put("humidity", humy);        
  	    cv.put("windvane", direction);
  	    cv.put("data", data);
  	    cv.put("hora", hora);
  	    cv.put("moisture", dew);
  	    cv.put("intensity", chA);
  	    cv.put("tempMinima", minima);        
  	    cv.put("tempMaxima", maxima);
  	    cv.put("chAcumulada", ch);
  	    cv.put("chuvaTresDias", rSum);
  	    bd.get().update("dados", cv, "id==?", new String[]{id});  	    
        bd.close();
      }
  	
  	public DadosBanco consultaBanco() {
  		Cursor cursor = banco.get().rawQuery("select * from dados limit ?", new String[]{"1"});
          while (cursor.moveToNext()) {
          	dados.setId((int)Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")).toString()));        	
          	dados.setAnemometer(cursor.getString(cursor.getColumnIndex("anemometer")));
          	dados.setTemperature(cursor.getString(cursor.getColumnIndex("temperature")));
          	dados.setHumidity(cursor.getString(cursor.getColumnIndex("humidity")));
          	dados.setWindvane(cursor.getString(cursor.getColumnIndex("windvane")));
          	dados.setData(cursor.getString(cursor.getColumnIndex("data")));
          	dados.setHora(cursor.getString(cursor.getColumnIndex("hora")));
          	dados.setMoisture(cursor.getString(cursor.getColumnIndex("moisture")));
          	dados.setIntensity(cursor.getString(cursor.getColumnIndex("intensity")));
          	dados.setTempMinima(cursor.getString(cursor.getColumnIndex("tempMinima")));
          	dados.setTempMaxima(cursor.getString(cursor.getColumnIndex("tempMaxima")));
          	dados.setChAcumulada(cursor.getString(cursor.getColumnIndex("chAcumulada")));
          	dados.setChuvaTresDias(cursor.getString(cursor.getColumnIndex("chuvaTresDias")));
          }                 
          cursor.close();
          return dados;
      }
  	
  	public class VerificaConexao extends AsyncTask<Void, Void, Boolean> {
  		
  		@Override
  		protected void onPreExecute(){ 
  			carregaTelaPrincipal();  	          
            super.onPreExecute();  
        } 
  		
  		@Override
  		protected Boolean doInBackground(Void... params) {
  			boolean lblnRet = false;
  			
  			temp = (TextView)findViewById(R.id.labelTemperature);
  	        max = (TextView)findViewById(R.id.labelMaxima);
  	        min = (TextView)findViewById(R.id.labelMinima);
  	        rain = (TextView)findViewById(R.id.labelRain);
  	        rainSum = (TextView)findViewById(R.id.labelRainSum);
  	        chuvaAgora = (TextView)findViewById(R.id.labelAgora);
  	        vento = (TextView)findViewById(R.id.labelVento);
  	        direcao = (TextView)findViewById(R.id.labelDirecao);
  	        umidade = (TextView)findViewById(R.id.labelHumidity);
  	        orvalho = (TextView)findViewById(R.id.labelMoisture);
  	        coleta = (TextView)findViewById(R.id.labelColeta);
  	        textoGraus1 = (TextView)findViewById(R.id.labelGraus1);
  	        textoGraus2 = (TextView)findViewById(R.id.labelGraus2);
  	        textoGraus3 = (TextView)findViewById(R.id.labelGraus3);
  	        textoGraus4 = (TextView)findViewById(R.id.labelGraus4);
  	        textoMaxima = (TextView)findViewById(R.id.labelMax);
  	        textoMinima = (TextView)findViewById(R.id.labelMin);
  	        textoMilimetros1 = (TextView)findViewById(R.id.labelMilimetros1);
  	        textoMilimetros2 = (TextView)findViewById(R.id.labelMilimetros2);
  	        textoAcumulada = (TextView)findViewById(R.id.labelAcumulada);
  	        textoHoje = (TextView)findViewById(R.id.labelHoje);
  	        textoKilometros = (TextView)findViewById(R.id.labelKilometros);
  	        textoVento = (TextView)findViewById(R.id.labelVentoAgora);
  	        textoOrvalho = (TextView)findViewById(R.id.labelOrvalho);
  	        textoUmidade = (TextView)findViewById(R.id.labelUmidade);
  	        textoPercentual = (TextView)findViewById(R.id.labelPercentual);
  	        layout1 = (LinearLayout)findViewById(R.id.layout1);
  	        layout2 = (LinearLayout)findViewById(R.id.layout2);
  	        layout3 = (LinearLayout)findViewById(R.id.layout3);
  	        layout4 = (LinearLayout)findViewById(R.id.layout4);
  	        layout5 = (LinearLayout)findViewById(R.id.layout5);
  			
  	        try
  	        {
  	            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); 
  	            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) { 
  	                lblnRet = true; 
  	            } else { 
  	                lblnRet = false; 
  	            }
  	        }catch (Exception e) {            
  	        }
  	        return lblnRet;
  		}
  		
  		@Override
  		protected void onPostExecute(Boolean result) {  
  			if (result == true) {  
  				WeatherStationREST wsREST = new WeatherStationREST();
  				try {
  					dm = wsREST.getDadosMeteorologicos();
  					dd = wsREST.getDadosDiarios();
  					ddList = wsREST.getListaDadosDiarios();
                    threeDaysRain = wsREST.getThreeDaysRain();
  					if ((dm != null) && (dd != null) && (ddList != null)) {
  						int gravado = 0;
  			        	String temperatura = "" + dm.getTemperature();
  			        	String maxima = "" + dd.getTempMaxima();
  			        	String minima = "" + dd.getTempMinima();
  			        	String ch = "" + dd.getChAcumulada();
  			        	String chA = dm.getIntensity();
  			        	float chuvaTresDias = arredondar((float) Float.parseFloat(threeDaysRain), 2);
                        if (chuvaTresDias >= 100) {
                            rainSum.setTextColor(getResources().getColor(R.color.red));
                        }
                        else if (chuvaTresDias > 80){
                            rainSum.setTextColor(getResources().getColor(R.color.darkOrange));
                        }
                        else {
                            rainSum.setTextColor(getResources().getColor(R.color.dimGray));
                        }
  			        	String rSum = "" + chuvaTresDias;
  			        	String wind = "" + dm.getAnemometer();
  			        	String direction = "";
  			        	if (dm.getAnemometer() > 0) {
  			        		direction = dm.getWindvane();
  			        	} else direction = "";
  			        	String dew = "" + dm.getMoisture();
  			        	String humy = "" + dm.getHumidity();
  			        	String ultimaColeta = "" + "Dados coletados às " + dm.getHora();
  			        	String data = dm.getData();
  			        	String hora = dm.getHora();
  			        	
  			        	banco.open();		    		
  			    		dados = consultaBanco();		    		
  			    		gravado = dados.getId();		    		
  			    		if (gravado == 0) {
                            insert(temperatura, maxima, minima, ch, chA, rSum, wind, direction, dew, humy, data, hora);
                            GCM.ativa(getApplicationContext());
                        }
  			    		banco.close();
  			    		if (gravado == 1) update("1", temperatura, maxima, minima, ch, chA, rSum, wind, direction, dew, humy, data, hora);
  			    		if (dm.getHora().equals(dados.getHora()) && bt == 1)toast("Os dados exibidos são os mais recentes");
  			        	preencherTela(temperatura, maxima, minima, ch, chA, rSum, wind, direction, dew, humy, ultimaColeta);		        	
  			        	}
  					} catch (Exception e) {
  						limparTela();
  						e.printStackTrace();
  						if (e.getMessage().equals("Falha de rede!")) gerarToast("A conexão está muito lenta");
  						else gerarToast(e.getMessage());
  						}  
  			} else {
  	    		int gravado = 0;
  	    		banco.open();
  	    		dados = consultaBanco();
  	    		gravado = dados.getId();
  	    		banco.close();
  	    		if (gravado == 1) {
  	    			showMessage("Não há conexão de dados", "Serão exibidos os dados da última leitura que foram salvos localmente. Para exibir os dados atualizados verifique sua conexão e tente novamente.");
  	    			String temperatura = dados.getTemperature();
  	    			String maxima = dados.getTempMaxima();
  	    			String minima = dados.getTempMinima();
  	    			String ch = dados.getChAcumulada();
  	    			String chA = "";
  	    			float chuvaTresDias = formatarFloat(dados.getChuvaTresDias(), "0.00");
                    if (chuvaTresDias >= 100) {
                        rainSum.setTextColor(getResources().getColor(R.color.red));
                    }
                    else if (chuvaTresDias > 80){
                        rainSum.setTextColor(getResources().getColor(R.color.darkOrange));
                    }
                    else {
                        rainSum.setTextColor(getResources().getColor(R.color.dimGray));
                    }
  		        	String rSum = "" + chuvaTresDias;
  	    			String wind = dados.getAnemometer();
  		        	String direction = "";
  		        	float velocity = formatarFloat(dados.getAnemometer(), "0.00");
  		        	if ( velocity > 0) {
  		        		direction = dados.getWindvane();
  		        	} else direction = "";
  		        	String dew = dados.getMoisture();
  		        	String humy = dados.getHumidity();
  		        	String ultimaColeta = "Dados coletados às " + dados.getHora() + " de " + dados.getData();
  		        	preencherTela(temperatura, maxima, minima, ch, chA, rSum, wind, direction, dew, humy, ultimaColeta);
  	    		} else {
  	    			showMessage("Não há conexão de dados", "Verifique sua conexão e tente novamente.");
  	    			limparTela();
  	    		}    		  
  	    	}   
  		}
  	}
}