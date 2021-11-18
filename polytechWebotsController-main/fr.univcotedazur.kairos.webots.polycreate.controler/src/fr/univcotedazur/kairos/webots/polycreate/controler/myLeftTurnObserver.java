package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class myLeftTurnObserver implements Observer<Void>{
	PolyCreateControler bot;
	myLeftTurnObserver(PolyCreateControler ws){
		bot=ws;
	}
	@Override
	public void next(Void value) {
		// TODO Auto-generated method stub
		System.out.println("this is a reaction to a leftTurn event");
		bot.leftTurn();
	}

}