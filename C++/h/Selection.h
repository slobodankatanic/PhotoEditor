#pragma once
#include <vector>
#include "Rectangle.h"
#include <iostream>

class Selection {
	std::vector<Rectangle*> shapes;
	std::string name;
	bool active;
public:
	Selection(std::string name, std::vector<Rectangle*> v);
	~Selection();
	bool isActiveCoordinates(int x, int y) const;
	bool isActive() const { return active; }
	void setActivity() { active = true; }
	void resetActivity() { active = false; }
	std::vector<Rectangle*>::iterator begin() { return shapes.begin(); }
	std::vector<Rectangle*>::iterator end() { return shapes.end(); }
	std::string getName() const { return name; }
	int numOfRect() const { return shapes.size(); }
	friend std::ostream& operator<<(std::ostream&, Selection&);
};

