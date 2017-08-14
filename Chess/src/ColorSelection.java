import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.LineBorder;

/**
 * class ColorSelection
 * 		creates a gui screen for the user to interact with in order to select desired play color
 * @author Kailash Jayaram
 *
 * main: the object being called from
 * select: the frame holding the gui
 * color: the turn selected
 */
public class ColorSelection implements ActionListener 
{
	Main main;
	JFrame select;
	private String color;
	public ColorSelection(Main main) 
	{
		// initialize the gui
		this.main = main;
		select = new JFrame("Color Selection");
		select.setSize(200, 100);
		JPanel p = new JPanel();
		JButton white = new JButton("White");
		JButton black = new JButton("Black");
		white.addActionListener(this);
		black.addActionListener(this);
		BorderLayout layout = new BorderLayout();
		p.setLayout(layout);
		JLabel lbl = new JLabel("What color pieces do you want?");
		p.add(lbl,BorderLayout.NORTH);
		JPanel southLayout = new JPanel();
		southLayout.add(white);
		southLayout.add(black);
		p.add(southLayout, BorderLayout.SOUTH);
		
		// add the layout to the panel
		select.add(p);
		select.setLocation(250,300);
		select.setVisible(true);
	}
	

	/**
	 * on color selected
	 * 
	 * @param e: the button pressed
	 */
	public void actionPerformed(ActionEvent e) 
	{
		// check which color has been selected and update variables
		// then launch the game
		if(e.getActionCommand().equals("Black"))
		{
			color = "White";
			select.setVisible(false);
			select.dispose();
			main.begin();
		}
		else
		{
			color = "Black";
			select.setVisible(false);
			select.dispose();
			main.begin();
		}
		
	}
	
	// returns the selected color
	public String getColor() 
	{
		return color;
	}

}
