package io.quarkiverse.easy.retrofit.client.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;

public class RetrofitAnnotationBeanRegister {
    private RetrofitResourceScanner scanner;

    public Set<Class<?>> scanRetrofitResource(EnableRetrofitBean enableRetrofit) {
        // scan RetrofitResource
        scanner = new RetrofitResourceScanner();
        List<String> basePackages = getBasePackages(enableRetrofit);
        return scanner.doScan(basePackages.toArray(new String[0]));
    }

    public List<String> getBasePackages(EnableRetrofitBean enableRetrofit) {
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(enableRetrofit.getValue()).filter(StringUtils::isNoneBlank).toList());
        basePackages.addAll(Arrays.stream(enableRetrofit.getBasePackages()).filter(StringUtils::isNoneBlank).toList());
        basePackages.addAll(Arrays.stream(enableRetrofit.getBasePackageClasses()).map(ClassUtils::getPackageName).toList());
        return basePackages;
    }
}
