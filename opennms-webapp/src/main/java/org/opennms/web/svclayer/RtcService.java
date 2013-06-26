/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2007-2012 The OpenNMS Group, Inc.
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

package org.opennms.web.svclayer;

import org.opennms.core.criteria.CriteriaBuilder;
import org.opennms.netmgt.model.OnmsCriteria;
import org.opennms.web.svclayer.support.RtcNodeModel;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>RtcService interface.</p>
 *
 * @author <a href="mailto:dj@opennms.org">DJ Gregor</a>
 * @version $Id: $
 * @since 1.8.1
 */
@Transactional(readOnly = true)
public interface RtcService {
    /**
     * <p>getNodeList</p>
     *
     * @return a {@link org.opennms.web.svclayer.support.RtcNodeModel} object.
     */
    public RtcNodeModel getNodeList();
    /**
     * <p>getNodeListForCriteria</p>
     *
     * @param serviceCriteria a {@link org.opennms.netmgt.model.OnmsCriteria} object.
     * @param outageCriteria a {@link org.opennms.netmgt.model.OnmsCriteria} object.
     * @return a {@link org.opennms.web.svclayer.support.RtcNodeModel} object.
     */
    public RtcNodeModel getNodeListForCriteria(CriteriaBuilder serviceCriteria, CriteriaBuilder outageCriteria);
    /**
     * <p>createServiceCriteria</p>
     *
     * @return a {@link org.opennms.netmgt.model.OnmsCriteria} object.
     */
    public CriteriaBuilder createServiceCriteria();
    /**
     * <p>createOutageCriteria</p>
     *
     * @return a {@link org.opennms.netmgt.model.OnmsCriteria} object.
     */
    public CriteriaBuilder createOutageCriteria();
}
