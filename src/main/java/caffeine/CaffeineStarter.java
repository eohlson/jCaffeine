package caffeine;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.SystemTray;

import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class CaffeineStarter {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.DEBUG);
		
		if (!SystemTray.isSupported()) {
			System.err.println("System tray not supported");
			System.exit(0);
		}

		if (!isRobotSupported()) {
			System.err.println("Robot is not supported");
			System.exit(0);
		}

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Problem with look and feel");
			System.exit(0);
		}

		new CaffeineMain();
	}

	private static boolean isRobotSupported() {
		try {
			new Robot();
			return true;
		} catch (AWTException e) {
			return false;
		}
	}
}
