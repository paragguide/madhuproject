package core;

import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import utility.TestConfig;
import utility.monitoringMail;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterTest;

public class Page
{
	public WebDriver driver = null;
	public Properties p =null;
	 public ExtentTest test=null;
		
		public ExtentReports report=null;

  @Parameters({"browser","url","screenshotname"})	
  @BeforeMethod
  public void openBrowser(String browser, String url, String screenshotname) throws Exception
  {
	  if(browser.equals("chrome"))
		{
			System.setProperty("webdriver.chrome.driver","D:\\browser-drivers\\chromedriver.exe");
			
			driver = new ChromeDriver();   // class return interface ref
			
		}
		else if(browser.equals("firefox"))
		{
			System.setProperty("webdriver.gecko.driver","D:\\browser-drivers\\geckodriver.exe");
			
			driver = new FirefoxDriver();
			
		}
		
		driver.navigate().to(url);  // open page to test
		
		// implicit wait
		
		driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS); 
		
		driver.manage().window().maximize();  // full screen size
		
		// screen shot
		
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); // store file to temprary location
		//Now you can do whatever you need to do with it, for example copy somewhere download org.apache.commons.io.FileUtils class API set classpath and use this class to copy.
		String screenshotpath = System.getProperty("user.dir")+"\\src\\test\\java\\screenshot\\"+screenshotname+".jpeg";
		
		FileUtils.copyFile(scrFile, new File(screenshotpath));
	
  }

  @AfterMethod
  public void closeBrowser() 
  {
	  driver.quit();
  }

  @Parameters({"filename"})
  @BeforeClass
  public void readXPath(String filename) throws Exception
  {
	  
	  // reading xpath
		// reading xpath from properties file
       
	     p = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\"+filename+".properties");
		p.load(fis);
  }

  
  @Parameters({"reportname"})
  @BeforeTest
  public void createReport(String reportname) 
  {
	// report initialize
				report = new ExtentReports(System.getProperty("user.dir")+"\\src\\test\\java\\report\\"+reportname+".html");
				  
				  test = report.startTest("ResolutionTest");
				  
  }

  @AfterTest
  public void afterTest()
  {
	  report.endTest(test);   // compulsory 
	  
	  report.flush();  // compulsory
	  
	  
	  // mail send
	  
	  monitoringMail m = new monitoringMail();
	  try {
		m.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, TestConfig.messageBody, TestConfig.attachmentPath, TestConfig.attachmentName);
	System.out.println("mail sent");
	  } catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

}
