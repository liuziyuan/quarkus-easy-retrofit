package io.quarkiverse.easy.retrofit.client.runtime;

import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import io.quarkus.arc.Arc;

public class QuarkusCDIBeanManager implements CDIBeanManager {

    @Override
    public <T> T getBean(Class<T> clazz) {
        try {
            return Arc.container().instance(clazz).get();
        } catch (Exception e) {
            return null;
        }
    }
}
