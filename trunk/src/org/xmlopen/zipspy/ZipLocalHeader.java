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

import org.apache.log4j.Logger;

/**
 * See Table C-2 in OOXML Pt 2
 */
public class ZipLocalHeader extends ZipHeaderBase
{

    static Logger logger = Logger.getLogger( ZipLocalHeader.class );
    static final short DATA_DESCRIPTOR_MASK = ( short )0x0008;


    public ZipLocalHeader( InputStream is )
    {
        try
        {

            is.read( this.extractVersion );
            this.general = Utils.readShortLittle( is );
            is.read( this.method );
            is.read( this.modTime );
            is.read( this.modDate );
            this.crc32 = Utils.readIntLittle( is );
            this.compressedSize = Utils.readIntLittle( is );
            this.uncompressedSize = Utils.readIntLittle( is );
            this.filenameLength = Utils.readShortLittle( is );
            this.extraFieldLength = Utils.readShortLittle( is );

            byte[] b = new byte[ filenameLength ];
            is.read( b );
            this.filename = new String( b ); // TODO: encoding??

            is.skip( this.extraFieldLength ); // not interested

        }
        catch( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public String asXmlString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "<local-header>" );

        sb.append( Utils.makeElement( "filename", this.filename ) );
        sb.append( Utils.makeByteElement( "version-needed-to-extract", this.extractVersion ) );
        sb.append( Utils.makeElement( "general-flag", this.general ) );
        sb.append( Utils.makeByteElement( "compression-method", this.method ) );
        sb.append( Utils.makeByteElement( "mod-time", this.extractVersion ) );
        sb.append( Utils.makeByteElement( "mod-date", this.extractVersion ) );
        sb.append( Utils.makeElement( "crc-32", this.crc32 ) );
        sb.append( Utils.makeElement( "compressed-size", this.compressedSize ) );
        sb.append( Utils.makeElement( "uncompressed-size", this.uncompressedSize ) );
        sb.append( Utils.makeElement( "filename-length", this.filenameLength ) );
        sb.append( Utils.makeElement( "extra-field-length", this.extraFieldLength ) );

        sb.append( "</local-header>" );

        return sb.toString();
    }

}
