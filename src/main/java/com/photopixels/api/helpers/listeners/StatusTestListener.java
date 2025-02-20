package com.photopixels.api.helpers.listeners;

import io.qameta.allure.Allure;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.testng.*;

import java.util.*;

public class StatusTestListener extends TestListenerAdapter implements StepLifecycleListener {

	@Override
	public void onTestSuccess(ITestResult tr) {

	}

	@Override
	public void onTestFailure(ITestResult tr) {
		Throwable e = tr.getThrowable();

		if (e.getClass().equals(AssertionError.class)) {
			failStepOnFailure(e.getMessage());
		}
	}

	@Override
	public void onTestSkipped(ITestResult tr) {

	}

	@Override
	public void onConfigurationFailure(ITestResult tr) {

	}

	@Override
	public void onConfigurationSuccess(ITestResult tr) {

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

	private void failStepOnFailure(final String message) {
		final String uuid = UUID.randomUUID().toString();
		final StepResult result = new StepResult().setName(message);

		Allure.getLifecycle().startStep(uuid, result);

		Allure.getLifecycle().updateStep(uuid, s -> s.setStatus(Status.FAILED));

		Allure.getLifecycle().stopStep(uuid);
	}
}
