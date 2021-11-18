package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;


public class myBackTurnObserver implements Observer<Void> {
	
	PolyCreateControler bot;
	myBackTurnObserver(PolyCreateControler ws){
		bot=ws;
	}
	@Override
	public void next(Void value) {
		// TODO Auto-generated method stub
		System.out.println("this is a reaction to a backTurn event");
		bot.backTurn();
	}
	}


