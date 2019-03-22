package com.ba.connection.pool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcConnectionTest {

	public static void main(String[] args) {
		String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
		String jdbcUrl = "jdbc:oracle:thin:@172.24.125.74:1521:v2_dev";
		String userName = "stksettleadmin";
		String userPass = "stksettleadmin";
		Driver driver;
		try {
			driver = (Driver) Class.forName(jdbcDriver).newInstance();
			DriverManager.registerDriver(driver);
			Connection conn = DriverManager.getConnection(jdbcUrl, userName, userPass);
			String sql = "select * from test_count";
			for (int i = 0; i < 10000; i++) {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				if (stmt != null)
					stmt.close();
			}
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

}
