package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Start extends JFrame{
	public static final int WIDTH = 400;
	public static final int HEIGHT = 350;
	private boolean flag;
	
	class SinglePlayerListener implements ActionListener {  
	    @Override  
	    public void actionPerformed(ActionEvent e) {  
	    	new GameClient(false).lauchFrame();
	    	setVisible(false);
	    }  
	} 
	
	class DoublePlayerListener implements ActionListener {  
	    @Override  
	    public void actionPerformed(ActionEvent e) {  
	    	new GameClient(true).lauchFrame();
	    	setVisible(false);
	    }  
	} 
	
	class WindowDestroyer extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			
		}
	}
	

	public Start(){
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		addWindowListener(new WindowDestroyer());
		setTitle("Zombie Crsis");
		Container contentPane = getContentPane();
		contentPane.setBackground(Color.LIGHT_GRAY);
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		
		contentPane.setLayout(new FlowLayout());
		
		ImageIcon singleIcon = new ImageIcon(Start.class.getResource("/images/single.jpg"));
		singleIcon.setImage(singleIcon.getImage().getScaledInstance(380,140,Image.SCALE_DEFAULT));
		JButton singleButton = new JButton(singleIcon);
		singleButton.setIcon(singleIcon);
		singleButton.addActionListener(new SinglePlayerListener());
		contentPane.add(singleButton);
		

		ImageIcon doubleIcon = new ImageIcon(Start.class.getResource("/images/double.jpg"));
		doubleIcon.setImage(doubleIcon.getImage().getScaledInstance(380,140,Image.SCALE_DEFAULT));
		JButton doubleButton = new JButton(doubleIcon);
		doubleButton.setIcon(doubleIcon);
		doubleButton.addActionListener(new DoublePlayerListener());
		contentPane.add(doubleButton);
		
		setVisible(true);
	}
}
