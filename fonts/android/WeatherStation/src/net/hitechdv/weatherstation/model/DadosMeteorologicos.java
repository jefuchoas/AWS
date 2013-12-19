package net.hitechdv.weatherstation.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DadosMeteorologicos implements Serializable {
	private String id;
	private float anemometer;
	private float raingauge;
	private float temperature;
	private float humidity;
	private String windvane;
    private String data;
    private String hora;
    private float moisture;
    private String origin;
    private String intensity;
 
    public DadosMeteorologicos() {
        super();
    }
 
    public DadosMeteorologicos(float anemometer, float raingauge, float temperature, String windvane, String data, String hora, float humidity, float moisture, String origin, String intensity) {
    	this.anemometer = anemometer;
        this.raingauge = raingauge;
        this.temperature = temperature;
        this.windvane = windvane;
        this.data = data;
        this.hora = hora;
        this.humidity = humidity;
        this.moisture = moisture;
        this.origin = origin;
        this.intensity = intensity;
    }
          
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getAnemometer() {
		return anemometer;
	}

	public void setAnemometer(float anemometer) {
		this.anemometer = anemometer;
	}

	public float getRaingauge() {
		return raingauge;
	}

	public void setRaingauge(float raingauge) {
		this.raingauge = raingauge;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}	

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public String getWindvane() {
		return windvane;
	}

	public void setWindvane(String windvane) {
		this.windvane = windvane;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}	

	public float getMoisture() {
		return moisture;
	}

	public void setMoisture(float moisture) {
		this.moisture = moisture;
	}	

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}	

	public String getIntensity() {
		return intensity;
	}

	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}
}
