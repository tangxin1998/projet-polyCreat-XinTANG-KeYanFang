package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class myGoBackwardObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	myGoBackwardObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to goBackward event");
	gui.backTurn();
}
}