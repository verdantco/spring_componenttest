package org.verdantco.spring_componenttest;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.testcontainers.containers.ComposeContainer;

public class spring_componenttestPostProcessor implements BeanPostProcessor {

    private enum ServiceInfo {
        POSTGRES(5432),
        MYSQL(3306);

        public int port;

        private ServiceInfo(int port) {
            this.port = port;
        }
    }

    private ComposeContainer container;

    public spring_componenttestPostProcessor() {
        container = new ComposeContainer();
    }

    private DataSourceProperties registerDataSource(DataSourceProperties props) {
        return switch (props.getDriverClassName()) {
            case "org.postgresql.Driver" -> {
                var newProps = new DataSourceProperties();

                newProps.setUrl("postgres");

                yield newProps;
            }
            case "com.mysql.jdbc.Driver" -> {
                var newProps = new DataSourceProperties();

                newProps.setUrl("postgres");

                yield newProps;
            }
            default -> props;
        };
    };

    @Override
    public Object postProcessAfterInitialization(Object bean, String name) {

        return switch (bean) {
            case DataSourceProperties props -> registerDataSource(props);
            default -> bean;
        };
    }

}
