package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class ReleaseObjectObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	ReleaseObjectObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to a releaseObject event");
	gui.releaseObject();
}
}