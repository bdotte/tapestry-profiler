package com.widen.profiler.services.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.widen.profiler.PerformanceReport;

public interface MailMessageConversionService
{
	/**
	 * Create a JavaMail MimeMessage object from a {@link PerformanceReport} object.
	 */
	public MimeMessage convert(PerformanceReport report, String emailAddress, String actions, boolean hideUninterestingLines) throws MessagingException;

}
