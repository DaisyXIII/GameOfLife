package Prueba5;

import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jtattoo.plaf.noire.NoireLookAndFeel;

public class App {

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);

		try {

			Properties props = new Properties();
			props.put("logoString", "MENU");
			NoireLookAndFeel.setCurrentTheme(props);
			UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
		} catch (Exception e) {

		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame();
			}
		});
	}
}
