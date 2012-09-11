package com.widen.profiler;

import com.widen.profiler.services.mail.*;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.security.ClientWhitelist;
import org.apache.tapestry5.services.security.WhitelistAnalyzer;

import com.widen.profiler.services.DAOIdentifier;
import com.widen.profiler.services.DefaultDAOIdentifier;
import com.widen.profiler.services.JobIdentifier;
import com.widen.profiler.services.JobIdentifierNoop;
import com.widen.profiler.services.mail.ProfilerReportToMailMessageConversionService;
import com.widen.profiler.services.mail.ProfilerReportToMailMessageConversionServiceImpl;

public class ProfilerModule
{
	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
	{
		configuration.add(ProfilerSymbols.APPLICATION_PACKAGE, "");
		configuration.add(ProfilerSymbols.MONITOR_JOBS, "false");
		configuration.add(ProfilerSymbols.POLLING_INTERVAL, "10");
		configuration.add(ProfilerSymbols.MAX_THREAD_RUNTIME, "60");
		configuration.add(ProfilerSymbols.IGNORED_STACK_STRINGS, "org.eclipse.jetty java. sun. javassist. ProfilerThread");

		configuration.add(ProfilerSymbols.MAIL_SUBJECT, "Performance Report");
		configuration.add(ProfilerSymbols.DEFAULT_MAIL_RECIPIENT, "");
		configuration.add(ProfilerSymbols.SMTP_SERVER, "");
		configuration.add(ProfilerSymbols.SMTP_FROM_ADDRESS, "");
		configuration.add(ProfilerSymbols.SMTP_STARTTLS_REQUIRED, "false");
		configuration.add(ProfilerSymbols.SMTP_AUTH, "false");
		configuration.add(ProfilerSymbols.SMTP_USERNAME, "");
		configuration.add(ProfilerSymbols.SMTP_PASSWORD, "");
		configuration.add(ProfilerSymbols.SMTP_PORT, "25");
	}

	public static void bind(ServiceBinder binder)
	{
		binder.bind(DAOIdentifier.class, DefaultDAOIdentifier.class); // override this if your DAOs do not have "dao" in the class name
		binder.bind(JobIdentifier.class, JobIdentifierNoop.class); // override this if you want stack traces associated with jobs to be identified separately (like pages)

		binder.bind(ProfilerReportToMailMessageConversionService.class, ProfilerReportToMailMessageConversionServiceImpl.class);
		binder.bind(ProfilerReportSmtpTransport.class, ProfilerReportSmtpTransportImpl.class);
	}

	@Contribute(ComponentClassResolver.class)
	public static void setupProfilerLibrary(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("profiler", "com.widen.profiler"));
	}

    @Contribute(ClientWhitelist.class)
    public static void contributeClientWhitelist(OrderedConfiguration<WhitelistAnalyzer> configuration)
    {
        configuration.add("MySiteWhitelistExample", new WhitelistAnalyzer()
        {
            public boolean isRequestOnWhitelist(Request request)
            {
                // String remoteHost = request.getRemoteHost();
                // return remoteHost.equals("1.2.3.4"); // your internal address or whatever

                return false;
            }
        });
    }
}
