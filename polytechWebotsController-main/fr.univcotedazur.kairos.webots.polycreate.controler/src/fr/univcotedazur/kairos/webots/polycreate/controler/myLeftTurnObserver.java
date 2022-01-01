package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class myLeftTurnObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	myLeftTurnObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to leftTurn event");
	gui.leftTurn();
}
}