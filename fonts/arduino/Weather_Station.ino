//=======================================================
// Importação das bibliotecas.
//=======================================================
#include <DHT.h>
#include <SPI.h>
#include <Ethernet.h>
#include <HttpClient.h>
#include <Xively.h>

//=======================================================
// Declaração das constantes do sistema.
//=======================================================
#define API_KEY "insira_sua_api_key_xively_aqui"
#define FEED_ID 187368934
#define uint  unsigned int
#define ulong unsigned long
#define PIN_ANEMOMETER  2     // Digital 2
#define PIN_VANE        5     // Analógico 5
#define PIN_RAINGAUGE   3     // Digital 3
#define PIN_DHT         7     // Digital 7

//=======================================================
// Com que frequência queremos calcular a velocidade do vento
//=======================================================
#define MSECS_CALC_WIND_SPEED 5000

volatile int numRevsAnemometer = 0;       // Incrementado a cada interrupção (contato)
volatile int numContactsRaingauge = 0;    // Incrementado a cada interrupção (contato)
unsigned long nextCalcSpeed;              // Quando será o próximo cálculo da velocidade
unsigned long time;                       // Millis() a cada início do loop().

//=======================================================
// Leituras ADC:
//=======================================================
#define NUMDIRS 8
ulong   adc[NUMDIRS] = {26, 45, 77, 118, 161, 196, 220, 256};

//=======================================================
// Estas direções correspondem uma a uma com os valores no adc, mas
// terão que ser ajustacas conforme a necessidade. Modificar 'dirOffset'
// para a direção que está 'a frente' (West (Oeste), neste caso).
//=======================================================
char *strVals[NUMDIRS] = {"W","NW","N","SW","NE","S","SE","E"};
byte dirOffset=0;

//=======================================================
// Declaração das variáveis do sistema.
//=======================================================
byte mac[] = { 0x90, 0xA2, 0xDA, 0x00, 0xED, 0x9E};
IPAddress ip(192,168,0, 177);
char server[] = "weatherstation-hitechdv.rhcloud.com";

float temp = 0;
float rain = 0;
String windDirection = "";
float windVelocity = 0;
float humidity = 0;

dht objetoDht;

unsigned long lastConnectionTime = 0;
const unsigned long connectionInterval = 900000;

char anemometerId[] = "anemometer";
char raingaugeId[] = "raingauge";
char temperatureId[] = "temperature";
char windVaneId[] = "windvane";
char humidityId[] = "humidity";

//=======================================================
// Declaração dos canais de dados Xively.
//=======================================================
XivelyDatastream datastreams[] = {
	XivelyDatastream(temperatureId, strlen(temperatureId), DATASTREAM_FLOAT),
	XivelyDatastream(raingaugeId, strlen(raingaugeId), DATASTREAM_FLOAT),
	XivelyDatastream(windVaneId, strlen(windVaneId), DATASTREAM_STRING),
	XivelyDatastream(anemometerId, strlen(anemometerId), DATASTREAM_FLOAT),  
	XivelyDatastream(humidityId, strlen(humidityId), DATASTREAM_FLOAT)};

//=======================================================
// Declaração do feed Xively.
//=======================================================
XivelyFeed feed(FEED_ID, datastreams, 5);

//=======================================================
// Declaração dos clientes ethernet.
//=======================================================
EthernetClient client;
EthernetClient c;
XivelyClient xivelyclient(client);

//=======================================================
// Método setup - inicialização dos parâmetros do sistema.
//=======================================================
void setup() {  
	Serial.begin(9600);
	pinMode(PIN_ANEMOMETER, INPUT);
	digitalWrite(PIN_ANEMOMETER, HIGH);
	pinMode(PIN_RAINGAUGE, INPUT);
	digitalWrite(PIN_RAINGAUGE, HIGH);
	attachInterrupt(0, countAnemometer, FALLING);
	attachInterrupt(1, countRaingauge, FALLING);
	nextCalcSpeed = millis() + MSECS_CALC_WIND_SPEED;

	if (Ethernet.begin(mac) == 0) {   	
		Ethernet.begin(mac, ip);
	}
}

