package io.quarkiverse.easy.retrofit.client.runtime;

import io.github.liuziyuan.retrofit.core.CDIBeanManager;
import io.quarkus.arc.ArcContainer;
import io.quarkus.arc.InstanceHandle;

public class QuarkusCDIBeanManager implements CDIBeanManager {
    private final ArcContainer arcContainer;

    public QuarkusCDIBeanManager(ArcContainer arcContainer) {
        this.arcContainer = arcContainer;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        try (InstanceHandle<T> instance = arcContainer.instance(clazz)) {
            if (instance.isAvailable()) {
                return instance.get();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
