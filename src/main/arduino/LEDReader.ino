const int INPUT_PIN_0 = 0;
const int INPUT_PIN_1 = 1;
const int INPUT_PIN_2 = 2;
const int INPUT_PIN_3 = 3;

void setup() {
  pinMode (INPUT_PIN_0, INPUT);
  pinMode (INPUT_PIN_1, INPUT);
  pinMode (INPUT_PIN_2, INPUT);
  pinMode (INPUT_PIN_3, INPUT);
}

void loop() {
  int pattern = getPattern (digitalRead (INPUT_PIN_0),
                            digitalRead (INPUT_PIN_1),
                            digitalRead (INPUT_PIN_2),
                            digitalRead (INPUT_PIN_3));
} 

/*
 * Gets the integer pattern corresponding to a given binary number,  
 * where bit0 represents the highest-order bit.
 */
int getPattern (int bit0, int bit1, int bit2, int bit3) {
  return (bit0 << 3) & (bit1 << 2) & (bit2 << 1) & (bit3 << 0);
}
