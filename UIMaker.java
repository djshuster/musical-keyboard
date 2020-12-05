/***
 * By Nat Roth, Sabir Meah, & David Shuster
 * Last Updated 4 May, 2020
 ***/

/***
* UIMaker has all the tools for making the user interface:
* the program's menus, buttons, and so on.
***/

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.Dimension;
import java.util.Random;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;  
import java.awt.*;
import java.nio.file.*;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;

public class UIMaker{
    private World world;
    private Main mainInstance;
	
    //=== START OF CONSTRUCTOR ===
    public UIMaker(Main mainInstance){
        this.world = mainInstance.world;
        this.mainInstance = mainInstance;
    }
    //=== END OF CONSTRUCTOR ===

    //makeUI() creates and activates the UI on mainInstance.
    public void makeUI(){
        mainInstance.setLayout(new BorderLayout());

        //Creates our standalone buttons and adds them to mainInstance
        OctaveButton octaveIncr = new OctaveButton(true);
        mainInstance.add(octaveIncr, BorderLayout.NORTH);
        OctaveButton octaveDecr = new OctaveButton(false);
        mainInstance.add(octaveDecr, BorderLayout.NORTH);
        Toggler pbToggler = new Toggler("Playback", world.width - 200, 30, 200, 50);
        mainInstance.add(pbToggler.b, BorderLayout.NORTH);
        Toggler displayMToggler = new Toggler("Display Key Mappings", world.width - 300, world.height - 350, 300, 50);
        mainInstance.add(displayMToggler.b, BorderLayout.NORTH);

        JMenuBar topMB = getMenuBar();
        mainInstance.add(topMB, BorderLayout.NORTH);
    }
	
    //getMenuBar() returns the JMenuBar that is displayed at the top of the program.
    private JMenuBar getMenuBar(){
        JMenuBar topMB = new JMenuBar();
        JMenu insMenu = getInstrumentMenu();
		JMenu MIDIMenu = getMIDIMenu();

        MyButton helpButton = new MyButton("Help");
        helpButton.addActionListener(new HelperAL());
        
        topMB.setPreferredSize(new Dimension(world.width, 30));
        topMB.add(insMenu);
        topMB.add(MIDIMenu);
        topMB.add(Box.createHorizontalGlue());

        topMB.add(helpButton);
        return topMB;
    }

    
    //getInstrumentMenu() returns the menu from which the user can switch instruments.
    private JMenu getInstrumentMenu(){
        JMenu menu = new JMenu("\u2022 Change Instrument");
        menu.addMenuListener(new MyMenuListener());
        for (int i = 0; i < world.instruments.size(); i ++){
            JMenuItem myItem = new JMenuItem(world.instruments.get(i).instrumentName);
            myItem.addActionListener(new InsChanger(i));
            menu.add(myItem);
        }		
        return menu;
    }

    //getMIDIMenu() returns the menu from which the user can load a MIDI file.
    private JMenu getMIDIMenu(){
        //Scans the "Tracks" folder for MIDI files.
        ArrayList<String> midiFileNames = new ArrayList<String>();
        Path path = FileSystems.getDefault().getPath("Tracks");
        try{
            Path[] filePaths = Files.list(path).toArray(Path[]::new);
            for (Path p : filePaths){
                String fileName = p.getFileName().toString();

                //Checks the extension by looking at the last four characters of the file name.
                String nameExt = fileName.substring(fileName.length() - 4, fileName.length());
                if (nameExt.equals(".mid")){
                    midiFileNames.add(fileName);
                }
            }
        }
        catch(Exception e){
            System.out.println("Error loading MIDI file list.");
        }

        //Creates the menu
        JMenu menu = new JMenu("\u2022 Load MIDI File for Playback");
        menu.addMenuListener(new MyMenuListener());
        for (int i = 0; i < midiFileNames.size(); i ++){
            String fileName = midiFileNames.get(i);
            JMenuItem myItem = new JMenuItem(fileName);
            myItem.addActionListener(new MidiLoader(fileName));
            menu.add(myItem);
        }

        //Adds the option to load a random track to the menu
        JMenuItem randomTrackItem = new JMenuItem("Load Random Track");
        randomTrackItem.addActionListener(new MidiLoader(midiFileNames));
        menu.add(randomTrackItem);		
        
        return menu;
    }

    /***
     * MyMenuListener ensures that when someone clicks something in the menu, our keys are not stuck down.
     ***/
    private class MyMenuListener implements MenuListener{
        @Override
        public void menuSelected(MenuEvent e){
            mainInstance.refresh();            
        }
        @Override
        public void menuDeselected(MenuEvent e){
        }
        @Override
        public void menuCanceled(MenuEvent e){
        }
    }

    /***
     * The MyButton class is similar to JButton, but it ensures that each button 
     * has the correct font and is not focusable
     * (i.e. the program focuses on the user's CPU keyboard inputs after a button is clicked).
     ***/
    private class MyButton extends JButton{
        //constructor
        //Does not take in any String as argument. Be sure to use "setText"
        //later for the button to label the button. Used to allow us to set
        //the text of the generalized octave button without 
        //setting any initial text.
        public MyButton(){ 
            super();
            setFocusable(false);
            setFont(new Font("Helvetica", Font.BOLD, 16));
        }
        
