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

// [START functions_concepts_after_timeout]

package functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class AfterTimeout implements HttpFunction {
  private static final Logger logger = Logger.getLogger(AfterTimeout.class.getName());

  // Simple function to return "Hello World"
  @Override
  public void service(HttpRequest request, HttpResponse response)
      throws IOException, InterruptedException {
    logger.info("Function running...");
    TimeUnit.MINUTES.sleep(2);

    // May not execute if function's timeout is <2 minutes
    logger.info("Function completed!");
    BufferedWriter writer = response.getWriter();
    writer.write("Function completed!");
  }
}
// [END functions_concepts_after_timeout]
