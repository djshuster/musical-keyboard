/***
* This class was written by Nat Roth.
* It handles finding the default local MIDI synthesizer and sequencer, stores them as fields,
* and closes them upon window close with a method called by MidiCloser.
* It interfaces all communication of other classes with those devices and their various objects,
* including turning notes on and off, changing the instrument, and loading MIDI files into the sequencer.
* (I've left all the fields public as well, just in case, but that could be changed.)
* The name is a relic of when this class was intended to handle recording and saving.
***/

import javax.sound.midi.*;
import java.io.*;

public class Song {
    public static Synthesizer _synth;
	public static MidiChannel _pianoChannel;
	public static Sequencer _sequencer;
    public static int _volume;
	private static boolean _fileLoaded;

	public Song(){
		try {
			//Obtain the default local synthesizer (probably Wavetable, sorry).
			_synth = MidiSystem.getSynthesizer();
			if (! _synth.isOpen()){
				_synth.open();
			}
			//Obtain the default local sequencer;
			//the sequencer is the part that sends music from the loaded sequence to the synthesizer.
			_sequencer = MidiSystem.getSequencer();
			if (! _sequencer.isOpen()){
				_sequencer.open();
			}
			//Set the sequencer to send events to the synth.
			_sequencer.getTransmitter().setReceiver(_synth.getReceiver());
		} catch(MidiUnavailableException e){
			System.out.println("Looks like there was a problem with obtaining your local synthesizer or sequencer.");
		}

		//Synthesizer channels usually function as different instruments.
		//This program's piano plays on the synthesizer's last channel, because the loaded files use the same channels,
		//and this minimizes the likelihood that they'll interact.
		//However, it's recommended to check new MIDI files in a separate editor to make sure there's nothing on channel 15.
		//(All files in the program folder have been checked/altered.)
		_pianoChannel = _synth.getChannels()[15];
		_fileLoaded = false;

		//If desired, change volume of keyboard output here.
		_volume = 80;
	}

	//The following four methods function to interface the other classes' communication with the synthesizer.
	//(There's potential for access control.)
	public void noteOn(int note){
		_pianoChannel.noteOn(note, _volume);
	}

	public void noteOff(int note){
		_pianoChannel.noteOff(note);
	}

	public void allNotesOff(){
		_pianoChannel.allNotesOff();
	}

	public void changeInstrument(int instrument){
		_pianoChannel.programChange(instrument);
	}

	//This method is currently unused, but would make it very easy to adjust the keyboard volume.
	public void changeVolume(int volume){
		if (volume >= 0){
			_volume = volume;
		}
	}

	//This method obtains the MIDI sequence from a .mid file and loads it into the sequencer.
	public void loadFile(String path){
		if (path.endsWith(".mid")){
			try {
				_sequencer.setSequence(MidiSystem.getSequence(new File(path)));
				_fileLoaded = true;
			} catch(InvalidMidiDataException | IOException e){
				System.out.println("There was a problem loading a MIDI file.");
			}
		}
	}

	//This method starts the sequencer running (once it has a sequence to run).
	public void play(){
		if (_fileLoaded && !_sequencer.isRunning()){
			try {
				_sequencer.start();
			} catch(IllegalStateException e){}
		}
	}

	//This interfaces the sequencer's stop command.
	public void stopPlaying(){
		_sequencer.stop();
	}

	//This method is called by MidiCloser when the window is closed to release the program's MIDI devices.
	public void close(){
		_sequencer.close();
		_synth.close();
	}
}