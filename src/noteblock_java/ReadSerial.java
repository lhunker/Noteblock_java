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

	public static ComboBoxModel<String> getPorts(){
		DefaultComboBoxModel<String> m;
		m = new DefaultComboBoxModel<String>();
		String[] ports = SerialPortList.getPortNames();
		for(String s : ports){
			m.addElement(s);
		}

		return m;
	}

	public ReadSerial()
	{
		//create file for output
		try {
			Files.write(Paths.get("../test.txt"), new String().getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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

	private void writeFile(String aFile, String write){
		try{
			Path path = Paths.get(aFile);
			Files.write(path, write.getBytes(Charset.forName("UTF-8")), StandardOpenOption.APPEND);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

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
	
	public void startMidi(MidiControl m){
		String in = null;
		int p = -1;
		try {
			//in = port.readString();
			in = port.readString(7);
			System.out.print(in);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
		if(in != null){
			String n1 = in.substring(0, in.indexOf(','));
			try{
				p = Integer.parseInt(n1);
			} catch (NumberFormatException e){
				e.printStackTrace();
			}
		}
		if(p != -1){
			m.playNote(p, 100);
		}
	}
	
	public void closePort(){
		try {
			port.closePort();
			System.out.println("DEBUG: port closed");
		} catch (SerialPortException e) {
			e.printStackTrace();
		}
	}

}
