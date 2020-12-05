/***
 * By Nat Roth, Sabir Meah, & David Shuster
 * Last Updated 4 May, 2020
 ***/
 
/***
* class Instrument provides a way to keep track of the available instruments.
***/

public class Instrument{
    public final String instrumentName;
    public final int midiNumber;
	
	//Constructor
    public Instrument(String name, int num){
        instrumentName = name;
        midiNumber = num;
    }
}