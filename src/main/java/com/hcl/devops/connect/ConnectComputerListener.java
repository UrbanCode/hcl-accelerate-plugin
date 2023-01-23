/*******************************************************************************
 * Licensed Materials - Property of IBM
 * (c) Copyright HCL Technologies Ltd. 2018, 2019, 2020, 2021. All Rights Reserved.
 *
 * Note to U.S. Government Users Restricted Rights:  Use,
 * duplication or disclosure restricted by GSA ADP Schedule
 * Contract with HCL Corp.
 *******************************************************************************/
package com.hcl.devops.connect;

import hudson.slaves.ComputerListener;
import jenkins.model.Jenkins;
import hudson.model.Computer;
import hudson.Extension;

import com.hcl.devops.connect.ReconnectExecutor;
import com.hcl.devops.connect.Endpoints.EndpointManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
@Extension
public class ConnectComputerListener extends ComputerListener {
	public static final Logger log = LoggerFactory.getLogger(ConnectComputerListener.class);

    private static CloudSocketComponent cloudSocketInstance;
    private static ReconnectExecutor reconnectExecutor;

    public static boolean isRabbitConnected(Entry entry) {
        return cloudSocketInstance.isAMQPConnected(entry);
    }

    private static void setCloudSocketComponent(CloudSocketComponent comp) {
        cloudSocketInstance = comp;
    }

    @Override
    public void onOnline(Computer c) {
        
        List<Entry> entries = Jenkins.getInstance().getDescriptorByType(DevOpsGlobalConfiguration.class).getEntries();
        for (Entry entry : entries) {
            if (c instanceof jenkins.model.Jenkins.MasterComputer && entry.isConfigured()) {
                String url = getConnectUrl(entry);
                String logPrefix = "[HCL Accelerate " + entry.getBaseUrl()
                        + "] ConnectComputerListener#onOnline - ";
                CloudWorkListener listener = new CloudWorkListener();
                ConnectComputerListener.setCloudSocketComponent(new CloudSocketComponent(listener, url));

                try {
                    log.info(logPrefix + "Connecting to Cloud Services...");
                    getCloudSocketInstance().connectToCloudServices(entry);
                } catch (Exception e) {
                    log.error(logPrefix + "Exception caught while connecting to Cloud Services: " + e);
                    e.printStackTrace();
                }

            // Synchronized to protect lazy initalization of static variable
            synchronized (this) {
                if (reconnectExecutor == null) {
                    reconnectExecutor = new ReconnectExecutor(cloudSocketInstance);
                    reconnectExecutor.startReconnectExecutor();
                }
                }
            }
        }
    }

    private String getConnectUrl(Entry entry) {
        EndpointManager em = new EndpointManager();
        return em.getConnectEndpoint(entry);
    }

    public CloudSocketComponent getCloudSocketInstance() {
        return ConnectComputerListener.cloudSocketInstance;
    }
}