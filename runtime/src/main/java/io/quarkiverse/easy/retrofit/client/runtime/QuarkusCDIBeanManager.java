package io.quarkiverse.easy.retrofit.client.runtime;

import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import io.quarkus.arc.Arc;

public class QuarkusCDIBeanManager implements CDIBeanManager {

    @Override
    public <T> T getBean(Class<T> clazz) {
        return Arc.container().instance(clazz).get();
    }
}
