package com.widen.profiler.services;

/**
 * Gives everyone access to the ProfilerResults page. It is highly recommended that you
 * override this implementation!
 */
public class AlwaysAvailableAccessController implements ProfilerAccessController
{
	public boolean hasAccessToResultsPage()
	{
		return true;
	}
}
