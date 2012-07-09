package caffeine.resource;

import java.awt.event.KeyEvent;

public class CaffeineConfiguration {

	public enum FunctionKeys {
		F13(KeyEvent.VK_F13), //
		F14(KeyEvent.VK_F14), //
		F15(KeyEvent.VK_F15), //
		F16(KeyEvent.VK_F16), //
		F17(KeyEvent.VK_F17), //
		F18(KeyEvent.VK_F18), //
		F19(KeyEvent.VK_F19), //
		F20(KeyEvent.VK_F20), //
		F21(KeyEvent.VK_F21), //
		F22(KeyEvent.VK_F22), //
		F23(KeyEvent.VK_F23), //
		F24(KeyEvent.VK_F24); //

		private int code;

		private FunctionKeys(int code) {
			this.code = code;
			
		}
		
		public int key() {
			return code;
		}
	}
	
	private int intervalInSeconds;
	private FunctionKeys button;

	public CaffeineConfiguration(FunctionKeys button , int intervalInSeconds) {
		this.button = button;
		this.intervalInSeconds = intervalInSeconds;
	}

	public CaffeineConfiguration(CaffeineConfiguration configuration) {
		this(configuration.getButton(), configuration.getIntervalInSeconds());
	}

	public void updateWithNewValues(CaffeineConfiguration config) {
		this.button = config.getButton();
		this.intervalInSeconds = config.getIntervalInSeconds();
	}
	
	public int getIntervalInSeconds() {
		return intervalInSeconds;
	}

	public void setIntervalInSeconds(int intervalInSeconds) {
		this.intervalInSeconds = intervalInSeconds;
	}

	public FunctionKeys getButton() {
		return button;
	}

	public void setButton(FunctionKeys button) {
		this.button = button;
	}
	
	
	@Override
	public String toString() {
		return "Button: " + button + " Poll-time: " + intervalInSeconds;
	
	}
}
