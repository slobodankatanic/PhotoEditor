#include "Selection.h"
#include <algorithm>
#include <string>

Selection::Selection(std::string name, std::vector<Rectangle*> v) : name(name), shapes(v), active(true) {}

bool Selection::isActiveCoordinates(int x, int y) const {

	return any_of(shapes.begin(), shapes.end(), [x, y](Rectangle* r) {
		return (r->getPosition().first + r->getDimensions().second - 1 >= x) && 
			(r->getPosition().first <= x) &&
			(r->getPosition().second + r->getDimensions().first - 1 >= y) &&
			(r->getPosition().second <= y);
	});
}

Selection::~Selection() {

	for (Rectangle* r : shapes) {
		delete r;
	}
}

std::ostream& operator<<(std::ostream& os, Selection& s) {

	os << s.name << " | aktivna - " << (s.active ? "DA" : "NE");
	if (s.shapes.size() == 0) os << std::endl << "-> Selekcija nema pravouganih oblika";
	else {
		for_each(s.shapes.begin(), s.shapes.end(), [&os, &s](Rectangle* r) {
			os << std::endl << "->" << *r;
		});
	}

	return os;
}