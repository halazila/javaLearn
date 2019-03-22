package com.ba.connection.pool;

import java.util.Map;
import java.util.Set;
import static java.lang.System.out;

public class ConnectionPoolTest {

	public static void main(String[] args) {
		String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
		String jdbcUrl = "jdbc:oracle:thin:@172.24.125.74:1521:v2_dev";
		String userName = "stksettleadmin";
		String userPass = "stksettleadmin";
		ConnectionPool jdbcPool = new ConnectionPool(jdbcUrl, jdbcDriver, userName, userPass, 5, 10);
		int threadNum = 10000;
		Thread[] threads = new Thread[threadNum];
		for (int i = 0; i < threadNum; i++) {
			threads[i] = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					PooledConnection conn = jdbcPool.getConnection();
					String sqlstr = "select * from test_count";
					Map<Object, Object> rs = conn.queryBySql(sqlstr);
					conn.close();
					out.print(Thread.currentThread().getName() + ":");
					Set<Object> keys = rs.keySet();
					for (Object key : keys) {
						out.print(rs.get(key) + ",");
					}
					out.println();
				}
			});
			threads[i].start();
		}

		for (int i = 0; i < threadNum; i++)
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println("jdbcPool size=" + jdbcPool.getSize());
	}

}
