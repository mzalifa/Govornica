package govornica;

public class Osoba implements Runnable {
	
	private Govornica govornica;
	
	public Osoba(Govornica govornica) {
		this.govornica = govornica;
	}

	@Override
	public void run() {
		try {
			govornica.koristiGovornicu();
		} catch (InterruptedException e) { e.printStackTrace(); }
		
	}

}
