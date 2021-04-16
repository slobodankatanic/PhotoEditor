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
};class FileFormatException : public std::exception {
public:
	const char* what() const override {
		return "!!!NEISPRAVAN FORMAT FAJLA!!!";
	}
};class OpenningFileException : public std::exception {
public:
	const char* what() const override {
		return "!!!FAJL NIJE MOGUCE OTVORITI!!!";
	}
};class NoVisibleLayers : public std::exception {
public:
	const char* what() const override {
		return "!!!NEMA VIDLJIVIH SLOJEVA!!!";
	}
};class NoActiveLayers : public std::exception {
public:
	const char* what() const override {
		return "!!!NEMA AKTIVNIH SLOJEVA!!!";
	}
};class NoCompOper : public std::exception {
public:
	const char* what() const override {
		return "!!!NE POSTOJI NIJEDNA KOMPOZITNA OPERACIJA!!!";
	}
};class NoLayers : public std::exception {
public:
	const char* what() const override {
		return "!!!NE POSTOJI NIJEDAN SLOJ!!!";
	}
};class DowncastException : public std::exception {
public:
	const char* what() const override {
		return "!!!PROBLEM SA KONVERZIJOM!!!";
	}
};class CompOperationNameException : public std::exception {public:
	const char* what() const override {
		return "!!!NE POSTOJI KOMPOZITNA OPERACIJA SA DATIM IMENOM!!!";
	}};class FileOrDirectoryNameException : public std::exception {public:
	const char* what() const override {
		return "IME FAJLA ILI DIREKTORIJUMA NE POSTOJI";
	}};