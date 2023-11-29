package it.betacom.main;

import java.util.Scanner;
import it.betacom.operation.FunzioniMenu;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println("Seleziona un opzione");
			System.out.println("1. Calcola età autore");
			System.out.println("2. Ottieni autori per nazionalita inserita");
			System.out.println("3. Termina operazioni ed esci");
			
			int scelta = scanner.nextInt();
			scanner.nextLine();
			
			switch(scelta) {
			
			case 1: 
				FunzioniMenu.calcolaEtaAutore(scanner);
				break;
			
			case 2:
				FunzioniMenu.ottieniEinserisciAutori(scanner);
				break;
				
			case 3:
				scanner.close();
				System.out.println("Termina programma");
				System.exit(0);
				break;
				
			default:
				System.out.println("Scelta non valida, riprova");
			}
		}
	}

}
