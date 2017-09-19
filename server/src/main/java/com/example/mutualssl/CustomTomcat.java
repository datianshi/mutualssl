package com.example.mutualssl;


import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class CustomTomcat {

    @Value("${ssl.port}")
    int port;

    @Value("${ssl.keystore.file}")
    private Resource keystoreFile;
    @Value("${ssl.keystore.pass}")
    private String keystorePass;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() throws IOException {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addAdditionalTomcatConnectors(createConnector());
        return tomcat;
    }

    private Connector createConnector() throws IOException {
        String absoluteKeystoreFile = keystoreFile.getFile().getAbsolutePath();
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8443);
        connector.setSecure(true);
        connector.setScheme("https");


        Http11NioProtocol proto = (Http11NioProtocol) connector.getProtocolHandler();
        proto.setSSLEnabled(true);
        proto.setKeystoreFile(absoluteKeystoreFile);
        proto.setKeystorePass(keystorePass);
        proto.setKeystoreType("JKS");
        proto.setClientAuth("want");
        return connector;
    }

}
