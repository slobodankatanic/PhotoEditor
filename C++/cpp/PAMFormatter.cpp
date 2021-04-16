#include "PAMFormatter.h"
#include "Exceptions.h"
#include <fstream>
#include <stack>

void PAMFormatter::exportImage(std::string path, Layer* img) {

	std::ofstream outputFile(path, std::ios::binary);
	if (!outputFile.is_open()) {
		outputFile.close();
		throw OpenningFileException();
	}

	unsigned char* uc = new unsigned char[46];

	uc[0] = 'P'; uc[1] = '7'; uc[2] = 0x0a; uc[3] = 'W'; 
	uc[4] = 'I'; uc[5] = 'D'; uc[6] = 'T'; uc[7] = 'H';
	uc[8] = ' ';
	outputFile.write((char*)uc, 9);

	int w = img->getDimensions().second;
	//std::cout << std::endl << w << std::endl;
	std::stack<unsigned char> suc;

	while (w > 0) {
		suc.push((unsigned char)(w % 10 + 0x30));
		w /= 10;
	}
	int size = suc.size();
	for (int i = 0; i < size; i++) {
		uc[i] = suc.top();
		suc.pop();
	}
	outputFile.write((char*)uc, size);

	uc[0] = 0x0a; uc[1] = 'H'; uc[2] = 'E'; uc[3] = 'I';
	uc[4] = 'G'; uc[5] = 'H'; uc[6] = 'T'; uc[7] = ' ';
	outputFile.write((char*)uc, 8);

	int h = img->getDimensions().first;

	while (h > 0) {
		suc.push((unsigned char)(h % 10 + 0x30));
		h /= 10;
	}
	size = suc.size();
	for (int i = 0; i < size; i++) {
		uc[i] = suc.top(); suc.pop();
	}
	outputFile.write((char*)uc, size);

	uc[0] = 0x0a; uc[1] = 'D'; uc[2] = 'E'; uc[3] = 'P'; uc[4] = 'T'; uc[5] = 'H';
	uc[6] = ' '; uc[7] = '4'; uc[8] = 0x0a; uc[9] = 'M'; uc[10] = 'A'; uc[11] = 'X';
	uc[12] = 'V'; uc[13] = 'A'; uc[14] = 'L'; uc[15] = ' '; uc[16] = '2'; uc[17] = '5';
	uc[18] = '5'; uc[19] = 0x0a; uc[20] = 'T'; uc[21] = 'U'; uc[22] = 'P'; uc[23] = 'L';
	uc[24] = 'T'; uc[25] = 'Y'; uc[26] = 'P'; uc[27] = 'E'; uc[28] = ' '; uc[29] = 'R';
	uc[30] = 'G'; uc[31] = 'B'; uc[32] = '_'; uc[33] = 'A'; uc[34] = 'L';
	uc[35] = 'P'; uc[36] = 'H'; uc[37] = 'A'; uc[38] = 0x0a; uc[39] = 'E';
	uc[40] = 'N'; uc[41] = 'D'; uc[42] = 'H'; uc[43] = 'D'; uc[44] = 'R'; uc[45] = 0x0a;
	outputFile.write((char*)uc, 46);
	delete[] uc;

	for (std::vector<Pixel>& vp : *img) {
		unsigned char* uc = nullptr;
		for (Pixel& p : vp) {
			uc = new unsigned char[4]{ (unsigned char)(p.getRed()),
				(unsigned char)(p.getGreen()), (unsigned char)(p.getBlue()),
				(unsigned char)(p.getAlpha()*(img->getTransparency())/100) };
			outputFile.write((char*)uc, 4);
			delete[] uc;
		}
	}
	outputFile.close();
}

void PAMFormatter::importImage(std::string path) {

	std::ifstream inputFile(path, std::ios::binary);
	if (!inputFile.is_open()) {
		inputFile.close();
		throw OpenningFileException();
	}

	unsigned char* id = new unsigned char[2];
	inputFile.read((char*)id, 2);
	if (id[0] != 0x50 || id[1] != 0x37) {
		delete[] id;
		throw FileFormatException();
	}
	delete[] id;

	inputFile.seekg(0x9, inputFile.beg);
	int width = 0;
	unsigned char* uc = new unsigned char[1];
	while (1) {
		inputFile.read((char*)uc, 1);
		if (*uc == 0x0a) { break; }
		width *= 10;
		width += (int)*uc - 0x30;
	}
	this->width = width;
	//std::cout << width << std::endl;

	inputFile.seekg(0x7, inputFile.cur);
	int heigth = 0;
	while (1) {
		inputFile.read((char*)uc, 1);
		if (*uc == 0x0a) { break; }
		heigth *= 10;
		heigth += (int)*uc - 0x30;
	}
	this->heigth = heigth;
	//std::cout << heigth << std::endl;

	inputFile.seekg(0x6, inputFile.cur);
	int depth = 0;
	while (1) {
		inputFile.read((char*)uc, 1);
		if (*uc == 0x0a) { break; }
		depth *= 10;
		depth += (int)*uc - 0x30;
	}

	inputFile.seekg(0x7, inputFile.cur);
	int maxval = 0;
	while (1) {
		inputFile.read((char*)uc, 1);
		if (*uc == 0x0a) { break; }
		maxval *= 10;
		maxval += (int)*uc - 0x30;
	}

	inputFile.seekg(0x9, inputFile.cur);
	std::string type;
	while (1) {
		inputFile.read((char*)uc, 1);
		if (*uc == 0x0a) { delete[] uc; break; }
		type.push_back(*uc);
	}
	inputFile.seekg(0x7, inputFile.cur);

	unsigned char blue; unsigned char green;
	unsigned char red; unsigned char alpha;
	uc = new unsigned char[8];

	if (type == "RGB") {
		for (int i = 0; i < heigth; i++) {
			std::vector<Pixel> vp;
			for (int j = 0; j < width; j++) {
				if (maxval <= 255) {
					inputFile.read((char*)uc, 3);
					blue = uc[2]; green = uc[1];
					red = uc[0]; alpha = 255;
				}
				else {
					inputFile.read((char*)uc, 6);
					blue = (uc[4] * 255 + uc[5]) / 255; green = (uc[2] * 255 + uc[3]) / 255;
					red = (uc[0] * 255 + uc[1]) / 255; alpha = 255;
				}
				vp.push_back(Pixel(red, green, blue, alpha));
			}
			pixels.push_back(vp);
			vp.erase(vp.begin(), vp.end());
		}
		inputFile.close();
		delete uc;
		return;
	}
	if (type == "RGB_ALPHA") {
		for (int i = 0; i < heigth; i++) {
			std::vector<Pixel> vp;
			for (int j = 0; j < width; j++) {
				if (maxval <= 255) {
					inputFile.read((char*)uc, 4);
					blue = uc[2]; green = uc[1];
					red = uc[0]; alpha = uc[3];
				}
				else {
					inputFile.read((char*)uc, 8);
					blue = (uc[4] * 255 + uc[5]) / 255; green = (uc[2] * 255 + uc[3]) / 255;
					red = (uc[0] * 255 + uc[1]) / 255; alpha = (uc[6] * 255 + uc[7]) / 255;
				}
				vp.push_back(Pixel(red, green, blue, alpha));
			}
			pixels.push_back(vp);
			vp.erase(vp.begin(), vp.end());
		}
		inputFile.close();
		delete uc;
		return;
	}

	throw FileFormatException();
}
