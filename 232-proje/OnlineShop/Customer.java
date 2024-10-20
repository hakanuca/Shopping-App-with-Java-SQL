package NeedBox;
import java.util.*;
import java.io.*;
import java.sql.*;

public class Customer extends Shop {
	private int Customer_ID;
	private String Customer_Password;
	private Cart Customer_Cart = new Cart();
	private int Cart_Flag = 0;
	private int orderPaidFlag = 0;
	private int checkFlag = -1;
	private ArrayList<Integer> Product_ID = new ArrayList<>();
	private ArrayList<String> Product_Name = new ArrayList<>();
	private ArrayList<String> Product_Category = new ArrayList<>();
	private ArrayList<Integer> Product_Count = new ArrayList<>();
	private ArrayList<Float> Product_Price = new ArrayList<>();
	private int products_Check;

	Customer(int Customer_ID,String Customer_Password) {
		this.Customer_ID = Customer_ID;
		this.Customer_Password = Customer_Password;
		Customer_Cart = new Cart();
		orderPaidFlag = 0;
		Cart_Flag = 0;
	}

	public void CustomerPage() throws IOException, SQLException, ClassNotFoundException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		products_Check = this.initializeProducts();
		System.out.println("WELCOME TO CUSTOMER SECTION\n");
		int choice;
		do {
			System.out.println("+---------------------------------------------------+");
			System.out.println("|		1 - VIEW CATEGORY AND PRODUCT LIST			|");
			System.out.println("|		2 - SEARCH A PRODUCT NAME-WISE				|");
			System.out.println("|		3 - SEARCH PRODUCTS TYPE-WISE				|");
			System.out.println("|		4 - ADD PRODUCT TO CART						|");
			System.out.println("|		5 - REMOVE PRODUCT FROM CART				|");
			System.out.println("|		6 - VIEW CART								|");
			System.out.println("|		7 - PROCEED TO PAYMENT						|");
			System.out.println("|		8 - EDIT PROFILE							|");
			System.out.println("|		9 - LOGOUT FROM SYSTEM						|");
			System.out.println("+---------------------------------------------------+");
			System.out.print("ENTER CHOICE: ");
			choice = Integer.parseInt(reader.readLine());

			switch (choice) {
				case 1 -> this.viewProducts();
				case 2 -> this.searchNameWise();
				case 3 -> this.searchTypeWise();
				case 4 -> this.addProducts();
				case 5 -> {
					int rem;
					System.out.print("ENTER PRODUCT ID TO REMOVE FROM CART = ");
					rem = Integer.parseInt(reader.readLine());
					Customer_Cart.removeFromCart(rem);
					this.updateArrayList();
				}
				case 6 -> Customer_Cart.viewCart();
				case 7 -> this.proceedPayment(Customer_Cart);
				case 8 -> editProfile(Customer_ID);
				case 9 -> choice = this.checkExit();
				default -> System.out.println("-INVALID CHOICE-");
			}
		} while(choice!=9);
	}

	private int checkExit() throws IOException {
		if(Cart_Flag == 1) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String continueChoice;
			System.out.println("YOU HAVE A PENDING CART !");
			System.out.print("DO YOU WANT TO MAKE PAYMENT (PRESS Y) ELSE CANCEL THE CART (PRESS N) :");
			continueChoice = reader.readLine();

			if(continueChoice.equalsIgnoreCase("Y")) {
				proceedPayment(Customer_Cart);
				if(orderPaidFlag !=1 && checkFlag == -1) return -1;
				else return 0;
			} else {
				Customer_Cart.cancelCart();
				Customer_Cart = new Cart();
				Cart_Flag = 0;
				orderPaidFlag = 0;
			}
		}
		System.out.println("THANK YOU!");
		return 9;
	}

	private void proceedPayment(Cart cart1) throws IOException {
		if(Cart_Flag == 1) {
			String c_name = "";
			String b_add = "";
			String c_phn = "";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
				PreparedStatement preparedStatement = connection.prepareStatement("select * from Customer where Customer_ID=?");
				preparedStatement.setString(1, Integer.toString(Customer_ID));
				ResultSet resultSet = preparedStatement.executeQuery();
				while(resultSet.next()) {
					c_name = resultSet.getString(2);
					b_add = resultSet.getString(5);
					c_phn = resultSet.getString(6);
				}
			} catch(Exception e) { System.out.println(e); }
			Payment payment = new Payment(cart1,c_name,b_add,c_phn);
			payment.paymentPage();
			if(payment.OrderStatus == 1) orderPaidFlag = 1;
			if(orderPaidFlag == 1) {
				Customer_Cart = new Cart();
				Cart_Flag = 0;
				orderPaidFlag = 0;
				checkFlag = -2;
			}
		} else System.out.println("CART IS EMPTY!");
	}

	private void updateArrayList() throws IOException {
		Product_ID.clear();
		Product_Name.clear();
		Product_Category.clear();
		Product_Count.clear();
		Product_Price.clear();
		initializeProducts();
	}

	private int searchProduction(int Product) throws IOException { //for searching product and retrieving the available quantity
		int result = Product_ID.indexOf(Product);
		if(result != -1) return Product_Count.get(result);
		else return -1;
	}

	private void updateCount(int Count, int productID) throws IOException {
		try {
			int res = Product_ID.indexOf(productID);
			int min = Product_Count.get(res);
			if(min - Count > 0)
				Product_Count.set(res, min-Count);
			else
				Product_Count.set(res,0);
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("update product set Product_Count =? where Product_ID=?");
			preparedStatement.setString(1, Integer.toString(Product_Count.get(res)));
			preparedStatement.setString(2, Integer.toString(productID));
			int m = preparedStatement.executeUpdate();
			if(m == 0)
				System.out.println("PRODUCT UPDATE FAILED!");
		} catch(Exception e) {System.out.println(e);}
	}

	private void addProducts() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String chc;
		do {
			int Product_ID;
			String Product_Name, Category;
			float Product_Price;

			int q_pur;
			int q_avail;

			System.out.print("ENTER PRODUCT ID TO ADD TO CART = ");
			Product_ID = Integer.parseInt(reader.readLine());
			q_avail = searchProduction(Product_ID);
			if(q_avail == -1)
				System.out.println("PRODUCT NOT FOUND !");
			else {
				System.out.println("COUNT AVAILABLE = " + q_avail);
				System.out.println("ENTER COUNT TO PURCHASE = ");
				q_pur = Integer.parseInt(reader.readLine());
				if(q_pur > q_avail)
					System.out.println("STOCK NOT AVAILABLE");
				else {
					updateCount(q_pur,Product_ID);

					//adding product to cart code begins below
					Product_Name = this.Product_Name.get(this.Product_ID.indexOf(Product_ID));
					Category = this.Product_Category.get(this.Product_ID.indexOf(Product_ID));
					Product_Price = (q_pur*(this.Product_Price.get(this.Product_ID.indexOf(Product_ID))));

					Customer_Cart.addToCart(Product_ID, Product_Name, Category, q_pur, Product_Price);
					Cart_Flag =1;
				}
			}
			System.out.print("DO YOU WANT TO CONTINUE PRESS (Y for yes, N for no)");
			chc = reader.readLine();
		} while(chc.equalsIgnoreCase("Y"));
	}

	private int initializeProducts() throws IOException { //return 1 if products are available 0 if not available
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("select * from product");
			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet == null)
				return 0;
			else
				while(resultSet.next()) {
					Product_ID.add(Integer.parseInt(resultSet.getString(1)));
					Product_Name.add(resultSet.getString(2));
					Product_Category.add(resultSet.getString(3));
					Product_Count.add(Integer.parseInt(resultSet.getString(4)));
					Product_Price.add(Float.parseFloat(resultSet.getString(5)));
				}
		} catch(Exception e) {System.out.println(e);}
		return 1;
	}

	private void viewProducts() throws IOException, SQLException, ClassNotFoundException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		if(products_Check == 0)
			System.out.println("PRODUCTS NOT AVAILABLE!");
		else {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT NEEDBOX.PRODUCT.CATEGORY ,count(*) AS TOTAL_PRODUCT FROM NEEDBOX.PRODUCT GROUP BY NEEDBOX.PRODUCT.CATEGORY ORDER BY TOTAL_PRODUCT DESC;");
			ResultSet resultSet = preparedStatement.executeQuery();

			if(resultSet == null)
				System.out.println("NO CATEGORISED PRODUCT AVAILABLE");
			else {
				System.out.println("**********************************************************************************************************************************************************************\n");
				System.out.printf("%-20s\t%-20s\n","CATEGORY","TOTAL-PRODUCT");
				System.out.println("**********************************************************************************************************************************************************************");


				while(resultSet.next()) {
					String Category_Name = resultSet.getString(1);
					int Total_Product = Integer.parseInt(resultSet.getString(2));

					System.out.printf("%-20s\t%-20s\n",Category_Name, Total_Product);
				}
				System.out.println("*********************************************************************************************************************************************************************\n");
			}
			System.out.print("DO YOU WANT TO CONTINUE PRESS (Y for yes, N for no)");
			String chc = reader.readLine();
			if (chc.equalsIgnoreCase("Y")){int x;
				x = Product_ID.size();
				int i;
				System.out.println("***********************************************************************************************************************");
				System.out.printf("%-20s \t %-20s \t %-20s \t %-20s \t %-20s\n", "Product_ID","Product_Name","Category","Product_Count","Product_Price");
				System.out.println("***********************************************************************************************************************");
				for(i = 0; i < x; i++)
					if(Product_Count.get(i)!=0)
						System.out.printf("%-20d \t %-20s \t %-20s \t %-20d \t %-20f\n", Product_ID.get(i), Product_Name.get(i), Product_Category.get(i), Product_Count.get(i), Product_Price.get(i) );
					else
						System.out.printf("%-20d \t %-20s \t %-20s \t %-20s \t %-20f\n", Product_ID.get(i), Product_Name.get(i), Product_Category.get(i),"NOT IN STOCK", Product_Price.get(i) );
				System.out.println("***********************************************************************************************************************\n");}

		}
	}

	private void searchNameWise() throws IOException {
		if(products_Check == 0)
			System.out.println("PRODUCTS NOT AVAILABLE!");
		else {
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			String sr;
			int res;
			String chc;
			do {
				System.out.print("ENTER PRODUCT NAME TO SEARCH: ");
				sr=br.readLine();
				res= Product_Name.indexOf(sr);
				if(res==-1)
					System.out.println("PRODUCT NOT FOUND!");
				else {

					System.out.println("PRODUCT DETAILS ARE :\n");
					System.out.printf("PRODUCT ID         = %-5d\n", Product_ID.get(res));
					System.out.printf("PRODUCT NAME       = %-20s\n", Product_Name.get(res));
					System.out.printf("PRODUCT TYPE       = %-20s\n", Product_Category.get(res));
					if(Product_Count.get(res)!=0)
						System.out.printf("COUNT AVAILABLE = %-5d\n", Product_Count.get(res));
					else
						System.out.printf("COUNT AVAILABLE = %-5s\n", "NOT IN STOCK");
					System.out.printf("PRODUCT PRICE      = %-10f\n", Product_Price.get(res));
				}
				System.out.print("PRESS Y to continue , N for exit : ");
				chc = br.readLine();
			} while(chc.equalsIgnoreCase("Y"));
		}
	}

	private void searchTypeWise() throws IOException {
		if(products_Check == 0)
			System.out.println("PRODUCTS NOT AVAILABLE!");
		else {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String sr;
			int res;
			String chc;
			do {
				System.out.print("ENTER PRODUCT TYPE TO SEARCH: ");
				sr = reader.readLine();
				res = Product_Category.indexOf(sr);
				if(res == -1)
					System.out.println("PRODUCT NOT FOUND!");
				else {
					System.out.println("PRODUCTS AVAILABLE ARE: \n");
					System.out.println("***********************************************************************************************************************");
					System.out.printf("%-20s \t %-20s \t %-20s \t %-20s \t %-20s\n", "Product_ID","Product_Name","Category","Product_Count","Product_Price");
					System.out.println("***********************************************************************************************************************");
					res= Product_ID.size();
					for(int i = 0; i < res; i++)
						if(sr.equalsIgnoreCase(Product_Category.get(i))) {
							if(Product_Count.get(i) != 0)
								System.out.printf("%-20d \t %-20s \t %-20s \t %-20d \t %-20f\n", Product_ID.get(i), Product_Name.get(i), Product_Category.get(i), Product_Count.get(i), Product_Price.get(i) );
							else
								System.out.printf("%-20d \t %-20s \t %-20s \t %-20s \t %-20f\n", Product_ID.get(i), Product_Name.get(i), Product_Category.get(i),"NOT IN STOCK", Product_Price.get(i) );
						}
					System.out.println("***********************************************************************************************************************");
				}
				System.out.print("PRESS Y to continue , N for exit : ");
				chc = reader.readLine();
			} while(chc.equalsIgnoreCase("Y"));
		}
	}

	private static void editProfile(int Customer_ID)throws IOException {
		try {
			int x = 0;
			String chc;
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/needbox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String s = "";
			int fc = -1;
			String name = "", email = "", addr = "", contact = "", passw = "";
			int Choice = 0;
			do {
				System.out.println("+---------------------------------------------------+");
				System.out.println("|			1 - EDIT NAME							|");
				System.out.println("|			2 - EDIT EMAIL							|");
				System.out.println("|			3 - EDIT ADDRESS						|");
				System.out.println("|			4 - EDIT CONTACT NUMBER					|");
				System.out.println("|			5 - CHANGE PASSWORD						|");
				System.out.println("|			6 - EXIT								|");
				System.out.println("+---------------------------------------------------+");
				System.out.print("Enter choice : ");
				Choice = Integer.parseInt(reader.readLine());

				switch (Choice) {
					case 1 -> {
						System.out.print("ENTER NEW NAME : ");
						name = reader.readLine();
						s = "Name";
						fc = 1;
					}
					case 2 -> {
						System.out.print("ENTER NEW EMAIL : ");
						email = reader.readLine();
						s = "Email";
						fc = 1;
					}
					case 3 -> {
						System.out.print("ENTER ADDRESS : ");
						addr = reader.readLine();
						s = "Address";
						fc = 1;
					}
					case 4 -> {
						System.out.print("ENTER NEW CONTACT NUMBER : ");
						contact = reader.readLine();
						s = "ContactNumber";
						fc = 1;
					}
					case 5 -> {
						System.out.print("ENTER NEW PASSWORD : ");
						passw = reader.readLine();
						s = "Password";
						fc = 0;
					}
					case 6 -> System.out.println("EXITING...");
					default -> System.out.println("INVALID CHOICE!");
				}

				if(fc == 1) {
					PreparedStatement ps;
					if(s.equalsIgnoreCase("Name")) {
						ps=connection.prepareStatement("update customer set Customer_Name = ? where Customer_ID=?");
						ps.setString(1, name);
						ps.setString(2, Integer.toString(Customer_ID));
						x=ps.executeUpdate();
					} else if(s.equalsIgnoreCase("Email")) {
						ps=connection.prepareStatement("update customer set Customer_Email = ? where Customer_ID=?");
						ps.setString(1,email);
						ps.setString(2, Integer.toString(Customer_ID));
						x=ps.executeUpdate();
					} else if(s.equalsIgnoreCase("Address")) {
						ps=connection.prepareStatement("update customer set Customer_Address = ? where Customer_ID=?");
						ps.setString(1, addr);
						ps.setString(2, Integer.toString(Customer_ID));
						x=ps.executeUpdate();
					} else if(s.equalsIgnoreCase("ContactNumber")) {
						ps=connection.prepareStatement("update customer set Contact_Number = ? where Customer_ID=?");
						ps.setString(1, contact);
						ps.setString(2, Integer.toString(Customer_ID));
						x=ps.executeUpdate();
					}
					if(x != 0)
						System.out.println("INFORMATION UPDATED SUCCESSFULLY !");
				} else if(fc==0) {
					PreparedStatement ps=connection.prepareStatement("update loginInfo set Password=? where User_ID=?");
					ps.setString(1,passw);
					ps.setString(2, Integer.toString(Customer_ID));
					x=ps.executeUpdate();
					if(x!=0)
						System.out.println("PASSWORD CHANGED SUCCESSFULLY !");

				}
				System.out.print("Do you want to continue ( Y for yes, N for No ) : ");
				chc = reader.readLine();
			} while(chc.equalsIgnoreCase("Y"));
		} catch(Exception e) {System.out.println(e);}
	}
}