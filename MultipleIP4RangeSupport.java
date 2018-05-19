package com.practice.com.practice.demo;  gggg'g'ngfhfgklghkl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

import oracle.fiat.common.FIATException;
import oracle.fiat.utils.TestUtils;
import oracle.oam.fiat.common.ui_constants.SystemConfiguration;
import oracle.oam.fiat.common.ui_libraries.CommonLib;
import oracle.oam.fiat.common.ui_libraries.SeleniumLib;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class MultipleIP4RangeSupport extends MultipleIP4RangeSupportCommon {

	String OAMServerURL;
	//static String SELECT_CONDITION ;
	String ResourceURL;
	static String appdomain;
	String agentport;
	String hostName;
	String agentNAPPort;
	String agentproto;
	String logOutUrl;
	protected ResourceBundle testData;
	public static WebDriver driver;
	String ConditionName;
	String ConditionType;
	String ConditionDesc;
	String DenyAccess;
	String expectedResult;
	String Errormsg;
	static String adminUserName;
	static String adminPwd;
	String IPRange;
	String FromIPRange;
	String ServerIP;
	String ToIPRange;
	String MultipleIPFrom;
	String MultipleIPTo;
	String ResourceIP;
	static String modifiedIP;
	String StoreName = "OID";
	String EntityType = "All";
	String EntityName = "ootbuser1";
	String Identity = "Identity Condition";
	Select select;
	/**
	 * This is constructor method, This file contains the following methods -
	 * setup - increases the number of sessions prereqsTestCase - adds the rule
	 * for particular testcase accessResource - accesses resource and checks for
	 * expected result
	 */
	public MultipleIP4RangeSupport() throws Exception {

		testUtils = new TestUtils(oamLogger);
		oamLogger.setComponent("MultipleIP4RangeSupport");
		appdomain = oam_config.getString("AGENT_NAME");
		agentproto = oam_config.getString("AGENT_PROTO");
		agentport = oam_config.getString("AGENT_PORT");
		hostName = oam_config.getString("HOST_ID");
		agentNAPPort = oam_config.getString("NAP_PORT");

		OAMServerURL = agentproto + "://" + serverHost + ":" + serverPort + "/oamconsole";
		System.out.println("*******************OAMServerURL********************"+ OAMServerURL);
		adminUserName = oam_config.getString("AdminUser");
		adminPwd = oam_config.getString("amadmin_password");
       }

	/**
	 * This method will create base policy required for testcases
	 * 
	 * @param testName
	 * @param evalId
	 * 
	 */

	@BeforeSuite(groups = { "embedded", "embedded_sec", "s1ds", "s1ds_sec",
			"ad", "ad_sec", "edir", "edir_sec", "jdbc", "jdbc_sec", "oid",
			"oid_sec", "ovd", "ovd_sec", "create" })
	public void setup() throws Exception {
		CreatePolicy(appdomain, hostName);
	}

	/**
	 * This method reads the variables from the properties file.
	 * 
	 * @param testName
	 *            - Name of the testcase
	 * 
	 * @param evalId
	 *            - test case index
	 * 
	 * @throws Exception
	 */
	@Parameters({ "TestName", "EvalId" })
	@BeforeClass(groups = { "embedded", "embedded_sec", "s1ds", "s1ds_sec",
			"ad", "ad_sec", "edir", "edir_sec", "jdbc", "jdbc_sec", "oid",
			"oid_sec", "ovd", "ovd_sec", "create" })
	public void prereqsTestCase(String testName, String evalId)
			throws Exception {

		try {
			oamLogger.log(Level.INFO, " prereqsTestCase", " appdomain = "+appdomain);
			Thread.sleep(2000);
			oamLogger.log(Level.INFO, "prereqsTestCase", "TestName is "+testName);
			oamLogger.log(Level.INFO, "prereqsTestCase","evalId i.e index value to be picked from properties file is "+evalId);
			oamLogger.log(Level.INFO, " prereqsTestCase"," ********************************* ");

			testData = ResourceBundle.getBundle("MultipleIP4RangeSupport"+ fileseparator + "MultipleIP4RangeSupport");

			ConditionName = testData.getString("MultipleIP4RangeSupport."+ evalId + ".ConditionName");
			oamLogger.log(Level.INFO, " prereqsTestCase", "1. ConditionName = "+ ConditionName);

			ConditionType = testData.getString("MultipleIP4RangeSupport."+ evalId + ".ConditionType");
			oamLogger.log(Level.INFO, " prereqsTestCase", "2. ConditionType = "+ConditionType);

			DenyAccess = testData.getString("MultipleIP4RangeSupport." + evalId + ".DenyAccess");
			oamLogger.log(Level.INFO, " prereqsTestCase", "4. DenyAccess = "+DenyAccess);

			expectedResult = testData.getString("MultipleIP4RangeSupport."+ evalId + ".expectedResult");
			oamLogger.log(Level.INFO, " prereqsTestCase","5. expectedResult = " +expectedResult);

			FromIPRange = testData.getString("MultipleIP4RangeSupport."+ evalId + ".FromIPRange");
			oamLogger.log(Level.INFO, " prereqsTestCase", "6. FromIPRange = "+FromIPRange);

			ToIPRange = testData.getString("MultipleIP4RangeSupport." + evalId + ".ToIPRange");
			oamLogger.log(Level.INFO, " prereqsTestCase", "7. ToIPRange = "+ToIPRange);

     		} catch (Exception e) {
			Reporter.log("prereqsTestCase exception " + e.getMessage());
			oamLogger.log(Level.SEVERE, "prereqsTestCase exception ",e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}
	/**
	 * This method start the firefox browser and 
	 * login to oam server and
	 * accessing the Authorization policy
	 */
	public static void oamLogin() throws Exception 
		{
		oamLogger.log(Level.INFO, "In Login() method", " ");
        	SeleniumLib.startSelenium(serverNamingURL);
		oamLogger.log(Level.INFO, "Selenium Server Started", " ");
		CommonLib.login(adminUserName, adminPwd);
		oamLogger.log(Level.INFO, "Logged in successfully", " ");
		String appDomain = oam_config.getString("AGENT_NAME");
		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.LAUNCH_PAD));
		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.LAUNCH_PAD_APP_DOMAIN));
		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.AUTHENTICATION_SCHEMES_SERACH_BUTTON));
		String appdomain = "//a[text()='" + appDomain + "']";
		SeleniumLib.clickWaitUntilBusy(By.xpath(appdomain));
		SeleniumLib.waitSeconds(5);
		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_POLICIES));
		SeleniumLib.waitSeconds(2);
		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_POLICIES_PROTECTED));
		SeleniumLib.waitSeconds(5);
	    }
	
	/**
	 * This method executes the testcases , It first add the condition and
	 * depending on this condtion it will access the resource uses curl or
	 * browser,
	 * 
	 * @param testName
	 * @param evalId
	 * 
	 */
	@Parameters({ "TestName", "EvalId" })
	@Test(groups = { "embedded", "embedded_sec", "s1ds", "s1ds_sec", "ad",
			"ad_sec", "edir", "edir_sec", "jdbc", "jdbc_sec", "oid", "oid_sec",
			"ovd", "ovd_sec", "create" })
	public void setIP4RangeCondition(String testName, String evalId)
			throws Exception {
		try {
			SeleniumLib.waitSeconds(5);
			oamLogin();
            addCondition(ConditionName,ConditionType);
			SeleniumLib.waitSeconds(5);
            SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD));
            SeleniumLib.waitSeconds(3);

			if (ConditionName.contains("invalid")) 
			{
				SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_FromIPRange),FromIPRange);
				SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ToIPRange),ToIPRange);
				SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.REMOVE_PLUGIN_OKBUTTON));
				SeleniumLib.waitSeconds(5);
		        if(ConditionName.contains("ToIPRangelessthanIPaddress"))
                            {
            	    	 	SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
            	    	 	SeleniumLib.waitSeconds(5);
            	            }
               String check = "//div[contains(text(),'" + expectedResult+ "')][1]";
               Errormsg = SeleniumLib.getTextForElement(check);
               SeleniumLib.waitSeconds(5);
                 	if (SeleniumLib.isElementVisible(By.xpath(SystemConfiguration.OK_BUTTON)))
				    {
					SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.OK_BUTTON));
				    }
                 	SeleniumLib.waitSeconds(3);
               		if (Errormsg.contains(expectedResult)){
					oamLogger.log(Level.INFO, " setIP4RangeCondition","PASSED: page after login contains expected result = "
                                   +expectedResult);
					assert true;
				} else {
					oamLogger.log(Level.SEVERE, " setIP4RangeCondition","FAILED: page after login contains expected result = "
									+expectedResult);
                    			assert false : " FAILED : page after login contains expected result ";
				}
               
             if(!(ConditionName.contains("ToIPRangelessthanIPaddress")))
               	{
            	 SeleniumLib.waitSeconds(3);
			    SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ToIPRange),ToIPRange);
				SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_FromIPRange),FromIPRange);
				SeleniumLib.waitSeconds(2);
				SeleniumLib.clickWaitUntilBusy(By.xpath("//button[text()='No']"));
				SeleniumLib.waitSeconds(10);
               			}
            
            		 } 
			else
			{   
				ServerIP = getIPAddress();
				System.out.println("************ServerIP************"+ ServerIP);
				
				if(FromIPRange.isEmpty())
				  {SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_FromIPRange),ServerIP);
				  }
				else if(FromIPRange.equals("*"))
				  { String[] arr = ServerIP.split("\\.");
					String newip = FromIPRange;
					arr[3] = newip;
					String modifiedIP = arr[0] + "." + arr[1] + "." + arr[2] + "." + arr[3];
					System.out.println("*************"+modifiedIP);
					SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_FromIPRange),modifiedIP);
				  }
				 else
				  { String modifiedIP= createIpAddress(ServerIP,FromIPRange);
					System.out.println("*************modifiedIP*************"+modifiedIP);
					SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_FromIPRange),modifiedIP);
				  }
			  
				
				if (ToIPRange.isEmpty())
                    {SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ToIPRange),ServerIP);
				    }
				else if(ToIPRange.equals("*"))
				  { String[] arr = ServerIP.split("\\.");
					String newip = ToIPRange;
					arr[2] = newip;
					arr[3] = newip;
					String modifiedIP = arr[0] + "." + arr[1] + "." + arr[2] + "." + arr[3];
					System.out.println("*************"+modifiedIP);
					SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ToIPRange),modifiedIP);
				  }
				 else 
				  { String modifiedIP= createIpAddress(ServerIP,ToIPRange);
					System.out.println("*************modifiedIP*************"+modifiedIP);
					SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ToIPRange),modifiedIP);
				  }
			
               	SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.REMOVE_PLUGIN_OKBUTTON));
               	SeleniumLib.waitSeconds(6);

				 if(ConditionName.contains("multiple"))
				   	  {  
				   		MultipleIPFrom = testData.getString("MultipleIP4RangeSupport."+ evalId + ".MultipleIPFrom");
				   		oamLogger.log(Level.INFO, " setIP4RangeCondition", "8. MultipleIPFrom = "+ MultipleIPFrom);
				   		MultipleIPTo = testData.getString("MultipleIP4RangeSupport." + evalId + ".MultipleIPTo");
				   		oamLogger.log(Level.INFO, " setIP4RangeCondition", "9. MultipleIPTo = "+ MultipleIPTo);
					
				   		String modifiedIP1= createIpAddress(ServerIP,MultipleIPFrom);
				   		System.out.println("*************modifiedIP1*************"+modifiedIP1);
				   		oamLogger.log(Level.INFO, " setIP4RangeCondition", "9. MultipleIPTo = "+ MultipleIPTo);
				   		
				   		String modifiedIP2= createIpAddress(ServerIP,MultipleIPTo);
				   		System.out.println("*************modifiedIP2*************"+modifiedIP2);
				   		oamLogger.log(Level.INFO, " setIP4RangeCondition", "9. MultipleIPTo = "+ MultipleIPTo);
				   		
				   		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD));
				   		SeleniumLib.waitSeconds(5);
				   		SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_FromIPRange),modifiedIP1);
				   		SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ToIPRange),modifiedIP2);
				   		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.REMOVE_PLUGIN_OKBUTTON));
				   		SeleniumLib.waitSeconds(8);
				   	 }
				 else if(ConditionName.contains("Allow & Deny"))
				 {
					 
					    addCondition("IPDeny Condition",ConditionType);
					    SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD));
			            SeleniumLib.waitSeconds(5);
					    String modifiedIP= createIpAddress(ServerIP,"-2");
					    System.out.println("*************modifiedIP*************"+modifiedIP);
					    SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_FromIPRange),modifiedIP);
					    String modifiedIP1= createIpAddress(ServerIP,"2");
					    System.out.println("*************modifiedIP*************"+modifiedIP1);
					    SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ToIPRange),modifiedIP1);	
					    SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.REMOVE_PLUGIN_OKBUTTON));	
				 }
				 
				  SeleniumLib.waitSeconds(5);
				  ResourceURL = agentproto + "://" + ServerIP + ":" + agentport + "/index.html";
				  System.out.println("*******************ResourceURL********************"+ ResourceURL);
				  logOutUrl = agentproto + "://" + ServerIP + ":" + agentNAPPort + "/oam/server/logout";
				  System.out.println("*******************logOutUrl********************"+ logOutUrl);
				  if(DenyAccess.equalsIgnoreCase("Allow"))
			        {
					  SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
					  addAllowRule(ConditionName,"IP Range");
					  SeleniumLib.waitSeconds(3);
			          SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
			          SeleniumLib.waitSeconds(3);
			          LoginBrowser(ResourceURL, logOutUrl, expectedResult);
			          SeleniumLib.waitSeconds(3);
			          removeAllowRule(ConditionName,"IP Range");
			          SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
			        }
				  else if(DenyAccess.equalsIgnoreCase("Allow & Deny"))
				  {
					    SeleniumLib.waitSeconds(2);
					    SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
						addDenyRule(ConditionName,"IP Range");
						SeleniumLib.waitSeconds(2);
						addAllowRule(ConditionName,"IP Range");
						SeleniumLib.waitSeconds(2);
					    SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
					    SeleniumLib.waitSeconds(2);
				        LoginBrowser(ResourceURL, logOutUrl, expectedResult);
				        SeleniumLib.waitSeconds(2);
				        removeAllowRule(ConditionName,"IP Range");
				        removeDenyRule(ConditionName,"IP Range");
				        SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
				  }
			      else
			        {
			    	  SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
			           addDenyRule(ConditionName,"IP Range");
			           SeleniumLib.waitSeconds(3);
				       SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
				       LoginBrowser(ResourceURL, logOutUrl, expectedResult);
				       SeleniumLib.waitSeconds(3);
				       removeDenyRule(ConditionName,"IP Range");
				       SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
			        }
				  SeleniumLib.waitSeconds(3);
				  SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS));
               }
			
			 SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_RESOUCES_DELETE_BUTTON));
			 SeleniumLib.waitSeconds(3);
			 if(DenyAccess.equalsIgnoreCase("Allow & Deny"))
			 {
				 SeleniumLib.clickWaitUntilBusy(By.xpath("//td[text()='valid condition with Allow & Deny details']"));
				 SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_RESOUCES_DELETE_BUTTON)); 
			 }
			 SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
			 System.out.println("********************** TC passed ********************");
			CommonLib.logout(adminUserName);
			SeleniumLib.waitSeconds(5);
            SeleniumLib.closeSelenium();
            SeleniumLib.stopSelenium();
	    	SeleniumLib.waitSeconds(5);
          }
          catch (Exception e) {
			oamLogger.log(Level.SEVERE, "setIP4RangeCondition","Got Exception " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}
	
   @Parameters({ "TestName", "EvalId" })
	@Test(groups = { "embedded", "embedded_sec", "s1ds", "s1ds_sec", "ad",
			"ad_sec", "edir", "edir_sec", "jdbc", "jdbc_sec", "oid", "oid_sec",
			"ovd", "ovd_sec", "create" })
	public void setIdentityCondition(String testName, String evalId)throws Exception {
		try {
			ServerIP = getIPAddress();
			System.out.println("************ServerIP************"+ ServerIP);
			SeleniumLib.waitSeconds(5);
			oamLogin();
			addCondition(ConditionName,ConditionType);
			SeleniumLib.waitSeconds(5);
			SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD));
            SeleniumLib.waitSeconds(5);
            
			String modifiedIP= createIpAddress(ServerIP,FromIPRange);
		    System.out.println("*************modifiedIP*************"+modifiedIP);
		    SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_FromIPRange),modifiedIP);
	  
		    modifiedIP= createIpAddress(ServerIP,ToIPRange);
		    System.out.println("*************modifiedIP*************"+modifiedIP);
		    SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ToIPRange),modifiedIP);
	  
            SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.REMOVE_PLUGIN_OKBUTTON));
            SeleniumLib.waitSeconds(5);
 
	        SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_RESPONSES_ADD));
			SeleniumLib.waitSeconds(5);
		    SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_NAME),Identity);
			SeleniumLib.waitSeconds(2);
            select = SeleniumLib.getDropDownList(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_RESPONSES_TYPE));
			SeleniumLib.selectListElement(select,"Identity");
            SeleniumLib.waitSeconds(2);
            SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD_BUTTON));
            SeleniumLib.waitSeconds(3);
            
            IdentityConditionAttribute(StoreName,EntityType,EntityName);
 		    SeleniumLib.waitSeconds(5);
 		    
 		    if(ConditionName.contains("Temporal"))
 		    { 
 		    	DateFormat dateformat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
 				Date date =new Date();
 				String date1=dateformat.format(date);
 				System.out.println(date1);
 				String[] ss = date1.split(" ");
 				String Starttime=ss[1];
 				System.out.println("*****Starttime*******"+Starttime);
 				String[] ss2 = Starttime.split(":");
 				String ss3=ss2[0];
 				int Endtime=Integer.parseInt(ss3);
 				Endtime=Endtime+1;
 				String endtime=Endtime+":"+ss2[1];
 				System.out.println("*****endtime*******"+endtime);
 		    
 				Calendar calendar = Calendar.getInstance();
 				Date date11 = calendar.getTime();
 				String dayss=(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date11.getTime()));
 				System.out.println(dayss);
 				
 		        addCondition("Temporal Condition","Temporal");
 		    	SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_NAME),Starttime);
 		    	SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_NAME),endtime); 		    	
 		    	SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
 		    	SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.REMOVE_PLUGIN_OKBUTTON));
 				//SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD_Selected));
 		    }
 		    	
 		     
            ResourceURL = agentproto + "://" + ServerIP + ":" + agentport + "/index.html";
	        System.out.println("*******************ResourceURL********************"+ ResourceURL);
	        logOutUrl = agentproto + "://" + ServerIP + ":" + agentNAPPort + "/oam/server/logout";
	        System.out.println("*******************logOutUrl********************"+ logOutUrl);
	       
	       if(DenyAccess.equalsIgnoreCase("Allow"))
	        {
	    	SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
	    	SeleniumLib.waitSeconds(2);
	        addAllowRule(ConditionName,"IP Range");
	        SeleniumLib.waitSeconds(2);
	        addDenyRule(Identity,"Identity");//check this condition
   		    SeleniumLib.waitSeconds(2);
	        SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
	        LoginBrowser(ResourceURL, logOutUrl, expectedResult);
	        SeleniumLib.waitSeconds(2);
	        removeAllowRule(ConditionName,"IP Range");
	        removeDenyRule(Identity,"Identity");
   		    SeleniumLib.waitSeconds(2);
   		    SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
	        }
	       
	      else if((DenyAccess.equalsIgnoreCase("Allow & Allow")))
	      {
	    	 SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
	    	 SeleniumLib.waitSeconds(2);
       		 addAllowRule(Identity,"Identity");
       		 SeleniumLib.waitSeconds(2);
       		 addAllowRule(ConditionName,"IP Range");
	         SeleniumLib.waitSeconds(2);
	         SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
	         LoginBrowser(ResourceURL, logOutUrl, expectedResult);
	         SeleniumLib.waitSeconds(2);
	         removeAllowRule(ConditionName,"IP Range");
	         SeleniumLib.waitSeconds(2);
	         removeAllowRule(Identity,"Identity");
    		 SeleniumLib.waitSeconds(2);
    		 SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
	      }
	        
	      else if((DenyAccess.equalsIgnoreCase("Deny & Deny")))
       	    {
	        	
	    	  SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
	    	  SeleniumLib.waitSeconds(2);
	    	    addDenyRule(Identity,"Identity");//check this condition
       		    SeleniumLib.waitSeconds(2);
       		    addDenyRule(ConditionName,"IP Range");//check this condition
	        	SeleniumLib.waitSeconds(2);
	        	SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
		        LoginBrowser(ResourceURL, logOutUrl, expectedResult);
		        SeleniumLib.waitSeconds(5);
		        removeDenyRule(ConditionName,"IP Range");
		        SeleniumLib.waitSeconds(2);
		        removeDenyRule(Identity,"Identity");
	            SeleniumLib.waitSeconds(2);
	            SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
	         }
		       
	      else if(DenyAccess.contains("Temporal denied"))
		     {
	    	    SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
	    	    SeleniumLib.waitSeconds(2);
	    	    addAllowRule(Identity,"Identity");
		        SeleniumLib.waitSeconds(2);
		        addAllowRule(ConditionName,"IP Range");
		        SeleniumLib.waitSeconds(2);
		        addDenyRule("Temporal Condition","Temporal");//check this condition
		        SeleniumLib.waitSeconds(2);
		        SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
		        LoginBrowser(ResourceURL, logOutUrl, expectedResult);
		        SeleniumLib.waitSeconds(2);
		        removeAllowRule(ConditionName,"IP Range");
		        SeleniumLib.waitSeconds(2);
		        removeAllowRule(Identity,"Identity");
	    		SeleniumLib.waitSeconds(2);
			    SeleniumLib.waitSeconds(2);
	    		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
		        
		        
		     }
	      else if(DenyAccess.contains("Temporal Allowed"))
		     {
	    	    SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
	    	    SeleniumLib.waitSeconds(2);
	    	    addAllowRule(Identity,"Identity");
		        SeleniumLib.waitSeconds(2);
		        addAllowRule(ConditionName,"IP Range");
		        SeleniumLib.waitSeconds(2);
		        addAllowRule("Temporal Condition","Temporal");
		        SeleniumLib.waitSeconds(2);
		        SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
		        LoginBrowser(ResourceURL, logOutUrl, expectedResult);
		        SeleniumLib.waitSeconds(2);
		        removeAllowRule(ConditionName,"IP Range");
		        SeleniumLib.waitSeconds(2);
		        removeAllowRule(Identity,"Identity");
	    		SeleniumLib.waitSeconds(2);
	    		removeAllowRule("Temporal Condition","Temporal");
			    SeleniumLib.waitSeconds(2);
			    SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
		     }
		      
		        else
		        {   SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
		            SeleniumLib.waitSeconds(2);
		        	addDenyRule(ConditionName,"IP Range");//check this condition
		        	SeleniumLib.waitSeconds(2);
			        addAllowRule(Identity,"Identity");
			        SeleniumLib.waitSeconds(2);
			        SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
			        LoginBrowser(ResourceURL, logOutUrl, expectedResult);
			        SeleniumLib.waitSeconds(5);
			        removeDenyRule(ConditionName,"IP Range");
			        removeAllowRule(Identity,"Identity");
			        SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
			        
		        }
	        
	        Thread.sleep(5000);
	        SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS));
	        
	        List<WebElement> liElements = driver.findElements(By.xpath("//table[@id='pt1:_d_reg:region3:1:sms2::leadUl']/tr"));
			 for(WebElement liItem:liElements)
		        {
		         System.out.println(liItem.getText());
			    if(!(liItem.getText().equalsIgnoreCase("True")))
	             {
			    	SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_RESOUCES_DELETE_BUTTON));
			    	 Thread.sleep(5000);
	             }
		       }
	        
	        
	      /*  SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_RESOUCES_DELETE_BUTTON));
	        Thread.sleep(5000);
	        
	        if(ConditionName.contains("valid condition"))
 		    {
            SeleniumLib.clickWaitUntilBusy(By.xpath("//label[contains(text(),'"+ConditionName+"')]"));
    		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_RESOUCES_DELETE_BUTTON)); 
    		Thread.sleep(5000);
 		    }
	        else
 		    
    		SeleniumLib.clickWaitUntilBusy(By.xpath("//label[contains(text(),'Temporal Condition')]"));
    		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_RESOUCES_DELETE_BUTTON));
 		    }*/
	         
            SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTHORIZATION_RESPONSES_APPLY_BUTTON));
            System.out.println("********************** TC passed ********************");
	        CommonLib.logout(adminUserName);
	        SeleniumLib.waitSeconds(5);
            SeleniumLib.closeSelenium();
            SeleniumLib.stopSelenium();
	        SeleniumLib.waitSeconds(5);
         }
        catch(Exception e) 
            {
            oamLogger.log(Level.SEVERE, "setIdentityCondition","Got Exception " + e.getMessage());
            e.printStackTrace();
            throw e;
            }
    }

    /**
	 * This method is used for configuring the Condition Attribute in the Authz Policy Condition.
	 * 
	 * @param StoreName
	 *                  - Store name needed while adding the conditions details in the Authz policies in the appDomain.     
	 *                      
	 * @param EntityType
	 *                  - Entity Type needed while adding the conditions details in the Authz policies in the appDomain.
	 *                         
	 * @param EntityName
	 *                  - Entity Name needed while adding the conditions details in the Authz policies in the appDomain.
	 *                                        
	 * @throws Exception 
	 * 
	 */
	public static void IdentityConditionAttribute(String StoreName,String EntityType,String EntityName) throws Exception{
		
		try {
			oamLogger.log(Level.INFO, "******** Condition Attribute Config Started,","Condition Attribute will be added in the Authz Policy********");
			SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD_BUTTON_Identity));
			SeleniumLib.waitSeconds(5);
			SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD_BUTTON_IdentityDropdown));
			SeleniumLib.waitSeconds(5);
			
			Select select = SeleniumLib.getDropDownList(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD_BUTTON_StoreName));
			System.out.println("***********StoreName****************"+StoreName);
			SeleniumLib.selectListElement(select,StoreName);
			SeleniumLib.waitSeconds(2);
			select = SeleniumLib.getDropDownList(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD_BUTTON_EntityType));
			SeleniumLib.selectListElement(select,EntityType);
			SeleniumLib.waitSeconds(2);
			System.out.println("***********EntityType****************"+EntityType);
			
	        SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD_BUTTON_EntityName),EntityName);
			SeleniumLib.waitSeconds(2);
			SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.SEARCH_BUTTON));
			SeleniumLib.waitSeconds(10);
			SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD_User_Row));
			SeleniumLib.waitSeconds(5);
			SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.REMOVE_PLUGIN_OKBUTTON));
		
			SeleniumLib.waitSeconds(5);
			
			oamLogger.log(Level.INFO, "******** Condition Attribute Config Done,","Condition Attribute is Added in the Authz Policy********");
						
		} catch (FIATException e) {
			oamLogger.log(Level.SEVERE, "Configuration Conditions Attribute AuthZ Policy",
					"Got FIATException " + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			oamLogger.log(Level.SEVERE, "Configuration Conditions Attribute AuthZ Policy",
					"Got IOException " + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (InterruptedException e) {
			oamLogger.log(Level.SEVERE, "Configuration Conditions Attribute AuthZ Policy",
					"Got InterruptedException " + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			oamLogger.log(Level.SEVERE, "Configuration Conditions Attribute AuthZ Policy",
					"Got Exception " + e.getMessage());
			e.printStackTrace();
			throw e;
		}  		
	}
	
	/**
	 * This method add Rule in Allow 
	 * block
	 *                                        
	 * @throws Exception 
	*/	
	public void addAllowRule(String ConditionName, String param) throws Exception
	{
		IPRange=ConditionName.concat(" "+"("+param+")");
		String SELECT_CONDITION = "//label[contains(text(),'"+IPRange+"')]";
		SeleniumLib.waitSeconds(5);
		SeleniumLib.clickWaitUntilBusy(By.xpath(SELECT_CONDITION));
		SeleniumLib.waitSeconds(2);
		SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB_ARROW));
	}
	
	/**
	 * This method remove Rule in Allow
	 * block 
	 * @throws Exception 
	 */
	public void removeAllowRule(String ConditionName,String param) throws Exception
	{
        IPRange=ConditionName.concat(" "+"("+param+")");
	    String SELECT_CONDITION = "//label[contains(text(),'"+IPRange+"')]";
	    SeleniumLib.waitSeconds(5);
		SeleniumLib.doubleClick(By.xpath(SELECT_CONDITION));
		if(SeleniumLib.isElementVisible(By.xpath("//label[contains(text(),'TRUE (True)')]")))
		   {
			 SeleniumLib.doubleClick(By.xpath("//label[contains(text(),'TRUE (True)')]"));
		   }
	}

	/**
	 * This method add Rule in deny
	 * block
	 *  
	 *  @throws Exception 
	*/
	public void addDenyRule(String ConditionName,String param) throws Exception
	  {
		 SeleniumLib.waitSeconds(3);
		 IPRange=ConditionName.concat(" "+"("+param+")");
		 String SELECT_CONDITION = "//label[contains(text(),'"+IPRange+"')]";
		
		List<WebElement> liElements = driver.findElements(By.xpath("//ul[@id='pt1:_d_reg:region3:1:sms2::leadUl']/li"));
		 for(WebElement liItem:liElements)
	        {
	        System.out.println(liItem.getText());
		    if(liItem.getText().equalsIgnoreCase(IPRange))
             {
			    SeleniumLib.clickWaitUntilBusy(By.xpath(SELECT_CONDITION));
			    SeleniumLib.waitSeconds(2);
				SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB_ARROW));
				break;
             }
	       }

		//String SELECT_CONDITION1= "//*[@id='pt1:_d_reg:region3:1:sms2::leadUl']/li[1]";
 		//SeleniumLib.clickWaitUntilBusy(By.xpath(SELECT_CONDITION1));
 		//SeleniumLib.waitSeconds(2);
		//SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB_ARROW));
	  }

	/**
	 * This method remove Rule in deny
	 * block
	 * 
	 * @throws Exception 
	*/
	public void removeDenyRule(String ConditionName,String param) throws Exception
	{  IPRange=ConditionName.concat(" "+"("+param+")");
	    String SELECT_CONDITION= "//*[@id='pt1:_d_reg:region3:1:sms2::trailUl']/li/label";
        SeleniumLib.doubleClick(By.xpath(SELECT_CONDITION));
	  }
	
		
	
	/**
	 * This method is used for configuring the Condition Name and Type in the
	 * Authz Policy.
	 * 
	 * @param conditionType
	 *            - Condition Type needed while adding the conditions in the
	 *            Authz policy of appDomain.
	 * 
	 * @param conditionName
	 *            - Condition Name needed while adding the conditions in the
	 *            Authz policy of appDomain.
	 * 
	 * @throws Exception
	 * 
	 */
    public void addCondition(String ConditionName,String ConditionType) throws Exception {
       try {
			oamLogger.log(Level.INFO, "******** Condition Config Started,","Condition Type will be added in the Authz Policy********");

			if((ConditionName.startsWith("valid")) && (DenyAccess.contains("Allow")))
			  {
				SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.RULES_TAB));
				if(SeleniumLib.isElementVisible(By.xpath("//label[contains(text(),'TRUE (True)')]")))
		               {
			              SeleniumLib.doubleClick(By.xpath("//label[contains(text(),'TRUE (True)')]"));
			              SeleniumLib.waitSeconds(5);
		               }
			   }
		    SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS));
			SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_RESPONSES_ADD));
			SeleniumLib.waitSeconds(5);
            SeleniumLib.type(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_NAME),ConditionName);
			SeleniumLib.waitSeconds(1);
            Select select = SeleniumLib.getDropDownList(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_RESPONSES_TYPE));
			SeleniumLib.selectListElement(select, ConditionType);
            SeleniumLib.waitSeconds(1);
            SeleniumLib.clickWaitUntilBusy(By.xpath(SystemConfiguration.APP_DOMAIN_AUTZN_CONDITIONS_ADD_BUTTON));
            oamLogger.log(Level.INFO, "******** Condition Config Done,","Condition Type is Added in the Authz Policy********");
           }catch (FIATException e) {
			 oamLogger.log(Level.SEVERE,"Configuration Conditions AuthZ Policy","Got FIATException " + e.getMessage());
			 e.printStackTrace();
			 throw e;
		   }catch (IOException e) {
			 oamLogger.log(Level.SEVERE,"Configuration Conditions AuthZ Policy", "Got IOException "+ e.getMessage());
			 e.printStackTrace();
			 throw e;
		   }catch (InterruptedException e) {
			 oamLogger.log(Level.SEVERE,"Configuration Conditions AuthZ Policy","Got InterruptedException " + e.getMessage());
			 e.printStackTrace();
			 throw e;
		   }catch (Exception e) {
			 oamLogger.log(Level.SEVERE,"Configuration Conditions AuthZ Policy", "Got Exception "+ e.getMessage());
			 e.printStackTrace();
			 throw e;
		   }
	}

	/**
	 * This method deletes all the added rules from advanced rules tab in
	 * oamconsole policies
	 * 
	 * @return void
	 */
    @AfterSuite(groups = { "embedded", "embedded_sec", "s1ds", "s1ds_sec",
			"ad", "ad_sec", "edir", "edir_sec", "jdbc", "jdbc_sec", "oid",
			"oid_sec", "ovd", "ovd_sec" })
	public void tearDown() throws Exception {
    	SeleniumLib.waitSeconds(5);
        SeleniumLib.closeSelenium();
        SeleniumLib.stopSelenium();     
        SeleniumLib.waitSeconds(5);
	}
