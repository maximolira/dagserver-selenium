package dagserver.test.operators;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import dagserver.pom.authenticated.AuthenticatedView;
import dagserver.pom.authenticated.EditDesignView;
import dagserver.pom.authenticated.JobsView;
import dagserver.pom.login.LoginForm;
import dagserver.utils.BaseTest;

public class FtpOperatorUploadUseCaseTest extends BaseTest {
	
	@Test
    @Parameters({"url","username","pwd","jarname","dagname","group","cronexpr","step1","file"})
	public void createDagDesignWithStepTest(String url,String username,String pwd,String jarname,String dagname,String group,String cronexpr,String step1,String file,ITestContext context) throws Exception {
		driver.get(url);
    	LoginForm login = new LoginForm(driver);
    	if(login.login(username, pwd)) {
    		AuthenticatedView autenticado = new AuthenticatedView(driver);
    		JobsView jobs = autenticado.goToJobs();
    		jobs.selectDesigndTab();
    		jobs.searchUncompiled(jarname);
    		if(jobs.existDesign(jarname)) {
    			jobs.deleteDesign(jarname);
    		}
    		var newform = jobs.newJobForm();
    		newform.setName(jarname);
    		newform.createCronDag(dagname, group, cronexpr);
    		newform.addStep(dagname,step1,"main.cl.dagserver.infra.adapters.operators.FTPOperator");    		
    		var host = this.getInfrastructure(this.getClass().getCanonicalName(), "host");
    		var port = this.getInfrastructure(this.getClass().getCanonicalName(), "port");
    		var ftpUser = this.getInfrastructure(this.getClass().getCanonicalName(), "ftpUser");
    		var ftpPass = this.getInfrastructure(this.getClass().getCanonicalName(), "ftpPass");
    		var params = newform.selectStage(step1);
    		params.sendParameter("host", host, "input");
    		params.sendParameter("port", port, "input");
    		params.sendParameter("ftpUser", ftpUser, "input");
    		params.sendParameter("ftpPass", ftpPass, "input");
    		
    		String filepath = "";
    		if(this.application.getProperty("driver.mode").equals("DOCKER")) {
    			filepath = "//statics//"+file;
    		} else {
    			filepath = Paths.get("statics").toAbsolutePath().toString() + "\\" +file;
    		}
    		
    		params.sendScript("upload /"+file+" "+filepath.replaceAll("\\\\", "/"));
    		params.save();
    		jobs.selectDesigndTab();
    		this.writeEvidence(context,"createDagDesignWithStepTest","OK",By.xpath("/html/body"));
    		assertTrue(true);
    	} else {
			this.writeEvidence(context,"createDagDesignWithStepTest","ERROR",By.xpath("/html/body"));
			assertTrue(false);
		}
	}
	
	
	@Test
    @Parameters({"url","username","pwd","jarname","dagname","step1"})
	public void editDagDesignWithStepTest(String url,String username,String pwd,String jarname,String dagname, String step1,ITestContext context) throws InterruptedException, IOException {
		driver.get(url);
    	LoginForm login = new LoginForm(driver);
    	if(login.login(username, pwd)) {
    		AuthenticatedView autenticado = new AuthenticatedView(driver);
    		JobsView jobs = autenticado.goToJobs();
    		jobs.selectDesigndTab();
    		jobs.searchUncompiled(jarname);
    		if(jobs.existDesign(jarname)) {
    			EditDesignView editor = jobs.editDesign(jarname);
    			editor.selectDag(dagname);
    			var result = editor.execute();
				var contentPrc = result.getOutputXcom(step1);
				this.writeEvidence(context,"editDagDesignWithStepTest","OK",By.xpath("/html/body"));
		    	assertNotNull(contentPrc);
    		} else {
    			this.writeEvidence(context,"editDagDesignWithStepTest","ERROR",By.xpath("/html/body"));
				assertTrue(false,"problema en editor?");
    		}
    	} else {
			this.writeEvidence(context,"editDagDesignWithStepTest","ERROR",By.xpath("/html/body"));
			assertTrue(false);
		}
	}
	
}
