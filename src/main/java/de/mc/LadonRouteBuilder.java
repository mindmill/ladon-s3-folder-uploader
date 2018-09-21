package de.mc;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;

/**
 * Ladon Filesystem Folder Uploader
 */
public class LadonRouteBuilder extends RouteBuilder {


    public void configure() {

        from("file:input?recursive=true&maxMessagesPerPoll=1000&scheduledExecutorService=#uploadExecutor")
                .log("Uploading ${file:name} to Ladon")
                .setHeader(S3Constants.KEY, simple("${file:path}"))
                .to("aws-s3://photos?amazonS3Client=#client");
    }

}
