package com.widen.profiler.services;

/**
 * Used to identify stack traces that belong to a particular job.
 */
public interface JobIdentifier
{
	/**
	 * @param stackClass
	 *          the Class of the current line of a stack trace
	 *
	 * @return  true if the given stackClass is considered a job
	 */
	boolean isJob(Class stackClass);
}
