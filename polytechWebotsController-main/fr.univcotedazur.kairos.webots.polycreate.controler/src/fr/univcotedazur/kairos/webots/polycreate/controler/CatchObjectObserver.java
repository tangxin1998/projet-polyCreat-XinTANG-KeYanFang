package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class CatchObjectObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	CatchObjectObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to a catchObject event");
	gui.catchObject();
}
}