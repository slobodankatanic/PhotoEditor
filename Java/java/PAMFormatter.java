package photoshop;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PAMFormatter extends Formatter {
	
	@Override
	public void exportImage(String path, Layer img) throws IOException {	
		File file = new File(path);
		if (file == null) throw new IOException();
 
        FileOutputStream fos = new FileOutputStream(file);
        
        byte[] uc = new byte[46];

    	uc[0] = 'P'; uc[1] = '7'; uc[2] = 0x0a; uc[3] = 'W'; 
    	uc[4] = 'I'; uc[5] = 'D'; uc[6] = 'T'; uc[7] = 'H';
    	uc[8] = ' ';
    	fos.write(uc, 0, 9);

    	int w = img.getImage().getWidth();
    	//std::cout << std::endl << w << std::endl;
    	List<Byte> suc = new ArrayList<>();

    	while (w > 0) {
    		suc.add((byte)(w % 10 + 0x30));
    		w /= 10;
    	}
    	int size = suc.size();
    	for (int i = 0; i < size; i++) {
    		uc[i] = suc.remove(suc.size() - 1);
    	}
    	fos.write(uc, 0, size);
    	
    	uc[0] = 0x0a; uc[1] = 'H'; uc[2] = 'E'; uc[3] = 'I';
    	uc[4] = 'G'; uc[5] = 'H'; uc[6] = 'T'; uc[7] = ' ';
    	fos.write(uc, 0, 8);

    	int h = img.getImage().getHeight();

    	while (h > 0) {
    		suc.add((byte)(h % 10 + 0x30));
    		h /= 10;
    	}
    	size = suc.size();
    	for (int i = 0; i < size; i++) {
    		uc[i] = suc.remove(suc.size() - 1);
    	}
    	fos.write(uc, 0, size);
    	
    	uc[0] = 0x0a; uc[1] = 'D'; uc[2] = 'E'; uc[3] = 'P'; uc[4] = 'T'; uc[5] = 'H';
    	uc[6] = ' '; uc[7] = '4'; uc[8] = 0x0a; uc[9] = 'M'; uc[10] = 'A'; uc[11] = 'X';
    	uc[12] = 'V'; uc[13] = 'A'; uc[14] = 'L'; uc[15] = ' '; uc[16] = '2'; uc[17] = '5';
    	uc[18] = '5'; uc[19] = 0x0a; uc[20] = 'T'; uc[21] = 'U'; uc[22] = 'P'; uc[23] = 'L';
    	uc[24] = 'T'; uc[25] = 'Y'; uc[26] = 'P'; uc[27] = 'E'; uc[28] = ' '; uc[29] = 'R';
    	uc[30] = 'G'; uc[31] = 'B'; uc[32] = '_'; uc[33] = 'A'; uc[34] = 'L';
    	uc[35] = 'P'; uc[36] = 'H'; uc[37] = 'A'; uc[38] = 0x0a; uc[39] = 'E';
    	uc[40] = 'N'; uc[41] = 'D'; uc[42] = 'H'; uc[43] = 'D'; uc[44] = 'R'; uc[45] = 0x0a;
    	fos.write(uc, 0, 46);
    		
    	BufferedImage bf = img.getImage();
		for (int i = 0; i < bf.getHeight(); i++) {
			for (int j = 0; j < bf.getWidth(); j++) {
				byte[] rgba = new byte[4];
				int r = bf.getRGB(j, i);
				rgba[0] = (byte) (r >> 16 & 0xff);
				rgba[1] = (byte) ((r >> 8) & 0xff);
				rgba[2] = (byte) ((r) & 0xff);
				rgba[3] = (byte) ((r >> 24) & 0xff);
				fos.write(rgba);			
			}
		}
	
		fos.close();
	}

	@Override
	public void importImage(String path) throws IOException {
		File file = new File(path);
		FileInputStream is = new FileInputStream(file);
		if (is == null) throw new IOException();
		
		byte[] id = new byte[2];
		is.read(id, 0, 2);
		if (id[0] != 0x50 || id[1] != 0x37) {
			throw new IOException();
		}
		
		is.getChannel().position(0x9);
		int width = 0;
		byte[] uc = new byte[1];
		while (true) {
			is.read(uc, 0, 1);
			if (uc[0] == 0x0a) { break; }
			width *= 10;
			width += (int)uc[0] - 0x30;
		}
		
		is.skip(0x7);
		int heigth = 0;
		while (true) {
			is.read(uc, 0, 1);
			if (uc[0] == 0x0a) { break; }
			heigth *= 10;
			heigth += (int)uc[0] - 0x30;
		}
		
		is.skip(0x6);
		int depth = 0;
		while (true) {
			is.read(uc, 0, 1);
			if (uc[0] == 0x0a) { break; }
			depth *= 10;
			depth += (int)uc[0] - 0x30;
		}

		is.skip(0x7);
		int maxval = 0;
		while (true) {
			is.read(uc, 0, 1);
			if (uc[0] == 0x0a) { break; }
			maxval *= 10;
			maxval += (int)uc[0] - 0x30;
		}
		
		is.skip(0x9);
		StringBuilder type = new StringBuilder();
		
		while (true) {
			is.read(uc, 0, 1);
			if (uc[0] == 0x0a) { break; }
			type.append((char)uc[0]);
		}
		is.skip(0x7);

		byte blue; byte green;
		byte red; byte alpha;
		uc = new byte[8];
		
		BufferedImage bf = new BufferedImage(width, heigth, BufferedImage.TYPE_4BYTE_ABGR);
		
		if (type.toString().equals("RGB_ALPHA")) {
			for (int i = 0; i < heigth; i++) {
				for (int j = 0; j < width; j++) {
					if (maxval <= 255) {
						is.read(uc, 0, 4);
						blue = uc[2]; green = uc[1];
						red = uc[0]; alpha = uc[3];
					}
					else {
						is.read(uc, 0, 8);
						blue = (byte) (((uc[4] & 0xff) * 255 + (uc[5] & 0xff)) / 255); 
						green = (byte) (((uc[2] & 0xff) * 255 + (uc[3] & 0xff)) / 255);
						red = (byte) (((uc[0] & 0xff) * 255 + (uc[1] & 0xff)) / 255); 
						alpha = (byte) (((uc[6] & 0xff) * 255 + (uc[7] & 0xff)) / 255);
					}
					int r = red & 0xff; int g = green & 0xff; 
					int b = blue & 0xff; int a = alpha & 0xff;
					int rgb = (a << 24) | (r << 16) | (g << 8) | (b); 
					bf.setRGB(j, i, rgb);
				}
			}
			is.close();
			this.image = bf;
			return;
		}
	
		if (type.toString().equals("RGB")) {
			for (int i = 0; i < heigth; i++) {
				for (int j = 0; j < width; j++) {
					if (maxval <= 255) {
						is.read(uc, 0, 3);
						blue = uc[2]; green = uc[1];
						red = uc[0]; alpha = (byte)255;
					}
					else {
						is.read(uc, 0, 6);
						blue = (byte) (((uc[4] & 0xff) * 255 + (uc[5] & 0xff)) / 255); 
						green = (byte) (((uc[2] & 0xff) * 255 + (uc[3] & 0xff)) / 255);
						red = (byte) (((uc[0] & 0xff) * 255 + (uc[1] & 0xff)) / 255); 
						alpha = (byte) 255;
					}
					int r = red & 0xff; int g = green & 0xff; 
					int b = blue & 0xff; int a = alpha & 0xff;
					int rgb = (a << 24) | (r << 16) | (g << 8) | (b); 
					bf.setRGB(j, i, rgb);
				}
			}
			is.close();
			this.image = bf;
			return;
		}
		throw new IOException();
	}
}
