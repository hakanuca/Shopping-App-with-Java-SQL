package NeedBox;
import java.io.*;
import java.util.*;
import java.sql.*;
public class Shop  {
	static int Seller_ID;
	static int Customer_ID;
	public static void main (String [] args) throws IOException, InterruptedException {
		DatabaseConnection.makeDatabase();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("		WELCOME TO ONLINE SHOPPING SYSTEM\n");
		int choice;
		do {
			System.out.println("+---------------------------------------------------+");
			System.out.println("|	 1 - REGISTER AS SELLER							|");
			System.out.println("|	 2 - REGISTER AS CUSTOMER						|");
			System.out.println("|	 3 - LOGIN TO SYSTEM						    |");
			System.out.println("| 	 4 - EXIT									    |");
			System.out.println("+---------------------------------------------------+");
			System.out.print("Enter choice : ");
			choice = Integer.parseInt(reader.readLine());

			switch (choice) {
				case 1 -> registerSeller();
				case 2 -> registerCustomer();
				case 3 -> loginSystem();
				case 4 -> {
					System.out.println("  _   _ ______ ______ _____  ____   ______   __");
					System.out.println(" | \\ | |  ____|  ____|  __ \\|  _ \\ / __ \\ \\ / /");
					System.out.println(" |  \\| | |__  | |__  | |  | | |_) | |  | \\ V / ");
					System.out.println(" | . ` |  __| |  __| | |  | |  _ <| |  | |> <  ");
					System.out.println(" | |\\  | |____| |____| |__| | |_) | |__| / . \\ ");
					System.out.println(" |_| \\_|______|______|_____/|____/ \\____/_/ \\_\\");
					System.out.println("--THANK YOU FOR USING NEEDBOX--");
				}
				default -> System.out.println("--WRONG CHOICE PLEASE CHECK IT AGAIN--");
			}
		} while(choice != 4);
	}
	static void loginSystem() throws IOException {
		String continueChoice;
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("\nWELCOME TO LOGIN PAGE\n");
		System.out.println("*****************************************************\n");
		
		//selecting data from login info table
		ArrayList<Integer> id = new ArrayList<>();
		ArrayList<String> pass = new ArrayList<>();
		ArrayList<Character> type = new ArrayList<>();
		
		try {
			int User_ID;
			String password;
			char tp=' ';
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/NeedBox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("select * from loginInfo");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				id.add(Integer.parseInt(resultSet.getString("User_ID")));
				pass.add(resultSet.getString("Password"));
				type.add((resultSet.getString("User_Type")).charAt(0));
			}

			int flag1=0;
			int f1,f2;
			do {
				System.out.print("Enter USER ID : ");
				User_ID = Integer.parseInt(bufferedReader.readLine());
				System.out.print("Enter PASSWORD : ");
				password = bufferedReader.readLine();
				f1 = id.indexOf(User_ID);
				f2 = pass.indexOf(password);
				if(f1 == f2 && f1 != -1) {
					flag1 = 1;
				}
				if(flag1 == 0) {
					System.out.println("INVALID CREDENTIALS , ENTER AGAIN !");
					System.out.print("Do you want to continue ( Y for yes, N for No)");
					continueChoice = bufferedReader.readLine();
					if(continueChoice.equalsIgnoreCase("N")) break;
				}
					
			} while(flag1 == 0);

			if(flag1 == 1)
				tp = type.get(id.indexOf(User_ID));

			if(tp == 'S') {
				Seller seller = new Seller(User_ID, password);
				seller.SellerPage();
			}
			else if(tp == 'C') {
				Customer customer = new Customer(User_ID, password);
				customer.CustomerPage();
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}
	static void registerSeller() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String password, name, contactNumber, address, email;
		float rating = 0;

		System.out.println("\nWELCOME TO SELLER REGISTRATION PAGE\n");
		System.out.println("+-----------------------------------------------------------+");
		setSellerID();
		System.out.println("SELLER ID = " + Seller_ID);
		System.out.print("Enter password = ");
		password = bufferedReader.readLine();
		System.out.print("Enter Name = ");
		name = bufferedReader.readLine();
		System.out.print("Enter Rating = ");
		rating = Float.parseFloat(bufferedReader.readLine());
		System.out.print("Enter contact number = ");
		contactNumber = bufferedReader.readLine();
		System.out.print("Enter address = ");
		address = bufferedReader.readLine();
		System.out.print("Enter email = ");
		email = bufferedReader.readLine();
		System.out.println("+-----------------------------------------------------------+\n");

		//inserting data into database
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/NeedBox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement ps=con.prepareStatement("insert into seller(Seller_ID,Seller_Name,Seller_Password,Seller_Rating,Seller_Email,Seller_Address,Contact_Number) values(?,?,?,?,?,?,?)");
			PreparedStatement ps1=con.prepareStatement("insert into loginInfo(User_ID,Password,User_Type) values(?,?,?)");
			ps.setString(1, Integer.toString(Seller_ID));
			ps.setString(2, name);
			ps.setString(3, password);
			ps.setString(4, String.valueOf(rating));
			ps.setString(5, email);
			ps.setString(6, address);
			ps.setString(7, contactNumber);
			ps1.setString(1, Integer.toString(Seller_ID));
			ps1.setString(2,password);
			ps1.setString(3, Character.toString('S'));
			int x = ps.executeUpdate();
			int y = ps1.executeUpdate();
			if(x > 0 && y > 0)
				System.out.println("REGISTRATION DONE SUCCESSFULLY !\n");
			else
				System.out.println("REGISTRATION FAILED !\n");
		}
		catch(Exception e) {System.out.println(e);}
	}
	static void registerCustomer() throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String password, name, contactNumber, address,email;

		System.out.println("\nWELCOME TO CUSTOMER REGISTRATION PAGE\n");
		System.out.println("+-----------------------------------------------------------+");
		setCustomerID();
		System.out.println("CUSTOMER ID = " + Customer_ID);
		System.out.print("Enter password = ");
		password = bufferedReader.readLine();
		System.out.print("Enter Name = ");
		name = bufferedReader.readLine();
		System.out.print("Enter contact number = ");
		contactNumber = bufferedReader.readLine();
		System.out.print("Enter address = ");
		address = bufferedReader.readLine();
		System.out.print("Enter email = ");
		email = bufferedReader.readLine();
		System.out.println("+-----------------------------------------------------------+\n");


		//inserting data into database
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/NeedBox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("insert into customer(Customer_ID, Customer_Name, Customer_Password, Customer_Email, Customer_Address,Contact_Number) values(?,?,?,?,?,?)");
			PreparedStatement preparedStatement1 = connection.prepareStatement("insert into loginInfo(User_ID,Password,User_Type) values(?,?,?)");
			preparedStatement.setString(1, Integer.toString(Customer_ID));
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, password);
			preparedStatement.setString(4, email);
			preparedStatement.setString(5, address);
			preparedStatement.setString(6, contactNumber);
			preparedStatement1.setString(1, Integer.toString(Customer_ID));
			preparedStatement1.setString(2,password);
			preparedStatement1.setString(3, Character.toString('C'));
			int x = preparedStatement.executeUpdate();
			int y = preparedStatement1.executeUpdate();
			if(x > 0 && y > 0)
				System.out.println("REGISTRATION DONE SUCCESSFULLY !\n");
			else
				System.out.println("REGISTRATION FAILED !\n");
		}
		catch(Exception e) { System.out.println(e); }
	}
	static void setCustomerID() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/NeedBox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("select Customer_ID from Customer");
			ResultSet resultSet = preparedStatement.executeQuery();
			int counter = 199;
			while(resultSet.next())
				counter=Integer.parseInt(resultSet.getString("Customer_ID"));
			Customer_ID = counter + 1;
		}
		catch(Exception e) {e.printStackTrace();}
	}
	static void setSellerID() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/NeedBox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("select Seller_ID from seller");
			ResultSet resultSet = preparedStatement.executeQuery();
			int counter = 99;
			while(resultSet.next())
				counter = Integer.parseInt(resultSet.getString("Seller_ID"));
			Seller_ID = counter + 1;
		}
		catch(Exception e) {e.printStackTrace();}
	}
}