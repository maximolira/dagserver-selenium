package dagserver.pom.authenticated;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DagTabPanelView {

	protected WebDriver driver;
	protected String name;
	
	public DagTabPanelView(WebDriver driver) {
		this.driver = driver;
		WebDriverWait wait2 = new WebDriverWait(driver,5);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(@id,'props-collapser')]")));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        List<String> arr = (List<String>) js.executeScript("let arr = [];$('.tabpill').each(function () {arr.push($(this).text());}); return arr;");
        this.name = arr.get(arr.size() - 1);
        driver.findElement(By.xpath("//*[@id=\"canvas-"+this.name+"\"]/a")).click();
        
        WebDriverWait wait3 = new WebDriverWait(driver,5);
        wait3.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"props-collapser-"+this.name+"\"]")));
        driver.findElement(By.xpath("//*[@id=\"props-collapser-"+this.name+"\"]")).click();
	}
	public void activate() throws InterruptedException {
		WebDriverWait wait2 = new WebDriverWait(driver,5);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"canvas-"+this.name+"\"]/a")));
		driver.findElement(By.xpath("//*[@id=\"canvas-"+this.name+"\"]/a")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"props-collapser-"+this.name+"\"]")).click();
		Thread.sleep(1000);
	}
	
	public void goToStartTab() throws InterruptedException {
		driver.findElement(By.xpath("//*[@id=\"page-wrapper\"]/div/div[2]/div/div/div[2]/div[1]/div[1]/div/input")).click();
		Thread.sleep(3000);
		WebDriverWait wait2 = new WebDriverWait(driver,10);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"canvas-"+this.name+"\"]/a")));
		driver.findElement(By.xpath("//*[@id=\"canvas-"+this.name+"\"]/a")).click();
		Thread.sleep(1000);
	}
	

	public void setData(String dagname, String group, String cronexpr) throws InterruptedException {
		
		WebDriverWait wait2 = new WebDriverWait(driver,5);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='daggroupinput-"+this.name+"']")));
        driver.findElement(By.xpath("//*[@id='daggroupinput-"+this.name+"']")).clear();
        driver.findElement(By.xpath("//*[@id='daggroupinput-"+this.name+"']")).sendKeys(group);
        driver.findElement(By.xpath("//*[@id='dagcroninput-"+this.name+"']")).clear();
        driver.findElement(By.xpath("//*[@id='dagcroninput-"+this.name+"']")).sendKeys(cronexpr);
        driver.findElement(By.xpath("//*[@id='dagnameinput-"+this.name+"']")).clear();
        driver.findElement(By.xpath("//*[@id='dagnameinput-"+this.name+"']")).sendKeys(dagname);
	}
	public void save(String dagname) {
		if(dagname != null) {
			this.name = dagname;	
		}
		WebDriverWait wait2 = new WebDriverWait(driver,5);
		wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"collapseOne"+this.name+"-det\"]/div[4]/button[1]")));
		driver.findElement(By.xpath("//*[@id=\"collapseOne"+this.name+"-det\"]/div[4]/button[1]")).click();
		//wait2.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"collapseOneTEST_DAG-det\"]/div[4]/button[1]")));
		//driver.findElement(By.xpath("//*[@id=\"collapseOneTEST_DAG-det\"]/div[4]/button[1]")).click();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
