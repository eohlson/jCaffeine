package caffeine;

import java.awt.AWTException;
import java.awt.Robot;

import org.apache.log4j.Logger;

import caffeine.resource.CaffeineConfiguration;


public class PressButtonThread extends Thread {

	Logger log = Logger.getLogger(PressButtonThread.class);
	
	private final CaffeineConfiguration configuration;

	private final Robot robot;
	
	private boolean running = false;


	public PressButtonThread(CaffeineConfiguration configuration) {
		super();
		this.configuration = configuration;

		try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {

		while (running) {
			log.debug("Running with config: " + configuration.toString());
			robot.keyPress(configuration.getButton().key());
			robot.keyRelease(configuration.getButton().key());

			try {
				sleep(configuration.getIntervalInSeconds() * 1000);
			} catch (InterruptedException e) {
				//We accept interruptions
			}
		}
	}
	
	@Override
	public synchronized void start() {
		this.running = true;
		super.start();
	}
	
	public boolean isRunning() {
		return running;
	}

	public void stopRunning() {
		this.running = false;
		this.interrupt();
	}

}
