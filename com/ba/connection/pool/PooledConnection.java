package com.ba.connection.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class PooledConnection {

	private Connection connection;
	private boolean busy = false;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public boolean isBusy() {
		return busy;
	}

	public PooledConnection(Connection connection, boolean busy) {
		this.connection = connection;
		this.busy = busy;
	}

	public void close() {
		setBusy(false);
	}

	public Map<Object, Object> queryBySql(String sql) {
		Statement stmt = null;
		ResultSet rs = null;
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			int columnCount = rs.getMetaData().getColumnCount();
			while (rs != null && rs.next()) {
				for (int i = 0; i < columnCount; i++) {
					result.put(rs.getMetaData().getColumnLabel(i + 1), rs.getObject(i + 1));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return result;
	}

}
