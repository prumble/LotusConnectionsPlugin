package com.ibm;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Entry;
import org.apache.abdera.parser.Parser;
import org.apache.abdera.protocol.Response.ResponseType;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.lang.StringEscapeUtils;




public class Poster {
    
    /**
     * The constant NAME.
     */
    private static final String NAME = "POSTER";

    
    private Logger fLogger = Logger.getAnonymousLogger();
    private String fServer;
    private String fUser;
    private String fPasswd;
    private String fMessage;
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Poster poster = new Poster();
        poster.processArgs(args);
        poster.start();
    }

    public void postStatus(String server, String user, String password, String message)
    {
        if ((server != null) 
            && (user != null) 
            && (password != null) 
            && (message != null))
        {
            fServer = server;
            fUser = user;
            fPasswd = password;
            fMessage = message;
            start();
        }
    }

    

    
    private void start()
    {
     // Initialize the Atom Client to update the status in Connections
        Abdera abdera = new Abdera();
        AbderaClient client = new AbderaClient(abdera);
        AbderaClient.registerTrustManager();
        try
        {
            //client.addCredentials("https://w3-connections.ibm.com", null, null, new UsernamePasswordCredentials(fUser, fPasswd));
            client.addCredentials(fServer, null, null, new UsernamePasswordCredentials(fUser, fPasswd));
        }
        catch (URISyntaxException e)
        {
        e.printStackTrace();
        }
        
        Parser parser = abdera.getParser();
        String urlText = fServer +"/profiles/atom/mv/thebuzz/entry/status.do?email=" + fUser;
        // Create the Atom entry with the new status message
        Entry status = abdera.newEntry();
        status.addCategory("http://www.ibm.com/xmlns/prod/sn/type", "entry", null);
        status.addCategory("http://www.ibm.com/xmlns/prod/sn/message-type", "status", null);
        fMessage = StringEscapeUtils.escapeHtml(fMessage);
        status.setContent(fMessage);
        
     // Send the new status message to Connections
        ClientResponse response = client.put(urlText, status);
        
        if (response.getType() == ResponseType.SUCCESS)
        {
        // yipee
        System.out.println("woo hoo");
        }
        else
        {
        // WTH?
        System.out.println("oh no!");
        }
    }
    
    private void processArgs(String [] args)
    {
        fLogger.info("PADLER");
        fLogger.info("Developed by: Phil Rumble prumble@au1.ibm.com");
        // create the command line parser
        CommandLineParser parser = new PosixParser();
        // create the Options
        Options options = new Options();
        Option server = new Option("s", true, "Connections url");
        server.setRequired(true);
        Option user = new Option("u", true, "Connections username");
        user.setRequired(true);
        Option passwd = new Option("p", true, "Connections password");
        passwd.setRequired(false);
        Option message = new Option("m", true, "status message");
        message.setRequired(true);
        

        options.addOption(server);
        options.addOption(user);
        options.addOption(passwd);
        options.addOption(message);

//        options.addOption( build );
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);
            fServer = line.getOptionValue("s");
            fUser = line.getOptionValue("u");
            fPasswd = line.getOptionValue("p");
            fMessage = line.getOptionValue("m");
            if (null == fPasswd)
            {
                fPasswd = getUserInput("Please enter your Password:");
            }
            
        }
        catch (ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
         // automatically generate the help statement
            HelpFormatter formatter = new HelpFormatter();
            String usage = "\n" + Poster.NAME + " -s <server> -u <user> -p <passwd> -m <status message>\n"
                            + "\n Developed by: Phil Rumble prumble@au1.ibm.com";
            formatter.printHelp(usage, options);
            System.exit(-1);
        }
    }
    
    /**
     * Gets the user input.
     *
     * @param prompt the prompt
     * @return the user input
     */
    private String getUserInput(String prompt)
    {
        System.out.print(prompt);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String response = null;
        try {
            response = in.readLine();
        } catch (IOException e1) {
            fLogger.log(Level.SEVERE, "Exception occurred reading user input - Aborting", e1);
            System.exit(-1);
        }
        return response;
    }
}
