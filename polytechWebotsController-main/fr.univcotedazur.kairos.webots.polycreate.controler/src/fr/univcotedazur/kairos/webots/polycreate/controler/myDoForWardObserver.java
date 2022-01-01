package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class myDoForwardObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	myDoForwardObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to a goForward event");
	gui.goForward();
}
}