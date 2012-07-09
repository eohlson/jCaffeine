package caffeine;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import caffeine.resource.CaffeineConfiguration;
import caffeine.resource.CaffeineResources;
import caffeine.resource.CaffeineConfiguration.FunctionKeys;


public class CaffeineMain {

	private PressButtonThread pressButtonThread;

	private final CaffeineConfiguration configuration;

	private CaffeineResources resources;

	private TrayIcon caffeineTrayIcon;

	private boolean storeConfigOnClose = false;

	public CaffeineMain() {
		// Setting up default
		configuration = new CaffeineConfiguration(FunctionKeys.F18, 60);

		resources = new CaffeineResources();

		caffeineTrayIcon = createCaffeineTrayIcon(resources);
		caffeineTrayIcon.setImageAutoSize(true);
		caffeineTrayIcon.setPopupMenu(createPopupWithMenues(configuration));

		try {
			SystemTray.getSystemTray().add(caffeineTrayIcon);
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}

		settingsDialogue();
	}

	private TrayIcon createCaffeineTrayIcon(final CaffeineResources resources) {
		final TrayIcon trayIcon = new TrayIcon(resources.getUpIcon(), "tray icon");
		MouseListener mouseListenere = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					return;
				}

				if (pressButtonThread != null && pressButtonThread.isRunning()) {
					trayIcon.setImage(resources.getUpIcon());
					stopClicking();
				} else {
					trayIcon.setImage(resources.getDownIcon());
					startClicking();
				}
			}

		};
		trayIcon.addMouseListener(mouseListenere);

		return trayIcon;
	}

	private PopupMenu createPopupWithMenues(CaffeineConfiguration configuration) {
		PopupMenu popup = new PopupMenu();


		MenuItem settings = new MenuItem("Settings");
		CheckboxMenuItem storeConfigOnClose = new CheckboxMenuItem("Store on close (expr)");
		
		MenuItem exit = new MenuItem("exit");

		
		
		
		settings.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				settingsDialogue();
			}
		});
		storeConfigOnClose.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					storeConfigOnClose(true);
				} else {
					storeConfigOnClose(false);
				}
			}
		});
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				killApplication();
			}
		});
		
		popup.add(settings);
		popup.add(storeConfigOnClose);
		popup.addSeparator();
		popup.add(exit);

		return popup;
	}

	private void startClicking() {
		pressButtonThread = new PressButtonThread(configuration);
		pressButtonThread.start();
	}

	private void stopClicking() {
		if (pressButtonThread != null)  {
			pressButtonThread.stopRunning();
		}
	}

	private void settingsDialogue() {
		final JFrame frame = new JFrame("Settings");
		frame.setAlwaysOnTop(true);
		frame.setSize(200, 200);
		frame.setLocation(200, 200);
		frame.setIconImage(resources.getApplicationIcon());
		frame.setLocationRelativeTo(null);

		FunctionKeys[] keys = FunctionKeys.values();

		final JComboBox<FunctionKeys> buttonChooser = new JComboBox<FunctionKeys>(keys);
		buttonChooser.setSelectedItem(configuration.getButton());

		final SpinnerNumberModel pollSpinnerModel = new SpinnerNumberModel(configuration.getIntervalInSeconds(), 1, 600, 1);
		JSpinner pollTimeSpinner = new JSpinner(pollSpinnerModel);

		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				CaffeineConfiguration newConfiguration = new CaffeineConfiguration((FunctionKeys) buttonChooser.getSelectedItem(), pollSpinnerModel.getNumber().intValue());
				
				configuration.updateWithNewValues(newConfiguration);
				
				stopClicking();
				startClicking();
				
				frame.setVisible(false);
				frame.dispose();
			}
		});

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame.dispose();
			}
		});

		// @formatter:off
		GroupLayout layout = new GroupLayout(frame.getContentPane());
		frame.getContentPane().setLayout(layout);
		
		JLabel buttonLabel = new JLabel("Button: ");
		JLabel pollLabel = new JLabel("Poll-time (s): ");
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(buttonLabel)
						.addComponent(pollLabel)
						.addComponent(cancel)
					)
				.addGroup(layout.createParallelGroup()
						.addComponent(pollTimeSpinner)
						.addComponent(buttonChooser)
						.addComponent(save)
					)
		);
		
		layout.linkSize(pollTimeSpinner, buttonChooser);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(buttonLabel).addComponent(buttonChooser)
					)
			    .addGroup(layout.createParallelGroup()
			    		.addComponent(pollLabel).addComponent(pollTimeSpinner)
			    	)
			    .addGroup(layout.createParallelGroup()
			    		.addComponent(cancel).addComponent(save)
			    	)
		  );
						
		// @formatter:on

		frame.pack();
		frame.setVisible(true);
	}
	
	private void storeConfigOnClose(boolean storeConfigOnClose) {
		this.storeConfigOnClose = storeConfigOnClose;
	}

	private void killApplication() {
		if (pressButtonThread != null) {
			pressButtonThread.stopRunning();
		}
		SystemTray.getSystemTray().remove(caffeineTrayIcon);
		System.exit(0);

	}
}
