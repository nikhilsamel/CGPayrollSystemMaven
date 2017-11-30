package com.ing.payroll.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ing.payroll.beans.Associate;
import com.ing.payroll.beans.BankDetails;
import com.ing.payroll.beans.Salary;
import com.ing.payroll.daoservices.PayrollDAOServicesImpl;
import com.ing.payroll.exception.AssociateDetailsNotFoundException;
import com.ing.payroll.exception.PayrollServicesDownException;
import com.ing.payroll.services.PayrollServices;
import com.ing.payroll.services.PayrollServicesImpl;
public class PayrollServicesTest {


	private static PayrollServices payrollServices;
	
	@BeforeClass
	public static void setUpPayrollServices() throws PayrollServicesDownException{
		payrollServices = new PayrollServicesImpl();
	}
	@Before 
	public void setUpTestData(){
		PayrollDAOServicesImpl.associates.put(1000, new Associate(1000, 78000, "Satish", "Mahajan", "Training", "Manager", "DTDYF736", "satish.mahajan@capgemini.com", new Salary(35000, 1800, 18000), new BankDetails(12345, "HDFC", "HDFC0097"))); 
		PayrollDAOServicesImpl.associates.put(1001, new Associate(1001, 86448, "Kumar", "Raj", "ADM", "Sr Manager", "DHTDT6353", "kumar.raj@capgemini.com", new Salary(50000, 1800, 18000), new BankDetails(64646, "HDFC", "HDFC0097"))); 
		PayrollDAOServicesImpl.ASSOCIATE_ID_COUNTER=1001;
	}
	
	
	@Test(expected=AssociateDetailsNotFoundException.class)
	public void testGetAssociateDataForInvalidAssociateId() throws PayrollServicesDownException, AssociateDetailsNotFoundException{
		payrollServices.getAssociateDetails(63363);
	}
	
	@Test 
	public void testGetAssociateDataForValidAssociateId() throws PayrollServicesDownException, AssociateDetailsNotFoundException{
		Associate expectedAssociate = new Associate(1000, 78000, "Satish", "Mahajan", "Training", "Manager", "DTDYF736", "satish.mahajan@capgemini.com", new Salary(35000, 1800, 18000), new BankDetails(12345, "HDFC", "HDFC0097"));
		Associate actualAssociate = payrollServices.getAssociateDetails(1000);
		assertEquals(expectedAssociate, actualAssociate);
	}
	
	
	@Test 
	public void testAcceptAssociateDetailsForValidData() throws PayrollServicesDownException{
		int expectedAssociateId =1002;
		int actualAssociateId =payrollServices.acceptAssociateDetails("NIlesh", "Patil", "nilesh.patil@capgemini.com", "ADM", "Manager", "ohhoh73763", 78999, 30000, 1800, 1800, 645454, "ICICI", "ICICI8437") ;
		assertEquals(expectedAssociateId, actualAssociateId);
	}
	
	@Test(expected=AssociateDetailsNotFoundException.class)
	public void testCalculateNetSalaryForInvalidAssociateId() throws PayrollServicesDownException, AssociateDetailsNotFoundException{
		payrollServices.getAssociateDetails(63363);
	}
	
	@Test 
	public void testCalculateNetSalaryForValidAssociateId(){
		fail();
	}
	
	@Test
	public void testGetAllAssociatesDetails() throws PayrollServicesDownException{
		List<Associate> expectedAssociateList = new ArrayList<>(PayrollDAOServicesImpl.associates.values());
		List<Associate>actualAssociateList = payrollServices.getAllAssociatesDetails();
		assertEquals(expectedAssociateList, actualAssociateList);
	}
	@After 
	public void tearDownTestData(){
		PayrollDAOServicesImpl.associates.clear();
		PayrollDAOServicesImpl.ASSOCIATE_ID_COUNTER=1000;
	}
	
	@AfterClass 
	public static void  tearDownPayrollServicesq(){
		payrollServices = null;
	}

}
