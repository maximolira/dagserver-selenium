package dagserver.test.operators;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import dagserver.pom.authenticated.AuthenticatedView;
import dagserver.pom.authenticated.EditDesignView;
import dagserver.pom.authenticated.JobsView;
import dagserver.pom.login.LoginForm;
import dagserver.utils.BaseTest;

public class MongoDBOperatorWriteUseCaseTest extends BaseTest {
	
	@Test
    @Parameters({"url","username","pwd","jarname","dagname","group","cronexpr","step1","step2"})
	public void createDagDesignWithStepTest(String url,String username,String pwd,String jarname,String dagname,String group,String cronexpr,String step1,String step2,ITestContext context) throws Exception {
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
    		
    		newform.addStep(dagname,step1,"main.cl.dagserver.infra.adapters.operators.GroovyOperator");    		
    		var cmd = context.getCurrentXmlTest().getParameter("source");
    		var params = newform.selectStage(step1);
    		params.sendScript(cmd);
    		params.save();
    		jobs.selectDesigndTab();
    		jobs.searchUncompiled(jarname);
    		EditDesignView editor2 = jobs.editDesign(jarname);
			editor2.selectDag(dagname);
			editor2.addStep(dagname,step2,"main.cl.dagserver.infra.adapters.operators.MongoDBOperator");  
    		params = newform.selectStage(step2);
    		var mode = context.getCurrentXmlTest().getParameter("mode");
    		var timeout = context.getCurrentXmlTest().getParameter("timeout");
    		
    		var hostname = this.getInfrastructure(this.getClass().getCanonicalName(), "hostname");
    		var port = this.getInfrastructure(this.getClass().getCanonicalName(), "port");
    		var database = this.getInfrastructure(this.getClass().getCanonicalName(), "database");
    		var collection = this.getInfrastructure(this.getClass().getCanonicalName(), "collection");
    		params.sendParameter("hostname", hostname, "input");
    		params.sendParameter("timeout", timeout, "input");
    		params.sendParameter("mode", mode, "list");
    		params.sendParameter("port", port, "input");
    		params.sendParameter("database", database, "input");
    		params.sendParameter("collection", collection, "input");
    		params.sendParameter("xcom", step1,"list");
    		params.save();
    		editor2.save();
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
