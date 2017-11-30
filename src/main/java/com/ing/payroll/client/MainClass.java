package com.ing.payroll.client;
import java.util.Iterator;
import java.util.Scanner;

import com.ing.payroll.beans.Associate;
import com.ing.payroll.exception.AssociateDetailsNotFoundException;
import com.ing.payroll.exception.PayrollServicesDownException;
import com.ing.payroll.provider.ServiceProvider;
import com.ing.payroll.services.PayrollServices;

public class MainClass {

	public static void main(String[] args) throws PayrollServicesDownException, AssociateDetailsNotFoundException {
		String firstName,lastName,emailId,department,designation,pancard,bankName,ifscCode;
		int basicSalary, epf, companyPf,yearlyInvestmentUnder80C,accountNumber,num;
		PayrollServices payServices = ServiceProvider.getPayrollServices();		
		do{
			Scanner sc =new Scanner(System.in);	
			System.out.println("1.Insert Associate\n2.Get Details of Associate\n3.Get Details of All Associate\n4.Calculate Salary of an Associate\n5.Delete Associate\n6.Exit");
			int option = sc.nextInt();
			try{
			switch (option) {
			case 1:System.out.println("How many associate you want to enter?");
					num = sc.nextInt();
					for(int i=0 ; i<num;i++ ){
						System.out.println("Enter your FirstName");  
							firstName=sc.next();   
						System.out.println("Enter your LastName");  
							lastName=sc.next();   
						System.out.println("Enter your Email ID");  
							emailId=sc.next();   
						System.out.println("Enter your Department");  
							department=sc.next();
						System.out.println("Enter your Designation");  
							designation=sc.next();   
						System.out.println("Enter your PAN Card"); 
							pancard=sc.next();   
						System.out.println("Enter your Bank Name");  
							bankName=sc.next();   
						System.out.println("Enter your ifsc Code");  
							ifscCode=sc.next(); 
						System.out.println("Enter your Account number");  
							accountNumber=sc.nextInt(); 
						System.out.println("Enter your Basic Salary"); 
							basicSalary=sc.nextInt();
						System.out.println("Enter your Yealy investment"); 
							yearlyInvestmentUnder80C=sc.nextInt();
						System.out.println("Enter your Company PF"); 
							companyPf=sc.nextInt();
						System.out.println("Enter your Employee PF"); 
							epf=sc.nextInt();
						int associateId=payServices.acceptAssociateDetails(firstName, lastName, emailId, department, designation, pancard, yearlyInvestmentUnder80C, basicSalary, companyPf, epf, accountNumber, bankName, ifscCode);
							System.out.println("Your associateId is:"+associateId);
							System.out.println(payServices.getAssociateDetails(associateId));
					}
				break;
			case 2:
				System.out.println("Enter Associate Id to Search:");
				int associateId = sc.nextInt();
				payServices.getAssociateDetails(associateId);
				System.out.println(payServices.getAssociateDetails(associateId));
				break;
			case 3:
				Iterator<Associate>iterator=payServices.getAllAssociatesDetails().iterator();
				while(iterator.hasNext())
					System.out.println(iterator.next());
					break;
			case 4:
				System.out.println("Enter Associate Id to Calculate Salary:");
				associateId = sc.nextInt();
				System.out.println("Your net salary is: "+payServices.calculateNetSalary(associateId));
				System.out.println(payServices.getAssociateDetails(associateId));
				break;
			case 5:
				System.out.println("Enter Associate Id to Delete:");
				 associateId = sc.nextInt();
				 payServices.removeAssociate(associateId);
				 System.out.println("Associate removed");
				
				break;
			case 6:
				System.out.println("Exit....");
				System.exit(0);
				sc.close();
			default:
				System.out.println("Please choose correct option.");
				break;
			}
		}catch(AssociateDetailsNotFoundException notFoundException){
			System.out.println(notFoundException.getMessage());
		}
		catch(PayrollServicesDownException payrollServicesDownException){
			payrollServicesDownException.printStackTrace();
			//System.out.println(payrollServicesDownException.getMessage());
		}
		}while(true);
	}
}
