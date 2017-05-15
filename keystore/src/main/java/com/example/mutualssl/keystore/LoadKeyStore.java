package com.example.mutualssl.keystore;


public class LoadKeyStore {

    private KeyStoreFactory keyStoreFactory;


    public static String TRUST_STORE = "TRUST_STORE";
    public static String TRUST_STORE_PASSWORD = "TRUST_STORE_PASSWORD";
    public static String KEY_STORE = "KEY_STORE";
    public static String KEY_STORE_PASSWORD = "KEY_STORE_PASSWORD";
    public static String BACK_END = "BACK_END";


    public LoadKeyStore() {
        String backend = System.getenv(BACK_END);
        if ("ENV".equals(backend)){
            keyStoreFactory = new EnvKeyStoreFactory();
        }
        else if("VAULT".equals(backend)){
            keyStoreFactory = new VaultKeyStoreFactory();
        }
        else{
            throw new IllegalArgumentException("BACK_END: " + backend + " is not supported");
        }
    }

    public void LoadKeyStore() {
        KeyStoreConfig keyStoreConfig = keyStoreFactory.createKeyStoreConfig();
        System.setProperty("javax.net.ssl.trustStore", keyStoreConfig.getTrustStore());
        System.setProperty("javax.net.ssl.trustStorePassword", keyStoreConfig.getTrustStorePass());
        System.setProperty("javax.net.ssl.keyStore",  keyStoreConfig.getKeyStore());
        System.setProperty("javax.net.ssl.keyStorePassword", keyStoreConfig.getKeyStorePass());
    }
}
