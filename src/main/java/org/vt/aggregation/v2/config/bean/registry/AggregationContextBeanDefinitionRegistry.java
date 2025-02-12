package org.vt.aggregation.v2.config.bean.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.vt.aggregation.v2.config.properties.ContextProperties;
import org.vt.aggregation.v2.service.client.ClientStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class AggregationContextBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {

    private static final Pattern NON_ALPHA_DIGIT_PATTERN =  Pattern.compile("[^a-zA-Z0-9]");

    private static final String CLIENT_SUFFIX = "Client";
    private static final String DATA_HANDLER_SUFFIX = "DataHandler";

    private final ContextProperties contextProperties;
    private final Map<String, String> clientBeanNames;
    private final List<String> dataHandlerBeanNames;

    public AggregationContextBeanDefinitionRegistry(Environment environment) {
        Binder binder = Binder.get(environment);
        this.contextProperties = binder.bind("context", Bindable.of(ContextProperties.class)).get();
        this.clientBeanNames = new HashMap<>();
        this.dataHandlerBeanNames = new ArrayList<>();
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("ClientStrategy bean definition registry");

        for (ContextProperties.Connection connection : contextProperties.getConnections()) {

            var constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addIndexedArgumentValue(0, connection);

            var connectionStrategy = connection.getStrategy();

            var genericBeanDefinition = new GenericBeanDefinition();
            genericBeanDefinition.setBeanClass(connectionStrategy.getClientClass());
            genericBeanDefinition.setAutowireCandidate(true);
            genericBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);
            genericBeanDefinition.setScope("singleton");

            var connectionName = connection.getName();
            var strategyName = StringUtils.capitalize(connectionStrategy.getValue());
            var beanName = connectionName + strategyName + CLIENT_SUFFIX;

            registry.registerBeanDefinition(beanName, genericBeanDefinition);

            clientBeanNames.put(connectionName, beanName);
        }

        log.info("Registered ClientStrategy definitions: {}", clientBeanNames.values());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        if (contextProperties.getDomains() == null) {
            return;
        }

        Map<ContextProperties.Group, List<ContextProperties.EntityDefinitions>> domains = contextProperties.getDomains();

        for (var domainEntry : domains.entrySet()) {
            ContextProperties.Group group = domainEntry.getKey();
            List<ContextProperties.EntityDefinitions> entityDefinitions = domainEntry.getValue();

            for (var entityDefinition : entityDefinitions) {

                String connectionName = entityDefinition.getConnectionName();

                var clientBeanName = clientBeanNames.get(connectionName);

                var clientStrategyBean = beanFactory.getBean(clientBeanName, ClientStrategy.class);

                var dataHandlerProcess = DataHandlerProcessFactory.createDataHandler(group, clientStrategyBean, entityDefinition);

                var strategyName = StringUtils.capitalize(clientStrategyBean.strategy().getValue());
                var entityName = StringUtils.capitalize(NON_ALPHA_DIGIT_PATTERN.matcher(entityDefinition.getEntity()).replaceAll(StringUtils.EMPTY));
                var conName = StringUtils.lowerCase(connectionName);

                var handlerBeanName = conName + entityName + strategyName + DATA_HANDLER_SUFFIX;

                beanFactory.registerSingleton(handlerBeanName, dataHandlerProcess);
                beanFactory.autowireBean(dataHandlerProcess);

                dataHandlerBeanNames.add(handlerBeanName);
            }

        }

        log.info("Registered data handler beans: {}", dataHandlerBeanNames);
    }

}
