package photoshop;

import java.awt.image.BufferedImage;

public class Img {
	
	private BufferedImage buffImg;
	
	public Img() {
		buffImg = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
		for (int i = 0; i < 100; i++)
			for (int j = 0; j < 100; j++)
				buffImg.setRGB(i, j, 1987335508);
	}
	
	public BufferedImage getImg() { return buffImg; }
}
