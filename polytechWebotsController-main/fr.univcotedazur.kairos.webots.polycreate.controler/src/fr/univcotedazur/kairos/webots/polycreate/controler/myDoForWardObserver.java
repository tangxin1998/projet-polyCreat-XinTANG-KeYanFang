package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class myDoForWardObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	myDoForWardObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to a goForward event");
	gui.goForward();
}
}