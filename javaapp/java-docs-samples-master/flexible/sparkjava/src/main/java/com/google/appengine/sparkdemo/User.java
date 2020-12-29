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

import java.util.UUID;

public class User {

  private String id;
  private String name;
  private String email;

  /**
   * Construct a user given a name and email. An ID is auto-generated for the user.
   */
  public User(String name, String email) {
    this(UUID.randomUUID().toString(), name, email);
  }

  /**
   * Construct a user given an ID, name, and email.
   */
  public User(String id, String name, String email) {
    this.id = id;
    this.email = email;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
