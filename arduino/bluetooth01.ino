#define AT 2
#define LED 13
#define ENA1 4
#define ENA2 5
#define ENB1 6
#define ENB2 7

bool isUp = true;
void setup()
{
  pinMode(LED, OUTPUT);
  pinMode(ENA1, OUTPUT);
  pinMode(ENA2, OUTPUT);
  pinMode(ENB1, OUTPUT);
  pinMode(ENB2, OUTPUT);
  pinMode(AT, OUTPUT);
  digitalWrite(AT, HIGH);
  Serial.begin(9600);//这里应该和你的模块通信波特率一致
  delay(100);
  Serial.println("AT");
  delay(100);
  Serial.println("AT+NAME=OPENJUMPER-Bluetooth");//命名模块名
  delay(100);
  Serial.println("AT+ROLE=1");//设置主从模式：0从机，1主机
  delay(100);
  Serial.println("AT+PSWD=12345");//设置配对密码，如1234
  delay(100);
  Serial.println("AT+UART=9600,0,0");//设置波特率9600，停止位1，校验位无
  delay(100);
  Serial.println("AT+RMAAD");//清空配对列表
  digitalWrite(LED, LOW);
}
void loop()
{
  // digitalWrite(LED, LOW);
  // delay(500);
  //  val = "";
  char val;
  if (Serial.available() > 0) //判断串口缓冲器是否有数据装入
  {
    val = Serial.read(); //读取串口
    if (val == 'u') { //判断输入的字符是否为a{
      isUp = true;
      digitalWrite(ENA1, HIGH);
      digitalWrite(ENA2, LOW);
      digitalWrite(ENB1, HIGH);
      digitalWrite(ENB2, LOW);
    } else if (val == 'l') {
      if (isUp) {

        digitalWrite(ENB1, LOW);
        digitalWrite(ENB2, HIGH);
        digitalWrite(ENA1, HIGH);
        digitalWrite(ENA2, LOW);
//        delay(100);
//        digitalWrite(ENA1, LOW);
//        digitalWrite(ENA2, LOW);
//        digitalWrite(ENB1, LOW);
//        digitalWrite(ENB2, LOW);
      } else {
        digitalWrite(ENA1, LOW);
        digitalWrite(ENA2, HIGH);
        digitalWrite(ENB1, HIGH);
        digitalWrite(ENB2, LOW);
//        delay(100);
//        digitalWrite(ENA1, LOW);
//        digitalWrite(ENA2, LOW);
//        digitalWrite(ENB1, LOW);
//        digitalWrite(ENB2, LOW);
      }
    } else if (val == 'r') {
      if (isUp) {
        digitalWrite(ENA1, LOW);
        digitalWrite(ENA2, HIGH);
        digitalWrite(ENB1, HIGH);
        digitalWrite(ENB2, LOW);
//        delay(100);
//        digitalWrite(ENA1, LOW);
//        digitalWrite(ENA2, LOW);
//        digitalWrite(ENB1, LOW);
//        digitalWrite(ENB2, LOW);
      } else {
        digitalWrite(ENA1, HIGH);
        digitalWrite(ENA2, LOW);
        digitalWrite(ENB1, LOW);
        digitalWrite(ENB2, HIGH);
//        delay(100);
//        digitalWrite(ENA1, LOW);
//        digitalWrite(ENA2, LOW);
//        digitalWrite(ENB1, LOW);
//        digitalWrite(ENB2, LOW);
      }
    } else if (val == 'd') {
      isUp = false;
      digitalWrite(ENA1, LOW);
      digitalWrite(ENA2, HIGH);
      digitalWrite(ENB1, LOW);
      digitalWrite(ENB2, HIGH);
    } else if (val == 's') {
      isUp = false;
      digitalWrite(ENA1, LOW);
      digitalWrite(ENA2, LOW);
      digitalWrite(ENB1, LOW);
      digitalWrite(ENB2, LOW);
    }
  }
}
