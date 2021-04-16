#pragma once

#include <exception>
#include <iostream>

class SelectionNameException : public std::exception {
public:
	const char* what() const override {
		return "!!!NE POSTOJI SELEKCIJA SA DATIM IMENOM!!!";
	}
};

class NameDuplicateException : public std::exception {
public:
	const char* what() const override {
		return "!!!UNETO IME VEC POSTOJI!!!";
	}
};

class InputException : public std::exception {
public:
	const char* what() const override {
		return "!!!UNETA JE NEVALIDNA VREDNOST!!!";
	}
};
public:
	const char* what() const override {
		return "!!!NEISPRAVAN FORMAT FAJLA!!!";
	}
};
public:
	const char* what() const override {
		return "!!!FAJL NIJE MOGUCE OTVORITI!!!";
	}
};
public:
	const char* what() const override {
		return "!!!NEMA VIDLJIVIH SLOJEVA!!!";
	}
};
public:
	const char* what() const override {
		return "!!!NEMA AKTIVNIH SLOJEVA!!!";
	}
};
public:
	const char* what() const override {
		return "!!!NE POSTOJI NIJEDNA KOMPOZITNA OPERACIJA!!!";
	}
};
public:
	const char* what() const override {
		return "!!!NE POSTOJI NIJEDAN SLOJ!!!";
	}
};
public:
	const char* what() const override {
		return "!!!PROBLEM SA KONVERZIJOM!!!";
	}
};
	const char* what() const override {
		return "!!!NE POSTOJI KOMPOZITNA OPERACIJA SA DATIM IMENOM!!!";
	}
	const char* what() const override {
		return "IME FAJLA ILI DIREKTORIJUMA NE POSTOJI";
	}