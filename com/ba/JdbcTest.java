package com.ba;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.internal.OracleTypes;
import static java.lang.System.out;

public class JdbcTest {
	public static void main(String[] args) throws Exception {
		String dbDriver = "oracle.jdbc.driver.OracleDriver";
		String dbConnection = "jdbc:oracle:thin:@172.24.125.74:1521:v2_dev";
		String dbName = "stksettleadmin";
		String dbPass = "stksettleadmin";
		ResultSet rs = null;
		Connection conn = null;
		CallableStatement callStmt = null;
		Statement prepStmt = null;
		// 调用存储过程，返回cursor
		try {
			out.println("call procedure*********");
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dbConnection, dbName, dbPass);
			//conn.setAutoCommit(false);// 设置为不自动提交
			callStmt = conn.prepareCall("call test_refout(?,?)");
			callStmt.setLong(1, 1);
			callStmt.registerOutParameter(2, OracleTypes.CURSOR);
			callStmt.execute();
			rs = (ResultSet) callStmt.getObject(2);
			int columnCount = rs.getMetaData().getColumnCount();
			String[] colNameArr = new String[columnCount];
			String[] colTypeArr = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				colNameArr[i] = rs.getMetaData().getColumnName(i + 1);
				colTypeArr[i] = rs.getMetaData().getColumnTypeName(i + 1);
				out.print(colNameArr[i] + "(" + colTypeArr[i] + ")" + (i == columnCount - 1 ? "" : "|"));
			}
			out.println();
			while (rs != null && rs.next()) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < columnCount; i++) {
					sb.append(rs.getString(i + 1) + (i == columnCount - 1 ? "" : "|"));
				}
				out.println(sb);
			}
			rs.close();
			//conn.commit();// 手动提交
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (callStmt != null) {
				callStmt.close();
			}
			if (conn != null)
				conn.close();
		}

	}
}
