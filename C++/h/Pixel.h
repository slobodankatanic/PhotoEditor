#pragma once

class Pixel {
private:
	 int red, green, blue, alpha;
public:
	Pixel(int r = 0, int g = 0, int b = 0, int a = 0) :
		red(r), green(g), blue(b), alpha(a) {}
	int getRed() const { return red; }
	int getGreen() const { return green; }
	int getBlue() const { return blue; }
	int getAlpha() const { return alpha; }
	void setRed(int r) { red = r; }
	void setGreen(int g) { green = g; }
	void setBlue(int b) { blue = b; }
	void setAlpha(int a) { alpha = a; }
};
