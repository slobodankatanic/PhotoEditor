#include "BMPFormatter.h"
#include <fstream>
#include <queue>
#include "Exceptions.h"

void BMPFormatter::importImage(std::string path) {

	std::ifstream inputFile(path, std::ios::binary);
	if (!inputFile.is_open()) {
		inputFile.close();
		throw OpenningFileException();
	}

	unsigned char* id = new unsigned char[2];
	inputFile.read((char*)id, 2);
	if (id[0] != 0x42 || id[1] != 0x4d) {
		delete[] id;
		throw FileFormatException();
	}
	delete[] id;

	//adresa niza piksela je na ofsetu 0xA, 4 bajta zauzima
	inputFile.seekg(0xA, inputFile.beg);
	unsigned char* pixelArrayPo = new unsigned char[4];
	inputFile.read((char*)pixelArrayPo, 4);
	int pixelArrayPos = pixelArrayPo[0] + pixelArrayPo[1] * 256 + pixelArrayPo[2] * 256 * 256;
	delete[] pixelArrayPo;

	//sirina i visina su na ofsetu 0x12 and 0x16 (oba po 4B)
	inputFile.seekg(0x12, inputFile.beg);
	unsigned char* widt = new unsigned char[4];
	unsigned char* heig = new unsigned char[4];
	inputFile.read((char*)widt, 4);
	inputFile.read((char*)heig, 4);
	int width = widt[0] + widt[1] * 256 + widt[2] * 256 * 256;
	int height = heig[0] + heig[1] * 256 + heig[2] * 256 * 256;
	this->width = width;
	this->heigth = height;
	delete[] widt;
	delete[] heig;

	//br bita po pikselu je na ofsetu 0x1C, 2B
	inputFile.seekg(0x1C, inputFile.beg);
	unsigned char* bytesPerPi = new unsigned char[2];
	inputFile.read((char*)bytesPerPi, 2);
	int bytesPerPix = bytesPerPi[0] + bytesPerPi[1] * 256;
	delete[] bytesPerPi;
	if (bytesPerPix != 32 && bytesPerPix != 24) {
		throw FileFormatException();
	}
	bytesPerPix &= 0xff;
	bytesPerPix /= CHAR_BIT;

	inputFile.seekg(0, inputFile.beg);
	unsigned char *mem = new unsigned char[pixelArrayPos];
	inputFile.read((char*)mem, pixelArrayPos);

	//padding (4B alignment)
	int padd = (width * bytesPerPix) % 4;
	if (padd) padd = 4 - padd;

	unsigned char* mem1 = new unsigned char[width*bytesPerPix];
	for (int i = 0; i < height; i++) {
		inputFile.read((char*)mem1, width*bytesPerPix);
		std::vector<Pixel> tmpv;
		for (int j = 0; j < width; j++) {
			unsigned char blue = mem1[j * bytesPerPix];
			unsigned char green = mem1[j * bytesPerPix + 1];
			unsigned char red = mem1[j * bytesPerPix + 2];
			unsigned char alpha = ((bytesPerPix == 4) ? (mem1[j * bytesPerPix + 3]) : 255);
			tmpv.push_back(Pixel(red, green, blue, alpha));
		}
		pixels.push_back(tmpv);
		tmpv.erase(tmpv.begin(), tmpv.end());
		inputFile.seekg(padd, inputFile.cur);
	}

	std::reverse(pixels.begin(), pixels.end());

	delete[] mem1;
	delete[] mem;
	inputFile.close();
}

