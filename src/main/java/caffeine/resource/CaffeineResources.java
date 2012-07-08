package caffeine.resource;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class CaffeineResources {

    private final Image downIcon;
    private final Image upIcon;
    
    public CaffeineResources() {
    	downIcon = createImage("/down.png");
    	upIcon = createImage("/up.png");
	}
	
    public Image getDownIcon() {
		return downIcon;
	}

	public Image getUpIcon() {
		return upIcon;
	}
	
	public Image getApplicationIcon() {
		return getDownIcon();
	}

	private Image createImage(String classPathImage) {
		URL image = getClass().getResource(classPathImage);
		
		if (image == null) {
			System.err.println("Image did not exist " + classPathImage);
			return null;
		} else {
			ImageIcon imageIcon = new ImageIcon(image);
			
			return imageIcon.getImage();
		}
	}
}
