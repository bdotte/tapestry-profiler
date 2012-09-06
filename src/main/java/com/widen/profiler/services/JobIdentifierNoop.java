package com.widen.profiler.services;

/**
 * The default job identifier; assumes there are no jobs
 */
public class JobIdentifierNoop implements JobIdentifier
{
	public boolean isJob(Class stackClass)
	{
		return false;
	}
}
