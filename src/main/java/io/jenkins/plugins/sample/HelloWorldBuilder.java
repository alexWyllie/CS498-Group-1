package io.jenkins.plugins.sample;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.TestResult;
import hudson.tasks.test.AggregatedTestResultAction;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;

public class HelloWorldBuilder extends Builder implements SimpleBuildStep {

	private final String testSuiteFile;
	private boolean compileResults;
	private String output;

	@DataBoundConstructor
	public HelloWorldBuilder() {
		this.testSuiteFile = "";
		this.compileResults = true;
	}

	@DataBoundSetter
	public void setCompileResults(boolean compileResults) {
		this.compileResults = compileResults;
	}
	

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
		if (!compileResults) {
			return;
		}
		output = "";
		listener.getLogger().println("Running my plugin...");

		//Get current test results
		//Also look up AbstractTestResultAction
		AggregatedTestResultAction currentTestResults = run.getAction(AggregatedTestResultAction.class);
		
		if (currentTestResults == null) {
			return;
		}
		
		//retrieve the test results 
		List<AggregatedTestResultAction.ChildReport> currentResults = currentTestResults.getResult();

		ArrayList<CaseResult> newlyPassed = new ArrayList<CaseResult>();
		ArrayList<CaseResult> newlyFailed = new ArrayList<CaseResult>();
		ArrayList<CaseResult> stillFailed = new ArrayList<CaseResult>();
		//iterate through the result of each test
		for(int i = 0; i < currentResults.size(); i++)
		{

			//obtain the report of a test	
			AggregatedTestResultAction.ChildReport child = currentResults.get(i);

			//retrieve the test result
			TestResult currentTestResultChild = (TestResult)child.result;

			//get the passed tests 
			ArrayList<CaseResult> currRes = ((ArrayList<CaseResult>)currentTestResultChild.getPassedTests());
			ArrayList<CaseResult> failedTests = ((ArrayList<CaseResult>)currentTestResultChild.getFailedTests());
			
			//iterate through each passed test
			for(int j = 0; j < currRes.size(); j++)
			{
				//obtain the status of the test in current build
				CaseResult currentTestResChild = currRes.get(j);

				//obtain the status of the test in previous build
				CaseResult previousTestResChild = (CaseResult) currRes.get(j).getPreviousResult();
				
				if (previousTestResChild == null) {
					continue;
				}
				
				if (currentTestResChild.isPassed() && previousTestResChild.isFailed()) {
					newlyPassed.add(currentTestResChild);
				}
			}
			
			//iterate through each failed test
			for (int j = 0; j < failedTests.size(); j++) {
				//obtain the status of the test in current build
				CaseResult currentTestResChild = failedTests.get(j);
				
				//obtain the status of the test in the previous build
				CaseResult previousTestResChild = (CaseResult) failedTests.get(j).getPreviousResult();
				
				if (previousTestResChild == null) {
					continue;
				}
				
				if (currentTestResChild.isFailed() && previousTestResChild.isPassed()) {
					newlyFailed.add(currentTestResChild);
				}
				else {
					stillFailed.add(currentTestResChild);
				}
			}

		}
		
		//StringBuilder toOutput = new StringBuilder();
		StringBuilder toOutputNewPass = new StringBuilder();
		StringBuilder toOutputNewFail = new StringBuilder();
		StringBuilder toOutputStillFail = new StringBuilder();
		toOutputNewPass.append("-------------------------------------------------------\r\n N E W L Y    P A S S I N G   T E S T S\r\n-------------------------------------------------------\n");
		for (int i = 0; i < newlyPassed.size(); i++) {
			toOutputNewPass.append(newlyPassed.get(i).getName() + "\n");
		}
		toOutputNewFail.append("-------------------------------------------------------\r\n N E W L Y    F A I L I N G   T E S T S\r\n-------------------------------------------------------\n");
		for (int i = 0; i < newlyFailed.size(); i++) {
			toOutputNewFail.append(newlyFailed.get(i).getName() + "\n");
		}
		toOutputStillFail.append("-------------------------------------------------------\r\n S T I L L    F A I L I N G   T E S T S\r\n-------------------------------------------------------\n");
		for (int i = 0; i < stillFailed.size(); i++) {
			CaseResult prev = stillFailed.get(i).getPreviousResult();
			int fails = 1;
			while (prev != null && prev.isFailed()) {
				fails++;
				prev = prev.getPreviousResult();
			}
			toOutputStillFail.append(stillFailed.get(i).getName() + "\t Failed for the last " + fails + " builds\n");
		}
		
		outputNewPass = toOutputNewPass.toString();
		outputNewFail = toOutputNewFail.toString();
		outputStillFail= toOutputStillFail.toString();
		
		listener.getLogger().println(outputNewPass);
		listener.getLogger().println(outputNewFail);
		listener.getLogger().println(outputStillFail);
		
		
		//obtain the information from previous build
		//Run<?, ?> previousBuiltBuild = run.getPreviousBuiltBuild();

		
		// try to read the test suite file from the workspace
		//InputStream inputStream = workspace.child(testSuiteFile).read();

		run.addAction(new OutputAction(outputNewPass, outputNewFail, outputStillFail));
	}

	@Symbol("greet")
	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

		/*public FormValidation doCheckName(@QueryParameter String value, @QueryParameter boolean useFrench)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName());
			if (value.length() < 4)
				return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_tooShort());
			if (!useFrench && value.matches(".*[éáàç].*")) {
				return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_reallyFrench());
			}
			return FormValidation.ok();
		}*/

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return "Compare test results to previous builds";
		}

	}

}
