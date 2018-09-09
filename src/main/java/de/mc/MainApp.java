package de.mc;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import org.apache.camel.main.Main;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A Camel Application
 */
public class MainApp {


    public static void main(String... args) throws Exception {
        Main main = new Main();
        AWSCredentials awsCredentials = new BasicAWSCredentials("byb5DVEIkNqJ9fptNJfh", "tQWwTXUL-Mavpi-GL7RzfiHmpN9S6nuEv1fj0TTq");


        ClientConfiguration clientConfiguration = new ClientConfiguration();

        AmazonS3 client = new AmazonS3Client(awsCredentials, clientConfiguration);
        client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
        client.setEndpoint("http://ladon.mind-consulting.de/services/s3");
        //((AmazonS3Client) client).setSignerRegionOverride("S3SignerType");
        main.bind("client", client);

        ScheduledExecutorService upload = Executors.newScheduledThreadPool(4);
        main.bind("uploadExecutor", upload);


        main.addRouteBuilder(new LadonRouteBuilder());
        main.run(args);
    }

}

