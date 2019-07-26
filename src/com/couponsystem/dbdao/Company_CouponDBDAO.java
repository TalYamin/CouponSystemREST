package com.couponsystem.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.couponsystem.dao.Company_CouponDAO;
import com.couponsystem.bean.Company;
import com.couponsystem.bean.Coupon;
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

public class Company_CouponDBDAO implements Company_CouponDAO{

	/* Static connectionPool Object */
	private static ConnectionPool connectionPool;
	
	
	/*
	 * Insert to Company_Coupon table override method:
	 * This method used to add record of relation between company and coupon.
	 * This method receive 2 parameters: company and coupon.
	 * According to parameters, the SQL query is defined with 
	 * the company ID and coupon ID. Both values defined as unified primary key.  
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for insert to table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void insertCompany_Coupon(Company company, Coupon coupon) throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "insert into Company_Coupon (Company_ID, Coupon_ID) values (?,?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setLong(1, company.getCompanyId());
			preparedStatement.setLong(2, coupon.getCouponId());

			preparedStatement.executeUpdate();

			System.out.println("Company_Coupon added. companyId: " + company.getCompanyId() + " couponId: " + coupon.getCouponId());
		}catch (SQLException e) {
			throw new Exception("DB error - Company_Coupon addition failed. companyId: " + company.getCompanyId() + " couponId: " + coupon.getCouponId());
		}catch (Exception e) {
			throw new Exception("Company_Coupon addition failed. companyId: " + company.getCompanyId() + " couponId: " + coupon.getCouponId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		
	}

	/*
	 * Remove from Company_Coupon table override method:
	 * This method used to remove record of relation between company and coupon.
	 * This method receive 2 parameters: company and coupon.
	 * According to parameters, the SQL query is defined with 
	 * the company ID and coupon ID.   
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for remove from table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void removeCompany_Coupon(Company company, Coupon coupon) throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "delete from Company_Coupon where Company_ID = ? AND Coupon_ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false);
			preparedStatement.setLong(1, company.getCompanyId());
			preparedStatement.setLong(2, coupon.getCouponId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println("Company_Coupon remove succeeded. companyId: " + company.getCompanyId() + " couponId: " + coupon.getCouponId());

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				throw new Exception("DB error - failed to remove Company_Coupon. companyId "+ company.getCompanyId() + " couponId: " + coupon.getCouponId());
			}
			throw new Exception("failed to remove Company_Coupon. companyId "+ company.getCompanyId() + " couponId: " + coupon.getCouponId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		
	}

	/*
	 * Remove from Company_Coupon table override method:
	 * This method used to remove record of relation between company and coupon.
	 * This method receive 1 parameters: company.
	 * According to parameter, the SQL query is defined with the company ID.    
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for remove from table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void removeCompany_Coupon(Company company) throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "delete from Company_Coupon where Company_ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false); 
			preparedStatement.setLong(1, company.getCompanyId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println("Company_Coupon remove succeeded. comapnyId: "+ company.getCompanyId());

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				throw new Exception("DB error - failed to remove Company_Coupon. comapnyId: "+ company.getCompanyId());
			}
			throw new Exception("failed to remove Company_Coupon. comapnyId: "+ company.getCompanyId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		
	}

	/*
	 * Remove from Company_Coupon table override method:
	 * This method used to remove record of relation between company and coupon.
	 * This method receive 1 parameters: coupon.
	 * According to parameter, the SQL query is defined with the coupon ID.    
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for remove from table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void removeCompany_Coupon(Coupon coupon) throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "delete from Company_Coupon where Coupon_ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false); 
			preparedStatement.setLong(1, coupon.getCouponId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println("Company_Coupon remove succeeded. couponId: " + coupon.getCouponId());

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				throw new Exception("DB error - failed to remove Company_Coupon. couponId: " + coupon.getCouponId());
			}
			throw new Exception("failed to remove Company_Coupon. couponId: " + coupon.getCouponId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		
	}

	/*
	 * Get companies ID list from Company_Coupon table override method:
	 * This method used to get list of companiesId by specific couponId.
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
	public List<Long> getCompanyId(long couponId) throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Long> companiesId = new ArrayList<>();
		String sql = "select * from Company_Coupon where Coupon_ID = " + couponId;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				long companyId = resultSet.getLong(1);
				companiesId.add(companyId);
			}
		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Company_Coupon data. couponId: " + couponId);
		} catch(Exception e) {
			throw new Exception("unable to get Company_Coupon data. couponId: " + couponId);
		}finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return companiesId;
	}

	/*
	 * Get all companies ID list from Company_Coupon table override method:
	 * This method used to get list of all companiesId in this table.
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
	public List<Long> getAllCompaniesId() throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Long> companiesId = new ArrayList<>();
		String sql = "select * from Company_Coupon";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				long companyId = resultSet.getLong(1);
				companiesId.add(companyId);

			}
		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Company_Coupon data");
		}catch (Exception e) {
			throw new Exception("unable to get Company_Coupon data");
		} 
		finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return companiesId;
	}

	/*
	 * Get coupons ID list from Company_Coupon table override method:
	 * This method used to get list of couponsId by specific companyId.
	 * This method receive 1 parameters: companyId. only records with this ID are relevant.
	 * There is generation of ArrayList which need to receive the data from table.
	 * According to parameter, the SQL query is defined with the company ID.    
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There is function add of ArrayList which used in order to keep the results and to return the list. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public List<Long> getCouponId(long companyId) throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Long> couponsId = new ArrayList<>();
		String sql = "select * from Company_Coupon where Company_ID = " + companyId;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				long couponId = resultSet.getLong(2);
				couponsId.add(couponId);
			}
		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Company_Coupon data. companyId: " + companyId);
		}catch (Exception e) {
			throw new Exception("unable to get Company_Coupon data. companyId: " + companyId);
		} 
		finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return couponsId;
	}

	/*
	 * Get all coupons ID list from Company_Coupon table override method:
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
		String sql = "select * from Company_Coupon";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			while (resultSet.next()) {
				long couponId = resultSet.getLong(2);
				couponsId.add(couponId);

			}
		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Company_Coupon data");
		}catch (Exception e) {
			throw new Exception("unable to get Company_Coupon data");
		} 
		finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return couponsId;
	}

}
