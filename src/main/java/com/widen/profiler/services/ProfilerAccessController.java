package com.widen.profiler.services;

/**
 * This is a mechanism for preventing access to the ProfilerResults page for non-superusers.
 * We want the page to be available in production mode (that's the whole point), so turning
 * this off in production mode like other internal pages isn't good enough.
 */
public interface ProfilerAccessController
{
	/**
	 * @return  true if the current user can access profiler results
	 */
	boolean hasAccessToResultsPage();
}
