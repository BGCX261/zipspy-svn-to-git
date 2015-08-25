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

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ZipArchiveTest extends TestCase
{
    ZipArchive za;

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


    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        URL url = new URL( "file:etc/testdata/maria.xlsx" );
        za = new ZipArchive( url );

    }


    @Override
    protected void tearDown() throws Exception
    {
        // vs.cleanup();
        super.tearDown();
    }


    @Test
    public void test_notNull()
    {

        assertTrue( za != null );
    }


    @Test
    public void test_headerCount()
    {

        assertTrue( za.getLocalHeaderCount() == 14 );
    }


    @Test
    public void test_countsMatch()
    {

        assertTrue( za.getLocalHeaderCount() == za.getCentralRecordCount() );
    }


    @Test
    public void test_aHeader()
    {

        ZipLocalHeader h = za.getLocalHeader( 0 );

        assertTrue( h.getExtractVersion()[ 0 ] == ( byte )0x14 );
        assertTrue( h.getExtractVersion()[ 1 ] == ( byte )0x00 );
        assertTrue( h.getGeneral() == 6 );
        assertTrue( h.getMethod()[ 0 ] == ( byte )0x08 );
        assertTrue( h.getMethod()[ 1 ] == ( byte )0x00 );
        assertTrue( h.getModTime()[ 0 ] == ( byte )0x00 );
        assertTrue( h.getModTime()[ 1 ] == ( byte )0x00 );
        assertTrue( h.getModDate()[ 0 ] == ( byte )0x21 );
        assertTrue( h.getModDate()[ 1 ] == ( byte )0x00 );
        assertTrue( h.getCrc32() == 0x65D2794B );
        assertTrue( h.getCompressedSize() == 0x0000019C );
        assertTrue( h.getUncompressedSize() == 0x0000034E );
        assertTrue( h.getFilenameLength() == 16 );
        assertTrue( h.getExtraFieldLength() == 264 );
        assertTrue( h.getFilename().equals( "docProps/app.xml" ) );
    }


    @Test
    public void test_xml()
    {

        String s = za.asXmlString();
        boolean ok = true;
        try
        {
            XMLReader r = XMLReaderFactory.createXMLReader();
            r.parse( new InputSource( new ByteArrayInputStream( s.getBytes() ) ) );
        }
        catch( Exception e )
        {
            ok = false;
        }

        assertTrue( ok );

    }

}
