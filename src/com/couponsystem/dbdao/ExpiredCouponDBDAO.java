package com.couponsystem.dbdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.couponsystem.dao.ExpiredCouponDAO;
import com.couponsystem.bean.Coupon;
import com.couponsystem.utils.ConnectionPool;

/**
 * @author Shay Ben Haroush
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

public class ExpiredCouponDBDAO implements ExpiredCouponDAO {

	/* Static connectionPool Object */
	private static ConnectionPool connectionPool;

	/*
	 * Insert to ExpiredCoupon table override method:
	 * This method used to add record of coupon object which expired.
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

		String sql = "insert into Expired_Coupon(ID_ExCoupon, TITLE, START_DATE, END_DATE, AMOUNT, TYPE, MESSAGE, PRICE, IMAGE, ACTIVE) values (?,?,?,?,?,?,?,?,?,?)";

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

			System.out.println("Expired Coupon created: " + coupon.toString());
		} catch (SQLException e) {
			throw new Exception("DB error - Expired Coupon creation failed. couponId: " + coupon.getCouponId());
		} catch (Exception e) {
			throw new Exception("Expired Coupon creation failed. couponId: " + coupon.getCouponId());
		} finally {
			connection.close();
			connectionPool.returnConnection(connection);
		}

	}

}
