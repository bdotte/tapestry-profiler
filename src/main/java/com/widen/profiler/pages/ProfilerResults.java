package com.widen.profiler.pages;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.mail.internet.MimeMessage;

import com.widen.profiler.services.mail.ProfilerReportToMailMessageConversionService;
import com.widen.profiler.services.mail.ProfilerReportSmtpTransport;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.WhitelistAccessOnly;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.RequestGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.widen.profiler.PerformanceReport;
import com.widen.profiler.ProfilerState;
import com.widen.profiler.ProfilerSymbols;
import com.widen.profiler.services.DAOIdentifier;

@WhitelistAccessOnly
@Import(stylesheet = "ProfilerResults.css")
public class ProfilerResults
{
	private final Logger log = LoggerFactory.getLogger(ProfilerResults.class);


    @Inject
    @Symbol(ProfilerSymbols.APPLICATION_PACKAGE)
    private String applicationPackage;

    @Inject
    @Symbol(ProfilerSymbols.POLLING_INTERVAL)
    private int pollingInterval;

    @Inject
    @Symbol(ProfilerSymbols.DEFAULT_MAIL_RECIPIENT)
    private String defaultMailRecipient;

    @Property
	@Persist
	private Boolean hideUninterestingLines;

	@Property
	private String emailAddress = defaultMailRecipient;

	@Property
	private String actions;

	@SessionState
	private ProfilerState profilerState;

	@Property
	private Map.Entry<String, Integer> sortedCount;

	@Property
	private int rowIndex;

	@Property
	@InjectComponent
	private Form options;

	@InjectComponent
	private Zone resultsZone;

	@Property
	private String pageName;

	@Property
	private String jobName;

	@Inject
	private RequestGlobals requestGlobals;

	@Inject
	private DAOIdentifier daoIdentifier;

	@Inject
	private ProfilerReportToMailMessageConversionService profilerReportToMailMessageConversionService;

	@Inject
	private ProfilerReportSmtpTransport profilerReportSmtpTransport;

	@Property
	@Persist("flash")
	private String emailedMessage;


	void pageReset()
	{
		emailedMessage = defaultMailRecipient;

		if (hideUninterestingLines == null)
		{
			hideUninterestingLines = true;
		}
	}

	public boolean isHasPageCounts()
	{
		return !getPageNames().isEmpty();
	}

	public boolean isHasJobCounts()
	{
		return !getJobNames().isEmpty();
	}

	public List<String> getPageNames()
	{
		List<String> pageNamesWithInfo = new ArrayList<String>();

		for (String pageName : profilerState.getPageCount().keySet())
		{
			if (!getSortedCounts(profilerState.getPageCount().get(pageName)).isEmpty())
			{
				pageNamesWithInfo.add(pageName);
			}
		}
		return pageNamesWithInfo;
	}

	public List<String> getJobNames()
	{
		List<String> jobsWithInfo = new ArrayList<String>();

		for (String jobName : profilerState.getJobCount().keySet())
		{
			if (!getSortedCounts(profilerState.getJobCount().get(jobName)).isEmpty())
			{
				jobsWithInfo.add(jobName);
			}
		}

		return jobsWithInfo;
	}

	public SortedSet<Map.Entry<String,Integer>> getSortedOverallCounts()
	{
		return getSortedCounts(profilerState.getOverallCount());
	}

	public SortedSet<Map.Entry<String,Integer>> getSortedPageCounts(String pageName)
	{
		return getSortedCounts(profilerState.getPageCount().get(pageName));
	}

	public SortedSet<Map.Entry<String,Integer>> getSortedJobCounts(String jobName)
	{
		return getSortedCounts(profilerState.getJobCount().get(jobName));
	}

	public SortedSet<Map.Entry<String,Integer>> getSortedCounts(Map<String, Integer> countMap)
	{
		return getSortedCountsStatic(hideUninterestingLines, countMap, applicationPackage);
	}

	public static SortedSet<Map.Entry<String,Integer>> getSortedCountsStatic(boolean hideUninterestingLines, Map<String, Integer> countMap, String applicationPackage)
	{
		SortedSet<Map.Entry<String, Integer>> sortedEntries = new TreeSet<Map.Entry<String, Integer>>(
				new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
						int countCompare = e2.getValue().compareTo(e1.getValue());
						if (countCompare == 0)
						{
							return e1.getKey().compareTo(e2.getKey());
						}
						return countCompare;
					}
				}
		);

		for (Map.Entry<String, Integer> entry : countMap.entrySet())
		{
			if (!hideUninterestingLines || StringUtils.startsWith(entry.getKey(), applicationPackage))
			{
				sortedEntries.add(entry);
			}
		}

		return sortedEntries;
	}

	public String getSeparatedClassInfo(String classInfo)
	{
		return StringUtils.substringBeforeLast(classInfo, ".") + " " + StringUtils.substringAfterLast(classInfo, ".");
	}

	public String getRowCss()
	{
		if (StringUtils.containsIgnoreCase(sortedCount.getKey(), ".pages."))
		{
			return "blueHlBg";
		}
		if (StringUtils.containsIgnoreCase(sortedCount.getKey(), ".components."))
		{
			return "greenBg";
		}
		if (daoIdentifier.isDAO(sortedCount.getKey()))
		{
			return "yellowBg";
		}
		return rowIndex % 2 == 0 ? "row0" : "row1";
	}

	public String getTime()
	{
		return new DecimalFormat("###,###").format(sortedCount.getValue() * pollingInterval);
	}

	Object onSuccessFromOptions()
	{
		return resultsZone.getBody();
	}

	void onSuccessFromEmailForm()
	{
		PerformanceReport report = new PerformanceReport(profilerState.getPageCount(), profilerState.getJobCount(), profilerState.getOverallCount(),
				applicationPackage, pollingInterval);

		try
		{
			MimeMessage message = profilerReportToMailMessageConversionService.convert(report, emailAddress, actions, hideUninterestingLines);
			profilerReportSmtpTransport.send(message);
		}
		catch (Exception e)
		{
			log.error("Error sending performance report by e-mail", e);
			emailedMessage = "Error sending e-mail: " + e.getMessage();
			return;
		}

		emailedMessage = "Sent results to " + emailAddress;
	}
}
