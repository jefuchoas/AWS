package net.hitechdv.weatherstation.rest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import net.hitechdv.weatherstation.model.DadosDiarios;
import net.hitechdv.weatherstation.model.DadosMeteorologicos;

import java.util.ArrayList;
import java.util.List;

public class WeatherStationREST {
	
private static final String URL_WS = "http://weatherstation-hitechdv.rhcloud.com/rest/dados/";
	
	public DadosMeteorologicos getDadosMeteorologicos() throws Exception {
		String[] resposta = new WebServiceWeatherStation().get(URL_WS + "dadosMeteorologicos");
		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			DadosMeteorologicos dm = gson.fromJson(resposta[1], DadosMeteorologicos.class);
			return dm;
			} else {
				throw new Exception(resposta[1]);
				}
		}
	
	public DadosDiarios getDadosDiarios() throws Exception {
		String[] resposta = new WebServiceWeatherStation().get(URL_WS + "dadosDiarios");
		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			DadosDiarios dd = gson.fromJson(resposta[1], DadosDiarios.class);
			return dd;
			} else {
				throw new Exception(resposta[1]);
				}
		}
	
	public List<DadosMeteorologicos> getListaDadosMeteorologicos() throws Exception {
		String[] resposta = new WebServiceWeatherStation().get(URL_WS + "dadosMeteorologicosList");
		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			ArrayList<DadosMeteorologicos> listaDadosMeteorologicos = new ArrayList<DadosMeteorologicos>();
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(resposta[1]).getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				listaDadosMeteorologicos.add(gson.fromJson(array.get(i), DadosMeteorologicos.class));
				}
			return listaDadosMeteorologicos;
			} else {
				throw new Exception(resposta[1]);
				}
		}
	
	public List<DadosDiarios> getListaDadosDiarios() throws Exception {
		String[] resposta = new WebServiceWeatherStation().get(URL_WS + "dadosDiariosList");
		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			ArrayList<DadosDiarios> listaDadosDiarios = new ArrayList<DadosDiarios>();
			JsonParser parser = new JsonParser();
			JsonArray array = parser.parse(resposta[1]).getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				listaDadosDiarios.add(gson.fromJson(array.get(i), DadosDiarios.class));
				}
			return listaDadosDiarios;
			} else {
				throw new Exception(resposta[1]);
				}
		}

    public String getThreeDaysRain() throws Exception {
        String[] resposta = new WebServiceWeatherStation().get(URL_WS + "threeDaysRain");
        String chuva = "";
        if (resposta[0].equals("200")) {
            JsonParser parser = new JsonParser();
            chuva = parser.parse(resposta[1]).getAsString();
        } else {
            throw new Exception(resposta[1]);
        }
        return chuva;
    }

    public String inserirDevice(String deviceId) throws Exception {
        Gson gson = new Gson();
        String deviceJSON = gson.toJson(deviceId);
        String[] resposta = new WebServiceWeatherStation().post(URL_WS + "insertDevice", deviceJSON);
        if (resposta[0].equals("200")) {
            return resposta[1];
        } else {
            throw new Exception(resposta[1]);
        }
    }

    public String removerDevice(String deviceId) throws Exception {
        Gson gson = new Gson();
        String deviceJSON = gson.toJson(deviceId);
        String[] resposta = new WebServiceWeatherStation().post(URL_WS + "removeDevice", deviceJSON);
        if (resposta[0].equals("200")) {
            return resposta[1];
        } else {
            throw new Exception(resposta[1]);
        }
    }

}
