package com.widen.profiler;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.LibraryMapping;

import com.widen.profiler.services.AlwaysAvailableAccessController;
import com.widen.profiler.services.DAOIdentifier;
import com.widen.profiler.services.DefaultDAOIdentifier;
import com.widen.profiler.services.JobIdentifier;
import com.widen.profiler.services.JobIdentifierNoop;
import com.widen.profiler.services.ProfilerAccessController;

public class ProfilerModule
{
	public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
	{
		configuration.add(ProfilerSymbols.APPLICATION_PACKAGE, "");
		configuration.add(ProfilerSymbols.MONITOR_JOBS, "false");
		configuration.add(ProfilerSymbols.POLLING_INTERVAL, "10");
		configuration.add(ProfilerSymbols.MAX_THREAD_RUNTIME, "60");
		configuration.add(ProfilerSymbols.IGNORED_STACK_STRINGS, "org.eclipse.jetty java. sun. javassist. ProfilerThread");
	}

	public static void bind(ServiceBinder binder)
	{
		binder.bind(ProfilerAccessController.class, AlwaysAvailableAccessController.class); // PLEASE override this to prevent access to the results for non-superusers
		binder.bind(DAOIdentifier.class, DefaultDAOIdentifier.class); // override this if your DAOs do not have "dao" in the class name
		binder.bind(JobIdentifier.class, JobIdentifierNoop.class); // override this if you want stack traces associated with jobs to be identified separately (like pages)
	}

	@Contribute(ComponentClassResolver.class)
	public static void setupProfilerLibrary(Configuration<LibraryMapping> configuration)
	{
		configuration.add(new LibraryMapping("widen", "com.widen.profiler"));
	}
}
