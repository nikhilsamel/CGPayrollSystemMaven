package com.ing.payroll.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.ing.payroll.daoservices.PayrollDAOServices;
import com.ing.payroll.exception.PayrollServicesDownException;
import com.ing.payroll.services.PayrollServices;

public class ServiceProvider {
	private static Properties payrollProperties;
	
	static{
		payrollProperties = new Properties();
		FileInputStream src;
		try {
			src = new FileInputStream(new File("src\\main\\resources\\CGPayrollSystemCollection.properties"));
			payrollProperties.load(src);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static PayrollDAOServices getPayrollDAOServices() throws PayrollServicesDownException {
		try{
			//String payServices = payrollProperties.getProperty("payServices");
			Class ref = Class.forName(payrollProperties.getProperty("daoServices"));
			return (PayrollDAOServices)ref.newInstance();
		}catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new PayrollServicesDownException("Payroll Services Down. Please try again later.");
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new PayrollServicesDownException("Payroll Services Down. Please try again later.");
		}
	}
	@SuppressWarnings("rawtypes")
	public static PayrollServices getPayrollServices() throws PayrollServicesDownException {
		try{
			//String payServices = payrollProperties.getProperty("payServices");
			Class ref = Class.forName(payrollProperties.getProperty("payServices"));
			return (PayrollServices)ref.newInstance();
		}catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new PayrollServicesDownException("Payroll Services Down. Please try again later.");
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new PayrollServicesDownException("Payroll Services Down. Please try again later.");
		}
	}
	public static Connection getDBConnection() throws PayrollServicesDownException {
		try {
			Class.forName(payrollProperties.getProperty("driver"));
			return DriverManager.getConnection(payrollProperties.getProperty("url"), payrollProperties.getProperty("userName"), payrollProperties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new PayrollServicesDownException("Payroll Services down. Please try again later.");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PayrollServicesDownException("Payroll Services down. Please try again later.");
		}
	}
}
