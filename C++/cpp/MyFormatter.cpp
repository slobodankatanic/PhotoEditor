#include "MyFormatter.h"
#include "BMPFormatter.h"
#include "CompositeOperation.h"
#include "Image.h"
#include "Exceptions.h"
#include <string>
#include <algorithm>
#include <direct.h>
#include <io.h>
#include <typeinfo>
#include <regex>
#include "BasicOperations.h"

void MyFormatter::importImage(std::string path) {

	int p = path.size() - 4;
	if (p >= 0 && path[p] == '.' && (path[p + 1] == 't' || path[p + 1] == 'T') &&
		(path[p + 2] == 'x' || path[p + 2] == 'X') && (path[p + 3] == 't' || path[p + 3] == 'T')) {

		Image* image = Image::getInstance();

		tinyxml2::XMLDocument xmldoc;
		tinyxml2::XMLError eResult = xmldoc.LoadFile(path.c_str());
		XMLCheckResult(eResult);

		tinyxml2::XMLNode *root = xmldoc.FirstChild();
		if (root == nullptr) throw FileFormatException();

		/****HEADER***/
		tinyxml2::XMLElement *xmlelem = root->FirstChildElement("header");
		if (xmlelem == nullptr) { std::cout << "OVNA1"; throw FileFormatException(); }

		const char* buffer = nullptr;
		buffer = xmlelem->GetText();
		if (buffer == nullptr) {
			std::cout << "OVNA2"; throw FileFormatException();
		}
		std::string s = buffer;

		std::regex rx("([0-9]*) ([0-9]*)");
		std::smatch result;
		if (std::regex_match(s, result, rx)) {
			image->imageDimensions.second = atoi(result.str(1).c_str());
			image->imageDimensions.first = atoi(result.str(2).c_str());
		}
		else {
			std::cout << "OVNA3";
			throw FileFormatException();
		}

		/****LAYERS***/
		xmlelem = root->FirstChildElement("layers");
		if (xmlelem == nullptr) {
			std::cout << "OVNA4";
			throw FileFormatException();
		}

		int num;
		eResult = xmlelem->QueryIntAttribute("num", &num);
		XMLCheckResult(eResult);

		tinyxml2::XMLElement *layers = xmlelem->FirstChildElement("layer");
		for (int i = 0; i < num; i++) {
			if (layers == nullptr) {
				std::cout << "OVNA5";
				throw FileFormatException();
			}

			tinyxml2::XMLElement *in = layers->FirstChildElement("info");
			if (in == nullptr) {
				std::cout << "OVNA6";
				throw FileFormatException();
			}
			buffer = in->GetText();
			if (buffer == nullptr) {
				std::cout << "OVNA1";
				throw FileFormatException();
			}

			std::string str = buffer;
			std::regex rx1("([0-9]*) ([0-9]*) ([^ ]*) ([^ ]*) ([0-9]*)");
			std::smatch sm;

			int w, h, t; bool act, vis;

			if (std::regex_match(str, sm, rx1)) {
				w = atoi(sm.str(1).c_str());
				h = atoi(result.str(2).c_str());

				if (sm.str(3) == "true") act = true;
				else if (sm.str(3) == "false") act = false;
				else { std::cout << "OVNA2"; throw FileFormatException(); }

				if (sm.str(4) == "true") vis = true;
				else if (sm.str(4) == "false") vis = false;
				else { std::cout << "OVNA3"; throw FileFormatException(); }

				t = atoi(sm.str(5).c_str());
				if (t < 0 || t > 100) throw FileFormatException();
			}
			else throw FileFormatException();
			/****ZNACI U KECU DA BRISEM STANJE NAKON EXCEPA***/
			tinyxml2::XMLElement *pathelem = layers->FirstChildElement("path");
			if (pathelem == nullptr) { std::cout << "OVNA3"; throw FileFormatException(); }
			buffer = pathelem->GetText();
			if (buffer == nullptr) { std::cout << "OVNA3"; throw FileFormatException(); }
			std::string strp = buffer;

			Layer* l = new Layer(strp);
			if (l->getDimensions().first != image->imageDimensions.first ||
				l->getDimensions().second != image->imageDimensions.second ||
				l->getDimensions().first != h || l->getDimensions().second != w) {
				delete l;
				std::cout << "OVNA3"; throw FileFormatException();
			}
			if (act) l->setActivity();
			else l->resetActivity();

			if (vis) l->setVisibility();
			else l->resetVisibility();

			l->setTransparency(t);

			image->addLayer(l);
			layers = layers->NextSiblingElement("layer");
		}
		
		/****SELECTIONS***/
		xmlelem = root->FirstChildElement("selections");
		if (xmlelem == nullptr) throw FileFormatException();

		eResult = xmlelem->QueryIntAttribute("num", &num);
		XMLCheckResult(eResult);

		tinyxml2::XMLElement *selections = xmlelem->FirstChildElement("selection");
		std::cout << num << std::endl;
		for (int i = 0; i < num; i++) {
			if (selections == nullptr) throw FileFormatException();

			std::string name; 
			std::string _active_;
			const char* ch = nullptr, *active_ = nullptr;
			bool act; 
			int nums;

			eResult = selections->QueryBoolAttribute("active", &act);
			XMLCheckResult(eResult);
			eResult = selections->QueryIntAttribute("num", &nums);
			XMLCheckResult(eResult);
			eResult = selections->QueryStringAttribute("name", &ch);
			name = ch;
			XMLCheckResult(eResult);


			tinyxml2::XMLElement *rect = selections->FirstChildElement("rectangle");
			std::vector<Rectangle*> vr;
			for (int j = 0; j < nums; j++) {
				if (rect == nullptr) throw FileFormatException();

				buffer = rect->GetText();
				if (buffer == nullptr) throw FileFormatException();

				std::string str = buffer;
				std::regex rx1("([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*)");
				std::smatch sm;

				int x, y, w, h;
				if (std::regex_match(str, sm, rx1)) {
					x = atoi(sm.str(1).c_str());
					y = atoi(sm.str(2).c_str());
					w = atoi(sm.str(3).c_str());
					h = atoi(sm.str(4).c_str());
					if (x < 0 || y < 0 || w < 0 || h < 0) throw FileFormatException();
					Rectangle* r = new Rectangle(x, y, w, h);
					vr.push_back(r);
				} 
				else {
					for (Rectangle* r : vr) {
						delete r;
					}
					vr.erase(vr.begin(), vr.end());
					throw FileFormatException();
				}

				rect = rect->NextSiblingElement("rectangle");
			}

			Selection* sel = new Selection(name, vr);
			if (act) sel->setActivity();
			else sel->resetActivity();
			image->addSelection(sel);

			selections = selections->NextSiblingElement("selection");
		}
		
		/*//PAZI SVUGDE MORA DA SE BRISE ERASE ZA VECT ILI STA VEC ZBOG NUMA//*/

		/****COMP OPERS****/
		xmlelem = root->FirstChildElement("compoperations");
		if (xmlelem == nullptr) throw FileFormatException();

		eResult = xmlelem->QueryIntAttribute("num", &num);
		XMLCheckResult(eResult);

		tinyxml2::XMLElement *compop = xmlelem->FirstChildElement("compoperation");
		for (int i = 0; i < num; i++) {
			if (compop == nullptr) {
				throw FileFormatException();
			}

			Operation* o = importCompOper(compop);
			image->addCompOperation(o);

			compop = compop->NextSiblingElement("compoperation");
		}
	}
	else throw FileOrDirectoryNameException();
}

