/*
 * Copyright 2020 Google LLC
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

package functions;

import com.google.common.testing.TestLogHandler;
import com.google.common.truth.Truth;
import com.google.gson.Gson;
import functions.eventpojos.GcsEvent;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class OcrProcessImageTest {
  private static String FUNCTIONS_BUCKET = "nodejs-docs-samples-tests";

  private static final Logger logger = Logger.getLogger(
      OcrProcessImage.class.getName());

  private static final TestLogHandler LOG_HANDLER = new TestLogHandler();

  private static OcrProcessImage sampleUnderTest;

  @BeforeClass
  public static void setUpClass() throws IOException {
    sampleUnderTest = new OcrProcessImage();
    logger.addHandler(LOG_HANDLER);
  }

  @After
  public void afterTest() {
    LOG_HANDLER.clear();
  }

  @Test(expected = IllegalArgumentException.class)
  public void functionsOcrProcess_shouldValidateParams() throws IOException {
    sampleUnderTest.accept(new GcsEvent(), null);
  }

  @Test
  public void functionsOcrProcess_shouldDetectText() throws IOException {
    GcsEvent gcsEvent = new GcsEvent();
    gcsEvent.setBucket(FUNCTIONS_BUCKET);
    gcsEvent.setName("wakeupcat.jpg");

    sampleUnderTest.accept(gcsEvent, null);

    List<LogRecord> logs = LOG_HANDLER.getStoredLogRecords();
    Truth.assertThat(logs.get(1).getMessage()).contains(
        "Extracted text from image: Wake up human!");
    Truth.assertThat(logs.get(2).getMessage()).contains(
        "Detected language en for file wakeupcat.jpg");
  }
}
