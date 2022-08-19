package testcases;

import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import core.Page;

import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;

public class LoginTest extends Page
{
  @Test(dataProvider = "logindata")
  public void test(String n, String s)
  {
	  test.log(LogStatus.INFO, "Test Login called");
	 String uidxpath =  p.getProperty("uid");
	 String pwdxpath = p.getProperty("pwd");
	 String loginxpath = p.getProperty("login");
	  
	 driver.findElement(By.xpath(uidxpath)).sendKeys(n);
	 
	 driver.findElement(By.xpath(pwdxpath)).sendKeys(s);
	 
	 driver.findElement(By.xpath(loginxpath)).click();
	 
	 test.log(LogStatus.PASS, "form submitted");
	 
  }

  @DataProvider
  public Object[][] logindata() throws Exception {
    
	  return utility.POIConfig.getData("d:\\corejava\\Login.xlsx", "Sheet1");
  }
}
