# Automated Plant Watering System

A Java-based system that monitors soil moisture levels and automatically waters plants using an Arduino board. It includes real-time data visualization and an OLED display for status updates.

## Features

- **Real-time Moisture Monitoring**: Measures soil moisture levels using an analog sensor.
- **Automatic Watering**: Activates a water pump based on predefined moisture thresholds.
- **OLED Display**: Shows current moisture level and pump status on an SSD1306 display.
- **Time-Based Graph**: Plots moisture levels over a 30-second window using `StdDraw`.
- **Button Control**: Includes a button to potentially stop/control the system (functionality may require further implementation).

## Hardware Requirements

- Arduino Uno (with StandardFirmata firmware)
- Soil moisture sensor (connected to analog pin A1)
- Water pump (connected to digital pin D7)
- SSD1306 OLED display (I2C address 0x3C)
- Push button (connected to digital pin D4)
- Jumper wires and breadboard

## Software Requirements

- Java JDK 8+
- Firmata4J library
- Princeton `StdDraw` library (for graphing)
- Arduino IDE (to upload StandardFirmata)

## Installation

1. **Upload Firmata to Arduino**:
   - Open Arduino IDE.
   - Load `StandardFirmata` example from `Examples > Firmata`.
   - Upload to your Arduino board.

2. **Set Up Dependencies**:
   - Add these libraries to your project:
     - `firmata4j` (Maven: `org.firmata4j:firmata4j:2.3.8`)
     - `firmata4j-ssd1306` (for OLED support)
     - `introcs` (for `StdDraw`)

3. **Connect Components**:
   - Moisture Sensor → A1
   - Pump → D7
   - Button → D4
   - OLED Display → I2C pins (SDA: A4, SCL: A5 on Arduino Uno)

## Usage

1. **Run the Program**:
   ```bash
   java -cp ".:firmata4j-2.3.8.jar:ssd1306-1.0.0.jar:stdlib.jar" org.example.Main
   ```

2. **System Behavior**:
   - Moisture levels are checked every second.
   - The pump activates based on these thresholds:
     - **Dry (≥3.4V)**: Pump runs for 1 second.
     - **Damp (3.04V–3.4V)**: Pump runs for 0.3 seconds.
     - **Wet (<2.45V)**: Pump remains off.
   - Real-time graph updates every 0.5 seconds and resets every 30 seconds.

3. **OLED Display**:
   - Shows "Pump ON/OFF" and current voltage.

## Code Structure

- `Main.java`: Initializes hardware and starts the timer task.
- `MoistureTestCheck.java`: Contains logic for sensor reading, pump control, and data visualization.

## Troubleshooting

- **Board Connection Issues**: Ensure correct USB port in `myUsbPort`.
- **Missing Dependencies**: Verify all JAR files are in the classpath.
- **Incorrect Pin Setup**: Double-check hardware connections against pin assignments.

## Limitations

- Button functionality to stop the system may require further implementation.
- Graph visualization depends on external `StdDraw` library.

## Small Scale Implementation Presentation

 [Click Here](https://rumble.com/v1zakmq-eecs-1011-minor-project.html) for a small scale implementation of this project.

---

Developed with [Firmata4J](https://github.com/firmata4j/firmata4j) and [Princeton StdLib](https://introcs.cs.princeton.edu/java/stdlib/).
