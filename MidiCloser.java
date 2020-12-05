/***
* This short class is used by CloseListener, and indirectly by main(),
* to close out of the local MIDI devices used in Song, and release
* their resources for other programs to use.
* Written by Nat Roth.
***/

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

public class MidiCloser extends AbstractAction {
	public Song song;
	public MidiCloser(Song s){
		this.song = s;
	}
	public void actionPerformed(ActionEvent e){
		song.close();
	}
}