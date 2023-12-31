package dagserver.pom.authenticated;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dagserver.utils.SeleniumTestException;

public class AuthenticatedView {
	
	protected WebDriver driver;
	private By logoutBtn = By.xpath("//*[@id=\"logout-btn\"]");
	private By credentialsLink = By.xpath("//*[@id=\"side-menu\"]/li[3]/ul/li/a");
	private By propsLink = By.xpath("//*[@id=\"side-menu\"]/li[2]/ul/li[1]/a");
	private By jobsLink = By.xpath("//*[@id=\"side-menu\"]/li[2]/ul/li[2]/a");
	private By channelLink = By.xpath("//*[@id=\"side-menu\"]/li[1]/ul/li/a");
	
	public AuthenticatedView(WebDriver driver) {
		this.driver = driver;
	}
	public AdminCredentialsView goToCredentials() throws SeleniumTestException {
		WebDriverWait wait = new WebDriverWait(driver,3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(credentialsLink));
		driver.findElement(credentialsLink).click();
		return new AdminCredentialsView(driver);
	}
	public void logout() {
        By elem = By.xpath("//*[@id=\"wrapper\"]/nav/ul[3]/li[3]/a");
		driver.findElement(elem).click();
		WebDriverWait wait = new WebDriverWait(driver,3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutBtn));
        driver.findElement(logoutBtn).click();
        WebDriverWait wait2 = new WebDriverWait(driver,3);
        wait2.until(ExpectedConditions.invisibilityOfElementLocated(logoutBtn));
	}
	public PropertiesView goToProps() {
		WebDriverWait wait = new WebDriverWait(driver,3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(propsLink));
		driver.findElement(propsLink).click();
		return new PropertiesView(driver);
	}
	public JobsView goToJobs() {
		WebDriverWait wait = new WebDriverWait(driver,3);
		wait.until(ExpectedConditions.elementToBeClickable(jobsLink));
		driver.findElement(jobsLink).click();
		return new JobsView(driver);
	}
	public InputChannelView goToInputChannels() throws InterruptedException {
		Thread.sleep(3000);
		WebDriverWait wait = new WebDriverWait(driver,3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(channelLink));
		driver.findElement(channelLink).click();
		return new InputChannelView(driver);
	}
}
