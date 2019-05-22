package com.couponsystem.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.couponsystem.dao.CompanyDAO;
import com.couponsystem.bean.Company;
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

public class CompanyDBDAO implements CompanyDAO {

	/* Static connectionPool Object */
	private static ConnectionPool connectionPool;

	/*
	 * Insert to Company table override method:
	 * This method used to add record of company object.
	 * This method receive 1 parameters: company.
	 * According to parameter, the SQL query is defined with 
	 * the company ID, name, password and email.
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for insert to table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void insertCompany(Company company) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "insert into Company(ID, COMP_NAME, PASSWORD, EMAIL) values (?,?,?,?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setLong(1, company.getCompanyId());
			preparedStatement.setString(2, company.getCompanyName());
			preparedStatement.setString(3, company.getCompanyPassword());
			preparedStatement.setString(4, company.getCompanyEmail());

			preparedStatement.executeUpdate();

			System.out.println("Company created: " + company.toString());
		} catch (SQLException e) {
			throw new Exception("DB error - Company creation failed. companyId: "+ company.getCompanyId());
		}catch (Exception e) {
			throw new Exception("Company creation failed. companyId: "+ company.getCompanyId());
		} 
		finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Remove from Company table override method:
	 * This method used to remove record of company object.
	 * This method receive 1 parameters: company.
	 * According to parameter, the SQL query is defined with the company ID.    
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for remove from table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void removeCompany(Company company) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "delete from Company where ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false);
			preparedStatement.setLong(1, company.getCompanyId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println("remove succeeded. Company removed id: " + company.getCompanyId()); 
																									
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				throw new Exception("DB error - failed to remove Company. companyId: " + company.getCompanyId());
			}
			throw new Exception("failed to remove Company. companyId: " + company.getCompanyId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		} 

	}

	/*
	 * Update Company table override method:
	 * This method used to update record of company object.
	 * This method receive 1 parameters: company.
	 * According to parameter, the SQL query is defined with 
	 * the company ID, name, password and email.
	 * The updates only available for company name, password and email where the relevant ID. 
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for update table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void updateCompany(Company company) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = String.format("update Company set COMP_NAME= '%s',PASSWORD = '%s', EMAIL= '%s' where ID = %d",
				company.getCompanyName(), company.getCompanyPassword(), company.getCompanyEmail(),
				company.getCompanyId());

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.executeUpdate();

			System.out.println("update Company succeeded. id which updated: " + company.getCompanyId());
		} catch (SQLException e) {
			throw new Exception("update Compnay failed. companyId: "+ company.getCompanyId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Get company from Company table override method:
	 * This method used to get specific record of company object.
	 * This method receive 1 parameters: companyId. 
	 * There is generation of Company object which need to receive the data from table.
	 * According to parameter, the SQL query is defined with the company ID.    
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There are setters methods of Company object which used in order to keep the results and to return the object. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public Company getCompany(long companyId) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		Company company = new Company();
		String sql = "SELECT * FROM Company WHERE ID=" + companyId;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			company.setCompanyId(resultSet.getLong(1));
			company.setCompanyName(resultSet.getString(2));
			company.setCompanyPassword(resultSet.getString(3));
			company.setCompanyEmail(resultSet.getString(4));

		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Company data. companyId: " + companyId);
		}catch (Exception e) {
			throw new Exception("unable to get Company data. companyId: " + companyId);
		} 
		finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return company;
	}

	/*
	 * Get all companies list from Company table override method:
	 * This method used to get all companies records.
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
	public synchronized List<Company> getAllCompanies() throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Company> list = new ArrayList<>();
		String sql = "select * from Company";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				long companyId = resultSet.getLong(1);
				String companyName = resultSet.getString(2);
				String companyPassword = resultSet.getString(3);
				String companyEmail = resultSet.getString(4);

				list.add(new Company(companyId, companyName, companyPassword, companyEmail));

			}

		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Company data");
		}catch (Exception e) {
			throw new Exception("unable to get Company data");
		} 
		finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return list;

	}
}
