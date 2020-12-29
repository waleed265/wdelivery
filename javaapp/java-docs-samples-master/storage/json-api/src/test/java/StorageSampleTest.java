/*
 * Copyright 2015 Google Inc.
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

// [START all]

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class StorageSampleTest {
  private static final String PROJECT_ID = System.getenv("GOOGLE_CLOUD_PROJECT");
  private static final String BUCKET = PROJECT_ID;
  private static final String TEST_OBJECT = "storage-sample-test-upload.txt";

  @Test
  public void testListBucket() throws Exception {
    List<StorageObject> listing = StorageSample.listBucket(BUCKET);
    assertThat(listing).isNotEmpty();
  }

  @Test
  public void testGetBucket() throws Exception {
    Bucket bucket = StorageSample.getBucket(BUCKET);
    assertWithMessage("bucket name").that(bucket.getName()).isEqualTo(BUCKET);
    assertWithMessage("bucket location").that(bucket.getLocation()).startsWith("US");
  }

  @Test
  public void testUploadDelete() throws Exception {
    // Create a temp file to upload
    Path tempPath = Files.createTempFile("StorageSampleTest", "txt");
    Files.write(tempPath, ("This object is uploaded and deleted as part of the "
            + "StorageSampleTest integration test.").getBytes());
    File tempFile = tempPath.toFile();
    tempFile.deleteOnExit();

    StorageSample.uploadFile(TEST_OBJECT, "text/plain", tempFile, BUCKET);

    try {
      // Verify that the object was created
      List<StorageObject> listing = StorageSample.listBucket(BUCKET);
      List<String> names = listing.stream().map(so -> so.getName()).collect(Collectors.toList());
      assertWithMessage("objects found after upload").that(names).contains(TEST_OBJECT);
    } finally {
      StorageSample.deleteObject(TEST_OBJECT, BUCKET);

      // Verify that the object no longer exists
      List<StorageObject> listing = StorageSample.listBucket(BUCKET);
      List<String> names = listing.stream().map(so -> so.getName()).collect(Collectors.toList());
      assertWithMessage("objects found after delete").that(names).doesNotContain(TEST_OBJECT);
    }
  }
}
// [END all]
