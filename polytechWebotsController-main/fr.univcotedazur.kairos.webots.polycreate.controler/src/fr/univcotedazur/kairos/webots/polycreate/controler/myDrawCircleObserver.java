package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class myDrawCircleObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	myDrawCircleObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to drawCircle event");
	gui.drawCircle();
}
}