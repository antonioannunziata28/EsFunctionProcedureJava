package it.betacom.main;

import java.util.Scanner;

import it.betacom.operation.Autore;

public class Main {

	public static void main(String[] args) {
		
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Inserisci il nome dell'autore");
		String nome = scanner.nextLine();
		
		System.out.println("Inserisci il cognome dell'autore");
		String cognome = scanner.nextLine();
		
		Autore result = Autore.getAgeByAutore(nome, cognome);
		
		if(result.getAge() != -1) {
			if(result.getIsDead()) {
				System.out.println("L'autore " + nome + " " + cognome + " è morto all'età di " + result.getAge() + " anni");
			} else {
				System.out.println("L'età di " + nome + " " + cognome + " è di " + result.getAge() + " anni.");
			} 
		} else {
			System.out.println("Autore non presente a database");
		}
		
		scanner.close();

	}

}
