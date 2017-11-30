package com.ing.payroll.services;
import java.sql.SQLException;
import java.util.List;

import com.ing.payroll.beans.Associate;
import com.ing.payroll.beans.BankDetails;
import com.ing.payroll.beans.Salary;
import com.ing.payroll.daoservices.PayrollDAOServices;
import com.ing.payroll.exception.AssociateDetailsNotFoundException;
import com.ing.payroll.exception.PayrollServicesDownException;
import com.ing.payroll.provider.ServiceProvider;

public class PayrollServicesImpl implements PayrollServices{
	private PayrollDAOServices daoServices;
	
	public PayrollServicesImpl() throws PayrollServicesDownException {
		daoServices = ServiceProvider.getPayrollDAOServices();
	}
	
	public PayrollServicesImpl(PayrollDAOServices daoServices) throws PayrollServicesDownException {
		this.daoServices = daoServices;
	}
	
	@Override
	public int acceptAssociateDetails(String firstName, String lastName,
			String emailId, String department,String designation, String pancard,
			int yearlyInvestmentUnder80C, int basicSalary, int epf, int companyPf, int acccountNo,
			String bankName, String ifscCode) throws PayrollServicesDownException{
		try {
			Associate associate = new Associate(yearlyInvestmentUnder80C, firstName, lastName, department, designation, pancard, emailId, new Salary(basicSalary, epf, companyPf), new BankDetails(acccountNo, bankName, ifscCode));
			int associateId=daoServices.insertAssociate(associate);	
			return associateId;
		} catch (SQLException e) {
			throw new PayrollServicesDownException("Payroll Services down. Please try again later.",e);
		}	
	}
	@Override
	public int calculateNetSalary(int associateId) throws PayrollServicesDownException,AssociateDetailsNotFoundException{
		try {
			Associate associate = this.getAssociateDetails(associateId);
			if(associate==null)throw new AssociateDetailsNotFoundException("not found");
				associate.getSalary().setHra((associate.getSalary().getBasicSalary()*40)/100);
				associate.getSalary().setConveyenceAllowance((associate.getSalary().getBasicSalary()*30)/100);
				associate.getSalary().setPersonalAllowance((associate.getSalary().getBasicSalary()*30)/100);
				associate.getSalary().setGratuity((associate.getSalary().getBasicSalary()*18)/100);
				associate.getSalary().setCompanyPf(1800);
				associate.getSalary().setEpf((associate.getSalary().getBasicSalary()*12)/100);
				associate. getSalary().setGrossSalary(associate.getSalary().getBasicSalary()+associate.getSalary().getHra()+associate.getSalary().getConveyenceAllowance()+associate.getSalary().getPersonalAllowance()+associate.getSalary().getCompanyPf()+associate.getSalary().getEpf());
				int annualSalary=associate.getSalary().getGrossSalary()*12;
				int yI80c=associate.getYearlyInvestmentUnder80C()+associate.getSalary().getCompanyPf()*12+associate.getSalary().getEpf()*12;
				int nonTax=0;
				int annualTax;
				if(yI80c>150000)
					nonTax = 150000;
				else
					nonTax = yI80c;
					annualTax = 0;
				if (annualSalary<=250000) 
					annualTax = 0;
				else if (annualSalary >= 250000 && annualSalary < 500000) 
					annualTax = (500000-250000-nonTax)*5/100;
				else if (annualSalary >= 500000 && annualSalary<= 1000000) 
					annualTax = (500000-250000-nonTax)*5/100 + (annualSalary - 500000)*20/100;
				else if (annualSalary> 1000000)
					annualTax = (500000-250000-nonTax)*5/100 + (1000000 - 500000)*20/100+(annualSalary-1000000)*30/100;
				associate.getSalary().setMonthlyTax(annualTax/12);
				associate.getSalary().setNetSalary(associate.getSalary().getGrossSalary() - associate.getSalary().getEpf() -1800 - associate.getSalary().getMonthlyTax());	
			daoServices.updateAssociate(associate);
			return associate.getSalary().getNetSalary();
		} catch (SQLException e) {
			throw new PayrollServicesDownException("Payroll Services down. Please try again later.",e);
		}
	}
	@Override
	public Associate getAssociateDetails(int associateId) throws AssociateDetailsNotFoundException, PayrollServicesDownException{
		try {
			Associate associate = daoServices.getAssociate(associateId);
			if(associate ==null)throw new AssociateDetailsNotFoundException("Associate details for Id "+associateId+" not found");
			return associate;
		} catch (SQLException e) {
			throw new PayrollServicesDownException("Payroll Services down. Please try again later.",e);
		}
	}
	@Override
	public List<Associate> getAllAssociatesDetails()
			throws PayrollServicesDownException {		
		try {
			return daoServices.getAssociates();
		} catch (SQLException e) {
			throw new PayrollServicesDownException("Payroll Services down. Please try again later.",e);
		}
	}
	@Override
	public boolean removeAssociate(int associateId) throws PayrollServicesDownException {
	  try {
		return daoServices.deleteAssciate(associateId);
	} catch (SQLException e) {
		e.printStackTrace();
		throw new PayrollServicesDownException("Payroll Services down. Please try again later.",e);
	}
	}
}
