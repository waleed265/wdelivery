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

package com.example.memcache;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

// [START example]
@SuppressWarnings("serial")
@WebServlet(name = "memcache", value = "")
public class MemcacheServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
      ServletException {
    String addr =
        System.getenv().containsKey("GAE_MEMCACHE_HOST")
            ? System.getenv("GAE_MEMCACHE_HOST") : "localhost";
    String port =
        System.getenv().containsKey("GAE_MEMCACHE_HOST")
            ? System.getenv("GAE_MEMCACHE_PORT") : "11211";
    String key = "count";
    MemcachedClientBuilder builder = new XMemcachedClientBuilder(
        AddrUtil.getAddresses(addr + ":" + port));
    MemcachedClient client = builder.build();
    long count = 0L;
    try {
      count = client.incr(key, 1L, 0L);
    } catch (TimeoutException | InterruptedException | MemcachedException e) {
      throw new ServletException("Memcache error", e);
    }
    resp.setContentType("text/plain");
    resp.getWriter().print("Value is " + count + "\n");
  }
}
// [END example]
