package com.widen.profiler;

import java.util.Map;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

import com.widen.profiler.services.JobIdentifier;

public class ProfilerState
{
	@Inject
	private JobIdentifier jobIdentifier;

	@Inject
	@Symbol(ProfilerSymbols.POLLING_INTERVAL)
	private int pollingInterval;

	@Inject
	@Symbol(ProfilerSymbols.MAX_THREAD_RUNTIME)
	private int maxThreadRuntime;

	@Inject
	@Symbol(ProfilerSymbols.IGNORED_STACK_STRINGS)
	private String ignoredStackStrings;

	private boolean showProfiler;
	private boolean ignoreJobs;

	private ProfilerThread profilerThread;

	private Map<String, Integer> overallCount;
	private Map<String, Map<String, Integer>> pageCount;
	private Map<String, Map<String, Integer>> jobCount;

	public boolean isShowProfiler()
	{
		return showProfiler;
	}

	public void setShowProfiler(boolean showProfiler)
	{
		this.showProfiler = showProfiler;
	}

	public boolean isIgnoreJobs()
	{
		return ignoreJobs;
	}

	public void setIgnoreJobs(boolean ignoreJobs)
	{
		this.ignoreJobs = ignoreJobs;
	}

	public void startProfiling()
	{
		profilerThread = new ProfilerThread(jobIdentifier, pollingInterval, maxThreadRuntime, ignoredStackStrings, ignoreJobs);

		Thread thread = new Thread(profilerThread, ProfilerThread.THREAD_NAME);

		thread.start();
	}

	public void stopProfiling()
	{
		profilerThread.stop();

		overallCount = profilerThread.getOverallCount();
		pageCount = profilerThread.getPageCount();
		jobCount = profilerThread.getJobCount();

		profilerThread = null;
	}

	public boolean isProfiling()
	{
		return profilerThread != null;
	}

	public Map<String, Integer> getOverallCount()
	{
		return overallCount;
	}

	public Map<String, Map<String, Integer>> getPageCount()
	{
		return pageCount;
	}

	public Map<String, Map<String, Integer>> getJobCount()
	{
		return jobCount;
	}
}