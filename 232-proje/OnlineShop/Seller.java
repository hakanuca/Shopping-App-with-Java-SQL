package NeedBox;
import java.sql.*;
import java.io.*;

public class Seller extends Shop {
	private int Seller_ID;
	private String Seller_Password;
	static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	Seller(int ID, String Seller_Password)  {
		this.Seller_ID = ID;
		this.Seller_Password = Seller_Password;
	}
	public void SellerPage() throws IOException {
		System.out.println("WELCOME TO SELLER SECTION\n");
		int operation;
		do {
			System.out.println("+----------------------------------------------------+");
			System.out.println("|			1 - MANAGE PRODUCTS						 |");
			System.out.println("|			2 - ADD CUSTOMERS						 |");
			System.out.println("|			3 - REMOVE CUSTOMERS					 |");
			System.out.println("|			4 - EDIT PROFILE						 |");
			System.out.println("|			5 - VIEW REGISTERED CUSTOMERS			 |");
			System.out.println("|			6 - LOGOUT FROM SYSTEM					 |");
			System.out.println("+----------------------------------------------------+");
			System.out.print("Enter choice : ");
			operation = Integer.parseInt(reader.readLine());

			switch (operation) {
				case 1 -> {
					Product ob = new Product();
					ob.ProductsPage();
				}
				case 2 -> addCustomer();
				case 3 -> removeCustomer();
				case 4 -> editProfile(Seller_ID);
				case 5 -> viewCustomers();
				case 6 -> System.out.println("Thank you");
				default -> System.out.println("Wrong choice");
			}
		} while (operation != 6);
	}
	private static void editProfile(int Seller_ID) throws IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);

			int updateCheck = 0;
			String continueCheck;
			int operator = 0;

			String Seller_Name = "", Seller_Email = "", Seller_Address = "", Contact_Number = "", Seller_Password = "";
			float Seller_Rating = 0;

			do {
				System.out.println("+-----------------------------------------------------------+");
				System.out.println("|				1 - EDIT NAME								|");
				System.out.println("|				2 - EDIT RATING								|");
				System.out.println("|				3 - EDIT EMAIL								|");
				System.out.println("|				4 - EDIT ADDRESS							|");
				System.out.println("|				5 - EDIT CONTACT NUMBER						|");
				System.out.println("|				6 - CHANGE PASSWORD							|");
				System.out.println("|				7 - EXIT									|");
				System.out.println("+-----------------------------------------------------------+");
				System.out.print("Enter choice: ");
				operator = Integer.parseInt(reader.readLine());

				switch (operator) {
					case 1 -> {
						System.out.print("ENTER NEW NAME : ");
						Seller_Name = reader.readLine();
						PreparedStatement preparedStatement = connection.prepareStatement("UPDATE seller SET Seller_Name = ? WHERE Seller_ID = ?");
						preparedStatement.setString(1, Seller_Name);
						preparedStatement.setString(2, Integer.toString(Seller_ID));
						updateCheck = preparedStatement.executeUpdate();
						if(updateCheck == 1)
							System.out.println("Name updated successfully");
						else
							System.out.println("Name not updated");
					}
					case 2 -> {
						System.out.print("ENTER NEW RATING : ");
						Seller_Rating = Float.parseFloat(reader.readLine());
						PreparedStatement preparedStatement = connection.prepareStatement("update seller set Seller_Rating = ? where Seller_ID=?");
						preparedStatement.setString(1, Float.toString(Seller_Rating));
						preparedStatement.setString(2, Integer.toString(Seller_ID));
						updateCheck = preparedStatement.executeUpdate();
						if (updateCheck == 1)
							System.out.println("Rating updated successfully");
						else
							System.out.println("Rating not updated");
					}
					case 3 -> {
						System.out.print("ENTER NEW EMAIL ID : ");
						Seller_Email = reader.readLine();
						PreparedStatement preparedStatement = connection.prepareStatement("update seller set Seller_Email = ? where Seller_ID=?");
						preparedStatement.setString(1, Seller_Email);
						preparedStatement.setString(2, Integer.toString(Seller_ID));
						updateCheck = preparedStatement.executeUpdate();
						if (updateCheck == 1)
							System.out.println("Email updated successfully");
						else
							System.out.println("Email not updated");
					}
					case 4 -> {
						System.out.print("ENTER ADDRESS : ");
						Seller_Address = reader.readLine();
						PreparedStatement preparedStatement = connection.prepareStatement("update seller set Seller_Address = ? where Seller_ID=?");
						preparedStatement.setString(1, Seller_Address);
						preparedStatement.setString(2, Integer.toString(Seller_ID));
						updateCheck = preparedStatement.executeUpdate();
						if (updateCheck == 1)
							System.out.println("Address updated successfully");
						else
							System.out.println("Address not updated");
					}
					case 5 -> {
						System.out.print("ENTER NEW CONTACT NUMBER : ");
						Contact_Number = reader.readLine();
						PreparedStatement preparedStatement = connection.prepareStatement("update seller set Contact_Number = ? where Seller_ID=?");
						preparedStatement.setString(1, Contact_Number);
						preparedStatement.setString(2, Integer.toString(Seller_ID));
						updateCheck = preparedStatement.executeUpdate();
						if (updateCheck == 1)
							System.out.println("Contact number updated successfully");
						else
							System.out.println("Contact number not updated");
					}
					case 6 -> {
						System.out.print("ENTER NEW PASSWORD : ");
						Seller_Password = reader.readLine();
						PreparedStatement preparedStatement = connection.prepareStatement("update loginInfo set Password = ? where User_ID=?");
						preparedStatement.setString(1, Seller_Password);
						preparedStatement.setString(2, Integer.toString(Seller_ID));
						updateCheck = preparedStatement.executeUpdate();
						if (updateCheck == 1)
							System.out.println("Password updated successfully");
						else
							System.out.println("Password not updated");
					}
					case 7 -> System.out.println("Thank you");
					default -> System.out.println("Wrong choice");
				}
				System.out.print("Do you want to continue (Y for yes, N for No) : ");
				continueCheck = reader.readLine();
			} while(continueCheck.equalsIgnoreCase("Y"));
		}
		catch(Exception e) {System.out.println(e);}
	}
	private static void viewCustomers() throws IOException {
		int Customer_ID;
		String Customer_Name, Customer_Email, Customer_Address, Contact_Number;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/NeedBox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("select * from customer");
			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet == null)
				System.out.println("NO CUSTOMERS AVAILABLE");
			else {
				System.out.println("**********************************************************************************************************************************************************************\n");
				System.out.printf("%-20s \t %-20s \t %-20s \t %-30s \t %-20s\n","CUSTOMER_ID","NAME","EMAIL","ADDRESS","CONTACT_NUMBER");
				System.out.println("**********************************************************************************************************************************************************************\n");


				while(resultSet.next()) {
					Customer_ID = Integer.parseInt(resultSet.getString(1));
					Customer_Name = resultSet.getString(2);
					Customer_Email = resultSet.getString(3);
					Customer_Address = resultSet.getString(4);
					Contact_Number = resultSet.getString(5);
					System.out.printf("%-20d \t %-20s \t %-20s \t %-30s \t %-20s\n",Customer_ID, Customer_Name, Customer_Email, Customer_Address, Contact_Number);
				}
				System.out.println("*********************************************************************************************************************************************************************\n");
			}
		}
		catch(Exception e) {System.out.println(e);}
	}
	private static void addCustomer() throws IOException {
		Shop.registerCustomer();
	}
	private static void removeCustomer() throws IOException {
		int Customer_ID;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("select * from customer");
			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet == null)
				System.out.println("NO CUSTOMERS AVAILABLE");
			else {
				System.out.print("Enter customer ID to delete : ");
				Customer_ID = Integer.parseInt(reader.readLine());
				PreparedStatement ps1 = connection.prepareStatement("delete from customer where Customer_ID=?");
				PreparedStatement ps2 = connection.prepareStatement("delete from loginInfo where User_ID=?");
				ps1.setString(1, Integer.toString(Customer_ID));
				ps2.setString(1, Integer.toString(Customer_ID));
				int x = ps1.executeUpdate();
				int y = ps2.executeUpdate();
				if(x != 0 && y != 0)
					System.out.println("CUSTOMER INFO DELETED SUCCESSFULLY !");
				else
					System.out.println("CUSTOMER INFO NOT FOUND !");
			}
		}
		catch(Exception e) {System.out.println(e);}
	}
}