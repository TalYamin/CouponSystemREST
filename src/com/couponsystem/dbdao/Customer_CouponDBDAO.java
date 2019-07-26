package com.couponsystem.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.couponsystem.dao.Customer_CouponDAO;
import com.couponsystem.bean.Coupon;
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

public class Customer_CouponDBDAO implements Customer_CouponDAO {

	/* Static connectionPool Object */
	private static ConnectionPool connectionPool;

	/*
	 * Insert to Customer_Coupon table override method:
	 * This method used to add record of relation between customer and coupon.
	 * This method receive 2 parameters: customer and coupon.
	 * According to parameters, the SQL query is defined with 
	 * the customer ID and coupon ID. Both values defined as unified primary key.  
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for insert to table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void insertCustomer_Coupon(Customer customer, Coupon coupon) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "insert into Customer_Coupon (Customer_ID, Coupon_ID) values (?,?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setLong(1, customer.getCustomerId());
			preparedStatement.setLong(2, coupon.getCouponId());

			preparedStatement.executeUpdate();

			System.out.println("Customer_Coupon added. customerId: " + customer.getCustomerId() + " couponId: " + coupon.getCouponId());
		} catch (SQLException e) {
			throw new Exception("Customer_Coupon addition failed. customerId: " + customer.getCustomerId() + " couponId: "+ coupon.getCouponId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Remove from Customer_Coupon table override method:
	 * This method used to remove record of relation between customer and coupon.
	 * This method receive 2 parameters: customer and coupon.
	 * According to parameters, the SQL query is defined with 
	 * the customer ID and coupon ID.   
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for remove from table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void removeCustomer_Coupon(Customer customer, Coupon coupon) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "delete from Customer_Coupon where Customer_ID = ? AND Coupon_ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false);
			preparedStatement.setLong(1, customer.getCustomerId());
			preparedStatement.setLong(2, coupon.getCouponId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println("Customer_Coupon remove succeeded. customerId: " + customer.getCustomerId() + "couponId: " + coupon.getCouponId());

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				throw new Exception("DataBase error");
			}
			throw new Exception("failed to remove Customer_Coupon. customerId: " + customer.getCustomerId() + "couponId: " + coupon.getCouponId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Remove from Customer_Coupon table override method:
	 * This method used to remove record of relation between customer and coupon.
	 * This method receive 1 parameters: customer.
	 * According to parameter, the SQL query is defined with the customer ID.    
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for remove from table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void removeCustomer_Coupon(Customer customer) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "delete from Customer_Coupon where Customer_ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false); 
			preparedStatement.setLong(1, customer.getCustomerId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println("Customer_Coupon remove succeeded. customerId: " + customer.getCustomerId());

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				throw new Exception("DataBase error");
			}
			throw new Exception("failed to remove Customer_Coupon. customerId: " + customer.getCustomerId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Remove from Customer_Coupon table override method:
	 * This method used to remove record of relation between customer and coupon.
	 * This method receive 1 parameters: coupon.
	 * According to parameter, the SQL query is defined with the coupon ID.    
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for remove from table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void removeCustomer_Coupon(Coupon coupon) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "delete from Customer_Coupon where Coupon_ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false);
			preparedStatement.setLong(1, coupon.getCouponId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println("Customer_Coupon remove succeeded. couponId: " + coupon.getCouponId());

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				throw new Exception("DataBase error");
			}
			throw new Exception("failed to remove Customer_Coupon. couponId: " + coupon.getCouponId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Get companies ID list from Customer_Coupon table override method:
	 * This method used to get list of customersId by specific couponId.
	 * This method receive 1 parameters: couponId. only records with this ID are relevant.
	 * There is generation of ArrayList which need to receive the data from table.
	 * According to parameter, the SQL query is defined with the coupon ID.    
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There is function add of ArrayList which used in order to keep the results and to return the list. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public List<Long> getCustomerId(long couponId) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Long> customersId = new ArrayList<>();
		String sql = "select * from Customer_Coupon where Coupon_ID = " + couponId;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			
			while (resultSet.next()) {
				long customerId = resultSet.getLong(1);
				customersId.add(customerId);
			}
		} catch (SQLException e) {
			throw new Exception("unable to get Customer_Coupon data. couponId: " + couponId);
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return customersId;

	}

	/*
	 * Get all companies ID list from Customer_Coupon table override method:
	 * This method used to get list of all customersId in this table.
	 * There is generation of ArrayList which need to receive the data from table.
	 * the SQL query is defined for all data in table.   
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There is function add of ArrayList which used in order to keep the results and to return the list. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 * Pay Attention - this method return duplicate values.
	 */
	@Override
	public List<Long> getAllCustomersId() throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Long> customersId = new ArrayList<>();
		String sql = "select * from Customer_Coupon";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				long customerId = resultSet.getLong(1);
				customersId.add(customerId);

			}
		} catch (SQLException e) {
			throw new Exception("unable to get Customer_Coupon data");
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return customersId;

	}

	/*
	 * Get coupons ID list from Customer_Coupon table override method:
	 * This method used to get list of couponsId by specific customerId.
	 * This method receive 1 parameters: customerId. only records with this ID are relevant.
	 * There is generation of ArrayList which need to receive the data from table.
	 * According to parameter, the SQL query is defined with the customer ID.    
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There is function add of ArrayList which used in order to keep the results and to return the list. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public List<Long> getCouponId(long customerId) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Long> couponsId = new ArrayList<>();
		String sql = "select * from Customer_Coupon where Customer_ID = " + customerId;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			
			while (resultSet.next()) {
				long couponId = resultSet.getLong(2);
				couponsId.add(couponId);
			}
		} catch (SQLException e) {
			throw new Exception("unable to get Customer_Coupon data. customerId: " + customerId);
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return couponsId;

	}

	/*
	 * Get all coupons ID list from Customer_Coupon table override method:
	 * This method used to get list of all couponsId in this table.
	 * There is generation of ArrayList which need to receive the data from table.
	 * the SQL query is defined for all data in table.   
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There is function add of ArrayList which used in order to keep the results and to return the list. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 * Pay Attention - this method return duplicate values.
	 */
	@Override
	public List<Long> getAllCouponsId() throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Long> couponsId = new ArrayList<>();
		String sql = "select * from Customer_Coupon";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
				long couponId = resultSet.getLong(2);
				couponsId.add(couponId);

			}
		} catch (SQLException e) {
			throw new Exception("unable to get Customer_Coupon data");
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return couponsId;
	}

}
