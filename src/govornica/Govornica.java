package govornica;

import java.util.LinkedList;
import java.util.concurrent.locks.*;

public class Govornica {
	
	private static final int brojKabina = 4;
	private static final int maxBrojOsobaURedu = 20;
	public int brojZauzetihKabina;
	public LinkedList<Thread> red; 
	private final Lock lock = new ReentrantLock();
	private Condition imaSlobodnaKabina;
	
	public Govornica() {
		brojZauzetihKabina = 0; 
		red = new LinkedList<>();
		imaSlobodnaKabina = lock.newCondition();
	}
	
	public void koristiGovornicu() throws InterruptedException {
		
		/* Dolazak u govornicu 
		Proverava se broj osoba u redu, 
		ako nema mesta u redu, čeka se da se oslobodi kabina, jer će neko drugi ući u istu
		i osloboditi mesto u redu.
		Ako ima slobodnih mesta u redu, a nema slobodnih kabina, osoba se dodaje u red.
		Ako ima slobodna kabina, a nema niko u redu, osoba direktno ulazi u kabinu.
		Primer: Thread-56 dolazi..
				Nema mesta u redu...
				Thread-33 napušta kabinu.
				Thread-36 koristi kabinu...
				Thread-56 dolazi u red. */
	    lock.lock();
	    try {
	    	System.out.println(Thread.currentThread().getName() + " dolazi..");
	        if (red.size() >= maxBrojOsobaURedu) {
	            System.out.println("Nema mesta u redu...");
	            imaSlobodnaKabina.await();
	        }
	        if (red.size() < 20 && brojZauzetihKabina == brojKabina) {
	        	red.addLast(Thread.currentThread());
	            System.out.println(Thread.currentThread().getName() + " dolazi u red.");
	        }
	    } finally {
	        lock.unlock();
	    }
	    
		/* Korišćenje kabine. */
	    lock.lock();
	    try {
	        while (brojZauzetihKabina >= brojKabina) {
	            imaSlobodnaKabina.await(); 
	        }
	        if (!red.isEmpty() && red.peekFirst() == Thread.currentThread()) {
	            red.removeFirst(); 
	        }    
	        brojZauzetihKabina++;
	        System.out.println(Thread.currentThread().getName() + " koristi kabinu...");
	    } finally {
	        lock.unlock();
	    }
	    Thread.sleep((long) (5000 + Math.random() * 5000));
	    
	    /* Izlazak iz kabine */
	    lock.lock();
	    try {
	        System.out.println(Thread.currentThread().getName() + " napušta kabinu.");
	        brojZauzetihKabina--;
	        imaSlobodnaKabina.signalAll();
	    } finally {
	        lock.unlock();
	    }
	    
	}

}