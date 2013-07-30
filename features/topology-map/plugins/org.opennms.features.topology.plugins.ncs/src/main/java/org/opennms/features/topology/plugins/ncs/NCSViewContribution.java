package org.opennms.features.topology.plugins.ncs;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;
import org.opennms.features.topology.api.IViewContribution;
import org.opennms.features.topology.api.SelectionManager;
import org.opennms.features.topology.api.WidgetContext;
import org.opennms.features.topology.api.osgi.VaadinApplicationContext;
import org.opennms.features.topology.api.support.FilterableHierarchicalContainer;
import org.opennms.features.topology.api.topo.Criteria;
import org.opennms.features.topology.api.topo.Edge;
import org.opennms.features.topology.api.topo.VertexRef;
import org.opennms.features.topology.plugins.ncs.internal.NCSCriteriaServiceManager;
import org.opennms.netmgt.model.ncs.NCSComponentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class NCSViewContribution implements IViewContribution {
	
	private NCSComponentRepository m_ncsComponentRepository;
	private NCSEdgeProvider m_ncsEdgeProvider;
    private NCSCriteriaServiceManager m_serviceManager;
    private int m_serviceCount = 0;

    public void setNcsComponentRepository(NCSComponentRepository ncsComponentRepository) {
		m_ncsComponentRepository = ncsComponentRepository;
	}

    @Override
    public Component getView(final VaadinApplicationContext applicationContext, final WidgetContext widgetContext) {
		final Tree tree = new Tree("Services", new FilterableHierarchicalContainer(new NCSServiceContainer(m_ncsComponentRepository)));
		tree.setMultiSelect(true);
		tree.setImmediate(true);
		tree.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		tree.setItemCaptionPropertyId("name");
		tree.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = -7443836886894714291L;
			
			public void valueChange(ValueChangeEvent event) {
				Collection<Long> selectedIds = new HashSet<Long>( (Collection<Long>) event.getProperty().getValue() );
				Collection<Long> nonSelectableIds = new ArrayList<Long>();
				
				for(Long id : selectedIds) {
				    boolean isRoot = (Boolean) tree.getItem(id).getItemProperty("isRoot").getValue();
				    if(id < 0 && isRoot) {
				        nonSelectableIds.add(id);
				    }
				}
				selectedIds.removeAll(nonSelectableIds);
				for(Long id : nonSelectableIds) {
				    tree.unselect(id);
				}
				
				Criteria criteria = NCSEdgeProvider.createCriteria(selectedIds);
				
				m_serviceManager.registerCriteria(applicationContext, criteria);
                if(m_serviceManager.isCriteriaRegistered(applicationContext, "ncsPath")) {
                    m_serviceManager.unregisterCriteria(applicationContext, "ncsPath");
                }
				selectVerticesForEdge(criteria, widgetContext.getGraphContainer().getSelectionManager());
			}
		});
		return tree;
	}

    protected void selectVerticesForEdge(Criteria criteria, SelectionManager selectionManager) {
	    List<VertexRef> vertexRefs = new ArrayList<VertexRef>();
	    List<Edge> edges = m_ncsEdgeProvider.getEdges(criteria);
	    for(Edge ncsEdge : edges) {
	        vertexRefs.add(ncsEdge.getSource().getVertex());
	        vertexRefs.add(ncsEdge.getTarget().getVertex());
	    }
	    selectionManager.setSelectedVertexRefs(vertexRefs);
	    
    }

    @Override
	public String getTitle() {
		return "Services";
	}

	@Override
	public Resource getIcon() {
		return null;
	}
	
	public void setNcsEdgeProvider(NCSEdgeProvider ncsEdgeProvider) {
        m_ncsEdgeProvider = ncsEdgeProvider;
    }
	
	public void setNcsCriteriaServiceManager(NCSCriteriaServiceManager manager) {
	    m_serviceManager = manager;
	}

}
