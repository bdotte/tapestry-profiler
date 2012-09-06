package com.widen.profiler.services;

import org.apache.commons.lang.StringUtils;

public class DefaultDAOIdentifier implements DAOIdentifier
{
	public boolean isDAO(String stackLine)
	{
		return StringUtils.containsIgnoreCase(stackLine, "dao");
	}
}
