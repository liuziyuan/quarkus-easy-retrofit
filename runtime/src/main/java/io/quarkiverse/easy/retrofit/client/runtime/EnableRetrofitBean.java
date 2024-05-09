package io.quarkiverse.easy.retrofit.client.runtime;

public class EnableRetrofitBean {

    private String[] value;

    private String[] basePackages;

    private Class<?>[] basePackageClasses;

    private String[] extensionPackages;

    private Class<?>[] extensionPackageClasses;

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public Class<?>[] getBasePackageClasses() {
        return basePackageClasses;
    }

    public void setBasePackageClasses(Class<?>[] basePackageClasses) {
        this.basePackageClasses = basePackageClasses;
    }

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    public String[] getExtensionPackages() {
        return extensionPackages;
    }

    public void setExtensionPackages(String[] extensionPackages) {
        this.extensionPackages = extensionPackages;
    }

    public Class<?>[] getExtensionPackageClasses() {
        return extensionPackageClasses;
    }

    public void setExtensionPackageClasses(Class<?>[] extensionPackageClasses) {
        this.extensionPackageClasses = extensionPackageClasses;
    }
}
