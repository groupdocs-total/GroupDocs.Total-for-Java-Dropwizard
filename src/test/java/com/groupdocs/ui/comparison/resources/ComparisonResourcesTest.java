package com.groupdocs.ui.comparison.resources;

import com.groupdocs.ui.common.MainService;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import com.groupdocs.ui.common.entity.web.request.FileTreeRequest;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class ComparisonResourcesTest {
    @ClassRule
    public static final DropwizardAppRule<GlobalConfiguration> RULE =
            new DropwizardAppRule<>(MainService.class, System.getProperty("user.dir") + File.separator +"configuration.yml");

    private static Client client;

    @BeforeClass
    public static void setupClass() {
        client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
        client.property(ClientProperties.CONNECT_TIMEOUT, 5000);
        client.property(ClientProperties.READ_TIMEOUT,    5000);
    }

    @AfterClass
    public static void clearClass() {
        client.close();
    }

    @Test
    public void getView() {
        Response response = client.target(
                String.format("http://localhost:%d/comparison", RULE.getLocalPort()))
                .request()
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    public void loadFileTree() {
        FileTreeRequest fileTreeRequest = new FileTreeRequest();
        fileTreeRequest.setPath("");

        Response response = client.target(
                String.format("http://localhost:%d/comparison/loadFileTree", RULE.getLocalPort()))
                .request()
                .post(Entity.json(fileTreeRequest));
        assertThat(response.getStatus()).isEqualTo(200);
    }

    public void downloadDocument() {
    }

    public void uploadDocument() {
    }

    public void getStoragePath() {
    }

    public void compareWithPaths() {
    }

    public void compareFiles() {
    }

    public void compareWithUrls() {
    }

    public void loadResultPage() {
    }

    public void compare() {
    }

    public void multiCompare() {
    }
}