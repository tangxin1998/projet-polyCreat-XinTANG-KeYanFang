package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class myDoSweepingObserver implements Observer<Void> {
	
	PolyCreateControler gui;
	myDoSweepingObserver(PolyCreateControler sw){
		gui=sw;
	}
@Override
public void next(Void value) {
System.out.println("this is a reaction to doSweeping event");
	gui.doSweeping();
}
}