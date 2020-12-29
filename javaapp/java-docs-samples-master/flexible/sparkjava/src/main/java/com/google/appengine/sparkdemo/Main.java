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

package com.google.appengine.sparkdemo;

import static spark.Spark.port;

import com.google.cloud.datastore.DatastoreOptions;

public class Main {

  /**
   * Starts the webapp on localhost:8080.
   */
  public static void main(String[] args) {
    port(8080);
    String kind = "DemoUser";
    if (args != null) {
      for (String arg : args) {
        if (arg.startsWith("kind=")) {
          kind = arg.substring("kind=".length());
        }
      }
    }
    UserController userController = new UserController(new UserService(
        DatastoreOptions.getDefaultInstance().getService(), kind));
  }
}