Operation* MyFormatter::importCompOper(tinyxml2::XMLElement* compop) {

	std::string name;
	const char* chn = nullptr;
	std::string type;
	const char* cht = nullptr;
	int nums;

	tinyxml2::XMLError eResult = compop->QueryStringAttribute("name", &chn);
	XMLCheckResult(eResult);
	eResult = compop->QueryIntAttribute("num", &nums);
	XMLCheckResult(eResult);
	eResult = compop->QueryStringAttribute("type", &cht);
	XMLCheckResult(eResult);

	name = chn;
	type = cht;

	if (type != "comp") throw FileFormatException();

	std::vector<Operation*> opers;
	tinyxml2::XMLElement *op = compop->FirstChildElement();

	for (int i = 0; i < nums; i++) {
		if (op == nullptr) throw FileFormatException();
		eResult = op->QueryStringAttribute("type", &cht);
		XMLCheckResult(eResult);

		type = cht;
		if (type == "basic") {
			const char* buffer = nullptr;
			buffer = op->GetText();
			if (buffer == nullptr) {
				for (Operation* o : opers) delete o;
				opers.erase(opers.begin(), opers.end());
				throw FileFormatException();
			}

			std::string str = buffer;
			std::regex rx("([^ ]*) ([0-9]*)");
			std::smatch sm;

			if (std::regex_match(str, sm, rx)) {
				std::string opnam = sm.str(1);
				int par = atoi(sm.str(2).c_str());

				Operation* newo = nullptr;

				if (opnam == "add") newo = new Add(par);
				else if (opnam == "sub") newo = new Sub(par);
				else if (opnam == "greyscale") newo = new Greyscale();
				else if (opnam == "median") newo = new Median();
				else if (opnam == "log") newo = new Log();
				else if (opnam == "inversion") newo = new Inversion();
				else if (opnam == "blackandwhite") newo = new BlackAndWhite();
				else if (opnam == "mul") newo = new Mul(par);
				else if (opnam == "div") newo = new Div(par);
				else if (opnam == "inversesub") newo = new InverseSub(par);
				else if (opnam == "inversediv") newo = new InverseDiv(par);
				else if (opnam == "power") newo = new Power(par);
				else if (opnam == "min") newo = new Min(par);
				else if (opnam == "max") newo = new Max(par);
				else if (opnam == "abs") newo = new Abs();
				if (newo == nullptr) {
					for (Operation* o : opers) delete o;
					opers.erase(opers.begin(), opers.end());
					throw FileFormatException();
				}
				opers.push_back(newo);
			}
			else {
				for (Operation* o : opers) delete o;
				opers.erase(opers.begin(), opers.end());
				throw FileFormatException();
			}
			op = op->NextSiblingElement();
		}
		else {
			if (type == "comp") {
				opers.push_back(importCompOper(op));
				op = op->NextSiblingElement();
			}
			else {
				for (Operation* o : opers) delete o;
				opers.erase(opers.begin(), opers.end());
				throw FileFormatException();
			}
		}
	}
	return new CompositeOperation(opers, name);
}

