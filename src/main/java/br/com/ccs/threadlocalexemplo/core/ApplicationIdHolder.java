package br.com.ccs.threadlocalexemplo.core;

public abstract class ApplicationIdHolder {

    private static final ThreadLocal<String> applicationId = new ThreadLocal<>();

    public static void set(String id) {
        applicationId.set(id);
    }

    public static void remove() {
        applicationId.remove();
    }

    public static String get() {
        return applicationId.get();
    }
}
