#include <iostream>
#include <vector>
#include <fstream>
#include <string>
#include "BMPFormatter.h"
#include "PAMFormatter.h"
#include "BasicOperations.h"
#include "Image.h"
#include "Exceptions.h"
#include "CompositeOperation.h"
#include "tinyxml2.h"
#include "MyFormatter.h"
#include <filesystem>
#include <set>

int main(int argc, char** argv) {
	
	Image* image = Image::getInstance();
	///////////////////////////////////////////////////////
	/*FOR JAVA NATIVE INTERFACE*/
	try {
		/*LOAD PROJECT*/

		Formatter* f = new MyFormatter();
		std::string ss = argv[1]; 
		std::cout << argv[1];
		//std::getline(std::cin, ss);
		//std::cin.clear();
		//std::cin.ignore(INT_MAX, '\n');
		//std::cout << "Ucitavanje projekta..." << std::endl;
		f->importImage(ss);
		//image->unsave();
		delete f;

		/*LOAD PROJECT*/



		/*PERFORM COMP OPER*/

		if (image->numOfCompOper() == 0) throw NoCompOper();
		//image->displayCompOperInfo();
		//std::string s;
		//std::cout << "Unesite ime kompozitne operacije: ";
		//std::getline(std::cin, s);
		//std::cin.clear();
		//std::cin.ignore(INT_MAX, '\n');
		//std::cout << "Obrada..." << std::endl;
		for (std::pair<std::string, Operation*> p : image->getCompositeOperations()) {
			image->performCompOperation(p.first);
		}

		/*PERFORM COMP OPER*/

		/*SAVE IMAGE*/

		f = new MyFormatter();
		ss = argv[2];
		//std::getline(std::cin, ss);
		//std::cin.clear();
		//std::cin.ignore(INT_MAX, '\n');
		//std::cout << "Eksportovanje..." << std::endl;
		f->exportImage(ss);
		//else f->exportImage(ss, image->createFinalImage());
		delete f;

		/*SAVE IMAGE*/
	}
	catch (std::exception& e) {
		std::cout << e.what() << std::endl;
		//system("pause");
	}
	return 0;
	/*FOR JAVA NATIVE INTERFACE*/
	/////////////////////////////////////////////////////

	/*INTERACTION WITH USER BEFORE JAVA NATIVE INTERFACE*/
	/*
	int opt;
	int ende = 0;
	while (1) {
		try {
			if (ende == 1) break;
			std::cout << "IZABERITE JEDNU OD OPCIJA" << std::endl;
			std::cout << "1. Rad sa slojevima" << std::endl;
			std::cout << "2. Rad sa selekcijama" << std::endl;
			std::cout << "3. Operacije nad slikom" << std::endl;
			std::cout << "4. Rad sa kompozitnim funkcijama" << std::endl;
			std::cout << "5. Importovanje projekta iz sopstvenog formata" << std::endl;
			std::cout << "6. Eksportovanje slike" << std::endl;
			std::cout << "0. Kraj rada" << std::endl;
			std::cin >> opt;
			std::cin.clear();
			std::cin.ignore(INT_MAX, '\n');
			switch (opt) {
			case 1: {
				while (1) {
					system("cls");
					std::cout << "IZABERITE JEDNU OD OPCIJA" << std::endl;
					std::cout << "1. Dodavanje sloja" << std::endl;
					std::cout << "2. Aktiviranje sloja" << std::endl;
					std::cout << "3. Deaktiviranje sloja" << std::endl;
					std::cout << "4. Postavljanje vidljivosti sloja" << std::endl;
					std::cout << "5. Uklanjanje vidljivosti sloja" << std::endl;
					std::cout << "6. Postavljanje providnosti sloja" << std::endl;
					std::cout << "7. Uklanjanje sloja" << std::endl;
					std::cout << "8. Ispis stanja slojeva" << std::endl;
					std::cout << "0. Nazad" << std::endl;
					int l;
					std::cin >> l;
					std::cin.clear();
					std::cin.ignore(INT_MAX, '\n');
					if (l == 0) break;
					switch (l) {
					case 1: {
						Layer* layer = nullptr;
						int o;
						std::cout << "Da li zelite da dodate sliku na dati sloj?" << std::endl;
						std::cout << "1. Da" << std::endl << "2. Ne" << std::endl << "0. Nazad" << std::endl;
						std::cin >> o;
						std::cin.clear();
						std::cin.ignore(INT_MAX, '\n');
						if (o == 0) break;
						if (o == 1) {
							try {
								std::cout << "Unesite putanju do zeljene slike: " << std::endl;
								std::string s;
								std::getline(std::cin, s);
								//std::cin.clear();
								//std::cin.ignore(INT_MAX, '\n');
								std::cout << "Ucitavanje slike..." << std::endl;
								layer = new Layer(s);
								image->addLayer(layer);
								image->unsave();
							}
							catch (std::exception& e) { std::cout << e.what() << std::endl; system("pause"); }
						}
						else {
							if (o == 2) {
								try {
									int w = image->getImageDimensions().second, h = image->getImageDimensions().second;
									if (image->numOfLayers() == 0) {
										std::cout << "Unesite sirinu i visinu sloja: ";
										std::cin >> w >> h;
										std::cin.clear();
										std::cin.ignore(INT_MAX, '\n');
									}
									std::cout << "Ucitavanje praznog sloja..." << std::endl;
									layer = new Layer("_Empty_", w, h);
									image->addLayer(layer);
									image->unsave();
								}
								catch (std::exception& e) {
									std::cout << e.what() << std::endl;
									system("pause");
								}
							}
							else {
								std::cout << "!!!UNETA JE NEVALIDNA VREDNOST!!!" << std::endl;
								system("pause");
							}
						}
						break;
					}
					case 2: {
						try {
							int s;
							std::cout << "Redni broj sloja: ";
							std::cin >> s;
							std::cin.clear();
							std::cin.ignore(INT_MAX, '\n');
							image->activateLayer(s);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl;
							system("pause");
						}
						break;
					}
					case 3: {
						try {
							int s;
							std::cout << "Redni broj sloja: ";
							std::cin >> s;
							std::cin.clear();
							std::cin.ignore(INT_MAX, '\n');
							image->deactivateLayer(s);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl;
							system("pause");
						}
						break;
					}
					case 4: {
						try {
							int s;
							std::cout << "Redni broj sloja: ";
							std::cin >> s;
							std::cin.clear();
							std::cin.ignore(INT_MAX, '\n');
							image->setLayerVis(s);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl;
							system("pause");
						}
						break;
					}
					case 5: {
						try {
							int s;
							std::cout << "Redni broj sloja: ";
							std::cin >> s;
							std::cin.clear();
							std::cin.ignore(INT_MAX, '\n');
							image->resetLayerVis(s);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl;
							system("pause");
						}
						break;
					}
					case 6: {
						try {
							int s, p;
							std::cout << "Redni broj sloja: ";
							std::cin >> s;
							std::cin.clear();
							std::cin.ignore(INT_MAX, '\n');
							std::cout << "Providnost: ";
							std::cin >> p;
							std::cin.clear();
							std::cin.ignore(INT_MAX, '\n');
							image->setLayerTransp(s, p);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl;
							system("pause");
						}
						break;
					}
					case 7: {
						try {
							int s;
							std::cout << "Redni broj sloja: ";
							std::cin >> s;
							std::cin.clear();
							std::cin.ignore(INT_MAX, '\n');
							image->deleteLayer(s);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl;
							system("pause");
						}
						break;
					}
					case 8: {
						image->displayLayerInformations();
						system("pause");
						break;
					}
					}
				}
				break;
			}
			case 2: {
				while (1) {
					system("cls");
					std::cout << "IZABERITE JEDNU OD OPCIJA" << std::endl;
					std::cout << "1. Selektovanje dela slike" << std::endl;
					std::cout << "2. Aktiviranje selekcije" << std::endl;
					std::cout << "3. Deaktiviranje selekcije" << std::endl;
					std::cout << "4. Popunjavanje selekcije zadatom bojom" << std::endl;
					std::cout << "5. Uklanjanje selekcije" << std::endl;
					std::cout << "6. Ispis stanja selekcija" << std::endl;
					std::cout << "0. Nazad" << std::endl;
					int l;
					std::cin >> l;
					std::cin.clear();
					std::cin.ignore(INT_MAX, '\n');
					if (l == 0) break;
					switch (l) {
					case 1: {
						try {
							if (image->numOfLayers() == 0) throw NoLayers();
							int ok = 0;
							std::cout << "Naziv selekcije: ";
							std::string s;
							std::getline(std::cin, s);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							if (image->selNameExists(s)) throw NameDuplicateException();
							std::vector<Rectangle*> vr;
							std::cout <<
								"Unesite x koordinatu, y koordinatu gornjeg levog ulga, sirinu i visinu odvojene razmakom za oblike date selekcije"
								<< std::endl << "(sve -1 za kraj unosa)" << std::endl;
							while (1) {
								try {
									int x, y, w, h;
									std::cout << "Oblik: ";
									std::cin >> x >> y >> w >> h;
									std::cin.clear();
									std::cin.ignore(INT_MAX, '\n');
									if (x == -1 && y == -1 && w == -1 && h == -1) {
										if (vr.size() > 0) {
											Selection* ss = new Selection(s, vr);
											image->addSelection(ss);
											image->unsave();
										}
										break;
									}
									if (x >= image->getImageDimensions().second) continue;
									if (y >= image->getImageDimensions().first) continue;
									if (w > image->getImageDimensions().second - x) w = image->getImageDimensions().second - x;
									if (h > image->getImageDimensions().first - y) h = image->getImageDimensions().first - y;
									Rectangle* rr = new Rectangle(x, y, w, h);
									vr.push_back(rr);
								}
								catch (std::exception& e) {
									std::cout << e.what() << std::endl;
									system("pause");
								}
							}
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl;
							system("pause");
						}
						break;
					}					
					case 2: {
						try {
							std::string s;
							std::cout << "Naziv selekcije: ";
							std::getline(std::cin, s);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							image->activateSelection(s);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl << std::endl;
							system("pause");
						}
						break;
					}
					case 3: {
						try {
							std::string s;
							std::cout << "Naziv selekcije: ";
							std::cin >> s;
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							image->deactivateSelection(s);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl << std::endl;
							system("pause");
						}
						break;
					}
					case 4: {
						try {
							std::string s;
							std::cout << "Naziv selekcije: ";
							std::getline(std::cin, s);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							std::cout << "RGB komponente boje odvojene razmakom: ";
							int r, g, b;
							std::cin >> r >> g >> b;
							std::cin.clear();
							std::cin.ignore(INT_MAX, '\n');
							std::cout << "Popunjavanje..." << std::endl;
							image->fillSelection(s, r, g, b);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl << std::endl;
							system("pause");
						}
						break;
					}
					case 5: {
						try {
							std::string s;
							std::cout << "Naziv selekcije: ";
							std::getline(std::cin, s);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							image->deleteSelection(s);
							image->unsave();
						}
						catch (std::exception& e) {
							std::cout << e.what() << std::endl << std::endl;
							system("pause");
						}
						break;
					}
					case 6: {
						image->displaySelectionInformations();
						system("pause");
						break;
					}
					}
				}
				break;
			}
			case 3: {
				int opt;
				std::cout << "IZABERITE JEDNU OD OPERACIJA" << std::endl <<
					"1. Sabiranje" << std::endl << "2. Oduzimanje" << std::endl <<
					"3. Inverzno oduzimanje" << std::endl << "4. Mnozenje" << std::endl <<
					"5. Deljenje" << std::endl << "6. Inverzno deljenje" << std::endl <<
					"7. POWER" << std::endl << "8. LOG" << std::endl << "9. ABS" << std::endl <<
					"10. MIN" << std::endl << "11. MAX" << std::endl << "12. Inverzija" << std::endl <<
					"13. Pretvaranje u sliku u nijansama sive" << std::endl <<
					"14. Pretvaranje u crno-belu sliku" << std::endl << "15. Medijana" << std::endl <<
					"16. KOMPOZITNA OPERACIJA" << std::endl << "0. Nazad" << std::endl;
				try {
					std::cin >> opt;
					std::cin.clear();
					std::cin.ignore(INT_MAX, '\n');
					if (opt == 0) break;
					if (opt > 16 || opt < 1) throw InputException();
					int par;
					if (opt == 1 || opt == 2 || opt == 3 || opt == 4 || opt == 5 || opt == 6 || opt == 7
						|| opt == 10 || opt == 11) {
						std::cout << "Parametar: "; std::cin >> par;
						std::cin.clear();
						std::cin.ignore(INT_MAX, '\n');
					}
					switch (opt) {
					case 1: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("add", par); break; }
					case 2: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("sub", par); break; }
					case 3: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("inversesub", par); break; }
					case 4: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("mul", par); break; }
					case 5: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("div", par); break; }
					case 6: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("inversediv", par); break; }
					case 7: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("power", par); break; }
					case 8: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("log"); break; }
					case 9: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("abs");	break; }
					case 10: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("min", par); break; }
					case 11: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("max", par); break; }
					case 12: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("inversion"); break; }
					case 13: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("greyscale"); break; }
					case 14: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("blackandwhite"); break; }
					case 15: { std::cout << "Obrada..." << std::endl; image->performBasicOperation("median"); break; }
					case 16: {
						if (image->numOfCompOper() == 0) throw NoCompOper();
						image->displayCompOperInfo();
						std::string s;
						std::cout << "Unesite ime kompozitne operacije: ";
						std::getline(std::cin, s);
						//std::cin.clear();
						//std::cin.ignore(INT_MAX, '\n');
						std::cout << "Obrada..." << std::endl;
						image->performCompOperation(s);
						break;
					}
					}
					std::cout << "Operacija je odradjena" << std::endl;
					image->unsave();
					system("pause");
				}
				catch (std::exception& e) {
					std::cout << e.what() << std::endl;
					system("pause");
				}
				break;
			}
			case 4: {
				while (1) {
					system("cls");
					std::cout << "IZABERITE JEDNU OD OPCIJA" << std::endl <<
						"1. Dodavanje kompozitne operacije" << std::endl <<
						"2. Importovanje kompozitne funkcije" << std::endl <<
						"3. Eksportovanje kompozitne funkcije" << std::endl <<
						"4. Prikaz kompozitnih funkcija" << std::endl <<
						"0. Nazad" << std::endl;
					int l;
					std::cin >> l;
					std::cin.clear();
					std::cin.ignore(INT_MAX, '\n');
					if (l == 0) break;
					switch (l) {
					case 1: {
						std::vector<Operation*> vo;
						try {
							std::string s;
							std::cout << "Unesite ime kompozitne operacije: ";
							std::getline(std::cin, s);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							if (image->compOperNameExists(s)) throw NameDuplicateException();
							std::cout << "Izaberite operacije (0 za kraj unosa)" << std::endl << "1. Sabiranje" << std::endl <<
								"2. Oduzimanje" << std::endl << "3. Inverzno oduzimanje" << std::endl <<
								"4. Mnozenje" << std::endl << "5. Deljenje" << std::endl << "6. Inverzno deljenje"
								<< std::endl << "7. POWER" << std::endl << "8. LOG" << std::endl << "9. ABS" <<
								std::endl << "10. MIN" << std::endl << "11. MAX" << std::endl << "12. Inverzija"
								<< std::endl << "13. Pretvaranje u sliku u nijansama sive" << std::endl <<
								"14. Pretvaranje u crno-belu sliku" << std::endl << "15. Medijana" << std::endl <<
								"16. KOMPOZITNA OPERACIJA" << std::endl;
							int op, par;
							while (1) {
								try {
									std::cout << "Unos: ";
									std::cin >> op;
									std::cin.clear();
									std::cin.ignore(INT_MAX, '\n');
									if (op == 0) break;
									if (op < 0 || op > 16) throw InputException();
									if (op == 1 || op == 2 || op == 3 || op == 4 || op == 5 || op == 6 || op == 7
										|| op == 10 || op == 11) {
										std::cout << "Parametar: "; std::cin >> par;
										std::cin.clear();
										std::cin.ignore(INT_MAX, '\n');
									}
									switch (op) {
									case 1: {vo.push_back(new Add(par)); break;}
									case 2: {vo.push_back(new Sub(par)); break;}
									case 3: {vo.push_back(new InverseSub(par)); break;}
									case 4: {vo.push_back(new Mul(par)); break;}
									case 5: {vo.push_back(new Div(par)); break;}
									case 6: {vo.push_back(new InverseDiv(par)); break;}
									case 7: {vo.push_back(new Power(par)); break;}
									case 8: {vo.push_back(new Log()); break;}
									case 9: {vo.push_back(new Abs()); break;}
									case 10: {vo.push_back(new Min(par)); break;}
									case 11: {vo.push_back(new Max(par)); break;}
									case 12: {vo.push_back(new Inversion()); break;}
									case 13: {vo.push_back(new Greyscale()); break;}
									case 14: {vo.push_back(new BlackAndWhite()); break;}
									case 15: {vo.push_back(new Median()); break;}
									case 16: {
										if (image->numOfCompOper() == 0) throw NoCompOper();
										image->displayCompOperInfo();
										std::string s;
										std::cout << "Unesite ime postojece kompozitne operacije" << std::endl
											<< "koju zelite da udje u sastav nove kompozitne: ";
										std::getline(std::cin, s);
										//std::cin.clear();
										//std::cin.ignore(INT_MAX, '\n');
										if (!(image->compOperNameExists(s))) throw InputException();
										vo.push_back(image->getCompOper(s));
										break;
									}
									}
								}
								catch (std::exception& e) {
									std::cout << e.what() << std::endl;
									system("pause");
								}
							}
							if (vo.size() > 0) {
								CompositeOperation* co = new CompositeOperation(vo, s);
								image->addCompOperation(co);
							}
						}
						catch (std::exception& e) { std::cout << e.what() << std::endl; system("pause"); }
						break;
					}
					case 3: {
						try {
							std::string s, p;
							std::cout << "Unesite putanju do fajla u koji hocete da eksportujete funkciju"
								<< std::endl;
							std::getline(std::cin, s);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							std::cout << "Unesite ime funkcije koju zelite da eksportujete"
								<< std::endl;
							std::getline(std::cin, p);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							image->exportCompOper(p, s);
						}
						catch (std::exception& e) { std::cout << e.what() << std::endl; system("pause"); }
						break;
					}
					case 2: {
						try {
							std::string s, p;
							std::cout << "Unesite putanju do fajla iz kog hocete da importujete funkciju"
								<< std::endl;
							std::getline(std::cin, s);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							image->importCompOper(s);
							image->unsave();
						}
						catch (std::exception& e) { std::cout << e.what() << std::endl; system("pause"); }
						break;
					}
					case 4: { image->displayCompOperInfo(); system("pause"); break; }
					}
				}
				break;
			}
			case 5: {
				try {
					int o;
					if (!(image->isSaved())) {
						std::cout << "Da li zelite da eksportujete tekucu sliku?" << std::endl <<
							"1. Da" << std::endl << "2. Ne" << std::endl;
						std::cin >> o;
						std::cin.clear();
						std::cin.ignore(INT_MAX, '\n');
					}
					else o = 2;
					if (o == 1) {
						Formatter* f;
						if (image->numOfLayers() > 0) {
							std::cout << "IZABERITE ZELJENI FORMAT" << std::endl <<
								"1. BMP" << std::endl << "2. PAM" << std::endl <<
								"3. Soptstveni format" << std::endl;
							int op;
							std::cin >> op;
							std::cin.clear();
							std::cin.ignore(INT_MAX, '\n');

							if (op == 1) f = new BMPFormatter();
							else if (op == 2) f = new PAMFormatter();
							else if (op == 3) f = new MyFormatter();
							else throw InputException();
							std::string s;
							std::cout << "Unesite putanju do fajla u koji hocete da eksportujete sliku"
								<< std::endl;
							std::getline(std::cin, s);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							std::cout << "Eksportovanje..." << std::endl;
							f->exportImage(s, image->createFinalImage());
							image->save();
							delete f;
						}
						else {
							f = new MyFormatter();
							std::string s;
							std::cout << "Unesite putanju do fajla u koji hocete da eksportujete sliku u sopstvenom formatu"
								<< std::endl;
							std::getline(std::cin, s);
							//std::cin.clear();
							//std::cin.ignore(INT_MAX, '\n');
							std::cout << "Eksportovanje..." << std::endl;
							f->exportImage(s);
							image->save();
							delete f;
						}
					}
					else {
						if (o != 2) throw InputException();
					}
					Image::deleteInstance();
					Formatter* f = new MyFormatter();
					std::cout << std::endl
						<< "Navedite putanju do fajla iz kog zelite da importujete projekat" << std::endl;
					std::string ss; std::getline(std::cin, ss);
					//std::cin.clear();
					//std::cin.ignore(INT_MAX, '\n');
					std::cout << "Ucitavanje projekta..." << std::endl;
					f->importImage(ss);
					image->unsave();
					delete f;
				}
				catch (std::exception& e) {
					std::cout << e.what() << std::endl;
					system("pause");
				}
				break;
			}
			case 6: {
				try {
					Formatter* f = nullptr;
					std::cout << "IZBERITE ZELJENI FORMAT:" << std::endl << "1. BMP" << std::endl << "2. PAM" <<
						std::endl << "3. Sopstveni format" << std::endl << "0. Nazad" << std::endl;

					std::cin >> opt;
					std::cin.clear();
					std::cin.ignore(INT_MAX, '\n');
					if (opt == 1) {
						if (image->numOfVisLay() == 0) throw NoVisibleLayers();
						f = new BMPFormatter();
					}
					else if (opt == 2) {
						if (image->numOfVisLay() == 0) throw NoVisibleLayers();
						f = new PAMFormatter();
					}
					else if (opt == 3) f = new MyFormatter();
					else if (opt == 0) break;
					else throw InputException();
					std::cout << "Navedite putanju do fajla u koji zelite da eksportujete sliku" << std::endl;
					std::string ss;
					std::getline(std::cin, ss);
					//std::cin.clear();
					//std::cin.ignore(INT_MAX, '\n');
					std::cout << "Eksportovanje..." << std::endl;
					if (opt == 3) f->exportImage(ss);
					else f->exportImage(ss, image->createFinalImage());
					delete f;
					image->save();
				}
				catch (std::exception& e) {
					std::cout << e.what() << std::endl;
					system("pause");
				}
				break;
			}
			case 0: {
				std::cout << "Da li zaista zelite da napustite program?";
				if (!(image->isSaved())) { std::cout << " Postoje izmene koje nisu sacuvane!"; }
				std::cout << std::endl << "1. Da" << std::endl << "2. Ne" << std::endl;

				int ok = 0;
				while (!ok) {
					ok = 1;
					std::cin >> opt;
					std::cin.clear();
					std::cin.ignore(INT_MAX, '\n');

					if (opt == 2) {
						ende = 0;
					}
					else {
						if (opt == 1) {
							ende = 1;
							std::cout << "Da li zelite da eksportujete sliku?" << std::endl <<
								"1. Da" << std::endl << "2. Ne" << std::endl;
							int end = 0;
							while (!end) {
								end = 1;
								int o;
								std::cin >> o;
								std::cin.clear();
								std::cin.ignore(INT_MAX, '\n');

								if (o == 1) {
									Formatter* f;
									if (image->numOfLayers() > 0) {
										std::cout << "IZABERITE ZELJENI FORMAT" << std::endl <<
											"1. BMP" << std::endl << "2. PAM" << std::endl <<
											"3. Soptstveni format" << std::endl;
										int op;
										int okk = 0;
										while (!okk) {
											okk = 1;
											std::cin >> op;
											std::cin.clear();
											std::cin.ignore(INT_MAX, '\n');

											if (op == 1) f = new BMPFormatter();
											else if (op == 2) f = new PAMFormatter();
											else if (op == 3) f = new MyFormatter();
											else {
												std::cout << "Nevalidan unos, ponovo izaberite zeljeni format" <<
													std::endl << "1. BMP" << std::endl << "2. PAM" << std::endl <<
													"3. Soptstveni format" << std::endl;
												okk = 0;
											}
										}
										okk = 0;
										while (!okk) {
											try {
												okk = 1;
												std::string s;
												std::cout << "Unesite putanju do fajla u koji hocete da eksportujete sliku"
													<< std::endl;
												std::getline(std::cin, s);
												//std::cin.clear();
												//std::cin.ignore(INT_MAX, '\n');
												f->exportImage(s, image->createFinalImage());
												delete f;
											}
											catch (std::exception& e) {
												std::cout << e.what() << std::endl;
												okk = 0;
											}
										}
									}
									else {
										int okk = 0;
										f = new MyFormatter();
										while (!okk) {
											try {
												okk = 1;
												std::string s;
												std::cout << "Unesite putanju do fajla u koji hocete da eksportujete sliku u sopstvenom formatu"
													<< std::endl;
												std::getline(std::cin, s);
												//std::cin.clear();
												//std::cin.ignore(INT_MAX, '\n');
												f->exportImage(s);
												delete f;
											}
											catch (std::exception& e) {
												std::cout << e.what() << std::endl;
												okk = 0;
											}
										}
									}
								}
								else {
									if (o != 2) {
										std::cout << "Pogresan unos, ponovite" << std::endl;
										end = 0;
									}
								}
							}
						}
						else {
							ok = 0;
							std::cout << "Pogresan unos, ponovite" << std::endl;
						}
					}
				}
				break;
			}
			}
		}
		catch (std::exception& e) {
			std::cout << e.what() << std::endl;
			system("pause");
		}
		system("cls");
	}*/
}