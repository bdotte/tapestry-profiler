package com.widen.profiler.services;

/**
 * Determines whether a particular stack trace element is from a DAO.
 */
public interface DAOIdentifier
{
	/**
	 * @param stackLine
	 *          the fully qualified classname
	 *
	 * @return  true if the given stackLine is considered a DAO
	 */
	boolean isDAO(String stackLine);
}
