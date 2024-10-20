package NeedBox;

import java.sql.*;
import java.io.*;

public class Product {
	private int Product_ID, Product_Count, Cargo_CompanyID;
	private String Product_Name, Category, Cargo_CompanyName;
	private float Product_Price;

	public void ProductsPage() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Product product = new Product();
		System.out.println("\nWELCOME TO PRODUCTS MANAGEMENT PAGE\n");
		int Choice;
		do {
			System.out.println("+------------------------------------------------+\n");
			System.out.println("|				1 - ADD PRODUCT					 |");
			System.out.println("|				2 - REMOVE PRODUCTS				 |");
			System.out.println("|				3 - UPDATE PRODUCT				 |");
			System.out.println("|				4 - VIEW ALL PRODUCT			 |");
			System.out.println("|				5 - SEARCH PRODUCT				 |");
			System.out.println("|				6 - EXIT PAGE					 |");
			System.out.println("+------------------------------------------------+\n");
			System.out.print("Enter choice : ");
			Choice = Integer.parseInt(reader.readLine());

			switch (Choice) {
				case 1 -> product.addProducts();
				case 2 -> product.removeProducts();
				case 3 -> product.alterProduct();
				case 4 -> product.viewProducts();
				case 5 -> product.searchProduct();
				case 6 -> System.out.println("Thank you");
				default -> System.out.println("Wrong choice ");
			}
		} while(Choice != 6);
	}

	private void alterProduct() throws IOException {
		int choice;
		String chd2,chd1 = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("select * from product");
			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet == null)
				System.out.println("NO PRODUCTS AVAILABLE");
			else {
				do {
					System.out.print("enter product ID to update info : ");
					Product_ID =Integer.parseInt(reader.readLine());
					int flag=0;
					while(resultSet.next()) {
						if(Integer.parseInt(resultSet.getString(1))== Product_ID) {
							flag=1;
							Product_Name =resultSet.getString(2);
							Category =resultSet.getString(3);
							Product_Count =Integer.parseInt(resultSet.getString(4));
							Product_Price =Float.parseFloat(resultSet.getString(5));
							Cargo_CompanyName =resultSet.getString(6);

							do {
								System.out.println("FETCHED PRODUCT INFO\t:\n");
								System.out.printf("Product ID\t= %-5d\n", Product_ID);
								System.out.printf("Product Name\t= %-20s\n", Product_Name);
								System.out.printf("Category\t= %-20s\n", Category);
								System.out.printf("Cargo Company\t= %-20s\n", Cargo_CompanyName);
								System.out.printf("Count\t= %-5d\n", Product_Count);
								System.out.printf("Price\t= %-10f\n", Product_Price);
								System.out.println("\n1 - UPDATE PRODUCT NAME\n");
								System.out.println("\n2 - UPDATE PRODUCT CATEGORY\n");
								System.out.println("\n3 - UPDATE CARGO COMPANY\n");
								System.out.println("\n4 - UPDATE PRODUCT COUNT\n");
								System.out.println("\n5 - UPDATE PRICE\n");
								System.out.print("\nEnter choice : ");
								choice = Integer.parseInt(reader.readLine());
								if(choice==1) {
									System.out.print("ENTER NEW NAME : ");
									Product_Name =reader.readLine();
								} else if(choice==2) {
									System.out.print("ENTER NEW CATEGORY : ");
									Category =reader.readLine();
								} else if(choice==3) {
									System.out.print("ENTER NEW CARGO COMPANY NAME : ");
									Cargo_CompanyName =reader.readLine();
								} else if(choice==4) {
									System.out.print("ENTER NEW COUNT : ");
									Product_Count =Integer.parseInt(reader.readLine());

								} else if(choice==5) {
									System.out.print("ENTER NEW PRICE : ");
									Product_Price =Float.parseFloat(reader.readLine());
								}
								System.out.print("DO YOU WANT TO CONTINUE PRESS ( Y for yes , N for No ) : ");
								chd2=reader.readLine();
							} while(chd2.equalsIgnoreCase("Y"));

							PreparedStatement ps1=connection.prepareStatement("update product set Product_Name = ? where Product_ID=?");
							ps1.setString(1, Product_Name);
							ps1.setString(2, Integer.toString(Product_ID));

							PreparedStatement ps2=connection.prepareStatement("update product set Category = ? where Product_ID=?");
							ps2.setString(1, Category);
							ps2.setString(2, Integer.toString(Product_ID));

							PreparedStatement ps3=connection.prepareStatement("update product set Product_Count = ? where Product_ID=?");
							ps3.setString(1, Integer.toString(Product_Count));
							ps3.setString(2, Integer.toString(Product_ID));

							PreparedStatement ps4=connection.prepareStatement("update product set Product_Price = ? where Product_ID=?");
							ps4.setString(1, Float.toString(Product_Price));
							ps4.setString(2, Integer.toString(Product_ID));

							PreparedStatement ps5=connection.prepareStatement("update cargocompany set cargocompany.Company_Name = ? where Company_ID = (SELECT Company_ID FROM product WHERE Product_ID = ?)");
							ps5.setString(1, (Cargo_CompanyName));
							ps5.setString(2, Integer.toString(Product_ID));

							int x1,x2,x3,x4,x5;

							x1 = ps1.executeUpdate();
							x2 = ps2.executeUpdate();
							x3 = ps3.executeUpdate();
							x4 = ps4.executeUpdate();
							x5 = ps5.executeUpdate();

							if(x1>0 && x2>0 && x3>0 && x4>0 && x5>0)
								System.out.println("PRODUCT INFO UPDATED SUCCESSFULLY !");
						}
					}
					if(flag == 0)
						System.out.println("PRODUCT NOT FOUND !");
					System.out.print("DO YOU WANT TO CONTINUE ( Y for yes , N for no )");
					chd1= reader.readLine();
					PreparedStatement preparedStatement1 = connection.prepareStatement("select * from product");
					ResultSet resultSet1 = preparedStatement.executeQuery();
					resultSet=resultSet1;

				} while(chd1.equalsIgnoreCase("Y"));
			}
		} catch(Exception e) {System.out.println(e);}
	}

	private void searchProduct()throws IOException {
		int flag=0;
		int x=0;
		String ch;
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement ps=con.prepareStatement("select * from product");
			ResultSet rs=ps.executeQuery();

			if(rs==null)
				System.out.println("NO PRODUCTS AVAILABLE");
			else {
				do {
					System.out.println("Enter product ID to search : ");
					Product_ID =Integer.parseInt(br.readLine());
					PreparedStatement ps1=con.prepareStatement("select * from product where Product_ID=?");

					ps1.setString(1,Integer.toString(Product_ID));
					ResultSet rs1=ps1.executeQuery();
					flag=0;
					while(rs1.next()) {
						if(Integer.parseInt(rs1.getString(1))== Product_ID) {
							System.out.println("+-----------------------------------------------------------------------------+\n");
							System.out.printf("Product ID   =  %-5d\n",Integer.parseInt(rs1.getString(1)));
							System.out.printf("Product Name =  %-20s\n",rs1.getString(2));
							System.out.printf("Category		=  %-20s\n",rs1.getString(3));
							System.out.printf("Count     	=  %-5d\n",Integer.parseInt(rs1.getString(4)));
							System.out.printf("Price        =  %-10f\n",Float.parseFloat(rs1.getString(5)));
							System.out.println("+-----------------------------------------------------------------------------+\n");
							flag=1;
							break;
						}
					}
					if(flag==0)
						System.out.println("PRODUCT NOT FOUND !");
					System.out.print("Do you want to continue, press Y for 'yes' N for 'no' : ");
					ch=br.readLine();
				} while(ch.equalsIgnoreCase("Y"));
			}
		} catch(Exception e) {System.out.println(e);}
	}

	private void removeProducts() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int x=0;
		String ch;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement ps=con.prepareStatement("delete from product where Product_ID=?");
			do {
				System.out.print("Enter product ID which you want to delete : ");
				Product_ID = Integer.parseInt(reader.readLine());
				ps.setString(1,Integer.toString(Product_ID));
				x=ps.executeUpdate();
				if(x==0)
					System.out.println("PRODUCT NOT FOUND !");
				else
					System.out.println("PRODUCT DELETED SUCCESSFULLY !");
				System.out.print("Do you want to continue ( Y for YES, N for NO ): ");
				ch=reader.readLine();

			} while(ch.equalsIgnoreCase("Y"));
		} catch(Exception e) {System.out.println(e);}
	}

	private void addProducts() throws IOException {
		int res;
		int res1;
		String ch;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("+------------------------------------------------------------+\n");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement1 = connection.prepareStatement("insert into cargoCompany(Company_ID, Company_Name) values(?,?)");
			PreparedStatement preparedStatement2 = connection.prepareStatement("insert into product(Product_ID, Product_Name, Category, Product_Count, Product_Price,Company_ID) values(?,?,?,?,?,?)");

			do {
				Product_ID = setProductID();
				System.out.println("Product ID = "+ Product_ID);
				System.out.print("Enter Name : ");
				Product_Name = reader.readLine();
				System.out.print("Enter Category : ");
				Category = reader.readLine();
				System.out.print("Enter Cargo Company : ");
				Cargo_CompanyName = reader.readLine();
				CargoCompany cargoCompany = new CargoCompany(Cargo_CompanyName);
				Cargo_CompanyID = cargoCompany.Company_ID;
				System.out.print("Enter Count : ");
				Product_Count =Integer.parseInt(reader.readLine());
				System.out.print("Enter Price : ");
				Product_Price =Float.parseFloat(reader.readLine());
				preparedStatement2.setString(1, Integer.toString(Product_ID));
				preparedStatement2.setString(2, Product_Name);
				preparedStatement2.setString(3, Category);
				preparedStatement2.setString(4, Integer.toString(Product_Count));
				preparedStatement2.setString(5, Float.toString(Product_Price));
				preparedStatement2.setString(6, Integer.toString(Cargo_CompanyID));
				preparedStatement1.setString(1, Integer.toString(Cargo_CompanyID));
				preparedStatement1.setString(2, Cargo_CompanyName);

				res1 = preparedStatement1.executeUpdate();
				res = preparedStatement2.executeUpdate();

				if(res>0 && res1>0)
					System.out.println("PRODUCT ADDED SUCCESSFULLY !\n");
				System.out.print("Do you want to continue ( Y for yes , N for NO )");
				ch=reader.readLine();
			} while(ch.equalsIgnoreCase("Y"));
		} catch(Exception e) {System.out.println(e);}
	}

	private void viewProducts() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement ps=con.prepareStatement("select * from product");
			ResultSet rs=ps.executeQuery();

			if(rs==null)
				System.out.println("NO PRODUCTS AVAILABLE !");
			else {
				System.out.println("The products are : \n");
				System.out.println("****************************************************************************************************************************");
				System.out.printf("%-15s\t%-20s\t%-20s\t%-15s\t%-15s\t%-20s\n","Product ID","Name","Category","Count","Price", "Cargo Company");
				System.out.println("****************************************************************************************************************************\n");
				while(rs.next()) {
					Product_ID = Integer.parseInt(rs.getString("Product_ID"));
					Product_Name =rs.getString("Product_Name");
					Category =rs.getString("Category");
					Product_Count =Integer.parseInt(rs.getString("Product_Count"));
					Product_Price =Float.parseFloat(rs.getString("Product_Price"));
					Cargo_CompanyID = Integer.parseInt(rs.getString("Company_ID"));
					System.out.printf("%-15d\t%-20s\t%-20s\t%-15d\t%-15f\t%-20s\n", Product_ID, Product_Name, Category, Product_Count, Product_Price, Cargo_CompanyID);
				}
				System.out.println("****************************************************************************************************************************\n");
			}
		} catch(Exception e) {System.out.println(e);}
	}

	private static int setProductID() {
		int y = 999;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement ps=con.prepareStatement("select Product_ID from product");
			ResultSet rs=ps.executeQuery();
			while(rs.next())
				y = Integer.parseInt(rs.getString("product_ID"));
		} catch(Exception e) {System.out.println(e);}
		return y+1;
	}
}