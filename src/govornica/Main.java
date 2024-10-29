package govornica;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		Govornica govornica = new Govornica();
		while (true) {
            Thread osoba = new Thread(new Osoba(govornica));
            osoba.start();
            Thread.sleep(2000);
        }
		
	}

}
