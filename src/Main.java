
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


import org.sikuli.script.ImagePath;


public class Main {
	

	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException   {
		//ImagePath.reset();

		ImagePath.setBundlePath("img/");
		
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		MainWindow main = new MainWindow();


	}
}
