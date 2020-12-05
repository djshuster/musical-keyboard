/***
 * By Nat Roth, Sabir Meah, & David Shuster
 * Last Updated 4 May, 2020
 ***/

/***
* Application designed and programmed by Nat Roth, Sabir Meah, & David Shuster,
* except for CloseListener.java which was written and released by Rob Camick.
* (See that file's comments for more info.)
* Enjoy!
***/

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Main extends JPanel implements KeyListener {
    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;
    public World world;
        
    //Constructor
    private Main(){
        world = new World(WIDTH, HEIGHT);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame("In Honor of Fatso, 'Keyboard Cat', 2007");
        Main mainInstance = new Main();
        UIMaker myUI = new UIMaker(mainInstance);
        myUI.makeUI();
        frame.setContentPane(mainInstance);
        frame.addWindowListener(new CloseListener(new MidiCloser(mainInstance.world.mySong)));
        frame.pack();
        frame.setVisible(true);
    }
    
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
		if (world.background == null){
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
		else{
			g.drawImage(world.background, 0, 0, this);
		}
        world.drawScreen(world, g);
    }

    
    //=== START OF CPU KEYBOARD MANAGEMENT SECTION ===
    
    //When a CPU key is pressed, play the corresponding note and depress that key visually
    @Override
    public void keyPressed (KeyEvent e) {
        char c = e.getKeyChar();
        
        if (world.keyBook.get(c) == null){
            return;
        }
        int kbIndex = world.keyBook.get(c);
        if (!world.keyboard[kbIndex].pressing){
            world.mySong.noteOn(12 + world.octave*12 + kbIndex);
            world.keyboard[kbIndex].pressing = true;
            repaint();
        }
    }

    @Override
    public void keyReleased (KeyEvent e) {
        
        //Enables octave changing using left and right arrow keys
        if (e.getKeyCode() == 39){
            if(world.octave >= 8){
                return;
            }
            world.octave ++;
            refresh();
        }
        else if (e.getKeyCode() == 37) {
            if(world.octave <= 0){
                return;
            }
            world.octave --;
            refresh();
        }
        
        //When a CPU key is released, stop the corresponding note and redraw that key visually
        else{
            char c = e.getKeyChar();
            if (world.keyBook.get(c) != null){
                int kbIndex = world.keyBook.get(c); //index in keyboard: 0 - 12, or C3 to C4
                world.keyboard[kbIndex].pressing = false;
                world.mySong.noteOff(12 + world.octave*12 + kbIndex);
                repaint();
            }
        }
    }

    @Override
    public void keyTyped (KeyEvent e) {
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    //=== END OF CPU KEYBOARD MANAGEMENT SECTION ===
    
    /***
     * When the user changes his chosen instrument, octave, and so on, refresh() 
     * ensures that notes do not stay held down.
     ***/
    public void refresh(){
        world.mySong.allNotesOff();
        for (Key k : world.keyboard){
            k.pressing = false;
        }
        repaint();
    }
}