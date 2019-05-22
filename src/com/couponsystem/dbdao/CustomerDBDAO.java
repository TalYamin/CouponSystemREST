package com.couponsystem.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.couponsystem.dao.CustomerDAO;
import com.couponsystem.bean.Customer;
import com.couponsystem.utils.ConnectionPool;

/**
 * @author Tal Yamin
 *
 */

/*
 * DBDAO classes implements the DAO interface. 
 * these classes allow data transitions in DB. 
 * Methods in these classes based on following pattern: 
 * (1) Receiving connection from connection pool. 
 * (2) Executing SQL queries using statements or prepared statements. 
 * (3) Closing the connection and returning back to connection pool.
*/

public class CustomerDBDAO implements CustomerDAO {

	/* Static connectionPool Object */
	private static ConnectionPool connectionPool;

	/*
	 * Insert to Customer table override method:
	 * This method used to add record of customer object.
	 * This method receive 1 parameters: customer.
	 * According to parameter, the SQL query is defined with 
	 * the company ID, name and password. 
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for insert to table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void insertCustomer(Customer customer) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "insert into Customer(ID, CUST_NAME, PASSWORD) values (?,?,?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setLong(1, customer.getCustomerId());
			preparedStatement.setString(2, customer.getCustomerName());
			preparedStatement.setString(3, customer.getCustomerPassword());

			preparedStatement.executeUpdate();

			System.out.println("Customer created: " + customer.toString());
		} catch (SQLException e) {
			throw new Exception("Customer creation failed. customerId: " + customer.getCustomerId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Remove from Customer table override method:
	 * This method used to remove record of customer object.
	 * This method receive 1 parameters: customer.
	 * According to parameter, the SQL query is defined with the customer ID.    
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for remove from table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void removeCustomer(Customer customer) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "delete from Customer where ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false);
			preparedStatement.setLong(1, customer.getCustomerId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println("remove succeeded. Customer removed id: " + customer.getCustomerId());

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				throw new Exception("DataBase error");
			}
			throw new Exception("failed to remove Customer. customerId: " + customer.getCustomerId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Update Customer table override method:
	 * This method used to update record of customer object.
	 * This method receive 1 parameters: customer.
	 * According to parameter, the SQL query is defined with 
	 * the customer ID, name and password.
	 * The updates only available for customer name and password where the relevant ID. 
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for update table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void updateCustomer(Customer customer) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = String.format("update Customer set CUST_NAME= '%s',PASSWORD = '%s'  where ID = %d",
				customer.getCustomerName(), customer.getCustomerPassword(), customer.getCustomerId());

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.executeUpdate();

			System.out.println("update Customer succeeded. id which updated: " + customer.getCustomerId());
		} catch (SQLException e) {
			throw new Exception("update Customer failed. customerId: " + customer.getCustomerId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Get customer from Customer table override method:
	 * This method used to get specific record of customer object.
	 * This method receive 1 parameters: customerId. 
	 * There is generation of Customer object which need to receive the data from table.
	 * According to parameter, the SQL query is defined with the customer ID.    
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There are setters methods of Customer object which used in order to keep the results and to return the object. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public Customer getCustomer(long customerId) throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		Customer customer = new Customer();
		String sql = "SELECT * FROM Customer WHERE ID=" + customerId;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			customer.setCustomerId(resultSet.getLong(1));
			customer.setCustomerName(resultSet.getString(2));
			customer.setCustomerPassword(resultSet.getString(3));

		} catch (SQLException e) {
			throw new Exception("unable to get Customer data. customerId: " + customerId);
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return customer;

	}

	/*
	 * Get all customers list from Customer table override method:
	 * This method used to get all customers records.
	 * There is generation of ArrayList which need to receive the data from table.
	 * the SQL query is defined for all data in table.   
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There is function add of ArrayList which used in order to keep the results and to return the list. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public synchronized List<Customer> getAllCustomers() throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Customer> list = new ArrayList<>();
		String sql = "select * from Customer";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			
			while (resultSet.next()) {
				long customerId = resultSet.getLong(1);
				String customerName = resultSet.getString(2);
				String customerPassword = resultSet.getString(3);

				list.add(new Customer(customerId, customerName, customerPassword));
			}

		} catch (SQLException e) {
			System.out.println(e);
			throw new Exception("unable to get Customer data");
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return list;

	}

}
