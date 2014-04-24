/**
 * 
 */
package noteblock_java;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;

import themidibus.*;

/**
 * @author Lukas
 * @version 1.0
 * An interface to create a midi output and send notes to it
 */
public class MidiControl {
	MidiBus midi;
	
	/**
	 * constructs a new noteblock midi controller
	 */
	public MidiControl(){
		midi = new MidiBus();
	}
	
	/**
	 * gets a combobox model with the available MIDI outputs
	 * @return the available midi outputs
	 */
	public static ComboBoxModel<String> getOuts(){
		DefaultComboBoxModel<String> m = new DefaultComboBoxModel<String>();
		String[] outs = MidiBus.availableOutputs();
		for(String s: outs){
			m.addElement(s);
		}
		
		return m;
	}
	
	/**
	 * adds a midi output
	 * @param out the name of the output
	 */
	public void selectOut(String out){
		midi.addOutput(out);
	}
	
	/**
	 * plays a midi note using both note on and note off
	 * currently uses presets for velocity and channel
	 * @param num the note number
	 * @param dur the duration of the note
	 */
	public void playNote(int num, int dur){
		Note n = new Note(1, num, 70, dur);
		midi.sendNoteOn(n);
	}
	
	/**
	 * plays the notes and durations through midi
	 * NOTE: nums and dur must be same size and less than 10
	 * @param nums the note pitches
	 * @param dur the note durations
	 */
	public void playNotes(int[] nums, int[]dur){
		for (int i = 0; i < nums.length; i++){
			Note n = new Note(i, nums[i], 70, dur[i]);
			midi.sendNoteOn(n);
		}
	}
}
