package com.couponsystem.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.couponsystem.dao.CouponDAO;
import com.couponsystem.bean.Coupon;
import com.couponsystem.bean.CouponType;
import com.couponsystem.utils.ConnectionPool;
import com.couponsystem.utils.DateConverterUtil;

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

public class CouponDBDAO implements CouponDAO {

	/* Static connectionPool Object */
	private static ConnectionPool connectionPool;

	/*
	 * Insert to Coupon table override method:
	 * This method used to add record of coupon object.
	 * This method receive 1 parameters: coupon.
	 * According to parameter, the SQL query is defined with 
	 * the coupon ID, title, startDate, endDate, amount, type, message, price, image and active.
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for insert to table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void insertCoupon(Coupon coupon) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "insert into Coupon(ID, TITLE, START_DATE, END_DATE, AMOUNT, TYPE, MESSAGE, PRICE, IMAGE, ACTIVE) values (?,?,?,?,?,?,?,?,?,?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setLong(1, coupon.getCouponId());
			preparedStatement.setString(2, coupon.getTitle());
			preparedStatement.setString(3, coupon.getStartDate().toString()); 
			preparedStatement.setString(4, coupon.getEndDate().toString());
			preparedStatement.setInt(5, coupon.getAmount());
			preparedStatement.setString(6, coupon.getType().toString());
			preparedStatement.setString(7, coupon.getCouponMessage());
			preparedStatement.setDouble(8, coupon.getPrice());
			preparedStatement.setString(9, coupon.getImage());
			preparedStatement.setBoolean(10, coupon.isActive());

			preparedStatement.executeUpdate();

			System.out.println("Coupon created: " + coupon.toString());
		}catch (SQLException e) {
			throw new Exception("DB error - Coupon creation failed. couponId: " + coupon.getCouponId());
		} 
		catch (Exception e) {
			throw new Exception("Coupon creation failed. couponId: " + coupon.getCouponId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

	/*
	 * Remove from Coupon table override method:
	 * This method used to remove record of coupon object.
	 * This method receive 1 parameters: coupon.
	 * According to parameter, the SQL query is defined with the coupon ID.    
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for remove from table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void removeCoupon(Coupon coupon) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = "delete from Coupon where ID = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			connection.setAutoCommit(false);
			preparedStatement.setLong(1, coupon.getCouponId());
			preparedStatement.executeUpdate();
			connection.commit();
			System.out.println("remove succeeded. Coupon removed id: " + coupon.getCouponId());

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				throw new Exception("DB error - failed to remove Coupon. couponId: " +coupon.getCouponId());
			}
			throw new Exception("failed to remove Coupon. couponId: " +coupon.getCouponId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}
	
	/*
	 * Update Coupon table override method:
	 * This method used to update record of coupon object.
	 * This method receive 1 parameters: coupon.
	 * According to parameter, the SQL query is defined with 
	 * the coupon ID, title, startDate, endDate, amount, type, message, price, image and active.
	 * The updates only available for coupon title, startDate, endDate, amount, type, message, price, image and active. where the relevant ID. 
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for update table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void updateCoupon(Coupon coupon) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = String.format(
				"update Coupon set TITLE = '%s',START_DATE = '" + coupon.getStartDate().toString() + "', END_DATE = '"
						+ coupon.getEndDate().toString()
						+ "', AMOUNT = %d, TYPE = '%s', MESSAGE = '%s', PRICE = %f, IMAGE = '%s', ACTIVE = " + coupon.isActive() + " where ID = %d",
				coupon.getTitle(), coupon.getAmount(), coupon.getType().toString(), coupon.getCouponMessage(), coupon.getPrice(),
				coupon.getImage(), coupon.getCouponId());

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.executeUpdate();

			System.out.println("update Coupon succeeded. id which updated: " + coupon.getCouponId());
		} catch (SQLException e) {
			throw new Exception("DB error - update Coupon failed. couponId: " + coupon.getCouponId());
		}catch (Exception e) {
			throw new Exception("update Coupon failed. couponId: " + coupon.getCouponId());
		} 
		finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}
	
	/*
	 * updateNoActive Coupon table override method:
	 * This method used to update record of coupon object - only activation.
	 * This method receive 1 parameters: coupon.
	 * According to parameter, the SQL query is defined with 
	 * the coupon ID and active.
	 * The updates only available for active where the relevant ID. 
	 * This method receive connection to DB from connectionPool and create prepareStatement.
	 * Then SQL query for update table is executed. 
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public void updateNoActiveCoupon(Coupon coupon) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();

		String sql = String.format(
				"update Coupon set ACTIVE = false where ID = %d", coupon.getCouponId());

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.executeUpdate();

			System.out.println("update to Not Active Coupon succeeded. id which updated: " + coupon.getCouponId());
		} catch (SQLException e) {
			throw new Exception("DB error - update activity Coupon failed. couponId: " + coupon.getCouponId());
		}catch (Exception e) {
			throw new Exception("update activity Coupon failed. couponId: " + coupon.getCouponId());
		} 
		finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}
	
	/*
	 * Get coupon from Coupon table override method:
	 * This method used to get specific record of coupon object.
	 * This method receive 1 parameters: couponId. 
	 * There is generation of Coupon object which need to receive the data from table.
	 * According to parameter, the SQL query is defined with the coupon ID.    
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There is setters methods of Coupon object which used in order to keep the results and to return the object. 
	 * Switch case: by the string of Type that received in resultSet - there is setter of Enum couponType.
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public Coupon getCoupon(long couponId) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		Coupon coupon = new Coupon();
		String sql = "SELECT * FROM Coupon WHERE ID=" + couponId;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			coupon.setCouponId(resultSet.getLong(1));
			coupon.setTitle(resultSet.getString(2));
			coupon.setStartDate(DateConverterUtil.convertDateLocal(resultSet.getDate(3)));
			coupon.setEndDate(DateConverterUtil.convertDateLocal(resultSet.getDate(4)));
			coupon.setAmount(resultSet.getInt(5));
			switch (resultSet.getString(6)) {
			case "Restaurants":
				coupon.setType(CouponType.RESTAURANTS);
				break;
			case "Health":
				coupon.setType(CouponType.HEALTH);
				break;
			case "Sports":
				coupon.setType(CouponType.SPORTS);
				break;
			case "Traveling":
				coupon.setType(CouponType.TRAVELING);
				break;
			default:
				break;
			}
			coupon.setCouponMessage(resultSet.getString(7));
			coupon.setPrice(resultSet.getDouble(8));
			coupon.setImage(resultSet.getString(9));
			coupon.setActive(resultSet.getBoolean(10));
			
		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Coupon data. couponId: " +couponId);
		}catch (Exception e) {
			throw new Exception("unable to get Coupon data. couponId: " +couponId);
		}
		finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return coupon;

	}
	/*
	 * Get all coupons list from Coupon table override method:
	 * This method used to get all coupons records.
	 * There is generation of ArrayList which need to receive the data from table.
	 * There is generation of Coupon object which need to receive the data from table.
	 * the SQL query is defined for all data in table.   
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There are setters methods of Coupon object which used in order to keep the results and to return the object. 
	 * There is function add of ArrayList which used in order to add the Coupon objects and return the list. 
	 * Switch case: by the string of Type that received in resultSet - there is setter of Enum couponType.
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public List<Coupon> getAllCoupons() throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Coupon> list = new ArrayList<>();
		String sql = "select * from Coupon";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				Coupon coupon = new Coupon();
				coupon.setCouponId(resultSet.getLong(1));
				coupon.setTitle(resultSet.getString(2));
				coupon.setStartDate(DateConverterUtil.convertDateLocal(resultSet.getDate(3)));
				coupon.setEndDate(DateConverterUtil.convertDateLocal(resultSet.getDate(4)));
				coupon.setAmount(resultSet.getInt(5));
				switch (resultSet.getString(6)) {
				case "Restaurants":
					coupon.setType(CouponType.RESTAURANTS);
					break;
				case "Health":
					coupon.setType(CouponType.HEALTH);
					break;
				case "Sports":
					coupon.setType(CouponType.SPORTS);
					break;
				case "Traveling":
					coupon.setType(CouponType.TRAVELING);
					break;
				default:
					break;
				}
				coupon.setCouponMessage(resultSet.getString(7));
				coupon.setPrice(resultSet.getDouble(8));
				coupon.setImage(resultSet.getString(9));
				coupon.setActive(resultSet.getBoolean(10));
				
				list.add(coupon);
			}

		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Coupon data");
		}catch (Exception e) {
			throw new Exception("unable to get Coupon data");
		}finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return list;

	}
	
	/*
	 * Get coupons list from Coupon table override method:
	 * This method used to get all coupons by couponId.
	 * This method receive 1 parameters: couponId. 
	 * There is generation of ArrayList which need to receive the data from table.
	 * There is generation of Coupon object which need to receive the data from table.
	 * the SQL query is defined with the coupon ID.   
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There are setters methods of Coupon object which used in order to keep the results and to return the object. 
	 * There is function add of ArrayList which used in order to add the Coupon objects and return the list. 
	 * Switch case: by the string of Type that received in resultSet - there is setter of Enum couponType.
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public List<Coupon> getAllCoupons(long couponId) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Coupon> list = new ArrayList<>();
		String sql = "select * from Coupon where ID = " + couponId;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			
			while (resultSet.next()) {
				Coupon coupon = new Coupon();
				coupon.setCouponId(resultSet.getLong(1));
				coupon.setTitle(resultSet.getString(2));
				coupon.setStartDate(DateConverterUtil.convertDateLocal(resultSet.getDate(3)));
				coupon.setEndDate(DateConverterUtil.convertDateLocal(resultSet.getDate(4)));
				coupon.setAmount(resultSet.getInt(5));
				switch (resultSet.getString(6)) {
				case "Restaurants":
					coupon.setType(CouponType.RESTAURANTS);
					break;
				case "Health":
					coupon.setType(CouponType.HEALTH);
					break;
				case "Sports":
					coupon.setType(CouponType.SPORTS);
					break;
				case "Traveling":
					coupon.setType(CouponType.TRAVELING);
					break;
				default:
					break;
				}
				coupon.setCouponMessage(resultSet.getString(7));
				coupon.setPrice(resultSet.getDouble(8));
				coupon.setImage(resultSet.getString(9));
				coupon.setActive(resultSet.getBoolean(10));

				list.add(coupon);
			}

		} catch (SQLException e) {
			System.out.println(e);
			throw new Exception("DB error - unable to get Coupon data. couponId: " + couponId);
		}catch (Exception e) {
			throw new Exception("unable to get Coupon data. couponId: " + couponId);
		}finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return list;
	}

	/*
	 * Get coupons list from Coupon table override method:
	 * This method used to get all coupons by couponId and typeName.
	 * This method receive 2 parameters: couponId and typeName. 
	 * There is generation of ArrayList which need to receive the data from table.
	 * There is generation of Coupon object which need to receive the data from table.
	 * the SQL query is defined with the coupon ID and typeName.  
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There are setters methods of Coupon object which used in order to keep the results and to return the object. 
	 * There is function add of ArrayList which used in order to add the Coupon objects and return the list. 
	 * Switch case: by the string of Type that received in resultSet - there is setter of Enum couponType.
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public List<Coupon> getAllCouponsByType(long couponId, String typeName) throws Exception {

		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Coupon> list = new ArrayList<>();
		String sql = String.format("select * from Coupon where ID = %d and TYPE = '%s'", couponId, typeName);

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			
			while (resultSet.next()) {
				Coupon coupon = new Coupon();
				coupon.setCouponId(resultSet.getLong(1));
				coupon.setTitle(resultSet.getString(2));
				coupon.setStartDate(DateConverterUtil.convertDateLocal(resultSet.getDate(3)));
				coupon.setEndDate(DateConverterUtil.convertDateLocal(resultSet.getDate(4)));
				coupon.setAmount(resultSet.getInt(5));
				switch (resultSet.getString(6)) {
				case "Restaurants":
					coupon.setType(CouponType.RESTAURANTS);
					break;
				case "Health":
					coupon.setType(CouponType.HEALTH);
					break;
				case "Sports":
					coupon.setType(CouponType.SPORTS);
					break;
				case "Traveling":
					coupon.setType(CouponType.TRAVELING);
					break;
				default:
					break;
				}
				coupon.setCouponMessage(resultSet.getString(7));
				coupon.setPrice(resultSet.getDouble(8));
				coupon.setImage(resultSet.getString(9));
				coupon.setActive(resultSet.getBoolean(10));

				list.add(coupon);
			}

		} catch (SQLException e) {
			System.out.println(e);
			throw new Exception("DB error - unable to get Coupon data. couponId: " + couponId + " couponType: "+ typeName);
		}catch (Exception e) {
			throw new Exception("unable to get Coupon data. couponId: " + couponId + " couponType: "+ typeName);
		}finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return list;
	}

	/*
	 * Get coupons list from Coupon table override method:
	 * This method used to get all coupons by couponId and price limit.
	 * This method receive 2 parameters: couponId and priceTop. 
	 * There is generation of ArrayList which need to receive the data from table.
	 * There is generation of Coupon object which need to receive the data from table.
	 * the SQL query is defined with the coupon ID and priceTop.  
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There are setters methods of Coupon object which used in order to keep the results and to return the object. 
	 * There is function add of ArrayList which used in order to add the Coupon objects and return the list. 
	 * Switch case: by the string of Type that received in resultSet - there is setter of Enum couponType.
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public List<Coupon> getAllCouponsByPrice(long couponId, double priceTop) throws Exception {
		
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		List<Coupon> list = new ArrayList<>();
		String sql = "select * from Coupon where ID = " + couponId + " and PRICE <= " + priceTop;
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			
			while (resultSet.next()) {
				Coupon coupon = new Coupon();
				coupon.setCouponId(resultSet.getLong(1));
				coupon.setTitle(resultSet.getString(2));
				coupon.setStartDate(DateConverterUtil.convertDateLocal(resultSet.getDate(3)));
				coupon.setEndDate(DateConverterUtil.convertDateLocal(resultSet.getDate(4)));
				coupon.setAmount(resultSet.getInt(5));
				switch (resultSet.getString(6)) {
				case "Restaurants":
					coupon.setType(CouponType.RESTAURANTS);
					break;
				case "Health":
					coupon.setType(CouponType.HEALTH);
					break;
				case "Sports":
					coupon.setType(CouponType.SPORTS);
					break;
				case "Traveling":
					coupon.setType(CouponType.TRAVELING);
					break;
				default:
					break;
				}
				coupon.setCouponMessage(resultSet.getString(7));
				coupon.setPrice(resultSet.getDouble(8));
				coupon.setImage(resultSet.getString(9));
				coupon.setActive(resultSet.getBoolean(10));

				list.add(coupon);
			}

		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Coupon data. couponId: " + couponId + " priceTop: " +priceTop);
		}catch (Exception e) {
			throw new Exception("unable to get Coupon data. couponId: " + couponId + " priceTop: " +priceTop);
		}finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return list;
	}

	/*
	 * Get coupons list from Coupon table override method:
	 * This method used to get all coupons by couponId and date limit.
	 * This method receive 2 parameters: couponId and untilDate. 
	 * There is Data Time Formatter which enable parsing date string to Local Date.
	 * There is generation of ArrayList which need to receive the data from table.
	 * There is generation of Coupon object which need to receive the data from table.
	 * the SQL query is defined with the coupon ID and untilDate.  
	 * This method receive connection to DB from connectionPool and create statement.
	 * Then SQL query for get from table is executed. 
	 * There is resultSet which generated so it will be available to receive results from DB.
	 * There are setters methods of Coupon object which used in order to keep the results and to return the object. 
	 * There is function add of ArrayList which used in order to add the Coupon objects and return the list. 
	 * Switch case: by the string of Type that received in resultSet - there is setter of Enum couponType.
	 * If there is DB issue, SQLException is activated.
	 * Finally connection closed and return to pool.
	 */
	@Override
	public List<Coupon> getAllCouponsByDate(long couponId, String untilDate) throws Exception {
		connectionPool = ConnectionPool.getInstance();
		Connection connection = connectionPool.getConnection();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
		List<Coupon> list = new ArrayList<>();
		LocalDate untilLocalDate = LocalDate.parse(untilDate, formatter);
		String sql = "select * from Coupon where ID = " + couponId + " and END_DATE <= '" + untilLocalDate.toString()+"'";
		try (PreparedStatement preparedStatement = connection.prepareStatement(sql); ResultSet resultSet = preparedStatement.executeQuery()) {
			
			while (resultSet.next()) {
				Coupon coupon = new Coupon();
				coupon.setCouponId(resultSet.getLong(1));
				coupon.setTitle(resultSet.getString(2));
				coupon.setStartDate(DateConverterUtil.convertDateLocal(resultSet.getDate(3)));
				coupon.setEndDate(DateConverterUtil.convertDateLocal(resultSet.getDate(4)));
				coupon.setAmount(resultSet.getInt(5));
				switch (resultSet.getString(6)) {
				case "Restaurants":
					coupon.setType(CouponType.RESTAURANTS);
					break;
				case "Health":
					coupon.setType(CouponType.HEALTH);
					break;
				case "Sports":
					coupon.setType(CouponType.SPORTS);
					break;
				case "Traveling":
					coupon.setType(CouponType.TRAVELING);
					break;
				default:
					break;
				}
				coupon.setCouponMessage(resultSet.getString(7));
				coupon.setPrice(resultSet.getDouble(8));
				coupon.setImage(resultSet.getString(9));
				coupon.setActive(resultSet.getBoolean(10));

				list.add(coupon);
			}

		} catch (SQLException e) {
			throw new Exception("DB error - unable to get Coupon data. couponId: " + couponId + " untilDate: " + untilDate);
		}catch (Exception e) {
			throw new Exception("unable to get Coupon data. couponId: " + couponId + " untilDate: " + untilDate);
		}finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}
		return list;
	}

}
