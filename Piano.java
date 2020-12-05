/***
 * By Nat Roth, Sabir Meah, & David Shuster
 * Last Updated 4 May, 2020
 ***/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

// Piano class used to draw graphics related to piano/keyboard
public class Piano{
	private int width; // width of window
	private int height; // height of window
	public int keyboardheight;  // desired height of keyboard, specified in creation of object
	private Key[] keyboard;  // array of keys, specified in creation of object
	public Piano(int keyboardheight, Key[] keyboard){  // constructor takes in height of keyboard and array of keys
		width = Main.WIDTH;  // width is set to width of window specified in Main
		height = Main.HEIGHT;  // height is set to height of window specified in Main
		this.keyboardheight = keyboardheight;
		this.keyboard = keyboard;
	}
	
	// Method to draw the default keyboard (i.e. with nothing pressed and no labels)
	public void initialize(Graphics g){  // all it needs to take in is the graphics object, but uses a lot of the parameters specified in the fields
		// Draw white rectangle over area assigned for the keyboard, keys will be drawn over this
		g.setColor(Color.WHITE);
		g.fillRect(0, height - keyboardheight + 1, width, keyboardheight);
		// Now draw keys
		g.setColor(Color.BLACK);  
		for(int i = 0; i <= 7; i++){  // iterate from 0 through 7 for the 8 white keys
			// Draw white keys as black rectangles over the white background
			// Don't have to worry about different shapes because black keys will be drawn over them
			g.drawRect(i * width / 8, height - keyboardheight + 1, width / 8, keyboardheight);  // draw keys using a function of the iterator (keys are just horizontally shifted rectangles)
			// Draw black keys as filled rectangles between white keys at a higher starting height but only if white keys should have a black one between them
			if(i != 2 && i != 6 && i != 7){  // only draw black key between white keys that should have a black key between them
				g.fillRect(i * width / 8 + 3 * width / 32, height - keyboardheight + 1, width / 16, keyboardheight/2);  // draw keys using a function of the iterator (keys are just horizontally shifted rectangles)
			}
			// Finally, draw half of unpressable final black key (can't be done inside previous for loop because width needs to be halfed so key doesn't run over the window)
			g.fillRect(7 * width/8 + 3*width/32, height - keyboardheight + 1, width/32, keyboardheight/2);  // coordinates are similar to previous function, but with width cut in half
		}
	}
	
