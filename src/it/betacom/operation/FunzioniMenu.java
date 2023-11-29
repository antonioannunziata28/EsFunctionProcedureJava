package it.betacom.operation;

import java.util.Scanner;

public class FunzioniMenu {

	public static void calcolaEtaAutore(Scanner scanner) {
		System.out.println("Inserisci il nome dell'autore");
		String nome = scanner.nextLine();
		
		System.out.println("Inserisci il cognome dell'autore");
		String cognome = scanner.nextLine();
		
		Autore result = Autore.getAgeByAutore(nome, cognome);
		
		if (result.getAge() != -1) {
            if (result.getIsDead()) {
                System.out.println("L'autore " + nome + " " + cognome + " è morto all'età di " + result.getAge() + " anni.");
            } else {
                System.out.println("L'età di " + nome + " " + cognome + " è " + result.getAge() + " anni.");
            }
        } else {
            System.out.println("Autore non presente nel database");
        }
	}
	
	public static void ottieniEinserisciAutori(Scanner scanner) {
		System.out.println("Inserisci la nazionalità degli autori che vuoi conoscere");
		String nazionalita = scanner.nextLine();
		Autore.getAgeAutoriNazione(nazionalita);
		System.out.println("Dati inseriti nella tabella temporanea");
	}
}
