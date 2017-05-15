package com.example.mutualssl.keystore;

import com.google.common.io.Files;
import org.junit.*;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.io.File;
import java.security.KeyStore;

import static org.junit.Assert.*;

/**
 * Created by sding on 5/15/17.
 * This is supposed to be integration tests. Require a vault dependency setup
 */

@Ignore
public class VaultKeyStoreFactoryTest {

    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Before
    public void setup(){
        environmentVariables.set(LoadKeyStore.TRUST_STORE, "jks_store/trust_store");
        environmentVariables.set(LoadKeyStore.TRUST_STORE_PASSWORD, "secret/trust_pass");
        environmentVariables.set(LoadKeyStore.KEY_STORE, "jks_store/key_store");
        environmentVariables.set(LoadKeyStore.KEY_STORE_PASSWORD, "secret/key_pass");
        environmentVariables.set(LoadKeyStore.BACK_END, "vault");
        environmentVariables.set(VaultKeyStoreFactory.VAULT_TOKEN, "62f1578f-6450-cfb1-6115-06179ce3c6c2");
        environmentVariables.set(VaultKeyStoreFactory.VAULT_SERVER, "http://127.0.0.1:8200");
    }

    @Test
    public void createKeyStoreConfig() throws Exception {
        VaultKeyStoreFactory vaultKeyStoreFactory = new VaultKeyStoreFactory();
        KeyStoreConfig config = vaultKeyStoreFactory.createKeyStoreConfig();
        Assert.assertTrue(Files.asByteSource(new File(config.getKeyStore())).contentEquals(Files.asByteSource(new File("src/test/resources/key.jks"))));
        Assert.assertEquals("s3cr3t", config.getKeyStorePass());

        Assert.assertTrue(Files.asByteSource(new File(config.getTrustStore())).contentEquals(Files.asByteSource(new File("src/test/resources/trust.jks"))));
        Assert.assertEquals("s3cr3t", config.getTrustStorePass());
    }

}