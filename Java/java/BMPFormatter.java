package photoshop;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BMPFormatter extends Formatter {

	@Override
	public void importImage(String path) throws IOException {
		BufferedImage img = ImageIO.read(new File(path));
		if (img == null) throw new IOException();
		if (img.getType() != BufferedImage.TYPE_4BYTE_ABGR &&  
				img.getType() != BufferedImage.TYPE_4BYTE_ABGR_PRE) {		
			image = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
			for (int i = 0; i < image.getHeight(); i++)
				for (int j = 0; j < image.getWidth(); j++) {
					int rgba = img.getRGB(j, i);
					image.setRGB(j, i, rgba);
				}
		}
		else image = img;
	}

	@Override
	public void exportImage(String path, Layer img) throws IOException {	
		if (img == null) return;
		File file = new File(path);
		if (file == null) throw new IOException();
 
        FileOutputStream fos = new FileOutputStream(file);
		//fos.write(data);
        
        int cnt = 0;
    	byte[] uchar = new byte[122];

    	uchar[cnt++] = 0x42;
    	uchar[cnt++] = 0x4d;
    	
    	int size = img.getImage().getHeight() * img.getImage().getWidth() * 4 + 122;
    	int tmp = 0;
		while (size > 0) {
			uchar[cnt++] = (byte) ((size % 256) & 0xff);
			size /= 256;
			tmp++;
		}
		while (tmp < 4) {
			uchar[cnt++] = 0x0;
			tmp++;
		}
		
		for (int i = 0; i < 4; i++)
			uchar[cnt++] = 0x0;

		uchar[cnt++] = 0x7a;
		for (int i = 0; i < 3; i++)
			uchar[cnt++] = 0x0;

		uchar[cnt++] = 0x6c;
		for (int i = 0; i < 3; i++)
			uchar[cnt++] = 0x0;
		
		//width
		size = img.getImage().getWidth();
		tmp = 0;
		while (size > 0) {
			uchar[cnt++] = (byte)((size % 256) & 0xff);
			size /= 256;
			tmp++;
		}
		while (tmp < 4) {
			uchar[cnt++] = 0x0;
			tmp++;
		}

		//heigth
		size = img.getImage().getHeight();
		tmp = 0;
		while (size > 0) {
			uchar[cnt++] = (byte)((size % 256) & 0xff);
			size /= 256;
			tmp++;
		}
		while (tmp < 4) {
			uchar[cnt++] = 0x0;
			tmp++;
		}
		
		//plane
		uchar[cnt++] = 0x01;
		uchar[cnt++] = 0x00;

		//bitsPerPixel
		uchar[cnt++] = 0x20;
		uchar[cnt++] = 0x00;

		uchar[cnt++] = 0x03;
		for (int i = 0; i < 3; i++)
			uchar[cnt++] = 0x0;
		
		size = img.getImage().getHeight() * img.getImage().getWidth() * 4 + 122;
		tmp = 0;
		while (size > 0) {
			uchar[cnt++] = (byte)((size % 256) & 0xff);
			size /= 256;
			tmp++;
		}
		while (tmp < 4) {
			uchar[cnt++] = 0x0;
			tmp++;
		}

		for (int i = 0; i < 2; i++) {
			uchar[cnt++] = 0x1e;
			uchar[cnt++] = 0x2e;
			uchar[cnt++] = 0x0;
			uchar[cnt++] = 0x0;
		}

		for (int i = 0; i < 10; i++) {
			uchar[cnt++] = 0x0;
		}

		uchar[cnt++] = (byte) 0xff; uchar[cnt++] = 0x0;
		uchar[cnt++] = 0x0; uchar[cnt++] = (byte) 0xff;
		uchar[cnt++] = 0x0; uchar[cnt++] = 0x0;
		uchar[cnt++] = (byte) 0xff; 

		for (int i = 0; i < 6; i++) {
			uchar[cnt++] = 0x0;
		}

		uchar[cnt++] = (byte) 0xff; 
		uchar[cnt++] = 0x20;
		uchar[cnt++] = 0x6e;
		uchar[cnt++] = 0x69;
		uchar[cnt++] = 0x57;

		for (int i = 0; i < 48; i++) {
			uchar[cnt++] = 0x0;
		}
		
		fos.write(uchar);
		
		BufferedImage bf = img.getImage();
		for (int i = bf.getHeight() - 1; i >= 0; i--) {
			for (int j = 0; j < bf.getWidth(); j++) {
				byte[] rgba = new byte[4];
				int r = bf.getRGB(j, i);
				rgba[0] = (byte) (r & 0xff);
				rgba[1] = (byte) ((r >> 8) & 0xff);
				rgba[2] = (byte) ((r >> 16) & 0xff);
				rgba[3] = (byte) ((r >> 24) & 0xff);
				fos.write(rgba);			
			}
		}
		
		//fos.flush();
		fos.close();
	}
}
