package NeedBox;
import java.io.*;

public class Payment {
	Order customerOrder;
	public int OrderStatus;
	private Cart customerCart;
	Payment(Cart cart1,String c_name,String b_add,String c_phn)throws IOException {
		customerCart = cart1;
		customerOrder = new Order(c_name,b_add,c_phn,customerCart.getProductID(),customerCart.getProductName(),customerCart.getCountPurchased(),customerCart.getTotalPrice());
		OrderStatus = 0;
	}
	public void paymentPage() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("WELCOME TO PAYMENTS PAGE\n");
		int choice;
		do {
			System.out.println("+--------------------------------------+\n");
			System.out.println("|		1 - PAY ORDER				   |");
			System.out.println("|		2 - DISPLAY ORDER			   |");
			System.out.println("|		3 - EXIT                       |");
			System.out.println("+--------------------------------------+\n");
			System.out.print("Enter choice : ");
			choice = Integer.parseInt(reader.readLine());

			switch (choice) {
				case 1 -> {
					customerOrder.displayOrder();
					System.out.println("\nENTER AMOUNT TO PAY = ");
					float flag;
					flag = Float.parseFloat(reader.readLine());
					while (flag < customerOrder.Total_Amount || flag > customerOrder.Total_Amount) {
						System.out.println("Invalid amount entered !");
						System.out.println("Enter again : ");
						flag = Float.parseFloat(reader.readLine());
					}
					System.out.println("ORDER PAID SUCCESSFULLY !");
					customerOrder.addToDatabase();

					OrderStatus = 1;
					choice = 3;
				}
				case 2 -> customerOrder.displayOrder();
				case 3 -> System.out.println("Thank you");
				default -> System.out.println("Wrong choice");
			}
		} while(choice!=3);
	}
}