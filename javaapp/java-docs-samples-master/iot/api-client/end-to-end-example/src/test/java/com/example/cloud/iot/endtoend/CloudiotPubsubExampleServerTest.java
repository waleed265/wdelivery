/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cloud.iot.endtoend;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.cloudiot.v1.model.DeviceRegistry;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for iot End to End sample. */
@RunWith(JUnit4.class)
@SuppressWarnings("checkstyle:abbreviationaswordinname")
public class CloudiotPubsubExampleServerTest {
  private ByteArrayOutputStream bout;
  private PrintStream out;

  private static final String CLOUD_REGION = "us-central1";
  private static final String DEVICE_ID_TEMPLATE = "test-device-%s";
  private static final String DEVICE_ID =
      String.format(DEVICE_ID_TEMPLATE, System.currentTimeMillis() * 1000);
  private static final String TOPIC_ID =
      String.format("test-device-events-%d", System.currentTimeMillis() * 1000);
  private static final String PROJECT_ID = System.getenv("GOOGLE_CLOUD_PROJECT");
  private static final String REGISTRY_ID =
      String.format("test-registry-%d", System.currentTimeMillis() * 1000);

  @Before
  public void setUp() throws Exception {
    bout = new ByteArrayOutputStream();
    out = new PrintStream(bout);
    System.setOut(out);
  }

  @After
  public void tearDown() throws Exception {
    System.setOut(null);
  }

  @Test
  public void testConfigTurnOn() throws GeneralSecurityException, IOException, JSONException {
    int maxTemp = 11;
    JSONObject data = new JSONObject();

    try {
      CloudiotPubsubExampleServer.createRegistry(CLOUD_REGION, PROJECT_ID, REGISTRY_ID, TOPIC_ID);
    } catch (GoogleJsonResponseException ex) {
      if (!ex.isSuccessStatusCode() || ex.getDetails().getCode() == 429) {
        System.out.println("Cleaning up registry: " + REGISTRY_ID);
        // Clean up the 80% of old registries
        deleteUnusedOldRegistries(PROJECT_ID, CLOUD_REGION);
        // retry since the creation failed.
        CloudiotPubsubExampleServer.createRegistry(CLOUD_REGION, PROJECT_ID, REGISTRY_ID, TOPIC_ID);
      }
    }

    // Set up
    CloudiotPubsubExampleServer.createDevice(PROJECT_ID, CLOUD_REGION, REGISTRY_ID, DEVICE_ID);

    data.put("temperature", maxTemp);
    CloudiotPubsubExampleServer server = new CloudiotPubsubExampleServer();
    server.updateDeviceConfig(PROJECT_ID, CLOUD_REGION, REGISTRY_ID, DEVICE_ID, data);
    String got = bout.toString();
    Assert.assertTrue(got.contains("on"));
    Assert.assertTrue(got.contains("11"));
    Assert.assertTrue(got.contains("test-device-"));

    // Clean up
    CloudiotPubsubExampleServer.deleteDevice(DEVICE_ID, PROJECT_ID, CLOUD_REGION, REGISTRY_ID);
    CloudiotPubsubExampleServer.deleteRegistry(CLOUD_REGION, PROJECT_ID, REGISTRY_ID);
  }

  private void deleteUnusedOldRegistries(String projectId, String region)
      throws IOException, GeneralSecurityException {
    // Clean 50 oldest registries with testing prefix in the project.
    System.out.println("The maximum number of registries is about to exceed.");
    System.out.println("Deleting the oldest 50 registries with IoT Test prefix");

    // Gather all the registries into temp list
    List<DeviceRegistry> registries = CleanUpHelper.getRegisteries(PROJECT_ID, CLOUD_REGION);

    // Filter all registries with prefix.
    // since the list is already sorted by currentMillis suffix,
    // first 50 will be the oldest.
    List<DeviceRegistry> filteredRegistries = new ArrayList<>();

    for (int i = 0; i < registries.size(); i++) {
      DeviceRegistry registry = registries.get(i);
      if (registry.getName().contains("test-registry-")
          || registry.getName().contains("java-reg-")) {
        filteredRegistries.add(registry);
      }
    }

    // Delete the 50 oldest registries
    for (DeviceRegistry registry : filteredRegistries) {
      CleanUpHelper.clearRegistry(CLOUD_REGION, PROJECT_ID, registry.getId());
    }
  }

  @Test
  public void testConfigOff() throws GeneralSecurityException, IOException, JSONException {
    int minTemp = -1;
    JSONObject data = new JSONObject();

    // Set up
    CloudiotPubsubExampleServer.createRegistry(CLOUD_REGION, PROJECT_ID, REGISTRY_ID, TOPIC_ID);
    CloudiotPubsubExampleServer.createDevice(PROJECT_ID, CLOUD_REGION, REGISTRY_ID, DEVICE_ID);

    data.put("temperature", minTemp);

    CloudiotPubsubExampleServer server = new CloudiotPubsubExampleServer();
    server.updateDeviceConfig(PROJECT_ID, CLOUD_REGION, REGISTRY_ID, DEVICE_ID, data);
    String got = bout.toString();
    Assert.assertTrue(got.contains("off"));
    Assert.assertTrue(got.contains("-1"));
    Assert.assertTrue(got.contains("test-device-"));

    // Clean up
    CloudiotPubsubExampleServer.deleteDevice(DEVICE_ID, PROJECT_ID, CLOUD_REGION, REGISTRY_ID);
    CloudiotPubsubExampleServer.deleteRegistry(CLOUD_REGION, PROJECT_ID, REGISTRY_ID);
  }
}
