package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class SearchObjctObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	SearchObjctObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to a searchObject event");
	gui.searchObject();
}
}