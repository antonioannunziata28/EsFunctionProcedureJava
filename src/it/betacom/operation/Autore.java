package it.betacom.operation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Autore implements Comparable<Autore>{

	private static String url_jdbc = "jdbc:mysql://localhost:3306/libri";
    private static String username = "root";
    private static String password = "rootroot";

    private int age;
    private boolean isDead;

    private String nome;
    private String cognome;

    public Autore(int age, boolean isDead) {
        this.age = age;
        this.isDead = isDead;
    }

    public Autore(int age, boolean isDead, String nome, String cognome) {
        this(age, isDead);
        this.nome = nome;
        this.cognome = cognome;
    }
	
	@Override
	public int compareTo(Autore altroAutore) {
		return Integer.compare(altroAutore.age, this.age);
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
	
	public static void getAgeAutoriNazione(String nazionalita) {
		createOrDropTable();	
	
		try {
			Connection connection = DriverManager.getConnection(url_jdbc, username, password);
			String query = "SELECT nome, cognome, anno_nascita, anno_morte FROM autori WHERE nazione = ?";
			PreparedStatement pStm = connection.prepareStatement(query);
			pStm.setString(1, nazionalita);
			
			ResultSet rs = pStm.executeQuery();
			while(rs.next()) {
				String nomeAutore = rs.getString("nome");
				String cognomeAutore = rs.getString("cognome");
				String dataNascita = rs.getString("anno_nascita");
				Integer annoMorte = rs.getInt("anno_morte");
				
				int age = calculateAge(dataNascita, annoMorte);
				insertIntoAutoreEtaTemp(connection, nomeAutore, cognomeAutore, age);
				
				
			}
			viewAutoriNazionalita(connection);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void viewAutoriNazionalita(Connection connection) {
		try {
			Statement stm = connection.createStatement();
			ResultSet rs = stm.executeQuery("SELECT nome, cognome FROM autori_eta_temp");
			
			while (rs.next()) {
	            System.out.println(
	                "Nome: " + rs.getString("nome") +
	                ", Cognome: " + rs.getString("cognome"));
			} 
			
		}catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void createOrDropTable() {
		
		try {
			Connection connection = DriverManager.getConnection(url_jdbc, username, password);
			Statement stm = connection.createStatement();
			
			//Drop table if exists
			stm.executeUpdate("DROP TABLE IF EXISTS autori_eta_temp");
			//Create table
			stm.executeUpdate("CREATE TABLE autori_eta_temp (" +
	                "id INT AUTO_INCREMENT PRIMARY KEY," +
	                "nome VARCHAR(255)," +
	                "cognome VARCHAR(255)," +
	                "eta INT," +
	                "data_odierna DATE"
	                + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void insertIntoAutoreEtaTemp(Connection connection, String nome, String cognome, int age) {
		
		String insertQuery = "INSERT INTO autori_eta_temp (nome, cognome, eta, data_odierna) VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement pStm = connection.prepareStatement(insertQuery);
			pStm.setString(1, nome);
			pStm.setString(2, cognome);
			pStm.setInt(3, age);
			
			//Data odierna
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat.format(Calendar.getInstance().getTime());
			pStm.setString(4, currentDate);
			
			//Eseguo l'inserimento
			pStm.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static int calculateAge(String dataDiNascita, Integer annoMorte) {
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


	public static String getUrl_jdbc() {
		return url_jdbc;
	}


	public static String getUsername() {
		return username;
	}


	public static String getPassword() {
		return password;
	}


	public static void setUrl_jdbc(String url_jdbc) {
		Autore.url_jdbc = url_jdbc;
	}


	public static void setUsername(String username) {
		Autore.username = username;
	}


	public static void setPassword(String password) {
		Autore.password = password;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	
	
}
