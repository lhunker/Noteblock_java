package noteblock_processing;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import jssc.SerialPort;
import jssc.SerialPortException;
import processing.core.*;
import themidibus.*;


public class ReadSerial extends PApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SerialPort port;

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "noteblock_processing.ReadSerial" });

	}

	//private Serial port;

	public void setup()
	{
		//declare new serial port
		  port = new SerialPort("COM13");
	        try {
	            System.out.println("DEBUG: Port opened: " + port.openPort());
	            System.out.println("DEBUG: Params setted: " + port.setParams(9600, 8, 1, 0));
	        }
	        catch (SerialPortException ex){
	            System.out.println(ex);
	        }

		size(100,100);

	}

	public void writeFile(String aFile, String write){
		try{
			Path path = Paths.get(aFile);
			Files.write(path, write.getBytes(Charset.forName("UTF-8")), StandardOpenOption.APPEND);
		} catch (IOException e){
			//do nothing for now 
		}
	}

	public void draw()
	{
//		String inBuff = port.readStringUntil(10);
//		if(inBuff != null){
//			System.out.println(inBuff);
//			writeFile ("test.txt", inBuff);
//		}

	}

	public void sendNote()
	{
		//Send a note.
	}

}
