package NeedBox;
import java.sql.*;
import java.io.*;
public class DatabaseConnection {
	static String rootPassword;

	public static void makeDatabase() throws IOException, InterruptedException {
		try  {
			Class.forName("com.mysql.jdbc.Driver");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			int checker = 1;
			System.out.println("  _   _ ______ ______ _____  ____   ______   __");
			System.out.println(" | \\ | |  ____|  ____|  __ \\|  _ \\ / __ \\ \\ / /");
			System.out.println(" |  \\| | |__  | |__  | |  | | |_) | |  | \\ V / ");
			System.out.println(" | . ` |  __| |  __| | |  | |  _ <| |  | |> <  ");
			System.out.println(" | |\\  | |____| |____| |__| | |_) | |__| / . \\ ");
			System.out.println(" |_| \\_|______|______|_____/|____/ \\____/_/ \\_\\");


			System.out.print("CONNECTING TO DATABASE");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			System.out.print(".");
			Thread.sleep(500);
			System.out.print(".\n");
			do {
				System.out.print("ENTER ROOT PASSWORD = ");
				rootPassword = reader.readLine();

				try {
					DriverManager.getConnection("jdbc:mysql://localhost:3306/?autoReconnect=true&useSSL=false","root", rootPassword);
					checker = 1;

				} catch(Exception e) {
					System.out.println("WRONG PASSWORD ENTERED ! ENTER AGAIN !");
					checker = 0;
				}
			} while(checker == 0);

			PreparedStatement query;
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?autoReconnect=true&useSSL=false","root", rootPassword);

			//creating database NeedBox
			query = connection.prepareStatement("CREATE DATABASE NeedBox");
			query.executeUpdate();

			//selecting database NeedBox
			query = connection.prepareStatement("USE NeedBox");
			query.executeUpdate();

			//creating table seller
			query = connection.prepareStatement(
					"CREATE TABLE seller(" +
							"Seller_ID int not null, " +
							"Seller_Name varchar(20) not null," +
							"Seller_Password varchar(20) not null," +
							"Seller_Rating float not null, " +
							"Seller_Email varchar(30) not null, " +
							"Seller_Address varchar(80) not null, " +
							"Contact_Number varchar(11) not null, " +
							"primary key(Seller_ID))");
			query.executeUpdate();

			//creating table loginInfo
			query = connection.prepareStatement(
					"CREATE TABLE loginInfo(" +
							"User_ID int not null, " +
							"Password varchar(20) not null, " +
							"User_Type char not null, " +
							"primary key(User_ID))");
			query.executeUpdate();

			//creating table customer
			query = connection.prepareStatement(
					"CREATE TABLE customer(" +
							"Customer_ID int not null, " +
							"Customer_Name varchar(20) not null, " +
							"Customer_Password varchar(20) not null," +
							"Customer_Email varchar(30) not null, " +
							"Customer_Address varchar(80) not null, " +
							"Contact_Number varchar(11) not null, " +
							"primary key(Customer_ID))");
			query.executeUpdate();

			//creating table cargoCompany
			query = connection.prepareStatement(
					"CREATE TABLE cargoCompany(" +
							"Company_ID int not null, " +
							"Company_Name varchar(25) not null, " +
							"primary key(Company_ID))");
			query.executeUpdate();

			//creating table product
			query = connection.prepareStatement(
					"CREATE TABLE product(" +
							"Product_ID int not null, " +
							"Product_Name varchar(20) not null, " +
							"Category varchar(20) not null, " +
							"Product_Count int not null, " +
							"Product_Price float not null, " +
							"Company_ID int not null, " +
							"primary key(Product_ID), " +
							"foreign key(Company_ID) references cargoCompany(Company_ID))");
			query.executeUpdate();

			//creating table order
			query = connection.prepareStatement(
					"CREATE TABLE `order` (" +
							"Order_ID int not null, " +
							"Buyer_Name varchar(20) not null, " +
							"Order_Address varchar(80) not null, " +
							"Order_Amount float not null, " +
							"primary key(Order_ID))");
			query.executeUpdate();

			System.out.println("DATABASE CONNECTED SUCCESSFULLY...\n");
		} catch(Exception e) {
			if(e.toString().equalsIgnoreCase("java.sql.SQLException: Can't create database 'needbox'; database exists")){
				System.out.println();
				System.out.println("+----------------------------------------------+");
				System.out.println("|			       !CAUTION!			       |");
				System.out.println("|											   |");
				System.out.println("| THE PROGRAM WORKS ON THE ALREADY EXIST TABLE |");
				System.out.println("|											   |");
				System.out.println("| 		 THE SYSTEM OPENING IN 5 SECONDS	   |");
				System.out.println("+----------------------------------------------+\n");
				System.out.print("			 5 ->");
				Thread.sleep(500);
				System.out.print(" 4 ->");
				Thread.sleep(500);
				System.out.print(" 3 ->");
				Thread.sleep(500);
				System.out.print(" 2 ->");
				Thread.sleep(500);
				System.out.print(" 1\n\n");


			} else{
				System.out.println("ERROR DATABASE CANNOT CONNECTED...\n" + e.toString());}
		}
	}
}