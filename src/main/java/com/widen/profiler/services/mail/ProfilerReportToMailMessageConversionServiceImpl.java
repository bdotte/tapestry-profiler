package com.widen.profiler.services.mail;

import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import com.widen.profiler.PerformanceReport;
import com.widen.profiler.ProfilerSymbols;

public class ProfilerReportToMailMessageConversionServiceImpl implements ProfilerReportToMailMessageConversionService
{
	private final String mailSubject;
	private final String fromAddress;

	public ProfilerReportToMailMessageConversionServiceImpl(@Inject @Symbol(ProfilerSymbols.MAIL_SUBJECT) String mailSubject,
                                                            @Inject @Symbol(ProfilerSymbols.SMTP_FROM_ADDRESS) String fromAddress)
	{
		this.mailSubject = mailSubject;
		this.fromAddress = fromAddress;
	}

	public MimeMessage convert(PerformanceReport report, String emailAddress, String actions, boolean hideUninterestingLines) throws MessagingException
	{
		MimeMessage message = new MimeMessage((Session) null);

		message.setFrom(new InternetAddress(fromAddress));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
		message.setContentLanguage(new String[] { "en" });
		message.setSentDate(new Date());
		message.setSubject(mailSubject);

		String actionsText = "";
		if (StringUtils.isNotBlank(actions))
		{
			actionsText = "Actions being performed:" + PerformanceReport.NEWLINE + actions + PerformanceReport.NEWLINE + PerformanceReport.NEWLINE;
		}

		message.setText(actionsText + report.getFormattedReport(hideUninterestingLines));

		return message;
	}
}
