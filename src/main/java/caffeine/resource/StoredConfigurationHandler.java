package caffeine.resource;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import caffeine.resource.CaffeineConfiguration.FunctionKeys;


public class StoredConfigurationHandler {

	private final String POLL_TIME_KEY = "saved.poll_time";
	private final String PRESSED_KEY = "saved.press_key";

	// TODO: This should be versioned. Also with update possibility
	public StoredConfigurationHandler() {
	}

	public File getUserConfigurationFile() {
		return new File(System.getProperty("user.home") + File.separator + ".jcaffeine" + File.separator + "user_preferences_9999.conf");
	}

	public void storeConfiguration(CaffeineConfiguration config) {
		Properties propConfig = new Properties();
		propConfig.put(PRESSED_KEY, config.getButton());
		propConfig.put(POLL_TIME_KEY, config.getIntervalInSeconds());

		FileOutputStream userConfigurationFop = null;
		try {
			userConfigurationFop = new FileOutputStream(getUserConfigurationFile());
			propConfig.store(userConfigurationFop, "User configuration for jCaffeine");
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				userConfigurationFop.close();
			} catch (IOException e) {
			}
		}
	}

	public CaffeineConfiguration loadConfiguration() {
		Properties properties = new Properties();

		if (!getUserConfigurationFile().exists()) {
			return null;
		}

		FileInputStream userConfigurationFop = null;
		try {
			userConfigurationFop = new FileInputStream(getUserConfigurationFile());
			properties.load(userConfigurationFop);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			closeQuietly(userConfigurationFop);
		}

		FunctionKeys fk = FunctionKeys.valueOf(properties.getProperty(PRESSED_KEY));
		int pT = -1;

		try {
			pT = Integer.parseInt(properties.getProperty(POLL_TIME_KEY));
		} catch (Exception e) {
			pT = -1;
		}

		return new CaffeineConfiguration(fk, pT);
	}

	public void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch (IOException e) {
		}
	}
}
