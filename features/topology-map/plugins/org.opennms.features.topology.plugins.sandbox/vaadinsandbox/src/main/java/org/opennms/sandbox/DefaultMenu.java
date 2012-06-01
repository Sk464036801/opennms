package org.opennms.sandbox;

import java.net.MalformedURLException;
import org.vaadin.peter.contextmenu.ContextMenu;
import org.vaadin.peter.contextmenu.ContextMenu.ClickEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItem;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.Application;

/**
 * The DefaultMen class is a simple Vaadin Application that builds a Topology map
 * proof-of-concept environment with context menus and Node management windows.
 * @author Leonardo Bell
 * @author Philip Grenon
 * @version 1.0
 */
@SuppressWarnings("serial")
public class DefaultMenu extends Application{

    private static final long serialVersionUID = 1L;
    private HorizontalSplitPanel horizontalSplit = new HorizontalSplitPanel(); //Creates two panels to represent two Nodes.
    private VerticalLayout mainLayout = null; //Master container for all components on the main window.
    private VerticalLayout leftLayout = new VerticalLayout(); //Left side of Horizontal split panel which represents the Cartman node.
    private VerticalLayout rightLayout = new VerticalLayout(); //Right side of Horizontal split panel which represents the Butters node.
    private EventsAlarmsWindow EA_Window = null; //Sub-window which contains an embedded browser that shows the Events and Alarms for a node.
    private ResourceGraphsWindow RG_Window = null; //Sub-window which contains an embedded browser that shows the Resource Graphs for a node.
    private PingWindow Ping_Window = null; //Sub-window which contains the functionality for Pinging a node.
    private TracerouteWindow Trace_Window = null; //Sub-window which contains the functionality for Tracerouting a node.
    private NodeInfoWindow Info_Window = null; //Sub-window which contains the functionality for getting node information
    private SSHWindow SSH_Window = null;
    private Label cartman = new Label("Cartman"); //Name of the node which is displayed at the top of the Left side of the split panel.
    private Label butters = new Label("Butters"); //Name of the node which is displayed at the top of the right side of the split panel.
    private ContextMenu cartmanMenu = new ContextMenu(); //Context Menu that appears when right clicking on the left side of the split panel.
    private ContextMenu buttersMenu = new ContextMenu(); //Context Menu that appears when right clicking on the right side of the split panel.

    /*Test Data*/
    private Node testNode = new Node(34,"172.20.1.201","NC State");

    /**
     * The init method initializes the DefaultMenu Application and sets up the layouts and windows.
     */
    public void init() {
        buildMainLayout();
    }

    /**
     * The buildMainLayout method sets up all of the layouts and windows and adds all of 
     * the visible and invisible components of the application to the main window.
     */
    private void buildMainLayout() {
        setMainWindow(new Window("Topology Maps"));
        getMainWindow().setImmediate(true);

        mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.addComponent(createMenuBar());
        mainLayout.addComponent(horizontalSplit);

        leftLayout.setSizeFull();
        leftLayout.addComponent(cartman);

        rightLayout.setSizeFull();
        rightLayout.addComponent(butters);

        horizontalSplit.setFirstComponent(leftLayout);
        horizontalSplit.setSecondComponent(rightLayout);

        buildCartmanMenu(); //Left side of split panel
        buildButtersMenu(); //Right side of split panel

        /*Sets up a right click listener which brings up the Cartman Context menu*/
        leftLayout.addListener(new LayoutClickListener() {

            public void layoutClick(LayoutClickEvent event) {
                if (LayoutClickEvent.BUTTON_RIGHT == event.getButton()) {
                    cartmanMenu.show(event.getClientX(), event.getClientY());
                }else if(LayoutClickEvent.BUTTON_LEFT == event.getButton()) {
                    if(event.isCtrlKey()){
                        cartmanMenu.show(event.getClientX(), event.getClientY());
                    }
                }
            }

        });

        /*Sets up a right click listener which brings up the Butters Context menu*/
        rightLayout.addListener(new LayoutClickListener() {

            public void layoutClick(LayoutClickEvent event) {
                if (LayoutClickEvent.BUTTON_RIGHT == event.getButton()) {
                    buttersMenu.show(event.getClientX(), event.getClientY());
                }else if(LayoutClickEvent.BUTTON_LEFT == event.getButton()) {
                    if(event.isCtrlKey()){
                        buttersMenu.show(event.getClientX(), event.getClientY());
                    }
                }
            }

        });

        /* Allocate all available extra space to the horizontal split panel */
        mainLayout.setExpandRatio(horizontalSplit, 1);

        /*Adds all of the layouts and components to the main window for the Application.*/
        getMainWindow().setContent(mainLayout);
        getMainWindow().addComponent(cartmanMenu);
        getMainWindow().addComponent(buttersMenu);
    }

