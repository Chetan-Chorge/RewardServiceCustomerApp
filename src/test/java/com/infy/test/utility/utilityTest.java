package com.infy.test.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.infy.exception.RewardprogramCustomerException;
import com.infy.utility.ErrorInfo;
import com.infy.utility.ExceptionControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
public class utilityTest {

	@Mock
	private Environment environment;

	@InjectMocks
	private ExceptionControllerAdvice advice;

	@Test
	public void testRewardprogramCustomerExceptionHandler() {
		RewardprogramCustomerException ex = new RewardprogramCustomerException("Custom.ERROR");
		when(environment.getProperty("Custom.ERROR")).thenReturn("Custom error occurred");

		ResponseEntity<ErrorInfo> response = advice.meetingSchedulerExceptionHandler(ex);

		assertEquals(400, response.getStatusCodeValue());
		assertEquals("Custom error occurred", response.getBody().getErrorMessage());
	}

	/**
	 * Test handling of general Exception
	 */
	@Test
	public void testGeneralExceptionHandler() {
		Exception ex = new Exception("Something went wrong");
		when(environment.getProperty("General.EXCEPTION_MESSAGE")).thenReturn("Internal server error");

		ResponseEntity<ErrorInfo> response = advice.generalExceptionHandler(ex);

		assertEquals(500, response.getStatusCodeValue());
		assertEquals("Internal server error", response.getBody().getErrorMessage());
	}

	/** Test handling of MethodArgumentNotValidException */
	@Test
	public void testMethodArgumentNotValidExceptionHandler() {
		ObjectError error = new ObjectError("field", "Invalid field");
		MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
		when(ex.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
		when(ex.getBindingResult().getAllErrors()).thenReturn(Collections.singletonList(error));

		ResponseEntity<ErrorInfo> response = advice.validatorExceptionHandler(ex);

		assertEquals(400, response.getStatusCodeValue());
		assertEquals("Invalid field", response.getBody().getErrorMessage());
	}

	/**
	 * ✅ Test handling of ConstraintViolationException
	 */
	@Test
	public void testConstraintViolationExceptionHandler() {
		ConstraintViolation<?> violation = mock(ConstraintViolation.class);
		when(violation.getMessage()).thenReturn("Constraint violated");

		ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

		ResponseEntity<ErrorInfo> response = advice.validatorExceptionHandler(ex);

		assertEquals(400, response.getStatusCodeValue());
		assertEquals("Constraint violated", response.getBody().getErrorMessage());
	}
}
