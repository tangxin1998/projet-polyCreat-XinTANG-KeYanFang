package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class myRightTurnObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	myRightTurnObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to rightTurn event");
	gui.rightTurn();
}
}