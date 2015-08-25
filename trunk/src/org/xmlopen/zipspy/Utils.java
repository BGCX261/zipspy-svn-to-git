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
import java.io.OutputStream;

public class Utils
{

    public static void streamClose( InputStream is )
    {
        try
        {
            if( is != null )
                is.close();
        }
        catch( Exception e )
        {
            // do nothing
        }
    }


    public static void streamClose( OutputStream os )
    {
        try
        {
            if( os != null )
                os.close();
        }
        catch( Exception e )
        {
            // do nothing
        }
    }


    public static int readIntLittle( byte b[] ) throws IOException
    {
    	int n = ( 0x000000FF & ( ( int )b[ 3 ] ) );
        n <<= 8;
        n |= ( 0x000000FF & ( ( int )b[ 2 ] ) );
        n <<= 8;
        n |= ( 0x000000FF & ( ( int )b[ 1 ] ) );
        n <<= 8;
        n |= ( 0x000000FF & ( ( int )b[ 0 ] ) );

        return n;
    }
    
    public static int readIntLittle( InputStream is ) throws IOException
    {
        byte b[] = new byte[ 4 ];
        is.read( b );

        return readIntLittle( b );
    }


    public static short readShortLittle( InputStream is ) throws IOException
    {
        byte b[] = new byte[ 2 ];
        is.read( b );

        int n = ( 0x000000FF & ( ( int )b[ 1 ] ) );
        n <<= 8;
        n |= ( 0x000000FF & ( ( int )b[ 0 ] ) );

        return ( short )n;
    }


    public static String makeElement( String name, Object o )
    {
        StringBuffer sb = new StringBuffer();

        sb.append( "<" );
        sb.append( name );
        sb.append( ">" );
        sb.append( o.toString() );
        sb.append( "</" );
        sb.append( name );
        sb.append( ">" );

        return sb.toString();
    }


    public static String makeByteElement( String name, byte[] b )
    {
        StringBuffer sb = new StringBuffer();

        sb.append( "<" );
        sb.append( name );
        sb.append( ">" );
        for( int i = 0; i < b.length; i++ )
        {
            sb.append( "<byte>" );
            sb.append( Integer.toHexString( ( 0x000000FF & ( ( int )b[ i ] ) ) ).toUpperCase() );
            sb.append( "</byte>" );
        }

        sb.append( "</" );
        sb.append( name );
        sb.append( ">" );

        return sb.toString();
    }

}
