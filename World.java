/***
 * By Nat Roth, Sabir Meah, & David Shuster
 * Last Updated 4 May, 2020
 ***/

/***
 * The class World keeps track of various objecst that 
 * Welcome to the...world-class World class.
 ***/

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class World{
    public boolean displayMapping; //True iff displaying "q" on the lower C-key, etc.
    public boolean playback; //True iff currently playing back a MIDI file
    public String loadedFileName; //name of a loaded MIDI file
    
    public int height;
    public int width;
    public BufferedImage background;

    public Song mySong;
    public ArrayList<Instrument> instruments;
    public Key[] keyboard;
    public int octave;

    public HashMap<Character, Integer> keyBook; //Maps a CPU keybaord key to n in [0,12], representing the keys in an octave
    
    private Piano piano; //Initializes and updates the piano drawing
    
    //=== START OF CONSTRUCTOR ===
    public World(int initWidth, int initHeight){
        try {
            background = ImageIO.read(new File("Backgrounds/Acoustic Grand Piano.jpg"));
        }
        catch (IOException ex) {
                background=null;
                System.out.println("Image not found.");
        }
        this.width = initWidth;
        this.height = initHeight;

        mySong = new Song();
        instruments = InsInitializer.getInsList();//To add more instruments to the program, edit insInitializer.java
        keyboard = new Key[13];
        octave = 4;
        
        //Used for taking input in Main's keyboard-listening methods
        keyBook = new HashMap<Character, Integer>(13);
        boolean [] isBlack = {false, true, false, true, false, false, true, false, true, false, true, false, false};
        char[] compKeys = {'q', '2', 'w', '3', 'e', 'r', '5', 't', '6', 'y', '7', 'u', 'i'};
        for (int i=0; i<13;i++) {
            keyboard[i] = new Key(isBlack[i]);
            keyBook.put(compKeys[i],i);
        }

        // Piano object that will be used to draw piano graphics
        piano = new Piano(300, keyboard);
    }    
    //=== END OF CONSTRUCTOR ===

    //Method called in Main's paintComponent method
    public void drawScreen(World w, Graphics g){
        //Displays the name of the currently loaded MIDI file, if any.
        g.setColor(Color.CYAN);
        g.fillRect(width-200, 80, 200, 24);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Helvetica Bold", Font.BOLD, 24));
        if (loadedFileName==null){
            g.drawString("No file loaded.", width-200, 100);
        }
        else{
            String shortenedName = loadedFileName.substring(0, Math.min(loadedFileName.length(),11))+"...";
            g.drawString(shortenedName, width-200, 100);
        }

        //Displays the currently playing file, if any.
        g.setColor(Color.BLUE);
        if(this.playback){
            g.fillRect(0, 30, width, 34);
            g.setColor(Color.WHITE);
            g.drawString("Now playing: " + loadedFileName, 0, 54);
        }

        //Displays the current octave that we are in.
        g.setColor(Color.BLUE);
        g.fillRect(width/2-65, height-380, 130, 100);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 16));
        drawStringCentered(g, "Octave", 0, width, height-380, height-350);
        drawStringCentered(g, Integer.toString(octave), 0, width, height-350, height-300);
        
        // Draw the default piano (i.e. all keys with none pressed)
        piano.initialize(g);

        // Draw each key being pressed
        for (int i = 0; i<13; i++){
            if(this.keyboard[i].pressing==true){
                piano.drawKey(g, i, this.keyboard[i].pressing);
            }
        }
		
        // If displayMapping is turned on, run the method in piano to display the key mappings
        if (w.displayMapping){
			piano.drawMapping(g, w);
        }

    }
    
    public void drawStringCentered(Graphics g, String s, int x0, int x1, int y0, int y1){
        int width = g.getFontMetrics().stringWidth(s);
        int height = g.getFontMetrics().getHeight();
        g.drawString(s, x0+(x1-x0-width)/2, y0+(y1-y0-height)/2+height);
    }
}