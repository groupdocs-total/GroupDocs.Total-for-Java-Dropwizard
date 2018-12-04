package com.groupdocs.ui.viewer.resources;

import com.groupdocs.ui.common.MainService;
import com.groupdocs.ui.common.config.GlobalConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.ClientProperties;
import org.junit.ClassRule;
import org.junit.Test;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.io.File;
import static org.assertj.core.api.Assertions.assertThat;

public class ViewerResourcesTest {
    @ClassRule
    public static final DropwizardAppRule<GlobalConfiguration> RULE =
            new DropwizardAppRule<>(MainService.class, System.getProperty("user.dir") + File.separator +"configuration.yml");
    @Test
    public void getView() {
        Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");
        client.property(ClientProperties.CONNECT_TIMEOUT, 5000);
        client.property(ClientProperties.READ_TIMEOUT,    5000);
        Response response = client.target(
                String.format("http://localhost:%d/viewer", RULE.getLocalPort()))
                .request()
                .get();
        assertThat(response.getStatus()).isEqualTo(200);
        client.close();
    }

    public void loadFileTree() {
    }

    public void loadDocumentDescription() {
    }

    public void loadDocumentPage() {
    }

    public void rotateDocumentPages() {
    }

    public void downloadDocument() {
    }

    public void uploadDocument() {
    }

    public void getStoragePath() {
    }
}