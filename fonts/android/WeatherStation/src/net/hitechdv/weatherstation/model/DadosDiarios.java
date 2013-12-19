package net.hitechdv.weatherstation.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DadosDiarios implements Serializable {
	private String id;
	private float tempMedia;
	private float tempMinima;
	private float tempMaxima;
	private float chAcumulada;
    private String data;
 
    public DadosDiarios() {
        super();
    }
 
    public DadosDiarios(float chAcumulada, float temperatura, String data) {
        this.tempMedia = temperatura;
        this.chAcumulada = chAcumulada;
        this.data = data;
    }
 
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	

	public float getTempMedia() {
		return tempMedia;
	}

	public void setTempMedia(float tempMedia) {
		this.tempMedia = tempMedia;
	}

	public float getTempMinima() {
		return tempMinima;
	}

	public void setTempMinima(float tempMinima) {
		this.tempMinima = tempMinima;
	}

	public float getTempMaxima() {
		return tempMaxima;
	}

	public void setTempMaxima(float tempMaxima) {
		this.tempMaxima = tempMaxima;
	}	

	public float getChAcumulada() {
		return chAcumulada;
	}

	public void setChAcumulada(float chAcumulada) {
		this.chAcumulada = chAcumulada;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
