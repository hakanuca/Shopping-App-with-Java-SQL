package NeedBox;
import java.util.*;
import java.io.*;
import java.sql.*;

public class Cart {
	private ArrayList<Integer> Product_ID = new ArrayList<>();
	private ArrayList<String> Product_Name = new ArrayList<>();
	private ArrayList<String> Category = new ArrayList<>();
	private ArrayList<Integer> Count_Purchased = new ArrayList<>();
	private ArrayList<Float> Total_Price = new ArrayList<>();
	public ArrayList<Integer> getProductID() throws IOException {
		return Product_ID;
	}
	public ArrayList<String> getProductName() throws IOException {
		return Product_Name;
	}
	public ArrayList<Integer> getCountPurchased() throws IOException {
		return Count_Purchased;
	}
	public ArrayList<Float> getTotalPrice() throws IOException {
		return Total_Price;
	}
	public void addToCart(int Product_ID, String Product_Name, String Category, int Count_Purchased, float Total_Price) throws IOException {
		this.Product_ID.add(Product_ID);
		this.Product_Name.add(Product_Name);
		this.Category.add(Category);
		this.Count_Purchased.add(Count_Purchased);
		this.Total_Price.add(Total_Price);
	}
	public void viewCart() throws IOException {
		int x = Product_ID.size();
		if(x != 0) {
			System.out.println("YOUR CART IS : \n");
			System.out.println("***********************************************************************************************************************\n");
			System.out.printf("%-20s \t %-20s \t %-20s \t %-20s \t %-20s\n", "Product_ID","Product_Name","Category","Count_Purchased","Total_Price");
			System.out.println("***********************************************************************************************************************\n");
			for(int i = 0; i < x; i++)
				System.out.printf("%-20d \t %-20s \t %-20s \t %-20d \t %-20f\n", Product_ID.get(i), Product_Name.get(i), Category.get(i), Count_Purchased.get(i), Total_Price.get(i) );
			System.out.println("***********************************************************************************************************************\n");
		}
		else
			System.out.println("CART IS EMPTY !");
	}
	public void removeFromCart(int Product_ID) throws IOException {
		int x = -1;
		int prevq = 0;
		int newq;
		int res = this.Product_ID.indexOf(Product_ID);
		if (res == -1)
			System.out.println("YOU HAVE NOT PURCHASED THIS PRODUCT !");
		else {
			this.Product_ID.remove(res);
			this.Product_Name.remove(res);
			this.Category.remove(res);
			this.Total_Price.remove(res);
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
				PreparedStatement preparedStatement = connection.prepareStatement("select Product_Count from product where Product_ID=?");
				preparedStatement.setString(1, Integer.toString(Product_ID));
				ResultSet resultSet = preparedStatement.executeQuery();
				while(resultSet.next())
					prevq = Integer.parseInt(resultSet.getString(1));
				newq = prevq + Count_Purchased.get(res);
				Count_Purchased.remove(res);
				PreparedStatement preparedStatement1 = connection.prepareStatement("update product set Product_Count=? where Product_ID=?");
				preparedStatement1.setString(1,Integer.toString(newq));
				preparedStatement1.setString(2, Integer.toString(Product_ID));
				x = preparedStatement1.executeUpdate();
			}
			catch(Exception e) { System.out.println(e); }
			if(x != 0)
				System.out.println("CART UPDATED SUCCESSFULLY !");
		}
	}
	public void cancelCart() throws IOException {
		try {
			int prevq = 0;
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("update product set Product_Count=? where Product_ID=?");
			int y;
			for(int x=0; x< Product_ID.size(); x++) {
				PreparedStatement preparedStatement1 = connection.prepareStatement("select Product_Count from product where Product_ID=?");
				preparedStatement1.setString(1, Integer.toString(Product_ID.get(x)));
				ResultSet resultSet = preparedStatement1.executeQuery();
				while(resultSet.next())
					prevq = Integer.parseInt(resultSet.getString(1));
				int newq = prevq + Count_Purchased.get(x);
				preparedStatement.setString(1, Integer.toString(newq));
				preparedStatement.setString(2, Integer.toString(Product_ID.get(x)));
				y = preparedStatement.executeUpdate();
				if(y == 0)
					System.out.println("PRODUCT NOT UPDATED BACK TO PRODUCTS TABLE !");
			}
		}
		catch(Exception e) {System.out.println(e);}
	}
}