//=======================================================
// Método loop - a cada execução são realizadas as medições.
// Atualiza variáveis de tempo quando ocorre o overflow da função millis()
//=======================================================
void loop() { 
			
  if (time > millis()) {
	nextCalcSpeed = millis() + MSECS_CALC_WIND_SPEED;
	lastConnectionTime = millis();
  }
	
  time = millis();

   if (time >= nextCalcSpeed) {
		windVelocity = calcWindSpeed();
		nextCalcSpeed = time + MSECS_CALC_WIND_SPEED;
	}
  
   if (time - lastConnectionTime >= connectionInterval) {    
    
		rain = calcRain();
		windDirection = calcWindDir();    
		int chk = objetoDht.read22(PIN_DHT);    
		temp = objetoDht.temperature;
		humidity = objetoDht.humidity;
    
		sendData(temp, rain, windDirection, windVelocity, humidity);
		sendDados(temp, rain, windDirection, windVelocity, humidity);  
		lastConnectionTime = millis();
	}
}

//=======================================================
// Método que realiza o envio dos dados para o Xively.
//=======================================================
void sendData(float temperature, float rain, String windDirection, float windVelocity, float humidity) {
	datastreams[0].setFloat(temperature);
	datastreams[1].setFloat(rain);
	datastreams[2].setString(windDirection);
	datastreams[3].setFloat(windVelocity); 
	datastreams[4].setFloat(humidity);
	int ret = xivelyclient.put(feed, API_KEY);
}

//=======================================================
// Método que realiza o envio dos dados para o OpenShift.
//=======================================================
void sendDados(float temp, float rain, String windDirection, float windVelocity,  float humidity) {    
    if (c.connect(server, 80)) {
		c.print("GET /rest/dados/insert/");
		c.print("senhaValidadora_");
		c.print(windVelocity);
		c.print("_");
		c.print(rain);
		c.print("_");
		c.print(temp);
		c.print("_");
		c.print(windDirection);
		c.print("_");
		c.print(humidity);     
		c.println(" HTTP/1.1");
		c.println("Host: weatherstation-hitechdv.rhcloud.com");
		c.println("User-Agent: arduino-ethernet");
		c.println("Connection: close");
		c.println();
		c.stop();
    } else {
		Serial.println("Nao conectado");
    }    
}

//=======================================================
// Controlador de interrupção para o sensor anemometer.
// Chamado cada vez que o gatilho é acionado (um giro).
//=======================================================
void countAnemometer() {
	numRevsAnemometer++;
}

//=======================================================
// Controlador de interrupção para o sensor raingauge.
// Chamado cada vez que o gatilho é acionado (um contato).
//=======================================================
void countRaingauge() {
	static unsigned long last_millis = 0;
	unsigned long m = millis();
	if (m - last_millis < 125){
		// Ignora a interrupção para compensar a velocidade de leitura do micro controlador
	}else{
		numContactsRaingauge++;
	}
	last_millis = m;
}

//=======================================================
// Encontra a direção do vento.
//=======================================================
String calcWindDir() {
   int val;
   byte x, reading;

   val = analogRead(PIN_VANE);
   val >>=2;                        
   reading = val;

   // Faz a leitura no ponteiro de direções e encontra
   // o primeiro valor >= ao valor lido.
   for (x=0; x<NUMDIRS; x++) {
      if (adc[x] >= reading)
         break;
   }
   x = (x + dirOffset) % 8;   // Ajuste para a direção
   return strVals[x];
}

//=======================================================
// Calcula a velociade do vento.
// 1 volta/segundo = 1.492 mph = 2.4 kmh
//=======================================================
double calcWindSpeed() {
   double x;
   // Este método gera kmh * 10
   double iSpeed = 24000;
   iSpeed *= numRevsAnemometer;
   iSpeed /= MSECS_CALC_WIND_SPEED;

   x = iSpeed / 10;
   numRevsAnemometer = 0;        // Reseta o contador
   return x;
}

//=======================================================
// Calcula a soma da chuva dentro do período de coleta.
// 1 contato = 0.2794 mm
//=======================================================
double calcRain() {
   double x;
   // Este método gera chuva * 10
   double iRain = 2.794;
   iRain *= numContactsRaingauge;

   x = iRain / 10;
   numContactsRaingauge = 0;        // Reseta o contador
   return x;
}
