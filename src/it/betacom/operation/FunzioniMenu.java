package it.betacom.operation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
	
	public static List<Autore> getAutoreListByNazione(String nazionalita){
		Autore.createOrDropTable();
	    List<Autore> autoriList = new ArrayList<>();

	    String querySelect = "SELECT nome, cognome, anno_nascita, anno_morte FROM autori WHERE nazione = ?";
	    try (Connection connection = DriverManager.getConnection(Autore.getUrl_jdbc(), Autore.getUsername(), Autore.getPassword());
	         PreparedStatement pStm = connection.prepareStatement(querySelect)) {

	        pStm.setString(1, nazionalita);

	        ResultSet rs = pStm.executeQuery();
	        while (rs.next()) {
	            String nomeAutore = rs.getString("nome");
	            String cognomeAutore = rs.getString("cognome");
	            String dataNascita = rs.getString("anno_nascita");
	            Integer annoMorte = rs.getInt("anno_morte");

	            int age = Autore.calculateAge(dataNascita, annoMorte);
	            Autore autore = new Autore(age, (annoMorte != null), nomeAutore, cognomeAutore);
	            autoriList.add(autore);
	            Autore.insertIntoAutoreEtaTemp(connection, nomeAutore, cognomeAutore, age);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return autoriList;
	}
}
