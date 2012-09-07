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
}
