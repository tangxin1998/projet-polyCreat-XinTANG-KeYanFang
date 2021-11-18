package fr.univcotedazur.kairos.webots.polycreate.controler;

import com.yakindu.core.rx.Observer;

import fr.unice.polytech.si4.fsm.stopwatch.RobotStateMachine;

public class myDoForWardObserver implements Observer<Void> {
	
	PolyCreateControler bot;
	myDoForWardObserver(PolyCreateControler ws){
		bot=ws;
	}
	@Override
	public void next(Void value) {
		// TODO Auto-generated method stub
		System.out.println("this is a reaction to a doForWard event");
		bot.doForWard();
	}
	}


