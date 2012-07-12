package com.ibm;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.AggregatedTestResultAction;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;




/**
 * @author cactusman
 * @author justinedelson
 */
public class ConnectionsPlugin extends Notifier {
    private static final List<String> VALUES_REPLACED_WITH_NULL = Arrays.asList("", "(Default)", "(System Default)");

    private static final Logger LOGGER = Logger.getLogger(ConnectionsPlugin.class.getName());

    public final String connectionsUrl;

    public final String username;

    public final String password;
    
    @DataBoundConstructor
    public ConnectionsPlugin(String connectionsUrl, String username, String password) {
        LOGGER.info("ConnectionsPlugin");
        this.connectionsUrl = connectionsUrl;
        this.username = username;
        this.password = password;
        LOGGER.info("ConnectionsPlugin url:" + connectionsUrl);
        LOGGER.info("ConnectionsPlugin user:" + username );

    }
    public String getConnectionsUrl()
    {
        return connectionsUrl;
    }
    public String getUserName()
    {
        return username;
    }
    public String getPassword()
    {
        return password;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        LOGGER.info("getRequiredMonitorService");
        return BuildStepMonitor.BUILD;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) {
        LOGGER.info("performing Lotus connections update");
//        connectionsUrl = ((ConnectionsPlugin.DescriptorImpl)getDescriptor()).getConnectionsUrl();
//        username = ((ConnectionsPlugin.DescriptorImpl)getDescriptor()).getUserName();
//        password = ((ConnectionsPlugin.DescriptorImpl)getDescriptor()).getPassword();
        try {
            String newStatus = createStatusMessage(build);
            LOGGER.info("performing Lotus connections update with message " + newStatus);
            ((DescriptorImpl) getDescriptor()).updateConnections(newStatus, connectionsUrl, username, password);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unable to update Lotus Connections Status.", e);
        }
        LOGGER.info("performed Lotus connections update");
        return true;
    }

    private String createStatusMessage(AbstractBuild<?, ?> build) {
        String projectName = build.getProject().getName();
        String result = build.getResult().toString();
        String message = "hello";
        AggregatedTestResultAction atra = build.getAggregatedTestResultAction();
        if (null != atra)
        {
            LOGGER.info("There is an aggregated test result");
            // Tests Executed = 702 Tests Passed = 518 Tests Failed = 184 Pass Rate = 73%
            int totalTests = build.getAggregatedTestResultAction().getTotalCount();
            int passedTests = build.getAggregatedTestResultAction().getTotalCount();
            int failedTests = build.getAggregatedTestResultAction().getTotalCount();
            int passRate = (passedTests/totalTests) * 100;
            message = String.format("%s (%d) : Tests Executed = %d Tests Passed = %d Tests Failed = %d Pass Rate = %d%",
                    projectName, 
                    build.number,
                    totalTests, 
                    passedTests,
                    failedTests,
                    passRate);
        }
        else
        {
            AbstractTestResultAction tra = build.getTestResultAction();
            if (null != tra)
            {
                int totalTests = tra.getTotalCount();
                int failedTests = tra.getFailCount();
                int passedTests = totalTests - failedTests - tra.getSkipCount();
               
                float in1 = (100*passedTests);
                float passRate = (in1/totalTests);
                message = String.format("%s (%d) : Tests Executed = %d Tests Passed = %d Tests Failed = %d Pass Rate = %.2f %%",
                        projectName, 
                        build.number,
                        totalTests, 
                        passedTests,
                        failedTests,
                        passRate);
            }
            
        }
//            int failedTests = build.getTestResultAction().getFailCount();
        LOGGER.info("message = " + message);
        return message;
//        String toblame = "";
//        try {
//            if (!build.getResult().equals(Result.SUCCESS)) {
//                toblame = getUserString(build);
//            }
//        } catch (Exception ignore) {
//        }
//        String tinyUrl = "";
//        if (shouldIncludeUrl()) {
//            String absoluteBuildURL = ((DescriptorImpl) getDescriptor()).getUrl() + build.getUrl();
//            try {
//                tinyUrl = createTinyUrl(absoluteBuildURL);
//            } catch (Exception e) {
//                tinyUrl = "?";
//            }
//        }
//        return String.format("%s%s:%s $%d - %s", toblame, result, projectName, build.number, tinyUrl);
//        return String.format("%s (%d) : Tests Executed = %d Tests Passed = %d Tests Failed = %d Pass Rate = %d%",
//                projectName, 
//                build.number,
//                totalTests, 
//                passedTests,
//                failedTests,
//                passRate);
        
    }

    public DescriptorImpl getDescriptor() {
        // see Descriptor javadoc for more about what a descriptor is.
        return (DescriptorImpl)super.getDescriptor();
    }

    
//  //Save the form data
//    @Override
//    public boolean configure(StaplerRequest req, JSONObject formData) {
//        envs.replaceBy(req.bindParametersToList(ConnectionsDescriptor.class, "env."));
//        save();
//        return true;
//    }
//
//    //repopulate the saved form data
//    @Override
//    public BuildWrapper newInstance(StaplerRequest req, JSONObject formData)
//            throws FormException {
//        return req.bindJSON(EnvSelectorBuildWrapper.class, formData);
////      return super.newInstance(req, formData);
//    }
    
    
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
        private static final Logger LOGGER = Logger.getLogger(DescriptorImpl.class.getName());


        public DescriptorImpl() {
//            super(ConnectionsPlugin.class);
//            LOGGER.info("DescriptorImpl");
            load();
        }

//        public String getPassword() {
//            return password;
//        }
//
//        public String getUserName() {
//            return username;
//        }
//
//        public String getConnectionsUrl() {
//            return connectionsUrl;
//        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {

//            req.bindParameters(this, "lotusconnections.");
//            req.bindParameters(this, "lotusconnections.");
//            hudsonUrl = Mailer.descriptor().getUrl();

//            String username = formData.getString("username");
//            String password = formData.getString("password");
//            String connectionsUrl = formData.getString("connectionsUrl");

            save();
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Lotus Connections Notifier";
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

//        @Override
//        public Publisher newInstance(StaplerRequest req, JSONObject formData) throws FormException {
////            return req.bindJSON(this.getClass(), formData);
//            
//            req.bindParameters(this, "lotusconnections.");
//            load();
//            return super.newInstance(req, formData);
////            save();
//////            if (formData.has("twitterid")) {
//////                return req.bindJSON(UserTwitterProperty.class, formData);
////            return super.newInstance(req, formData);
//        }

        public void updateConnections(String message,
                String url,
                String user,
                String passwd) throws Exception {
            try
            {
                LOGGER.info("updateConnections");
               
                Poster poster = new Poster();
                LOGGER.info("Connecting to Connections Server --  " + url);
                poster.postStatus(url, user, passwd, message);
                LOGGER.info("Updated Connection status: " + message);
            }
            catch (Exception e)
            {
                LOGGER.info("updateConnections - Exception caught.");
                throw e;
            }
        }
        
        public String connectionsUrl;

        public String username;

        public String password;
        
        
        public String getConnectionsUrl() {
            return connectionsUrl;
        }
     
        public void setConnectionsUrl(String connectionsUrl) {
            this.connectionsUrl = connectionsUrl;
        }
     
        public String getUserName() {
            return username;
        }
     
        public void setUserName(String username) {
            this.username = username;
        }
     
        public String getPassword() {
            return password;
        }
     
        public void setPassword(String password) {
            this.password = password;
        }
    }
}