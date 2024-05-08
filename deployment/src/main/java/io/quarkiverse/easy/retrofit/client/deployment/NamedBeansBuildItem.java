package io.quarkiverse.easy.retrofit.client.deployment;

import java.util.List;

import io.quarkus.arc.processor.BeanInfo;
import io.quarkus.builder.item.MultiBuildItem;

public class NamedBeansBuildItem extends MultiBuildItem {
    private final List<BeanInfo> beanInfos;

    public NamedBeansBuildItem(List<BeanInfo> beanInfos) {
        this.beanInfos = beanInfos;
    }

    public List<BeanInfo> getBeanInfos() {
        return beanInfos;
    }

}
