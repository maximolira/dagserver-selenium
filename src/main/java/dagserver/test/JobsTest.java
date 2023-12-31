package dagserver.test;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import dagserver.pom.authenticated.AuthenticatedView;
import dagserver.pom.authenticated.DependenciesView;
import dagserver.pom.authenticated.EditDesignView;
import dagserver.pom.authenticated.JobsDetailView;
import dagserver.pom.authenticated.JobsView;
import dagserver.pom.authenticated.LogsDetailView;
import dagserver.pom.jobs.ExecutionDialog;
import dagserver.pom.login.LoginForm;
import dagserver.utils.BaseTest;

public class JobsTest extends BaseTest {

	@Test
    @Parameters({"url","username","pwd"})
	void systemJobsTest(String url,String username, String pwd,ITestContext context) throws InterruptedException, IOException {
		driver.get(url);
    	LoginForm login = new LoginForm(driver);
    	if(login.login(username, pwd)) {
    		AuthenticatedView autenticado = new AuthenticatedView(driver);
    		JobsView jobs = autenticado.goToJobs();
    		jobs.selectCompiledTab();
    		if(jobs.existJob("background_system_dag") && jobs.existJob("event_system_dag")) {
    			jobs.selectOption("background_system_dag", 1);
    			JobsDetailView detail = new JobsDetailView(driver);
    			detail.selectTab();
    			var dialog = detail.selectStage("background_system_dag", "internal");
    			dialog.close();
    			jobs = autenticado.goToJobs();
    			jobs.selectCompiledTab();
    			jobs.selectOption("background_system_dag", 2);
    			LogsDetailView logdetail = new LogsDetailView(driver);
    			var data = logdetail.getActualLogs();
    			if(!data.isEmpty()) {
    					var logdata = data.get(0);
    					logdetail.deleteById(logdata.get("Id"));
    					if(!logdetail.existLog(logdata.get("Id"))) {
    						var data1 = logdetail.getActualLogs();
    						var logdata1 = data1.get(0);
    						logdetail.viewLog(logdata1.get("Id"));
    					} else {
    						assertTrue(false,"existe un problema en el despliegue de los logs");
    					}
    			}
    			jobs = autenticado.goToJobs();
    			jobs.selectCompiledTab();
    			jobs.selectOption("background_system_dag", 3);
    			new DependenciesView(driver);
    			this.writeEvidence(context,"systemJobsTest","OK",By.xpath("/html/body"));
    			assertTrue(true);
			}
    	} else {
    		this.writeEvidence(context,"systemJobsTest","ERROR",By.xpath("/html/body"));
    		assertTrue(false,"Jobs de sistema no incluidos?");
    	}
   }
	
	
	@Test
    @Parameters({"url","username","pwd","jarname","dagname","group","cronexpr","step1","step2"})
	void addDesignCronTest(String url,String username, String pwd, String jarname,String dagname,String group,String cronexpr,String step1, String step2,ITestContext context) throws InterruptedException, IOException {
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
    		var dagpanel = newform.createCronDag(dagname, group, cronexpr);
    		newform.addDummyStep(step1,dagpanel.getName());
    		var params = newform.selectStage(step1);
    		params.remove();
    		newform.addDummyStep(step2,dagpanel.getName());
    		var params2 = newform.selectStage(step2);
    		params2.close();
    		newform.save();
    		this.writeEvidence(context,"addDesignCronTest","OK",By.xpath("/html/body"));
    		assertTrue(true);
    	} else {
    		this.writeEvidence(context,"addDesignCronTest","ERROR",By.xpath("/html/body"));
    		assertTrue(false,"no login?");
    	}
	}
	@Test
    @Parameters({"url","username","pwd","jarname","dagname","group","listenerType","triggerType","nameTarget","step1","step2"})
	void addDesignListenerTest(String url,String username, String pwd, String jarname,String dagname,String group,String listenerType,String triggerType,String nameTarget,String step1, String step2,ITestContext context) throws InterruptedException, IOException {
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
    		jobs.selectCompiledTab();
    		var newform = jobs.newJobForm();
    		newform.setName(jarname);
    		var panel = newform.createListenerDag(dagname, group,listenerType,triggerType,nameTarget);
    		panel.save(dagname);
    		newform.addDummyStep(step1,panel.getName());
    		var params = newform.selectStage(step1);
    		params.remove();
    		newform.addDummyStep(step2,panel.getName());
    		var params2 = newform.selectStage(step2);
    		params2.close();
    		newform.save();
    		this.writeEvidence(context,"addDesignListenerTest","OK",By.xpath("/html/body"));
    		assertTrue(true);
    	} else {
    		this.writeEvidence(context,"addDesignListenerTest","ERROR",By.xpath("/html/body"));
    		assertTrue(false,"no login?");
    	}
	}
	@Test
    @Parameters({"url","username","pwd","jarname","dagname","newjarname","stepname","uploadfile"})
	void editDesignDagTest(String url,String username, String pwd,String jarname, String dagname, String newjarname,String stepname,String uploadfile,ITestContext context) throws InterruptedException, IOException {
		String uploadFileReal = "";
		if(this.application.getProperty("driver.mode").equals("DOCKER")) {
			uploadFileReal = "/statics/"+uploadfile;
		} else {
			uploadFileReal = Paths.get("statics").toAbsolutePath().toString() + "/" +uploadfile;	
		}
		driver.get(url);
    	LoginForm login = new LoginForm(driver);
    	if(login.login(username, pwd)) {
    		AuthenticatedView autenticado = new AuthenticatedView(driver);
    		JobsView jobs = autenticado.goToJobs();
    		jobs.selectDesigndTab();
    		jobs.searchUncompiled(jarname);
    		if(jobs.existDesign(jarname)) {
    			EditDesignView editor = jobs.editDesign(jarname);
    			if(editor.execute() == null) {
    				editor.selectDag(dagname);
    				var resultModal = editor.execute();
    				resultModal.close();
    				var renamer = editor.rename();
    				renamer.save(newjarname);
    				jobs.selectDesigndTab();
    				EditDesignView editor2 = jobs.editDesign(newjarname);
    				editor2.selectDag(dagname);
    				
    				var params = editor2.selectStage(stepname,dagname);
    				Thread.sleep(2000);
    				var result = params.test();
    				result.close();
    				jobs = autenticado.goToJobs();
    	    		jobs.selectDesigndTab();
    	    		jobs.exportDesign(newjarname);
    	    		jobs.deleteDesign(newjarname);
    	    		
    	    		jobs = autenticado.goToJobs();
    	    		jobs.selectDesigndTab();
    	    		
    	    		var importer = jobs.importJar();
    	    		importer.importNewJob(uploadFileReal);
    	    		jobs = autenticado.goToJobs();
    	    		jobs.selectDesigndTab();
    	    		jobs.compileDesign(newjarname);
    	    		this.writeEvidence(context,"editDesignDagTest","OK",By.xpath("/html/body"));
    	    		assertTrue(true);
    			} else {
    				this.writeEvidence(context,"editDesignDagTest","ERROR",By.xpath("/html/body"));
    				assertTrue(false,"problema en editor?");
    			}
    		} else {
    			this.writeEvidence(context,"editDesignDagTest","ERROR",By.xpath("/html/body"));
    			assertTrue(false,"problema en diseñador?");
    		}
    	}
	}
	
	@Test
    @Parameters({"url","username","pwd","jarname","dagname","logid","stepname"})
	void compiledDagTest(String url,String username, String pwd, String jarname,String dagname,String logid,String stepname,ITestContext context) throws InterruptedException, IOException {
		driver.get(url);
    	LoginForm login = new LoginForm(driver);
    	if(login.login(username, pwd)) {
    		AuthenticatedView autenticado = new AuthenticatedView(driver);
    		JobsView jobs = autenticado.goToJobs();
    		jobs.selectCompiledTab();
    		jobs.searchCompiled(jarname);
    		if(jobs.existJob(jarname)) {
    			jobs.selectOption(dagname, 5);
    			new DependenciesView(driver);
    			jobs = autenticado.goToJobs();
        		jobs.selectCompiledTab();
        		jobs.selectOption(dagname, 4);
        		String rv = jobs.getSchedulerActive(dagname);
        		if(Boolean.parseBoolean(rv)) {
        			jobs.selectOption(dagname, 4);
        			jobs.selectOption(dagname, 1);
        			ExecutionDialog exec = new ExecutionDialog(driver);
        			exec.close();
        			jobs.selectOption(dagname, 3);
        			LogsDetailView viewer = new LogsDetailView(driver);
        			viewer.viewLog(logid);
        			jobs = autenticado.goToJobs();
            		jobs.selectCompiledTab();
            		jobs.selectOption(dagname, 3);
            		viewer = new LogsDetailView(driver);
            		viewer.deleteById(logid);
            		jobs = autenticado.goToJobs();
            		jobs.selectCompiledTab();
            		jobs.selectOption(dagname, 3);
            		viewer = new LogsDetailView(driver);
            		viewer.deleteAll();
            		jobs = autenticado.goToJobs();
            		jobs.selectCompiledTab();
            		jobs.selectOption(dagname, 2);
            		JobsDetailView detail = new JobsDetailView(driver);
        			detail.selectTab();
        			var paramdialog = detail.selectStage(dagname, stepname);
        			paramdialog.close();
        			this.writeEvidence(context,"compiledDagTest","OK",By.xpath("/html/body"));
        			assertTrue(true);
        		} else {
        			this.writeEvidence(context,"compiledDagTest","ERROR",By.xpath("/html/body"));
        			assertTrue(false,"problema en scheduling?");
        		}
    		} else {
    			this.writeEvidence(context,"compiledDagTest","ERROR",By.xpath("/html/body"));
    			assertTrue(false,"job no existe?");
    		}
    	}
	}
	@Test
	@Parameters({"url","username","pwd","jarname","dagname","group","cronexpr","step1"})
	void multipleDagsCreateTest(String url, String username, String pwd, String jarname,String dagname,String group,String cronexpr,String step1, ITestContext context) throws InterruptedException, IOException {
		driver.get(url);
    	LoginForm login = new LoginForm(driver);
    	if(login.login(username, pwd)) {
    		AuthenticatedView autenticado = new AuthenticatedView(driver);
    		JobsView jobs = autenticado.goToJobs();
    		jobs.selectDesigndTab();
    		var newform = jobs.newJobForm();
    		newform.setName(jarname);
    		var dagpanel1 = newform.generate();
    		newform.addDummyStep(step1,dagpanel1.getName());
    		var dagpanel2 = newform.generate();
    		dagpanel2.activate();
    		dagpanel2.activate();
    		newform.addDummyStep(step1,dagpanel2.getName());
    		newform.save();
    		this.writeEvidence(context,"multipleDagsCreateTest","OK",By.xpath("/html/body"));
    		assertTrue(true);
    	} else {
    		this.writeEvidence(context,"multipleDagsCreateTest","ERROR",By.xpath("/html/body"));
    		assertTrue(false,"no login?");
    	}
	}
}