void BMPFormatter::exportImage(std::string path, Layer* img) {

	std::ofstream outputFile(path, std::ios::binary);
	if (!outputFile.is_open()) {
		outputFile.close();
		throw OpenningFileException();
	}	
	
	/**********DODATO************/

	int cnt = 0;
	unsigned char* uchar = new unsigned char[122];

	uchar[cnt++] = (unsigned char)0x42;
	uchar[cnt++] = (unsigned char)0x4d;

	int size = img->getDimensions().first *
		img->getDimensions().second * 4 + 122;
	int tmp = 0;
	while (size > 0) {
		uchar[cnt++] = (unsigned char)(size % 256);
		size /= 256;
		tmp++;
	}
	while (tmp < 4) {
		uchar[cnt++] = (unsigned char)(0x0);
		tmp++;
	}

	for (int i = 0; i < 4; i++)
		uchar[cnt++] = (unsigned char)(0x0);

	uchar[cnt++] = (unsigned char)(0x7a);
	for (int i = 0; i < 3; i++)
		uchar[cnt++] = (unsigned char)(0x0);

	uchar[cnt++] = (unsigned char)(0x6c);
	for (int i = 0; i < 3; i++)
		uchar[cnt++] = (unsigned char)(0x0);

	//width
	size = img->getDimensions().second;
	tmp = 0;
	while (size > 0) {
		uchar[cnt++] = (unsigned char)(size % 256);
		size /= 256;
		tmp++;
	}
	while (tmp < 4) {
		uchar[cnt++] = (unsigned char)(0x0);
		tmp++;
	}

	//heigth
	size = img->getDimensions().first;
	tmp = 0;
	while (size > 0) {
		uchar[cnt++] = (unsigned char)(size % 256);
		size /= 256;
		tmp++;
	}
	while (tmp < 4) {
		uchar[cnt++] = (unsigned char)(0x0);
		tmp++;
	}

	//plane
	uchar[cnt++] = (unsigned char)(0x01);
	uchar[cnt++] = (unsigned char)(0x00);

	//bitsPerPixel
	uchar[cnt++] = (unsigned char)(0x20);
	uchar[cnt++] = (unsigned char)(0x00);

	uchar[cnt++] = (unsigned char)(0x03);
	for (int i = 0; i < 3; i++)
		uchar[cnt++] = (unsigned char)(0x0);

	size = img->getDimensions().first *
		img->getDimensions().second * 4;
	tmp = 0;
	while (size > 0) {
		uchar[cnt++] = (unsigned char)(size % 256);
		size /= 256;
		tmp++;
	}
	while (tmp < 4) {
		uchar[cnt++] = (unsigned char)(0x0);
		tmp++;
	}

	for (int i = 0; i < 2; i++) {
		uchar[cnt++] = (unsigned char)(0x1e);
		uchar[cnt++] = (unsigned char)(0x2e);
		uchar[cnt++] = (unsigned char)(0x0);
		uchar[cnt++] = (unsigned char)(0x0);
	}

	for (int i = 0; i < 10; i++) {
		uchar[cnt++] = (unsigned char)(0x0);
	}

	uchar[cnt++] = (unsigned char)(0xff); uchar[cnt++] = (unsigned char)(0x0);
	uchar[cnt++] = (unsigned char)(0x0); uchar[cnt++] = (unsigned char)(0xff);
	uchar[cnt++] = (unsigned char)(0x0); uchar[cnt++] = (unsigned char)(0x0);
	uchar[cnt++] = (unsigned char)(0xff); 

	for (int i = 0; i < 6; i++) {
		uchar[cnt++] = (unsigned char)(0x0);
	}

	uchar[cnt++] = (unsigned char)(0xff); 
	uchar[cnt++] = (unsigned char)(0x20);
	uchar[cnt++] = (unsigned char)(0x6e);
	uchar[cnt++] = (unsigned char)(0x69);
	uchar[cnt++] = (unsigned char)(0x57);

	for (int i = 0; i < 48; i++) {
		uchar[cnt++] = (unsigned char)(0x0);
	}

	//std::cout << std::endl << cnt << std::endl;

	outputFile.write((char*)uchar, cnt);

	/**********DODATO************/

	std::reverse(img->begin(), img->end());
	for (std::vector<Pixel>& vp : *img) {
		for (Pixel& p : vp) {
			unsigned char* uc = new unsigned char[4]{ (unsigned char)(p.getBlue()),
				(unsigned char)(p.getGreen()), (unsigned char)(p.getRed()),
				(unsigned char)(p.getAlpha()*(img->getTransparency())/100) };
			outputFile.write((char*)uc, 4);
		}
	}
	std::reverse(img->begin(), img->end());
	delete[] uchar;
	outputFile.close();
}