        //constructor
        public MyButton(String s){
            super(s);
            setFocusable(false);
            setFont(new Font("Helvetica", Font.BOLD, 16));            
        }
    }
    
    //OctaveButton allows for the creation of our +/- octave buttons.
    private class OctaveButton extends MyButton{        
        public OctaveButton(boolean up){
            super();

            //For increasing the octave
            if(up){
                setText("+");
                setToolTipText("Or use right arrow key.");
                setBounds(world.width/2 + 15, world.height - 350, 50, 50);
                addActionListener(new OctaveChanger(1));
            }

            //For decreasing the octave
            else {
                setText("-");
                setToolTipText("Or use left arrow key.");
                setBounds(world.width/2 - 65, world.height - 350, 50, 50);
                addActionListener(new OctaveChanger(-1));
            }

        }
    }
 
    /***
     * Below are the ActionListener implementations for our buttons.
     ***/
	 
    //=== START OF AN ActionListener IMPLEMENTATION ===
    //For the "Help" button
    private class HelperAL implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e){
			mainInstance.refresh();
			try{
				File file = new File("README.txt");
				Desktop desktop = Desktop.getDesktop();
				desktop.open(file);
			}
			catch(Exception ex){
				System.out.println("Oops...cannot find instructions.");
			}
		}
    }
    //=== END OF AN ActionListener IMPLEMENTATION ===

    //=== START OF AN ActionListener IMPLEMENTATION ===
    //For loading a given MIDI File
    private class MidiLoader implements ActionListener {
        private Random rand;
        private String fileName;
        private boolean isRandom;
        private ArrayList<String> ls;
        
        //Constructor
        //for regular MIDI file menu items
        public MidiLoader(String s){
            this.fileName = s;
            isRandom = false;
        }

        //Constructor
        //for "Load random song" button
        public MidiLoader(ArrayList<String> ls){
            this.ls = ls;
            rand = new Random();
            isRandom = true;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if (isRandom){
                fileName = ls.get(rand.nextInt(ls.size()));
            }
            
            //Sends the MIDI file to mySong from the Tracks folder
			try{
				world.mySong.loadFile("Tracks/"+fileName);
				world.loadedFileName = fileName;
				mainInstance.repaint();
			}
			catch(Exception ex){
				System.out.println("Failed to load random file.");
			}
        }
    }
    //=== END OF AN ActionListener IMPLEMENTATION ===

    //=== START OF AN ActionListener IMPLEMENTATION ===
    //For changing instruments
    private class InsChanger implements ActionListener {
        private int n; //Denotes the index of the instrument in the list of instruments

        //Constructor
        public InsChanger(int n){
            this.n = n;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            //Sets the background to correspond to the chosen instrument
            try {
                world.background = ImageIO.read(new File("Backgrounds/" + world.instruments.get(n).instrumentName + ".jpg"));
            }
            catch (IOException ex) {
                world.background = null;
                System.out.println("Image not found.");
            }

            //Changes the instrument
            world.mySong.changeInstrument(world.instruments.get(n).midiNumber);
            mainInstance.refresh();            
        }
    }    
    //=== END OF AN ActionListener IMPLEMENTATION ===

    //=== START OF AN ActionListener IMPLEMENTATION ===
    private class OctaveChanger implements ActionListener{
        private int n; //amount by which world.octave is being incremented
        
        //Constructor
        public OctaveChanger(int n){//take in either 1 or -1 so as to change the octave by +/- 1
            this.n = n;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if((world.octave <= 0 && n < 0) || (world.octave >= 8 && n > 0))
                return;
            else
                world.octave += n;
            mainInstance.refresh();
        }
    }
    //=== END OF AN ActionListener IMPLEMENTATION ===
    
    //=== START OF AN ActionListener IMPLEMENTATION ===
    /***
     * At present, the Toggler class may seem a little bit unnecessary.
     * However, it allows for easy addition of further toggle buttons such as
     * buttons for "Recording" or "Reverb", etc.
     ***/
    private class Toggler implements ActionListener{
        private MyButton b;
        private String toggledItemName;
        
        //Constructor
        public Toggler(String toggledItemName, int x, int y, int width, int height){
            if (toggledItemName.equals("Playback")){
                b = new MyButton("Play");
            } else if (toggledItemName.equals("Display Key Mappings")){
                b = new MyButton("Display Key Mappings");
            }
            b.setBounds(x, y, width, height);
            b.addActionListener(this);
            this.toggledItemName = toggledItemName;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            switch(toggledItemName){
                //For the play button
                case "Playback":
                    //If a MIDI file has been loaded, the button will start playback.
                    if (world.loadedFileName != null){
                        world.playback = !world.playback;
                        if (world.playback){
                            world.mySong.play();
                            this.b.setText("Pause");
                        }
                        //If a song is already playing, the button will pause the playback.
                        else {
                            world.mySong.stopPlaying();
                            this.b.setText("Play");
                        }
                    }
                    break;
                //For the display key mappings button
                case "Display Key Mappings":
                    world.displayMapping =! world.displayMapping;
                    if (!world.displayMapping){
                        this.b.setText("Display Key Mappings");
                    } else {
                        this.b.setText("Hide Key Mappings");
                    }			                    
                    break;
            }
            mainInstance.repaint();
        }
    }	
    //=== END OF AN ActionListener IMPLEMENTATION ===
}
//=== END OF CLASS ===