package io.quarkiverse.easy.retrofit.client.runtime;

import java.util.function.Supplier;

import io.github.liuziyuan.retrofit.core.RetrofitResourceContext;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class RetrofitRecorder {
    public RuntimeValue<RetrofitResourceContext> createRetrofitResourceContext(RetrofitResourceContext context) {
        return new RuntimeValue<>(context);
    }

    public Supplier<RetrofitResourceContext> getRetrofitResourceContextSupplier(RuntimeValue<RetrofitResourceContext> context) {
        return context::getValue;
    }
}
