package dagserver.pom.jobs;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ParamsDialog {

	protected WebDriver driver;
	
	public ParamsDialog(WebDriver driver) {
		this.driver = driver;
		WebDriverWait wait2 = new WebDriverWait(driver,3);
        wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"param-modaljardetailj\"]")));
	}

	public void close() {
		driver.findElement(By.xpath("//*[@id=\"param-modaljardetailj\"]/div[2]/div/div[3]/button[1]")).click();
		WebDriverWait wait2 = new WebDriverWait(driver,3);
        wait2.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"param-modaljardetailj\"]")));
	}
	
	public void remove() {
		driver.findElement(By.xpath("//*[@id=\"param-modalexistingj\"]/div[2]/div/div[3]/button[1]")).click();
		WebDriverWait wait2 = new WebDriverWait(driver,3);
        wait2.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[@id=\"param-modaljardetailj\"]")));
	}
}
