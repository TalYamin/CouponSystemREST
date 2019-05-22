package com.couponsystem.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.couponsystem.db.DataBase;
import com.couponsystem.exceptions.ConnectionException;

/**
 * @author Shay Ben Haroush and Tal Yamin
 *
 */

/*
 * The ConnectionPool Class is a singleton class which manage the connections
 * allowed simultaneously. it has 2 synchronized methods: getConnection() and
 * returnConnection() that manages the connection requests from the threads
 * using wait() and notiftyAll() Object class methods. Once the connections have
 * reached the Max simultaneously connection allowed, any additional thread that
 * will request a connection via getConnection method will have to "wait()"
 * until the returnConnection() method will "notifyAll()" that there are
 * available connections in the ConnectionPool.
 */

public class ConnectionPool {

	/* the ConnectionPool one instance */
	private static ConnectionPool instance;

	/* Maximum connections that can be active simultaneously */
	private final int MAX_CON_NUM = 10;

	/*
	 * BlockingQueue of connections, its size is limited by Maximum connections
	 * allowed
	 */
	private BlockingQueue<Connection> conQ = new LinkedBlockingQueue<>();

	/*
	 * Private CTOR (SingelTone design pattern). only one Instance can be created,
	 * and only within the ConnectionPool Class. other classes can interact with
	 * this instance via getInstance() Method.
	 */
	private ConnectionPool() throws SQLException, Exception {

		/* Derby Driver Connection */
		try {
			Class.forName(DataBase.getDriverConnextion());
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		/*
		 * DB connection port - Using While Loop in order to set all the available connections
		 * in BlockingQueue
		 */
		while (this.conQ.size() < MAX_CON_NUM) {
			Connection con1 = DriverManager.getConnection(DataBase.getConnectionString());
			this.conQ.offer(con1);
		}

	}

	/*
	 * allows to get the single instance variable that was created in the singleton
	 * class.
	 */
	public static ConnectionPool getInstance() throws Exception {
		if (instance == null) {
			try {
				instance = new ConnectionPool();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new Exception("unable to get instance of connectioPool");
			}
		}

		return instance;
	}

	/*
	 * Send a connection if available and forces threads to wait if the connections
	 * have reach the Maximum connections allowed simultaneously.
	 * This method is synchronized so only one thread can get to the connection pool at a time.
	 */
	public synchronized Connection getConnection() throws Exception {

		try {

			/* Check if BlockingQueue is empty, and if it is empty - wait() activated */
			while (conQ.isEmpty()) {
				wait();
				System.out.println("ALERT: connection pool is empty, please wait...");
			}

			/* If BlockingQueue is not empty, so connection is removed from BlockingQueue */
			Connection c = conQ.poll();
			c.setAutoCommit(true);
			return c;
		} catch (Exception e) {
			throw new ConnectionException("failed to get connection. ", this.conQ.size());
		}
	}

	/*
	 * Returns the connection once it has been released and notifying all that there
	 * is an available connection within the connection pool.
	 * This method is synchronized so only one thread can get to the connection pool at a time.
	 */
	public synchronized void returnConnection(Connection c) throws Exception {
		try {
			Connection c1 = DriverManager.getConnection(DataBase.getConnectionString());
			conQ.offer(c1);
			notifyAll();
		} catch (Exception e) {
			throw new ConnectionException("failed to return connection. ", this.conQ.size());
		}
	}

	/*
	 * closeAllConnection: check if there is connection available in BlockingQueue,
	 * remove all the connections and close them. Using this method for shutdown of
	 * system.
	 */

	public void closeAllConnections() throws Exception {

		Connection c;
		while (this.conQ.peek() != null) {
			c = this.conQ.poll();
			try {
				c.close();
			} catch (Exception e) {
				throw new ConnectionException("Unable to close all connections. ", this.conQ.size());
			}
		}
		
		instance = null;
		System.out.println("All connections have been closed in ConnectionPool");
	}

	/* Return the number of the available connections in BlockingQueue */

	public int availableConnections() {
		System.out.println("The num of available connections: " + this.conQ.size());
		return this.conQ.size();
	}

}
