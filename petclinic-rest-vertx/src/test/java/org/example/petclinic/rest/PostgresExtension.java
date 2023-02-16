package org.example.petclinic.rest;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresExtension implements BeforeAllCallback, AfterAllCallback {

  private static final Logger log = LoggerFactory.getLogger(PostgresExtension.class);

  final PostgreSQLContainer postgreSQLContainer;

  public PostgresExtension(int port) {
    DockerImageName postgres = DockerImageName.parse("postgres:14.0");
    postgreSQLContainer = new PostgreSQLContainer<>(postgres)
        .withDatabaseName("petclinic")
        .withUsername("postgres")
        .withPassword("petclinic")
        .withReuse(true)
        .withExposedPorts(port)
        .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
            new HostConfig()
                .withPortBindings(
                    new PortBinding(Ports.Binding.bindPort(port), new ExposedPort(port)))
        ));
  }

  @Override
  public void afterAll(ExtensionContext extensionContext) throws Exception {
    postgreSQLContainer.stop();
    log.info("### postgreSQLContainer stoped ###");
  }

  @Override
  public void beforeAll(ExtensionContext extensionContext) throws Exception {
    postgreSQLContainer.start();
    log.info("### fpostgreSQLContainer started ###");
  }
}