package com.photopixels.listeners;

import com.photopixels.base.MobileBaseTest;
import com.photopixels.base.WebBaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;

public class StatusTestListener implements ITestListener, StepLifecycleListener, TestLifecycleListener {

	private static WebDriver driver;

	@Override
	public void afterStepStop(StepResult result) {

	}

	@Override
	public void beforeTestStop(TestResult result) {
		List<StepResult> steps = result.getSteps();
		StepResult stepResult = steps.get(steps.size() - 1);

		// Attach screenshot for web test
		if (driver != null) {
			Allure.addAttachment(stepResult.getName(),
					new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
		}

		if ((result.getStatus() == Status.FAILED || result.getStatus() == Status.BROKEN)) {

			// Fail step in case of soft assertion error
			if (stepResult.getStatus() == Status.PASSED){
				stepResult.setStatus(Status.FAILED);
			}

			String link = result.getLinks().stream().filter(l -> l.getType().equals("issue")).findFirst().get().getUrl();

			// Check if issue link is attached and mark it as known issue
			if (!link.isEmpty()) {
				StatusDetails statusDetails = result.getStatusDetails();
				statusDetails.setKnown(true);
				statusDetails.setMessage("Known issue: " + link + " \n\n" + statusDetails.getMessage());
			}
		}

	}

	@Override
	public void onTestStart(ITestResult tr) {
		Object testClass = tr.getInstance();

		if (testClass.getClass().getSuperclass().equals(WebBaseTest.class)) {
			driver = ((WebBaseTest) testClass).getDriver();
		}

		if (testClass.getClass().getSuperclass().equals(MobileBaseTest.class)) {
			driver = ((MobileBaseTest) testClass).getMobileDriver();
		}
	}

	@Override
	public void onTestSuccess(ITestResult tr) {

	}

	@Override
	public void onTestFailure(ITestResult tr) {

	}

	@Override
	public void onTestSkipped(ITestResult tr) {

	}

	@Override
	public void onFinish(ITestContext tc) {
		Iterator<ITestResult> skippedTestCases = tc.getSkippedTests().getAllResults().iterator();

		while (skippedTestCases.hasNext()) {
			ITestResult skippedTestCase = skippedTestCases.next();
			ITestNGMethod method = skippedTestCase.getMethod();

			if (!tc.getFailedTests().getResults(method).isEmpty()
					|| !tc.getPassedTests().getResults(method).isEmpty()) {
				skippedTestCases.remove();
			}
		}
	}

}
