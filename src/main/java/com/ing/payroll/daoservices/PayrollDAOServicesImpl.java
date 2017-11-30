package com.ing.payroll.daoservices;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ing.payroll.beans.Associate;
import com.ing.payroll.beans.BankDetails;
import com.ing.payroll.beans.Salary;
import com.ing.payroll.exception.PayrollServicesDownException;
import com.ing.payroll.provider.ServiceProvider;

public class PayrollDAOServicesImpl implements PayrollDAOServices {

	private static Connection conn;
	public PayrollDAOServicesImpl() throws PayrollServicesDownException {
		conn = ServiceProvider.getDBConnection();
	}
	
	public static HashMap<Integer, Associate> associates = new HashMap<>();
	public static int ASSOCIATE_ID_COUNTER =1000;
	
	@Override
	public int insertAssociate(Associate associate) throws SQLException {

		try {
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into Associate (yearlyInvestmentUnder80C,firstName,lastName,department,designation,pancard,emailId) values(?,?,?,?,?,?,?)");
			pstmt.setInt(1, associate.getYearlyInvestmentUnder80C());
			pstmt.setString(2, associate.getFirstName());
			pstmt.setString(3, associate.getLastName());
			pstmt.setString(4, associate.getDepartment());
			pstmt.setString(5, associate.getDesignation());
			pstmt.setString(6, associate.getPancard());
			pstmt.setString(7, associate.getEmailId());
			pstmt.executeUpdate();
			
			ResultSet rs =conn.prepareStatement("select max(associateId)  from Associate").executeQuery();
			rs.next(); 
			int associateId =rs.getInt(1); 
			
			pstmt = conn.prepareStatement("insert into Salary(associateId,basicSalary,epf,companyPf)values(?,?,?,?)");
			pstmt.setInt(1, associateId);
			pstmt.setInt(2, associate.getSalary().getBasicSalary());
			pstmt.setInt(3, associate.getSalary().getEpf());
			pstmt.setInt(4, associate.getSalary().getCompanyPf());
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("insert into BankDetails(associateId,accountNo,bankName, ifscCode)values(?,?,?,?)");
			pstmt.setInt(1, associateId);
			pstmt.setInt(2, associate.getBankDetails().getAccountNo());
			pstmt.setString(3, associate.getBankDetails().getBankName());
			pstmt.setString(4, associate.getBankDetails().getIfscCode());
			pstmt.executeUpdate();
			conn.commit();
			return associateId;
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	@Override
	public boolean updateAssociate(Associate associate) throws SQLException {
		try {
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("update Associate set yearlyInvestmentUnder80C=?,firstName=?,lastName=?,department=?,designation=?,pancard=?,emailId=? where associateId="+associate.getAssociateID());
			pstmt.setInt(1, associate.getYearlyInvestmentUnder80C());
			pstmt.setString(2, associate.getFirstName());
			pstmt.setString(3, associate.getLastName());
			pstmt.setString(4, associate.getDepartment());
			pstmt.setString(5, associate.getDesignation());
			pstmt.setString(6, associate.getPancard());
			pstmt.setString(7, associate.getEmailId());
			pstmt.executeUpdate();
			
			ResultSet rs =conn.prepareStatement("select max(associateId)  from Associate").executeQuery();
			rs.next(); 
			pstmt = conn.prepareStatement("update Salary set associateId=?, basicSalary=?,epf=?,companyPf=?"+
											", hra=?, conveyenceAllowance=?, otherAllowance=?,"
											+ "personalAllowance=?,"
											+ "monthlyTax=?,gratuity=?,"
											+ "grossSalary=?,netSalary=? where associateId="+associate.getAssociateID());
			pstmt.setInt(1, associate.getAssociateID());
			pstmt.setInt(2, associate.getSalary().getBasicSalary());
			pstmt.setInt(3, associate.getSalary().getEpf());
			pstmt.setInt(4, associate.getSalary().getCompanyPf());
			pstmt.setInt(5, associate.getSalary().getHra());
			pstmt.setInt(6, associate.getSalary().getConveyenceAllowance());
			pstmt.setInt(7, associate.getSalary().getOtherAllowance());
			pstmt.setInt(8, associate.getSalary().getPersonalAllowance());
			pstmt.setInt(9, associate.getSalary().getMonthlyTax());
			pstmt.setInt(10, associate.getSalary().getGratuity());
			pstmt.setInt(11, associate.getSalary().getGrossSalary());
			pstmt.setInt(12, associate.getSalary().getNetSalary());
			
			
			pstmt.executeUpdate();
			
			pstmt = conn.prepareStatement("update BankDetails set associateId=?,accountNo=?,"
								+ "bankName=?, ifscCode=? where associateId="+associate.getAssociateID());
			pstmt.setInt(1, associate.getAssociateID());
			pstmt.setInt(2, associate.getBankDetails().getAccountNo());
			pstmt.setString(3, associate.getBankDetails().getBankName());
			pstmt.setString(4, associate.getBankDetails().getIfscCode());
			pstmt.executeUpdate();
			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			conn.rollback();
			throw e;
		}
	}

	@Override
	public boolean deleteAssciate(int associateId) throws SQLException {
		conn.setAutoCommit(false);
		PreparedStatement pstmt = conn.prepareStatement("delete from Associate where associateId="+associateId);
		if(pstmt==null)
		return false;
		else
			return true;
	}

	@Override
	public Associate getAssociate(int associateId) throws SQLException {
		ResultSet rs= conn.prepareStatement("select * from Associate a , Salary s , BankDetails b  "
				+ "where a.associateId=s.associateId and  "
				+ "a.associateId=b.associateId and a.associateId="+associateId).executeQuery();
		if(rs.next())
			return new Associate(rs.getInt("associateId"), rs.getInt("yearlyInvestmentUnder80C"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("department"), rs.getString("designation"), rs.getString("pancard"), rs.getString("emailId"), new Salary(rs.getInt("basicSalary"), rs.getInt("hra"), rs.getInt("conveyenceAllowance"), rs.getInt("otherAllowance"), rs.getInt("personalAllowance"), rs.getInt("monthlyTax"), rs.getInt("epf"), rs.getInt("companyPf"), rs.getInt("gratuity"), rs.getInt("grossSalary"), rs.getInt("netSalary")), new BankDetails(rs.getInt("accountNo"), rs.getString("bankName"), rs.getString("ifscCode")));
		return null;
	}

	@Override
	public List<Associate> getAssociates() throws SQLException {
		List<Associate> associateList = new ArrayList<>();
		ResultSet rs = conn.prepareStatement("select * from Associate a , Salary s , BankDetails b  "
				+ "where a.associateId=s.associateId and  "
				+ "a.associateId=b.associateId").executeQuery();
		while(rs.next())
			associateList.add(new Associate(rs.getInt("associateId"), rs.getInt("yearlyInvestmentUnder80C"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("department"), rs.getString("designation"), rs.getString("pancard"), rs.getString("emailId"), new Salary(rs.getInt("basicSalary"), rs.getInt("hra"), rs.getInt("conveyenceAllowance"), rs.getInt("otherAllowance"), rs.getInt("personalAllowance"), rs.getInt("monthlyTax"), rs.getInt("epf"), rs.getInt("companyPf"), rs.getInt("gratuity"), rs.getInt("grossSalary"), rs.getInt("netSalary")), new BankDetails(rs.getInt("accountNo"), rs.getString("bankName"), rs.getString("ifscCode"))));
		return associateList;
	}
}
