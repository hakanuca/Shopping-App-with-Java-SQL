package NeedBox;
import java.util.*;
import java.io.*;
import java.sql.*;

public class Order
{
	private int Order_ID;
	private String Customer_Name;
	private String Order_Address;
	private String Customer_Contact_Number;
	public float Total_Amount;
	private ArrayList<Integer> Product_ID;
	private ArrayList<String> Product_Name;
	private ArrayList<Integer> Count;
	private ArrayList<Float> Price;
	Order(String Customer_Name, String Order_Address, String Customer_Contact_Number, ArrayList<Integer> Product_ID, ArrayList<String> Product_Name, ArrayList<Integer> Count, ArrayList<Float> Price) throws IOException {
		this.Customer_Name = Customer_Name;
		this.Order_Address = Order_Address;
		this.Customer_Contact_Number = Customer_Contact_Number;
		Total_Amount = 0.0f;
		this.Product_ID = Product_ID;
		this.Product_Name =Product_Name;
		this.Count =Count;
		this.Price =Price;
	}
	private void generateOrder() throws IOException {
		Order_ID = setOrderID();
		float sum = 0.0f;
		for(int i = 0; i < Product_ID.size(); i++)
			sum += Price.get(i);
		Total_Amount = sum;
	}
	private static int setOrderID() throws IOException {
		int x = 2999;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root", DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("select Order_ID from `order`");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next())
				x = Integer.parseInt(resultSet.getString(1));
		}
		catch(Exception e) { System.out.println(e); }
		return x + 1;
	}
	public void displayOrder() throws IOException {
		generateOrder();

		System.out.println("YOUR ORDER IS :");
		System.out.println("************************************************************************************************\n");
		System.out.printf("ORDER ID                  =  %-5d\n", Order_ID);
		System.out.printf("CUSTOMER NAME            =  %-20s\n", Customer_Name);
		System.out.printf("CUSTOMER CONTACT NUMBER  =  %-20s\n", Customer_Contact_Number);
		System.out.printf("CUSTOMER ADDRESS         =  %-30s\n", Order_Address);
		System.out.println("************************************************************************************************");
		System.out.printf("%-20s \t %-20s \t %-20s \t %-20s\n", "PRODUCT_ID","PRODUCT_NAME","COUNT_PURCHASED","TOTAL_PRICE");
		for(int x = 0; x < Product_ID.size(); x++)
			System.out.printf("%-20d \t %-20s  \t %-20d \t %-20f\n", Product_ID.get(x), Product_Name.get(x), Count.get(x), Price.get(x));
		System.out.println("************************************************************************************************\n");
		System.out.printf("TOTAL AMOUNT PAYABLE = " + Total_Amount + " $\n");
		System.out.println("************************************************************************************************\n");
	}
	public void addToDatabase() throws IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root", DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("insert into `order`(Order_ID, Buyer_Name, Order_Address,Order_Amount) values(?,?,?,?)");
			preparedStatement.setString(1, Integer.toString(Order_ID));
			preparedStatement.setString(2, Customer_Name);
			preparedStatement.setString(3, Order_Address);
			preparedStatement.setString(4, Float.toString(Total_Amount));
		    int x = preparedStatement.executeUpdate();
			if(x == 0)
				System.out.println("ORDER NOT ADDED TO DATABASE!");
		}
		catch(Exception e) {System.out.println(e);}
	}
}