    /**
     * The createMenuBar method builds a simple Vaadin menu bar with File, View, and Node Options.
     * Listeners are also added for each option selected so that the corresponding window opens when clicked.
     * @return MenuBar component
     */
    private MenuBar createMenuBar() {
        final MenuBar menubar = new MenuBar();

        /*Sets up command for clicking on Node -> Events/Alarms option*/
        MenuBar.Command EA_Select = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                try {
                    showEventsAlarmsWindow();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        };

        /*Sets up command for clicking on Node -> Resource Graphs option*/
        MenuBar.Command RG_Select = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                try {
                    showResourceGraphsWindow();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        };

        /*Sets up command for clicking on Node -> Ping option*/
        MenuBar.Command Ping_Select = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                showPingWindow();
            }
        };

        /*Sets up command for clicking on Node -> Traceroute option*/
        MenuBar.Command Trace_Select = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                showTracerouteWindow();
            }
        };

        /*Sets up command for clicking on Node -> Node Info option*/
        MenuBar.Command NodeInfo_Select = new MenuBar.Command() {
            public void menuSelected(MenuItem selectedItem) {
                try {
                    showNodeInfoWindow();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        };
        
        MenuBar.Command SSH_Select = new MenuBar.Command() {
			
			public void menuSelected(MenuItem selectedItem) {
				showSSHWindow();
			}
		};

        /*Creates menu item dropdowns*/
        MenuBar.MenuItem file = menubar.addItem("File", null);
        MenuBar.MenuItem view = menubar.addItem("View", null);
        MenuBar.MenuItem node = menubar.addItem("Node Options", null);

        /* Add options to the main Menu bar along with commands for each*/
        file.addItem("Open", null);
        file.addItem("Close", null);
        view.addItem("Layouts", null);
        node.addItem("SSH", SSH_Select);
        node.addItem("Node Info", NodeInfo_Select);
        node.addItem("Events/Alarms", EA_Select);
        node.addItem("Ping", Ping_Select);
        node.addItem("Traceroute", Trace_Select);
        node.addItem("Resource Graphs", RG_Select);
        menubar.setWidth("100%");

        return menubar;
    }

    /**
     * The buildCartmanMenu method creates a Vaadin Context menu and adds it to the
     * left side of the split panel
     */
    private void buildCartmanMenu() {
        final ContextMenuItem ssh = cartmanMenu.addItem("SSH");
    	final ContextMenuItem nodeInfo = cartmanMenu.addItem("Node Info");
        final ContextMenuItem ping = cartmanMenu.addItem("Ping");
        final ContextMenuItem traceroute = cartmanMenu.addItem("Traceroute");
        final ContextMenuItem eventsAlarms = cartmanMenu.addItem("Events/Alarms");
        final ContextMenuItem resourceGraphs = cartmanMenu.addItem("Resource Graphs");
        cartmanMenu.addListener(new ContextMenu.ClickListener() {

            public void contextItemClick(ClickEvent event) {
                try {
                    if (eventsAlarms == event.getClickedItem()){
                        showEventsAlarmsWindow();
                    } else if (resourceGraphs == event.getClickedItem()){
                        showResourceGraphsWindow();
                    } else if (ping == event.getClickedItem()) {
                        showPingWindow();
                    } else if (traceroute == event.getClickedItem()) {
                        showTracerouteWindow();
                    } else if (nodeInfo == event.getClickedItem()) {
                        showNodeInfoWindow();
                    } else if (ssh == event.getClickedItem()) {
                    	showSSHWindow();
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * The buildButtersMenu method creates a Vaadin Context menu and adds it to the
     * right side of the split panel
     */
    private void buildButtersMenu() {
    	final ContextMenuItem ssh = cartmanMenu.addItem("SSH");
    	final ContextMenuItem nodeInfo = buttersMenu.addItem("Node Info");
        final ContextMenuItem eventsAlarms = buttersMenu.addItem("Events/Alarms");
        final ContextMenuItem resourceGraphs = buttersMenu.addItem("Resource Graphs");
        buttersMenu.addListener(new ContextMenu.ClickListener() {

            public void contextItemClick(ClickEvent event) {
                try {
                    if (eventsAlarms == event.getClickedItem()){
                        showEventsAlarmsWindow();
                    } else if (resourceGraphs == event.getClickedItem()){
                        showResourceGraphsWindow();
                    } else if (nodeInfo == event.getClickedItem()) {
                        showNodeInfoWindow();
                    } else if (ssh == event.getClickedItem()) {
                    	showSSHWindow();
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * The getNodeInfoWindow method initializes the Node Info Sub-window and returns it.
     * @return NodeInfoWindow component
     * @throws MalformedURLException 
     */
    private Window getNodeInfoWindow() throws MalformedURLException {
        Info_Window = new NodeInfoWindow(testNode, getMainWindow().getWidth(), getMainWindow().getHeight());
        return Info_Window;
    }

    /**
     * The showNodeInfoWindow method adds the Node info Sub-window to the main window of
     * the Application and makes it visible to the user.
     * @throws IllegalArgumentException
     * @throws NullPointerException
     * @throws MalformedURLException
     */
    private void showNodeInfoWindow() throws IllegalArgumentException, NullPointerException, MalformedURLException{
        getMainWindow().addWindow(getNodeInfoWindow());
    }

    /**
     * The getEventsAlarmsWindow method initializes the Events and Alarms Sub-window and returns it.
     * @return EventsAlarmsWindow component
     * @throws MalformedURLException
     */
    private EventsAlarmsWindow getEventsAlarmsWindow() throws MalformedURLException{
        EA_Window = new EventsAlarmsWindow(getMainWindow().getWidth(), getMainWindow().getHeight());
        return EA_Window;
    }

    /**
     * The showEventsAlarmsWindow method adds the Events and Alarms Sub-window to the main window of
     * the Application and makes it visible to the user.
     * @throws IllegalArgumentException
     * @throws NullPointerException
     * @throws MalformedURLException
     */
    private void showEventsAlarmsWindow() throws IllegalArgumentException, NullPointerException, MalformedURLException{
        getMainWindow().addWindow(getEventsAlarmsWindow());
    }

    /**
     * The getPingWindow method initializes the Ping Sub-window and returns it.
     * @return PingWindow component
     */
    private Window getPingWindow() {
        Ping_Window = new PingWindow(testNode, getMainWindow().getWidth(), getMainWindow().getHeight());
        return  Ping_Window;
    }

    /**
     * The showPingWindow method adds the Ping Sub-window to the main window of
     * the Application and makes it visible to the user.
     */
    private void showPingWindow(){
        getMainWindow().addWindow(getPingWindow());
    }

    /**
     * The getTracerouteWindow method initializes the Traceroute Sub-window and returns it.
     * @return TracerouteWindow component
     */
    private Window getTracerouteWindow() {
        Trace_Window = new TracerouteWindow(testNode, getMainWindow().getWidth(), getMainWindow().getHeight());
        return Trace_Window;
    }

    /**
     * The showTracerouteWindow method adds the Traceroute Sub-window to the
     * main window of the Application and makes it visible to the user.
     */
    private void showTracerouteWindow() {
        getMainWindow().addWindow(getTracerouteWindow());
    }

    /**
     * The getResourceGraphsWindow method initializes the Resource Graph Sub-window and returns it.
     * @return ResourceGraph component
     * @throws MalformedURLException
     */
    private ResourceGraphsWindow getResourceGraphsWindow() throws MalformedURLException{
        RG_Window = new ResourceGraphsWindow(testNode, getMainWindow().getWidth(), getMainWindow().getHeight());
        return RG_Window;
    }

    /**
     * The showResourceGraphsWindow method adds the Resource Graph Sub-window to the main window of
     * the Application and makes it visible to the user.
     * @throws IllegalArgumentException
     * @throws NullPointerException
     * @throws MalformedURLException
     */
    private void showResourceGraphsWindow() throws IllegalArgumentException, NullPointerException, MalformedURLException{
        getMainWindow().addWindow(getResourceGraphsWindow());
    }
    
    private SSHWindow getSSHWindow() {
    	SSH_Window = new SSHWindow(testNode, getMainWindow().getWidth(), getMainWindow().getHeight());
    	return SSH_Window;
    }
    private void showSSHWindow() {
    	getMainWindow().addWindow(getSSHWindow());
    }

}