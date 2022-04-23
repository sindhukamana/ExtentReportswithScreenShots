package com.auto.crmpacakge;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import freemarker.template.SimpleDate;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CrmTest {
	static WebDriver driver;
	//static ExtentHtmlReporter reports;
    static ExtentReports extent;
    static ExtentTest extentTest;
	@BeforeTest
	public void Extentmethod()
	{
		//reports=new ExtentHtmlReporter("ExtentsReports/output.html");
		System.out.println("@BeforeTest");
		extent = new ExtentReports(System.getProperty("user.dir")+"/ExtentReports/ExtentReport.html", true);
		 
	     extent.addSystemInfo("username","sindhu");
	     extent.addSystemInfo("projectname","extentreportswithscreenshort");
	     extent.addSystemInfo("testing","automationscript");
	     
	     
	}
	@AfterTest
	public void endreport()
	{
		System.out.println("@AfterTest");
		extent.flush();
		extent.close();
		
	}
	public static String getscreenshot(WebDriver driver,String screenshotname)
	{
		String datename=new SimpleDateFormat("yyyymmddhhmmss").format(new Date(0));
		
		
		 TakesScreenshot screenshot=(TakesScreenshot)driver; 
		 File srcfile=screenshot.getScreenshotAs(OutputType.FILE);
		
		 String destination= System.getProperty(("user.dir") + "/FailedTestsScreenshots/" + screenshotname + datename
					+ ".png");   
		 File finalDestination = new File(destination);
		 
         try {
			FileUtils.copyFile(srcfile, finalDestination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     	return destination;
	}
	@BeforeMethod
	public void setup()
	{
		System.out.println("@BeforeMethod");
		WebDriverManager.chromedriver().setup();
		//disable automation popup
	 ChromeOptions  option=new ChromeOptions();
	  option.setExperimentalOption("excludeSwitches", new String[] { "enable-automation" });
	  //disable UI based pop-ups
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		option.setExperimentalOption("prefs", prefs);
		driver=new ChromeDriver(option);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://www.freecrm.com/");
		
		
	}
	
	@Test
	public void freecrmMethod()
	{
		System.out.println("@Test");
		String title=driver.getTitle();
		System.out.println(title);
		Assert.assertEquals(title, "Free CRM software for customer relationship management, sales, marketing campaigns and support123.");
		
		
	}
	
   
	@AfterMethod
	public void teardown(ITestResult result)
	
	{
		System.out.println("@AfterMethod");
		if(result.getStatus()==ITestResult.FAILURE)
		{
			extentTest.log(LogStatus.FAIL, "testCase is failed"+result.getName());
			extentTest.log(LogStatus.FAIL, "testcae iis failed"+result.getThrowable());
			String screenshotpath=CrmTest.getscreenshot(driver, result.getName());
			extentTest.log(LogStatus.FAIL,"testcase is fauiled"+extentTest.addScreenCapture(screenshotpath));
		}
			
		else if( result.getStatus()==ITestResult.SKIP)
		{
			extentTest.log(LogStatus.SKIP, "testcase is skipped");
			
		}
		else if(result.getStatus()==ITestResult.SUCCESS)
		{
			extentTest.log(LogStatus.PASS, "testcase is passed");
		}
		extent.endTest(extentTest);
		driver.quit();
		
	}
	
}
