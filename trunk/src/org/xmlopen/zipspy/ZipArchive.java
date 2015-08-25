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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class ZipArchive
{
    static Logger logger = Logger.getLogger( ZipArchive.class );

    public final static int ZIP_LOCAL_SIGNATURE = 0x04034b50;
    public final static int ZIP_DATA_SIGNATURE = 0x08074b50;
    public final static int ZIP_CENTRAL_SIGNATURE = 0x02014b50;
    public final static int ZIP_CENTRAL_END_SIGNATURE = 0x06054b50;

    private ArrayList< ZipLocalHeader > localHeaders = new ArrayList< ZipLocalHeader >();
    private ArrayList< ZipCentralRecord > centralRecords = new ArrayList< ZipCentralRecord >();

    boolean usesDataDescriptors;
    private InputStream is;


    public ZipArchive( URL url )
    {
        try
        {
            URLConnection conn = url.openConnection();
            conn.connect();
            is = conn.getInputStream();
            process();
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            Utils.streamClose( is );
        }
    }


    public ZipArchive( InputStream is )
    {
    	this.is = is;
        process();
    }


    private void process()
    {
        try
        {
        	int sig = -1;
            do
            {
            	sig = skipData( is );

                if( sig == ZIP_LOCAL_SIGNATURE )
                {
                    logger.debug( "Reading local header" );
                    ZipLocalHeader zlh = new ZipLocalHeader( is );
                    localHeaders.add( zlh );
                    logger.debug( "Read header for entry: " + zlh.getFilename() );

                    if( !usesDataDescriptors
                            && ( zlh.general & ZipLocalHeader.DATA_DESCRIPTOR_MASK ) != 0 )
                    {
                        usesDataDescriptors = true;
                        logger.info( "Archive uses data descriptors" );
                    }
                }
                else if( sig == ZIP_CENTRAL_SIGNATURE )
                {
                    logger.debug( "Reading central directory header" );
                    ZipCentralRecord zcr = new ZipCentralRecord( is );
                    centralRecords.add( zcr );
                    logger.debug( "Read central record for entry: " + zcr.getFilename() );

                }
                else if( sig == ZIP_DATA_SIGNATURE )
                {
                	usesDataDescriptors = true;
                }
                else if( sig == ZIP_CENTRAL_END_SIGNATURE )
                {
                	logger.debug( "Central dir end" );
                }
            } while ( sig != -1 );
        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ZipLocalHeader getLocalHeader( int n )
    {
        return this.localHeaders.get( n );
    }


    public ZipCentralRecord getCentralRecord( int n )
    {
        return this.centralRecords.get( n );
    }


    public int getLocalHeaderCount()
    {
        return this.localHeaders.size();
    }


    public int getCentralRecordCount()
    {
        return this.centralRecords.size();
    }


    public String asXmlString()
    {
        logger.debug( "Creating XML representation of ZIP" );
        StringBuffer sb = new StringBuffer();
        sb.append( "<?xml version='1.0' standalone='yes'?><zip-archive><local-headers>" );

        for( int i = 0; i < this.localHeaders.size(); i++ )
        {
            sb.append( getLocalHeader( i ).asXmlString() );
        }

        sb.append( "</local-headers><central-directory>" );

        for( int i = 0; i < this.centralRecords.size(); i++ )
        {
            sb.append( getCentralRecord( i ).asXmlString() );
        }

        sb.append( "</central-directory></zip-archive>" );

        return sb.toString();
    }


    public boolean usesDataDescriptors()
    {
        return usesDataDescriptors;
    }

    
    private int skipData( InputStream is ) throws IOException
    {
    	// Read 4 first bytes
    	byte tmp[] =  new byte[4];
    	is.read( tmp );
    	
    	boolean bFound = false;
    	boolean bEOS = false;
    	int sig = -1;
    	
    	byte b[] = new byte[1];
    	do {
    		int value = Utils.readIntLittle( tmp );
    		bFound = ( value == ZIP_CENTRAL_SIGNATURE ) ||
    			( value == ZIP_DATA_SIGNATURE ) ||
    			( value == ZIP_LOCAL_SIGNATURE ) || 
    			( value == ZIP_CENTRAL_END_SIGNATURE );
    		
    		if ( bFound )
    			sig = value;
    		else {
    			bEOS = is.read( b ) < 0;
    			
	    		byte shift[] = new byte[4];
	    		System.arraycopy( tmp, 1, shift, 0, 3 );
	    		shift[3] = b[0];
	    		tmp = shift;
    		}
    	} while( !bEOS && !bFound );
    	
    	return sig;
    }
}
