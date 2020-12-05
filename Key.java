/***
 * By Nat Roth, Sabir Meah, & David Shuster
 * Last Updated 4 May, 2020
 ***/
 
/***
* class Key allows you to keep track of the basic properties of a given piano key in an octave.
***/

public class Key{
    public final boolean isBlack; 
    public boolean pressing;

	//constructor
    public Key(boolean isBlack){
        this.isBlack=isBlack;
    } 
}