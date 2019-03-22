package com.ba;

import java.sql.*;

public class igniteconn {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("org.apache.ignite.IgniteJdbcThinDriver");
		Connection conn = DriverManager.getConnection("jdbc:ignite:thin://127.0.0.1:10800;user=ignite;password=ignite");
		ResultSet rs = conn.createStatement().executeQuery("select * from pubdata");
		while (rs.next()) {
			int pubnum = rs.getInt(1);
			String bcasttype = rs.getString(2);
			int setid = rs.getInt(3);
			int seqnum = rs.getInt(4);
			String recordtime = rs.getString(5);
			String mdtext = rs.getString(6);
			System.out.println("pubnum=" + pubnum + ";bcasttype=" + bcasttype + ";setid=" + setid + ";seqnum=" + seqnum
					+ ";reqcodtime=" + recordtime + ";mdtext=" + mdtext);
		}

	}
}
