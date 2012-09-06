package com.widen.profiler.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.widen.profiler.ProfilerState;
import com.widen.profiler.pages.ProfilerResults;

public class Profiler
{
	@Parameter
	@Property
	private String cssClass;

	@Property
	@SessionState
	private ProfilerState profilerState;

	@InjectComponent
	private Zone profilerZone;

	@Property
	@InjectComponent
	private Form options;

	@Inject
	private JavaScriptSupport jsSupport;

	@Inject
	private ComponentResources resources;


	public boolean isProfiling()
	{
		return profilerState.isProfiling();
	}

	public String getProfileLinkText()
	{
		return isProfiling() ? "Capture Snapshot" : "Start Profiling";
	}

	void onShow()
	{
		profilerState.setShowProfiler(true);
	}

	void onHide()
	{
		profilerState.setShowProfiler(false);
	}

	Object onSuccessFromOptions()
	{
		if (profilerState.isProfiling())
		{
			profilerState.stopProfiling();

			return ProfilerResults.class;
		}

		profilerState.startProfiling();

		return profilerZone.getBody();
	}
}

