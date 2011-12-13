/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2011 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2011 The OpenNMS Group, Inc.
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
package org.opennms.protocols.xml.collector;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.collectd.BasePersister;
import org.opennms.netmgt.collectd.CollectionAgent;
import org.opennms.netmgt.collectd.GroupPersister;
import org.opennms.netmgt.collectd.ServiceCollector;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.collector.ServiceParameters;
import org.opennms.netmgt.model.RrdRepository;
import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.protocols.xml.config.XmlSource;
import org.opennms.protocols.xml.dao.jaxb.XmlDataCollectionConfigDaoJaxb;
import org.opennms.test.FileAnticipator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

/**
 * The Class XmlCollectorTest.
 * 
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
public class XmlCollectorTest {

    /** The file anticipator. */
    private FileAnticipator m_fileAnticipator;

    /** The SNMP directory. */
    private File m_rrdDirectory;

    /**
     * The Class MockXmlCollector.
     */
    private class MockXmlCollector extends XmlCollector {

        /* (non-Javadoc)
         * @see org.opennms.protocols.xml.collector.XmlCollector#getXmlDocument(org.opennms.netmgt.collectd.CollectionAgent, org.opennms.protocols.xml.config.XmlSource)
         */
        @Override
        public Document getXmlDocument(CollectionAgent agent, XmlSource source) {
            Document doc = null;
            try {
                log().info("getXmlDocument: loading G3PP testing data...");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setIgnoringComments(true);
                DocumentBuilder builder = factory.newDocumentBuilder();
                doc = builder.parse("src/test/resources/A20111025.0030-0500-0045-0500_MME00001.xml");
                doc.getDocumentElement().normalize();
                log().info("getXmlDocument: loaded data from " + doc.getBaseURI());
                return doc;
            } catch (Exception e) {
                Assert.fail(e.getMessage());
            }
            return doc;
        }

        /**
         * Log.
         *
         * @return the thread category
         */
        private ThreadCategory log() {
            return ThreadCategory.getInstance(getClass());
        }

    }

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        m_fileAnticipator = new FileAnticipator();
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
        m_fileAnticipator.deleteExpected();
    }

    /**
     * Test XML collector.
     *
     * @throws Exception the exception
     */
    @Test
    public void testXmlCollector() throws Exception {
        CollectionAgent agent = EasyMock.createMock(CollectionAgent.class);
        EasyMock.expect(agent.getNodeId()).andReturn(1).anyTimes();
        EasyMock.expect(agent.getHostAddress()).andReturn("127.0.0.1").anyTimes();
        EventProxy eproxy = EasyMock.createMock(EventProxy.class);
        EasyMock.replay(agent, eproxy);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("collection", "3GPP");

        XmlDataCollectionConfigDaoJaxb xmlCollectionDao = new XmlDataCollectionConfigDaoJaxb();
        Resource resource = new FileSystemResource("src/test/resources/xml-datacollection-config.xml");
        xmlCollectionDao.setConfigResource(resource);
        xmlCollectionDao.afterPropertiesSet();

        MockXmlCollector collector = new MockXmlCollector();
        collector.setXmlCollectionDao(xmlCollectionDao);
        CollectionSet collectionSet = collector.collect(agent, eproxy, parameters);
        Assert.assertEquals(ServiceCollector.COLLECTION_SUCCEEDED, collectionSet.getStatus());

        ServiceParameters serviceParams = new ServiceParameters(new HashMap<String,Object>());
        BasePersister persister =  new GroupPersister(serviceParams, createRrdRepository()); // storeByGroup=true;
        collectionSet.visit(persister);

        EasyMock.verify(agent, eproxy);
    }

    /**
     * Creates the RRD repository.
     *
     * @return the RRD repository
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private RrdRepository createRrdRepository() throws IOException {
        RrdRepository repository = new RrdRepository();
        repository.setRrdBaseDir(getRrdDirectory());
        repository.setHeartBeat(600);
        repository.setStep(300);
        repository.setRraList(Collections.singletonList("RRA:AVERAGE:0.5:1:100"));
        return repository;
    }

    /**
     * Gets the RRD directory.
     *
     * @return the RRD directory
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private File getRrdDirectory() throws IOException {
        if (m_rrdDirectory == null) {
            m_rrdDirectory = m_fileAnticipator.tempDir("snmp"); 
        }
        return m_rrdDirectory;
    }

}