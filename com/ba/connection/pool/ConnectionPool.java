package com.ba.connection.pool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class ConnectionPool {

	private String jdbcUrl;
	private String jdbcDriver;
	private String userName;
	private String userPass;
	private int stepSize = 2;
	private int initSize;
	private int maxSize;
	private Vector<PooledConnection> connectionVec = new Vector<>();

	public int getSize() {
		return connectionVec.size();
	}

	public synchronized int getStepSize() {
		return stepSize;
	}

	public synchronized void setStepSize(int stepSize) {
		this.stepSize = stepSize;
	}

	public synchronized int getMaxSize() {
		return maxSize;
	}

	public synchronized void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public ConnectionPool(String jdbcUrl, String jdbcDriver, String userName, String userPass, int initSize,
			int maxSize) {
		super();
		this.jdbcUrl = jdbcUrl;
		this.jdbcDriver = jdbcDriver;
		this.userName = userName;
		this.userPass = userPass;
		this.initSize = initSize;
		this.maxSize = maxSize;
		init();
	}

	private void init() {
		try {
			Driver driver = (Driver) Class.forName(jdbcDriver).newInstance();
			DriverManager.registerDriver(driver);
			createConnection(initSize);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized void createConnection(int count) {
		if (maxSize > 0 && (connectionVec.size() + count > maxSize)) {
			count = maxSize - connectionVec.size();
		}

		for (int i = 0; i < count; i++) {
			try {
				Connection conn = DriverManager.getConnection(jdbcUrl, userName, userPass);
				PooledConnection pooledConn = new PooledConnection(conn, false);
				connectionVec.add(pooledConn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public PooledConnection getConnection() {
		PooledConnection connection = getRealConnection();
		while (connection == null) {
			createConnection(stepSize);
			connection = getRealConnection();
		}
		return connection;
	}

	private synchronized PooledConnection getRealConnection() {
		for (PooledConnection pooledConn : connectionVec) {
			if (!pooledConn.isBusy()) {
				Connection connection = pooledConn.getConnection();
				try {
					if (!connection.isValid(2000)) {
						Connection newConnection = DriverManager.getConnection(jdbcUrl, userName, userPass);
						pooledConn.setConnection(newConnection);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				pooledConn.setBusy(true);
				return pooledConn;
			}			
		}
		return null;
	}

}
