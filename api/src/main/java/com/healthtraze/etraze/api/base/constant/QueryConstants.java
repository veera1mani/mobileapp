package com.healthtraze.etraze.api.base.constant;

public class QueryConstants {

	
	public final static String DASHBOARD_QUERY = "SELECT\r\n"
			+ "    COUNT(DISTINCT t.ticket_id) AS total,\r\n"
			+ "    SUM(CASE WHEN t.status = 'ASSIGNED' THEN 1 ELSE 0 END) AS assignedCount,\r\n"
			+ "    SUM(CASE WHEN t.status = 'CREATED' THEN 1 ELSE 0 END) AS unassignedCount\r\n"
			+ "FROM\r\n"
			+ "    tbl_ticket t where t.tenant_id =:tenantId\r\n"
			+ "UNION ALL\r\n"
			+ "SELECT\r\n"
			+ "    COUNT(DISTINCT t.ticket_id) AS total,\r\n"
			+ "    SUM(CASE WHEN toi.status = 'INVOICE CREATED' THEN 1 ELSE 0 END) AS assignedCount,\r\n"
			+ "    COUNT(DISTINCT t.ticket_id) - SUM(CASE WHEN toi.status = 'INVOICE CREATED' THEN 1 ELSE 0 END) AS unassignedCount\r\n"
			+ "FROM\r\n"
			+ "    tbl_ticket_order t\r\n"
			+ "LEFT JOIN\r\n"
			+ "    tbl_ticket_order_invoice toi ON t.ticket_id = toi.ticket_id\r\n"
			+ "UNION ALL\r\n"
			+ "\r\n"
			+ "SELECT\r\n"
			+ "    COUNT(DISTINCT CASE WHEN t.type != 'order' THEN t.ticket_id END) AS total,\r\n"
			+ "    SUM(CASE WHEN t.status = 'INPROCESS' THEN 1 ELSE 0 END) AS assignedCount,\r\n"
			+ "    SUM(CASE WHEN t.status = 'COMPLETED' THEN 1 ELSE 0 END) AS unassignedCount\r\n"
			+ "FROM\r\n"
			+ "    tbl_ticket t;\r\n";
//		public final static String DASHBOARD_QUERY_USER = "SELECT " +
//                "COUNT(DISTINCT CASE WHEN t.type = 'order' THEN t.ticket_id END) AS total, " +
//                "SUM(CASE WHEN toi.status = 'INVOICE CREATED' THEN 1 ELSE 0 END) AS assignedCount, " +
//                "COUNT(DISTINCT t.ticket_id) - SUM(CASE WHEN toi.status = 'INVOICE CREATED' THEN 1 ELSE 0 END) AS unassignedCount " +
//                "FROM tbl_ticket t " +
//                "LEFT JOIN tbl_ticket_order_invoice toi ON t.ticket_id = toi.ticket_id " +
//                "WHERE t.assigned_to = :assignedTo " +
//                "UNION ALL " +
//                "SELECT " +
//                "COUNT(DISTINCT CASE WHEN t.type != 'order' THEN t.ticket_id END) AS total, " +
//                "SUM(CASE WHEN t.status = 'INPROCESS' THEN 1 ELSE 0 END) AS assignedCount, " +
//                "SUM(CASE WHEN t.status = 'COMPLETED' THEN 1 ELSE 0 END) AS unassignedCount " +
//                "FROM tbl_ticket t " +
//                "WHERE t.assigned_to = :assignedTo";

	
}