void MyFormatter::exportImage(std::string path, Layer* l) {

	int p = path.size() - 4;
	if (p >= 0 && path[p] == '.' && (path[p + 1] == 't' || path[p + 1] == 'T') &&
		(path[p + 2] == 'x' || path[p + 2] == 'X') && (path[p + 3] == 't' || path[p + 3] == 'T')) {

		Image* img = Image::getInstance();
		tinyxml2::XMLDocument xmldoc;

		tinyxml2::XMLNode* root = xmldoc.NewElement("image");
		xmldoc.InsertFirstChild(root);

		/**HEADER**/
		tinyxml2::XMLElement* xmlelem = xmldoc.NewElement("header");

		std::string s = std::to_string(img->getImageDimensions().second) + " " +
			std::to_string(img->getImageDimensions().first);

		xmlelem->SetText(s.c_str());
		root->InsertEndChild(xmlelem);

		/**LAYERS**/
		xmlelem = xmldoc.NewElement("layers");

		_mkdir(".\\saved");

		Formatter* f = new BMPFormatter();
		int n = 0;
		for (Layer* layer : *img) {
			tinyxml2::XMLElement* elem = xmldoc.NewElement("layer");
			tinyxml2::XMLElement* elemi = xmldoc.NewElement("info");
			tinyxml2::XMLElement* elemp = xmldoc.NewElement("path");
			char *templ = new char[9]{ "_XXXXXXX" };
			std::string name = _mktemp(templ);
			std::string p = ".\\saved\\layer_" + std::to_string(n) + name + ".bmp";
			std::string s = std::to_string(layer->getDimensions().second) + " " + std::to_string(layer->getDimensions().first)
				+ " " + (layer->isActive() ? "true" : "false") + " " + (layer->isVisible() ? "true" : "false") + " " + std::to_string(layer->getTransparency());
			elemi->SetText(s.c_str());
			elemp->SetText(p.c_str());
			elem->InsertEndChild(elemi);
			elem->InsertEndChild(elemp);
			xmlelem->InsertEndChild(elem);
			f->exportImage(p, layer);
			delete templ;
			n++;
		}
		delete f;

		xmlelem->SetAttribute("num", img->numOfLayers());
		root->InsertEndChild(xmlelem);

		xmlelem = xmldoc.NewElement("selections");

		for_each(img->beginSelection(), img->endSelection(), [&xmldoc, &xmlelem, this]
		(std::pair<std::string, Selection*> s) {

			tinyxml2::XMLElement* elem = xmldoc.NewElement("selection");

			for (Rectangle* r : *(s.second)) {
				tinyxml2::XMLElement* elemr = xmldoc.NewElement("rectangle");
				std::string s = std::to_string(r->getPosition().first) + " " + std::to_string(r->getPosition().second) +
					" " + std::to_string(r->getDimensions().second) + " " +
					std::to_string(r->getDimensions().first);
				elemr->SetText(s.c_str());
				elem->InsertEndChild(elemr);
			}

			elem->SetAttribute("name", s.first.c_str());
			elem->SetAttribute("active", s.second->isActive());
			elem->SetAttribute("num", s.second->numOfRect());
			xmlelem->InsertEndChild(elem);
		});

		xmlelem->SetAttribute("num", img->numOfSelections());
		root->InsertEndChild(xmlelem);

		/**COMP OPERS**/
		xmlelem = xmldoc.NewElement("compoperations");

		for (auto op : img->compositeOperation) {
			CompositeOperation* co = dynamic_cast<CompositeOperation*>(op.second);
			if (co == nullptr) throw DowncastException();
			exportCompOper(co, xmlelem, xmldoc);
		}

		xmlelem->SetAttribute("num", img->numOfCompOper());
		root->InsertEndChild(xmlelem);

		tinyxml2::XMLError eResult = xmldoc.SaveFile(path.c_str());
		XMLCheckSaveResult(eResult);
	}
	else throw FileOrDirectoryNameException();
}

void MyFormatter::exportCompOper(CompositeOperation* co, tinyxml2::XMLElement* e,
	tinyxml2::XMLDocument& xmldoc) {

	tinyxml2::XMLElement* elemcomp = xmldoc.NewElement("compoperation");

	for (Operation* o : *co) {
		if (!(typeid(*o) == typeid(CompositeOperation))) {
			tinyxml2::XMLElement* elembas = xmldoc.NewElement("operation");
			std::string s = o->getName() + " " + std::to_string(o->getParameter());
			elembas->SetText(s.c_str());
			elembas->SetAttribute("type", "basic");
			elemcomp->InsertEndChild(elembas);
		}
		else {
			CompositeOperation* cop = dynamic_cast<CompositeOperation*>(o);
			if (cop == nullptr) throw DowncastException();
			exportCompOper(cop, elemcomp, xmldoc);
		}
	}
	elemcomp->SetAttribute("type", "comp");
	elemcomp->SetAttribute("num", co->getSize());
	elemcomp->SetAttribute("name", co->getName().c_str());
	e->InsertEndChild(elemcomp);
}