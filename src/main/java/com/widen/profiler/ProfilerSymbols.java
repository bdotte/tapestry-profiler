package com.widen.profiler;

public class ProfilerSymbols
{
	/**
	 * Your base application package (com.whatever). Providing this gives you the option to toggle
	 * stack trace lines that are not from your application in the results.
	 */
	public static final String APPLICATION_PACKAGE = "profiler.application-package";

	/**
	 * If you have jobs running (for example with Quartz), enable this and override the JobIdentifier
	 * service.
	 */
	public static final String MONITOR_JOBS = "profiler.monitor-jobs";

	/**
	 * The time interval between polls (ms).
	 */
	public static final String POLLING_INTERVAL = "profiler.polling-interval";

	/**
	 * The maximum amount of time the profiler thread is allowed to run after it is started (min).
	 */
	public static final String MAX_THREAD_RUNTIME = "profiler.max-thread-runtime";

	/**
	 * Space-separated list of low-level and uninteresting stack trace elements (like java., sun. etc.)
	 * If any of these strings are found anywhere in a stack trace line, it is ignored.
	 */
	public static final String IGNORED_STACK_STRINGS = "profiler.ignored-stack-strings";

	/**
	 * The subject line to use for performance reports sent over e-mail.
	 */
	public static final String MAIL_SUBJECT = "profiler.mail-subject";

	/**
	 * The default e-mail address to send performance reports to.
	 */
	public static final String DEFAULT_MAIL_RECIPIENT = "profiler.default-mail-recipient";

	/**
	 * The SMTP server to use to send performance reports.
	 */
	public static final String SMTP_SERVER = "profiler.smtp-server";

	/**
	 * The sender for sending performance reports over SMTP.
	 */
	public static final String SMTP_FROM_ADDRESS = "profiler.smtp-from-address";

	/**
	 * true to set mail.smtp.starttls.required to true when sending a performance report via SMTP.
	 */
	public static final String SMTP_STARTTLS_REQUIRED = "profiler.smtp-starttls-required";

	/**
	 * true to authenticate when sending over SMTP (using the SMTP_USERNAME and SMTP_PASSWORD)
	 */
	public static final String SMTP_AUTH = "profiler.smtp-auth";

	/**
	 * The SMTP username to send performance reports.
	 */
	public static final String SMTP_USERNAME = "profiler.smtp-username";

	/**
	 * The SMTP password to send performance reports.
	 */
	public static final String SMTP_PASSWORD = "profiler.smtp-password";

	/**
	 * The SMTP port to send performance reports.
	 */
	public static final String SMTP_PORT = "profiler.smtp-port";
}
