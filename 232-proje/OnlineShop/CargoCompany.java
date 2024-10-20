package NeedBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CargoCompany {
    public int Company_ID;
    public String Company_Name;
    public CargoCompany(String company_Name) {
        setCompanyID();
        Company_Name = company_Name;
    }
     void setCompanyID() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/NeedBox?autoReconnect=true&useSSL=false","root",DatabaseConnection.rootPassword);
            PreparedStatement preparedStatement = connection.prepareStatement("select Company_ID from cargoCompany");
            ResultSet resultSet = preparedStatement.executeQuery();
            int counter = 9;
            while(resultSet.next())
                counter = Integer.parseInt(resultSet.getString("Company_ID"));
            Company_ID = counter + 1;
        }
        catch(Exception e) {e.printStackTrace();}
    }
}
