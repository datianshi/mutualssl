package com.example.mutualssl.keystore;


import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;

import java.io.*;
import java.util.Base64;

/**
 * Created by sding on 5/14/17.
 */
public class VaultKeyStoreFactory implements KeyStoreFactory {

    public static String VAULT_SERVER = "VAULT_SERVER";
    public static String VAULT_TOKEN = "VAULT_TOKEN";
    private Vault vault;

    public VaultKeyStoreFactory(){
        try {
            VaultConfig config = new VaultConfig(System.getenv(VAULT_SERVER), System.getenv(VAULT_TOKEN));
            vault = new Vault(config);
        } catch (VaultException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public KeyStoreConfig createKeyStoreConfig() {
        KeyStoreConfig storeConfig = new KeyStoreConfig();
        try {
            String trustStore = vault.logical().read(System.getenv(LoadKeyStore.TRUST_STORE)).getData().get("value");
            storeConfig.setTrustStore(tmpfileFromBase64(trustStore));
            String trustStorePass = vault.logical().read(System.getenv(LoadKeyStore.TRUST_STORE_PASSWORD)).getData().get("value");
            storeConfig.setTrustStorePass(trustStorePass);

            String keyStore = vault.logical().read(System.getenv(LoadKeyStore.KEY_STORE)).getData().get("value");
            storeConfig.setKeyStore((tmpfileFromBase64(keyStore)));
            String keyStorePass = vault.logical().read(System.getenv(LoadKeyStore.KEY_STORE_PASSWORD)).getData().get("value");
            storeConfig.setKeyStorePass(keyStorePass);
        } catch (VaultException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return storeConfig;
    }


    private String tmpfileFromBase64(String base64Str) throws IOException {
        byte[] content = Base64.getDecoder().decode(base64Str);
        File file = File.createTempFile("keyStore", ".jks");
        OutputStream ostream = new FileOutputStream(file);
        ostream.write(content);
        return file.getAbsolutePath();
    }
}
