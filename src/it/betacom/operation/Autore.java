package it.betacom.operation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Autore {

	private static String url_jdbc = "jdbc:mysql://localhost:3306/libri";
	private static String username = "root";
	private static String password = "rootroot";
	
	private int age;
	private boolean isDead;
	
	public Autore(int age, boolean isDead) {
		this.age = age;
		this.isDead = isDead;
	}
	
	
	public int getAge() {
		return age;
	}

	public boolean getIsDead() {
		return isDead;
	}


	public static Autore getAgeByAutore(String nome, String cognome) {
		
		try {
			Connection connection = DriverManager.getConnection(url_jdbc, username, password);
			
			String query = "SELECT anno_nascita, anno_morte FROM autori WHERE nome = ? AND cognome = ?";
			PreparedStatement pStm = connection.prepareStatement(query);
			
			pStm.setString(1, nome);
			pStm.setString(2, cognome);
			try(ResultSet rs = pStm.executeQuery()) {
				if(rs.next()) {
					String dataDiNascita = rs.getString("anno_nascita");
					Integer annoMorte = (Integer) rs.getObject("anno_morte");
					
					int age = calculateAge(dataDiNascita, annoMorte);
					boolean isDead =(annoMorte != null);
					
					return new Autore(age, isDead);
				} else {
					return new Autore(-1, false);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
            return new Autore(-1, false);
		}
	}
	
	
	private static int calculateAge(String dataDiNascita, Integer annoMorte) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		
		try {
			Date dateOfBirth = dateFormat.parse(dataDiNascita);
			
			Calendar currentDate = Calendar.getInstance();
			int currentYear = currentDate.get(Calendar.YEAR);
			
			int deathYear = (annoMorte != null) ? annoMorte : currentYear;
			
			Calendar deathDate = new GregorianCalendar(deathYear, 0,1);
			
			long ageInMillis = deathDate.getTimeInMillis() - dateOfBirth.getTime();
			return (int) (ageInMillis / (1000 * 60 * 60 * 24 * 365.25));
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return -1;
		
	}
	
}
