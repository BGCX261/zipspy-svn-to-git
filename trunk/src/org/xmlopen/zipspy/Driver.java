/*
 * This file is part of the source of
 * 
 * ZipSpy - a library for examining ZIP files
 * 
 * Copyright (c) 2010 Griffin Brown Digital Publishing Ltd.
 * 
 * All rights reserved world-wide.
 * 
 * The contents of this file are subject to the Mozilla Public License Version 1.1 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY
 * OF ANY KIND, either express or implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package org.xmlopen.zipspy;

import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class Driver
{
	
	static
    {
        // set up log message format, etc.
        String logLvl = System.getProperty( "property://probatron.org/officeotron-log-level" );
        logLvl = ( logLvl == null ) ? "DEBUG" : logLvl;

        Properties p = new Properties();
        p.setProperty( "log4j.rootCategory", logLvl + ", A1" );
        p.setProperty( "log4j.appender.A1", "org.apache.log4j.ConsoleAppender" );
        p.setProperty( "log4j.appender.A1.target", "System.err" );
        p.setProperty( "log4j.appender.A1.layout", "org.apache.log4j.PatternLayout" );
        p.setProperty( "log4j.appender.A1.layout.ConversionPattern", "%c %p - %m%n" );
        PropertyConfigurator.configure( p );

    }

    /**
     * @param args
     */
    public static void main( String[] args )
    {
    	// 	TODO Auto-generated method stub
    	try {
    		if ( args.length != 1 )
    		{
    			System.err.println( "arguments: path/to/the/file/to/test.zip" );
    			System.exit( 1 );
    		}
    			
	    	FileInputStream is = new FileInputStream( args[0] );
	    	ZipArchive archive = new ZipArchive( is );
    	
	    	int crCount = archive.getCentralRecordCount();
	    	int lhCount = archive.getLocalHeaderCount();
	    	
	    	System.out.println( MessageFormat.format( "crCount: {0}, lhCount: {1}", crCount, lhCount ) );
	    	System.exit( 0 );
    	} catch (Exception e) {
    		e.printStackTrace();
    		System.exit( 1 );
    	}
    }

}
