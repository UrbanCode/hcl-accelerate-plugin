package com.hcl.devops.connect.SecuredActions;

import jenkins.model.Jenkins;
import hudson.model.AbstractItem;

import com.hcl.devops.connect.CloudWorkListener2;
import com.ibm.cloud.urbancode.connect.client.ConnectSocket;

import java.util.List;

public class TriggerJob extends AbstractSecuredAction {

    protected void run(AbstractSecuredAction.ParamObj paramObj) {
        TriggerJobParamObj triggerJobParamObj = (TriggerJobParamObj) paramObj;

        CloudWorkListener2 cwl = new CloudWorkListener2();
        cwl.callSecured(triggerJobParamObj.socket, triggerJobParamObj.event, 
                triggerJobParamObj.getJenkinsAuthenticationError(), triggerJobParamObj.args);
    }

    public class TriggerJobParamObj extends AbstractSecuredAction.ParamObj {

        public ConnectSocket socket;
        public String event;
        public Object[] args;

        public TriggerJobParamObj(ConnectSocket socket, String event, Object... args) {
            this.socket = socket;
            this.event = event;
            this.args = args;
        }
    }
}