package io.jenkins.plugins.sample;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
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
	public HelloWorldBuilder(String testSuiteFile) {
		this.testSuiteFile = testSuiteFile;
	}

	@DataBoundSetter
	public void setCompileResults(boolean compileResults) {
		this.compileResults = compileResults;
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
		
		listener.getLogger().println("Running my plugin...");

		//Get current test results
		//Also look up AbstractTestResultAction
		AggregatedTestResultAction currentTestResults = run.getAction(AggregatedTestResultAction.class);

		//retrieve the test results 
		List<AggregatedTestResultAction.ChildReport> currentResults = currentTestResults.getResult();


		//iterate through the result of each test
		for(int i = 0; i < currentResults.size(); i++)
		{

			//obtain the report of a test	
			AggregatedTestResultAction.ChildReport child = currentResults.get(i);

			//retreive the test result
			TestResult currentTestResultChild = (TestResult)child.result;

			//get the passed tests 
			ArrayList<TestResult> currRes = ((ArrayList<TestResult>)currentTestResultChild.getPassedTests());

			//iterate through each passed test
			for(int j = 0; j < currRes.size(); j++)
			{
				//obtain the status of the test in current build
				TestResult currentTestResChild = currRes.get(j);
				
				System.out.println(currentTestResChild);

				//obtain the status of the test in previous build
				TestResult previousTestResChild = (TestResult) currRes.get(j).getPreviousResult();
				
				//Figure out how to compare them. Build output from this.
				
			}

		}

		//obtain the information from previous build
		Run<?, ?> previousBuiltBuild = run.getPreviousBuiltBuild();

		
		// try to read the test suite file from the workspace
		InputStream inputStream = workspace.child(testSuiteFile).read();

		run.addAction(new OutputAction(output));
	}

	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

		public FormValidation doCheckName(@QueryParameter String value, @QueryParameter boolean useFrench)
				throws IOException, ServletException {
			if (value.length() == 0)
				return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName());
			if (value.length() < 4)
				return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_tooShort());
			if (!useFrench && value.matches(".*[éáàç].*")) {
				return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_reallyFrench());
			}
			return FormValidation.ok();
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		@Override
		public String getDisplayName() {
			return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName();
		}

	}

}
