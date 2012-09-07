package com.widen.profiler.services.mail;

import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.widen.profiler.ProfilerSymbols;

/**
 * Use JavaMail to put a MimeMessage onto the wire using SMTP.
 */
public class SmtpTransportImpl implements SmtpTransport
{
	private final Logger log = LoggerFactory.getLogger(SmtpTransportImpl.class);

	private final String server;
	private final Boolean startTlsRequired;
	private final boolean auth;
	private final String username;
	private final String password;
	private final Integer port;

	public SmtpTransportImpl(@Inject @Symbol(ProfilerSymbols.SMTP_SERVER) String server,
	                         @Inject @Symbol(ProfilerSymbols.SMTP_STARTTLS_REQUIRED) Boolean startTlsRequired,
	                         @Inject @Symbol(ProfilerSymbols.SMTP_AUTH) boolean auth,
	                         @Inject @Symbol(ProfilerSymbols.SMTP_USERNAME) String username,
	                         @Inject @Symbol(ProfilerSymbols.SMTP_PASSWORD) String password,
	                         @Inject @Symbol(ProfilerSymbols.SMTP_PORT) Integer port)
	{
		this.server = server;
		this.startTlsRequired = startTlsRequired;
		this.auth = auth;
		this.username = username;
		this.password = password;
		this.port = port;
	}

	public boolean send(MimeMessage message) throws MessagingException
	{
		try
		{
			Session session = getSession();

			MimeMessage m = new SettableSessionMimeMessage(message, session);

			Transport.send(m);

			log.info("Sent message '{}' to '{}'", new Object[] { message.getSubject(), message.getRecipients(Message.RecipientType.TO)[0] });

			return true;
		}
		catch (Exception e)
		{
			log.error(String.format("Error sending message '%s' to '%s'", message.getSubject(), message.getRecipients(Message.RecipientType.TO)[0]), e);
			return false;
		}
	}

	private Session getSession()
	{
		Properties props = new Properties();

		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.starttls.required", startTlsRequired.toString());
		props.put("mail.smtp.host", server);
		props.put("mail.smtp.port", port.toString());
		props.put("mail.mime.charset", "UTF-8");
		props.put("mail.smtp.connectiontimeout", TimeUnit.SECONDS.toMillis(10));
		props.put("mail.smtp.timeout", TimeUnit.SECONDS.toMillis(20));

		if (auth)
		{
			props.put("mail.smtp.auth", "true");

			return Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(username, password);
				}
			});
		}

		props.put("mail.smtp.auth", "false");

		return Session.getInstance(props);
	}

	private class SettableSessionMimeMessage extends MimeMessage
	{
		public SettableSessionMimeMessage(MimeMessage message, Session session) throws MessagingException
		{
			super(message);
			this.session = session;
		}
	}

}