	// Method to draw an individual key, intended to be used when a key is pressed
	public void drawKey(Graphics g, int key, boolean pressed){ // takes in graphics object, which key to draw, and if it is pressed
		if(pressed == true){  // recolor pressed keys
			if(keyboard[key].isBlack == true){  // color pressed black and white keys different colors
				g.setColor(Color.DARK_GRAY);  // color pressed black key dark gray
			} else {
				g.setColor(Color.LIGHT_GRAY);  // color pressed white key light gray
			}
		} else if(keyboard[key].isBlack == true){ // color unpressed key its original color 
			g.setColor(Color.BLACK); // color black key black
		} else {
			g.setColor(Color.WHITE); // color white key white
		}
		/***
		* If statements are listed for each possible key with cooresponding functions to draw the key
		*
		* FOR WHITE KEYS:
		* Creates 2 arrays representing the x and y coordiates of points in the key
		* Calls fillPolygon() using these two arrays to draw the key body
		* fillPolygon() and not fillRect() is used because single white keys are not rectangular due to black keys cutting out some of their space
		* Changes color to black then calls drawPolygon() using these two arrays to draw the key outline
		*
		* FOR BLACK KEYS: 
		* Calls fillRect() using the appropriate arguments
		* fillRect() can be used instead of fillPolygon() because all black keys are perfectly rectangular
		* No borders need to be redrawn since the keys are black
		***/
		if(key == 0){
			int[] x0 = new int[]{0, width/8, width/8, width/8 - width/32, width/8 - width/32, 0};
			int[] y0 = new int[]{height, height, height - keyboardheight/2, height - keyboardheight/2, height - keyboardheight + 1, height - keyboardheight + 1};
			g.fillPolygon(x0, y0, 6);
			g.setColor(Color.BLACK);
			g.drawPolygon(x0, y0, 6);
		} else if(key == 1){
			g.fillRect(0 * width / 8 + 3 * width / 32, height - keyboardheight + 1, width / 16, keyboardheight/2);
		} else if(key == 2){
			int[] x2 = new int[]{width/8, 2*width/8, 2*width/8, 2*width/8 - width/32, 2*width/8 - width/32, width/8 + width/32, width/8 + width/32, width/8};
			int[] y2 = new int[]{height, height, height - keyboardheight/2, height - keyboardheight/2, height - keyboardheight + 1, height - keyboardheight + 1, height - keyboardheight/2, height - keyboardheight/2};
			g.fillPolygon(x2, y2, 8);
			g.setColor(Color.BLACK);
			g.drawPolygon(x2, y2, 8);
		} else if(key == 3){
			g.fillRect(1 * width / 8 + 3 * width / 32, height - keyboardheight + 1, width / 16, keyboardheight/2);
		} else if(key == 4){
			int[] x4 = new int[]{2*width/8, 3*width/8, 3*width/8, 2*width/8 + width/32, 2*width/8 + width/32, 2*width/8};
			int[] y4 = new int[]{height, height, height - keyboardheight + 1, height - keyboardheight + 1, height - keyboardheight/2, height - keyboardheight/2};
			g.fillPolygon(x4, y4, 6);
			g.setColor(Color.BLACK);
			g.drawPolygon(x4, y4, 6);
		} else if(key == 5){
			int[] x5 = new int[]{3*width/8, 4*width/8, 4*width/8, 4*width/8 - width/32, 4*width/8 - width/32, 3*width/8};
			int[] y5 = new int[]{height, height, height - keyboardheight/2, height - keyboardheight/2, height - keyboardheight + 1, height - keyboardheight + 1};;
			g.fillPolygon(x5, y5, 6);
			g.setColor(Color.BLACK);
			g.drawPolygon(x5, y5, 6);
		} else if(key == 6){
			g.fillRect(3 * width / 8 + 3 * width / 32, height - keyboardheight + 1, width / 16, keyboardheight/2);
		} else if(key == 7){
			int[] x7 = new int[]{4*width/8, 5*width/8, 5*width/8, 5*width/8 - width/32, 5*width/8 - width/32, 4*width/8 + width/32, 4*width/8 + width/32, 4*width/8};
			int[] y7 = new int[]{height, height, height - keyboardheight/2, height - keyboardheight/2, height - keyboardheight + 1, height - keyboardheight + 1, height - keyboardheight/2, height - keyboardheight/2};
			g.fillPolygon(x7, y7, 8);
			g.setColor(Color.BLACK);
			g.drawPolygon(x7, y7, 8);
		} else if(key == 8){
			g.fillRect(4 * width / 8 + 3 * width / 32, height - keyboardheight + 1, width / 16, keyboardheight/2);
		} else if(key == 9){
			int[] x9 = new int[]{5*width/8, 6*width/8, 6*width/8, 6*width/8 - width/32, 6*width/8 - width/32, 5*width/8 + width/32, 5*width/8 + width/32, 5*width/8};
			int[] y9 = new int[]{height, height, height - keyboardheight/2, height - keyboardheight/2, height - keyboardheight + 1, height - keyboardheight + 1, height - keyboardheight/2, height - keyboardheight/2};
			g.fillPolygon(x9, y9, 8);
			g.setColor(Color.BLACK);
			g.drawPolygon(x9, y9, 8);
		} else if(key == 10){
			g.fillRect(5 * width / 8 + 3 * width / 32, height - keyboardheight + 1, width / 16, keyboardheight/2);
		} else if(key == 11){
			int[] x11 = new int[]{6*width/8, 7*width/8, 7*width/8, 6*width/8 + width/32, 6*width/8 + width/32, 6*width/8};
			int[] y11 = new int[]{height, height, height - keyboardheight + 1, height - keyboardheight + 1, height - keyboardheight/2, height - keyboardheight/2};
			g.fillPolygon(x11, y11, 6);
			g.setColor(Color.BLACK);
			g.drawPolygon(x11, y11, 6);
		} else if(key == 12){
			int[] x12 = new int[]{7*width/8, 8*width/8, 8*width/8, 8*width/8 - width/32, 8*width/8 - width/32, 7*width/8};
			int[] y12 = new int[]{height, height, height - keyboardheight/2, height - keyboardheight/2, height - keyboardheight + 1, height - keyboardheight + 1};;
			g.fillPolygon(x12, y12, 6);
			g.setColor(Color.BLACK);
			g.drawPolygon(x12, y12, 6);
		}
	}
	
	// Method used to draw the computer keyboard mappings on top of each key
	// Should be one of the methods called in the drawScreen method when this mapping is turned on
	public void drawMapping(Graphics g, World w){
		String[] whitekeys = new String[]{"Q", "W", "E", "R", "T", "Y", "U", "I"};  // array of cooresponding key names of white keys
		String[] blackkeys = new String[]{"2", "3", "", "5", "6", "7"};  // array of cooresponding key names of black keys
		g.setFont(new Font("Comic Sans MS", Font.BOLD, 48));  // set font and size
		// Call helper method in for loop for each white key in array
		for(int i = 0; i < 8; i++){
			drawMappingHelper(g, w, i, whitekeys[i], false);
		}
		// Call helper method in for loop for each black key in array
		for(int i = 0; i < 6; i++){
			drawMappingHelper(g, w, i, blackkeys[i], true);
		}
	}
	
	// Helper method used within drawMapping()
	// Takes in various arguments and draws cooresponding key mapping
	// Uses drawStringCentered() from World to do this
	private void drawMappingHelper(Graphics g, World w, int i, String name, boolean isBlack){
		if(isBlack == false){  // draws black mapping on white key
			g.setColor(Color.BLACK);
			w.drawStringCentered(g, name, i * width/8, (i + 1) * width/8, height, height - keyboardheight/2);
		} else {
			g.setColor(Color.WHITE);  // draws white mapping on black key
			w.drawStringCentered(g, name, i * width / 8 + 3 * width / 32, i * width / 8 + 3 * width / 32 + width / 16, height - keyboardheight + 1, height - keyboardheight + 1 + keyboardheight/2);
		}
	}
}