package noteblock_java;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import themidibus.*;


public class ReadSerial{


	private SerialPort port;

	/**
	 * sends a comboBox model with the available ports
	 * @return a comboBox model with available strings
	 */
	public static ComboBoxModel<String> getPorts(){
		DefaultComboBoxModel<String> m;
		m = new DefaultComboBoxModel<String>();
		String[] ports = SerialPortList.getPortNames();
		for(String s : ports){
			m.addElement(s);
		}

		return m;
	}

	/**
	 * a default constructor
	 */
	public ReadSerial()
	{
		//create file for output
		try {
			Files.write(Paths.get("../test.txt"), new String().getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * starts the serial reader with the specified port
	 * @param p the name of the port to open
	 */
	public void setPort(String p){
		//declare new serial port
		port = new SerialPort("COM13");
		try {
			System.out.println("DEBUG: Port opened: " + port.openPort());
			System.out.println("DEBUG: Params setted: " + port.setParams(9600, 8, 1, 0));
		}
		catch (SerialPortException ex){
			System.out.println(ex);
		}

	}

	/**
	 * write the input to the specified file
	 * @param aFile the file path to write to
	 * @param write the string to write
	 */
	private void writeFile(String aFile, String write){
		try{
			Path path = Paths.get(aFile);
			Files.write(path, write.getBytes(Charset.forName("UTF-8")), StandardOpenOption.APPEND);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * polls the serial port for input then writes it to the file
	 */
	public void pollPort()
	{
		String inBuff = null;
		try {
			inBuff = port.readString();
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		if(inBuff != null){
			System.out.print(inBuff);
			writeFile ("../test.txt", inBuff);
		}

	}

	/**
	 * sends the midi signal from the cube to the midi controller
	 * @param m the midi controller to send the note to
	 */
	public void sendMidi(MidiControl m){
		String in = null;
		int p1 = -1;
		int p2 = -1;
		try {
			//in = port.readString();
			in = port.readString(7);
			System.out.print(in);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		if(in != null){
			String n1 = in.substring(0, in.indexOf(','));
			String n2 = in.substring(in.indexOf(',') + 1, in.indexOf('\r'));
			try{
				p1 = Integer.parseInt(n1);
				p2 = Integer.parseInt(n2);
			} catch (NumberFormatException e){
				e.printStackTrace();
			}
		}
		if(p1 != -1 && p2!= -1){
			m.playNotes(new int[] {p1, p2}, new int [] {100, 100});
		}
	}

	/**
	 * closes the serial port
	 */
	public void closePort(){
		try {
			port.closePort();
			System.out.println("DEBUG: port closed");
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

	/**
	 * sends the toggle cub sound signal if the port is open
	 */
	public void soundSwitch(){
		if(port.isOpened()){
			try {
				port.writeString("S\n");
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * sends the signal to change the cube program
	 * @param prog the program number to change to
	 */
	public void setProg(int prog){
		if(port.isOpened()){
			try {
				port.writeString("P" + prog + "\n");
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		}
	}

}
