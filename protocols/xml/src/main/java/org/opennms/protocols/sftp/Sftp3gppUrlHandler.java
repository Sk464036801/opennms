/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2012 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2012 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.protocols.sftp;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * The class for handling SFTP+3GPP URL Connection.
 * 
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public class Sftp3gppUrlHandler extends URLStreamHandler {

    /** The Constant PROTOCOL. */
    public static final String PROTOCOL = "sftp.3gpp";

    /* (non-Javadoc)
     * @see java.net.URLStreamHandler#getDefaultPort()
     */
    @Override
    protected int getDefaultPort() {
        return 22;
    }

    /* (non-Javadoc)
     * @see java.net.URLStreamHandler#openConnection(java.net.URL)
     */
    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        return new Sftp3gppUrlConnection(url);
    }

}
