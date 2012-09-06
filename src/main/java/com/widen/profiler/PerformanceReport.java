package com.widen.profiler;

import java.util.Map;
import java.util.SortedSet;

import com.widen.profiler.pages.ProfilerResults;

public class PerformanceReport
{
	private final static String NEWLINE = System.getProperty("line.separator");

	private final String applicationPackage;
	private final int pollingInterval;

	private Map<String, Map<String, Integer>> pageCount;
	private Map<String, Map<String, Integer>> jobCount;
	private Map<String, Integer> overallCount;

	public PerformanceReport(Map<String, Map<String, Integer>> pageCount, Map<String, Map<String, Integer>> jobCount, Map<String, Integer> overallCount,
	                         String applicationPackage, int pollingInterval)
	{
		this.applicationPackage = applicationPackage;
		this.pollingInterval = pollingInterval;
		this.pageCount = pageCount;
		this.jobCount = jobCount;
		this.overallCount = overallCount;
	}

	public String getFormattedReport(boolean hideUninterestingLines)
	{
		StringBuffer sb = new StringBuffer();

		for (String pageName : pageCount.keySet())
		{
			Map<String, Integer> count = pageCount.get(pageName);
			SortedSet<Map.Entry<String,Integer>> sortedCount = ProfilerResults.getSortedCountsStatic(hideUninterestingLines, count, applicationPackage);

			if (!sortedCount.isEmpty())
			{
				sb.append(NEWLINE);
				sb.append("Page \"" + pageName + "\"" + NEWLINE);
				for (Map.Entry<String,Integer> entry : sortedCount)
				{
					sb.append((entry.getValue() * pollingInterval) + "ms " + entry.getKey() + NEWLINE);
				}
			}
		}

		for (String jobName : jobCount.keySet())
		{
			Map<String, Integer> count = jobCount.get(jobName);
			SortedSet<Map.Entry<String,Integer>> sortedCount = ProfilerResults.getSortedCountsStatic(hideUninterestingLines, count, applicationPackage);

			if (!sortedCount.isEmpty())
			{
				sb.append(NEWLINE);
				sb.append("Job \"" + jobName + "\"" + NEWLINE);
				for (Map.Entry<String,Integer> entry : sortedCount)
				{
					sb.append((entry.getValue() * pollingInterval) + "ms " + entry.getKey() + NEWLINE);
				}
			}
		}

		SortedSet<Map.Entry<String,Integer>> sortedOverallCount = ProfilerResults.getSortedCountsStatic(hideUninterestingLines, overallCount, applicationPackage);

		if (!sortedOverallCount.isEmpty())
		{
			sb.append(NEWLINE);
			sb.append("Overall" + NEWLINE);
			for (Map.Entry<String,Integer> entry : sortedOverallCount)
			{
				sb.append((entry.getValue() * pollingInterval) + "ms " + entry.getKey() + NEWLINE);
			}
		}

		return sb.toString();
	}
}
