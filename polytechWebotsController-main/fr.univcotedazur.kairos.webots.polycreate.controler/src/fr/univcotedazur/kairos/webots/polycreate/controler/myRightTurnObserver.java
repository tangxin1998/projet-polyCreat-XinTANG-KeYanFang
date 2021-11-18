package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

public class myRightTurnObserver implements Observer<Void>{
	PolyCreateControler bot;
	myRightTurnObserver(PolyCreateControler ws){
		bot=ws;
	}
	@Override
	public void next(Void value) {
		// TODO Auto-generated method stub
		System.out.println("this is a reaction to a rightTurn event");
		bot.rightTurn();
	}

}
