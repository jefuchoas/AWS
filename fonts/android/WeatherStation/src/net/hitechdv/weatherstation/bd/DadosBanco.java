package net.hitechdv.weatherstation.bd;

public class DadosBanco {
	private int id;
	private String anemometer;
	private String temperature;
	private String humidity;
	private String windvane;
    private String data;
    private String hora;
    private String moisture;
    private String intensity;
    private String tempMinima;
	private String tempMaxima;
	private String chAcumulada;
	private String chuvaTresDias;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAnemometer() {
		return anemometer;
	}
	public void setAnemometer(String anemometer) {
		this.anemometer = anemometer;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
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
	public String getMoisture() {
		return moisture;
	}
	public void setMoisture(String moisture) {
		this.moisture = moisture;
	}
	public String getIntensity() {
		return intensity;
	}
	public void setIntensity(String intensity) {
		this.intensity = intensity;
	}
	public String getTempMinima() {
		return tempMinima;
	}
	public void setTempMinima(String tempMinima) {
		this.tempMinima = tempMinima;
	}
	public String getTempMaxima() {
		return tempMaxima;
	}
	public void setTempMaxima(String tempMaxima) {
		this.tempMaxima = tempMaxima;
	}
	public String getChAcumulada() {
		return chAcumulada;
	}
	public void setChAcumulada(String chAcumulada) {
		this.chAcumulada = chAcumulada;
	}
	public String getChuvaTresDias() {
		return chuvaTresDias;
	}
	public void setChuvaTresDias(String chuvaTresDias) {
		this.chuvaTresDias = chuvaTresDias;
	}		
}
