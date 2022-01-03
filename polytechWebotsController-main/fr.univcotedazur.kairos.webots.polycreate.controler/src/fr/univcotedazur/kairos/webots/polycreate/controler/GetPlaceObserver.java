package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class GetPlaceObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	GetPlaceObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to a InputPlace event");
	gui.inputPlace();
}
}
