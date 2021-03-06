package de.mc;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import org.apache.camel.main.Main;
import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A Camel Ladon Upload Application
 */
public class MainApp {


    public static void main(String... args) throws Exception {
        Main main = new Main();
        AWSCredentials awsCredentials = new BasicAWSCredentials("emrTasANHPRUPwY6IZIQ", "JfVVzYvYyG30yAE1-pN93CnCN1aVyzyVaB15Ez1C");


        ClientConfiguration clientConfiguration = getClientConfiguration();

        AmazonS3 client = new AmazonS3Client(awsCredentials, clientConfiguration);
        client.setS3ClientOptions(new S3ClientOptions().withPathStyleAccess(true));
        client.setEndpoint("http://localhost:8080/services/s3");
        main.bind("client", client);
        ScheduledExecutorService upload = Executors.newScheduledThreadPool(4);
        main.bind("uploadExecutor", upload);

        main.addRouteBuilder(new LadonRouteBuilder());
        main.run(args);
    }

    //Disable SSL v
    private static ClientConfiguration getClientConfiguration() throws NoSuchAlgorithmException, KeyManagementException {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[]{new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }}, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((string, ssls) -> true);
        clientConfiguration.getApacheHttpClientConfig().setSslSocketFactory(new SSLSocketFactory(sc));
        return clientConfiguration;
    }

}

