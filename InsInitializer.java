/***
 * By Nat Roth, Sabir Meah, & David Shuster
 * Last Updated 4 May, 2020
 ***/
 
 /***
 * NOTE: This class really exists for the sole purpose of being able to very 
 * easily get or change the list of available instruments.
 * How about trying the Goblin synth effect next?
 ***/

import java.util.ArrayList;

public class InsInitializer{
    public static ArrayList <Instrument> getInsList(){
        //Shout-out to http://fmslogo.sourceforge.net/manual/midi-instrument.html from which the instruments were chosen (accessed 3 May, 2020).
        ArrayList<Instrument> insList = new ArrayList<Instrument>();
        insList.add(new Instrument("Acoustic Grand Piano", 0));
        insList.add(new Instrument("Music Box", 10));
        insList.add(new Instrument("Rock Organ", 18));
        insList.add(new Instrument("Distortion Guitar", 30));
        insList.add(new Instrument("Slap Bass", 36));
        insList.add(new Instrument("Bagpipes", 109));
        insList.add(new Instrument("Woodblock", 115));
        insList.add(new Instrument("Telephone Ring", 124));
        insList.add(new Instrument("Helicopter", 125));        
        insList.add(new Instrument("Gunshot", 127));
		insList.add(new Instrument("Bird Tweet", 123));
		insList.add(new Instrument("Seashore", 122));

        //Add more instruments here, if desired.

        return insList;
    }    
}