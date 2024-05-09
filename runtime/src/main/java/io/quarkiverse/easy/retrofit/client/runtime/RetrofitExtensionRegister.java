package io.quarkiverse.easy.retrofit.client.runtime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import io.github.liuziyuan.retrofit.core.RetrofitBuilderExtension;
import io.github.liuziyuan.retrofit.core.RetrofitInterceptorExtension;
import io.github.liuziyuan.retrofit.core.RetrofitResourceScanner;

public class RetrofitExtensionRegister {
    private static final Logger LOG = Logger.getLogger(RetrofitExtensionRegister.class);

    public RetrofitResourceScanner.RetrofitExtension scanRetrofitExtension(EnableRetrofitBean enableRetrofit) {
        // scan RetrofitExtension
        RetrofitResourceScanner scanner = new RetrofitResourceScanner();
        List<String> basePackages = getExtensionPackages(enableRetrofit);
        return scanner.doScanExtension(basePackages.toArray(new String[0]));
    }

    public List<RetrofitInterceptorExtension> getRetrofitInterceptorExtensions(
            Set<Class<? extends RetrofitInterceptorExtension>> retrofitInterceptorClasses) {
        List<RetrofitInterceptorExtension> retrofitInterceptorExtensions = new ArrayList<>();
        for (Class<? extends RetrofitInterceptorExtension> retrofitInterceptorClass : retrofitInterceptorClasses) {
            try {
                retrofitInterceptorExtensions.add(retrofitInterceptorClass.newInstance());
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        return retrofitInterceptorExtensions;
    }

    public RetrofitBuilderExtension getRetrofitBuilderExtension(
            Set<Class<? extends RetrofitBuilderExtension>> retrofitBuilderClasses) {
        if (retrofitBuilderClasses.size() > 1) {
            LOG.warn("There are multiple RetrofitBuilderExtension class, please check your configuration");
            return null;
        } else if (retrofitBuilderClasses.size() == 1) {
            Class<? extends RetrofitBuilderExtension> clazz = new ArrayList<>(retrofitBuilderClasses).get(0);
            try {
                return clazz.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    private List<String> getExtensionPackages(EnableRetrofitBean enableRetrofit) {
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(enableRetrofit.getExtensionPackages()).filter(StringUtils::isNoneBlank).toList());
        basePackages
                .addAll(Arrays.stream(enableRetrofit.getExtensionPackageClasses()).map(ClassUtils::getPackageName).toList());
        return basePackages;
    }
}
