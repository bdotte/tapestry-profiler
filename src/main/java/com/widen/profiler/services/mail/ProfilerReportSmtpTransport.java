package com.widen.profiler.services.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public interface ProfilerReportSmtpTransport
{

	/**
	 * Process a mail message
	 *
	 * @param message the message to send, should already be committed to database
	 * @return true if the message was sent immediately, false if message will be sent in the future
	 */
	public boolean send(MimeMessage message) throws MessagingException;

}
