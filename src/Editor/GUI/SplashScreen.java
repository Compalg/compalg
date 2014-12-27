package Editor.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.border.Border;
/** Splashscreen do programa */
public class SplashScreen extends JWindow {
  /**
	 * Classe da telinha do inicio do programa
	 */
	private static final long serialVersionUID = 1L;

private int duration;  
  
  private JProgressBar progressBar = new JProgressBar();
  /** Splashscreen do programa(parametro: duracao de tempo) */
  public SplashScreen(int d) {
    duration = d;
  }

/** Splashscreen do programa */
  public void showSplash() {
	progressBar.setValue(0);
	progressBar.setStringPainted(true);
	Border border = BorderFactory.createTitledBorder("Carregando...");
	progressBar.setBorder(border);  
	  
    JPanel content = (JPanel) getContentPane();
    content.setBackground(Color.decode("#CCCCCC"));

    // Set the window's bounds, centering the window
    int width = 483;
    int height = 475;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screen.width - width) / 2;
    int y = (screen.height - height) / 2;
    setBounds(x, y, width, height);

    // Build the splash screen
    JLabel label = new JLabel(new ImageIcon(getClass().getResource("splash.png")));
    JLabel copyrt = new JLabel("Copyright 2012, Augusto Bilabila",
        JLabel.CENTER);
    copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
    content.add(label, BorderLayout.CENTER);
    content.add(progressBar, BorderLayout.SOUTH);
    Color oraRed = new Color(0, 41, 102, 140);
    content.setBorder(BorderFactory.createLineBorder(oraRed, 1));

    // Display it
    setVisible(true);
    //carregando o progress bar
    
    Random random = new Random();
    int r = random.nextInt(4);
    progressBar.setValue(r);
    
    
    // Wait a little while, maybe while loading resources
    int loading = 100 - Thread.activeCount() + r;
    try {
    	progressBar.setValue(loading);
    	Thread.sleep(duration);    	
    } catch (Exception e) {
    }    
    setVisible(false);
  }
  /** carrega o programa*/
  public void showSplashAndExit() {
    showSplash();
    //Carrega o programa...
  }
  	
}