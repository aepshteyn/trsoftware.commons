/*
 * Copyright 2020 TR Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package solutions.trsoftware.commons.server.servlet.testutil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import solutions.trsoftware.commons.server.servlet.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

import static java.util.Collections.enumeration;
import static java.util.Collections.singletonList;
import static solutions.trsoftware.commons.shared.util.CollectionUtils.isEmpty;


/**
 * Date: Jul 17, 2007
 * Time: 6:53:01 PM
 *
 * @author Alex
 */
public class DummyHttpServletRequest implements HttpServletRequest {

  private DummyHttpSession session = new DummyHttpSession();
  private String uri;
  private String url;
  private String queryString;
  private Map<String,String> paramMap = new HashMap<>();
  private String remoteAddr = "127.0.0.1";
  private List<Locale> locales;
  private String method;
  private List<Cookie> cookies = new ArrayList<>();
  private Map<String, Object> attributes = new LinkedHashMap<>();
  private HttpHeaders headers = new HttpHeaders(ArrayListMultimap.create());  // start with empty multimap for headers

  public DummyHttpServletRequest() {
  }

  public DummyHttpServletRequest(Map<String, String> paramMap) {
    this(null, null, paramMap);
  }

  public DummyHttpServletRequest(String url, String queryString) {
    this.url = url;
    this.queryString = queryString;
  }

  public DummyHttpServletRequest(String url, String queryString, Map<String, String> paramMap) {
    this(url, queryString);
    this.paramMap.putAll(paramMap);
  }

  public DummyHttpServletRequest(String method, String url, String queryString, Map<String, String> paramMap) {
    this(url, queryString, paramMap);
    this.method = method;
  }

  public DummyHttpServletRequest(DummyHttpSession session) {
    this.session = session;
  }

  public DummyHttpServletRequest(String url, SortedMap<String, String> params) {
    this(url, null, params);
  }

  public DummyHttpServletRequest setQueryString(String queryString) {
    this.queryString = queryString;
    return this;
  }

  public DummyHttpServletRequest setMethod(String method) {
    this.method = method;
    return this;
  }

  public DummyHttpServletRequest setSession(DummyHttpSession session) {
    this.session = session;
    return this;
  }

  public DummyHttpServletRequest setRequestURL(String url) {
    this.url = url;
    return this;
  }

  public DummyHttpServletRequest setRequestURI(String uri) {
    this.uri = uri;
    return this;
  }

  public DummyHttpServletRequest setRemoteAddr(String remoteAddr) {
    this.remoteAddr = remoteAddr;
    return this;
  }

  public DummyHttpServletRequest addCookie(Cookie cookie) {
    cookies.add(cookie);
    return this;
  }

  public String getAuthType() {
    System.err.println("Method DummyHttpServletRequest.getAuthType has not been fully implemented yet.");
    return null;
  }

  public Cookie[] getCookies() {
    return cookies.toArray(new Cookie[cookies.size()]);
  }

  public DummyHttpServletRequest setHeaders(Multimap<String, String> headers) {
    this.headers = new HttpHeaders(headers);
    return this;
  }

  @Override
  public long getDateHeader(String name) {
    return headers.getDateHeader(name);
  }

  @Override
  public String getHeader(String name) {
    return headers.getHeader(name);
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    return headers.getHeaders(name);
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    return headers.getHeaderNames();
  }

  @Override
  public int getIntHeader(String name) {
    return headers.getIntHeader(name);
  }

  public Multimap<String, String> getHeadersAsMultimap() {
    return headers.getHeadersAsMultimap();
  }

  public String getMethod() {
    return method;
  }

  public String getPathInfo() {
    System.err.println("Method DummyHttpServletRequest.getPathInfo has not been fully implemented yet.");
    return null;
  }

  public String getPathTranslated() {
    System.err.println("Method DummyHttpServletRequest.getPathTranslated has not been fully implemented yet.");
    return null;
  }

  public String getContextPath() {
    System.err.println("Method DummyHttpServletRequest.getContextPath has not been fully implemented yet.");
    return null;
  }

  public String getQueryString() {
    return queryString;
  }

  public String getRemoteUser() {
    System.err.println("Method DummyHttpServletRequest.getRemoteUser has not been fully implemented yet.");
    return null;
  }

  public boolean isUserInRole(String val) {
    System.err.println("Method DummyHttpServletRequest.isUserInRole has not been fully implemented yet.");
    return false;
  }

  public Principal getUserPrincipal() {
    System.err.println("Method DummyHttpServletRequest.getUserPrincipal has not been fully implemented yet.");
    return null;
  }

  public String getRequestedSessionId() {
    System.err.println("Method DummyHttpServletRequest.getRequestedSessionId has not been fully implemented yet.");
    return null;
  }

  public String getRequestURI() {
    return uri;
  }

  public StringBuffer getRequestURL() {
    return new StringBuffer(url != null ? url : ""); 
  }

  public String getServletPath() {
    System.err.println("Method DummyHttpServletRequest.getServletPath has not been fully implemented yet.");
    return null;
  }

  public HttpSession getSession(boolean b) {
    return getSession();
  }

  public HttpSession getSession() {
    return session;
  }

  @Override
  public String changeSessionId() {
    System.err.println("Method DummyHttpServletRequest.changeSessionId has not been fully implemented yet.");
    return null;
  }

  public boolean isRequestedSessionIdValid() {
    System.err.println("Method DummyHttpServletRequest.isRequestedSessionIdValid has not been fully implemented yet.");
    return false;
  }

  public boolean isRequestedSessionIdFromCookie() {
    System.err.println("Method DummyHttpServletRequest.isRequestedSessionIdFromCookie has not been fully implemented yet.");
    return false;
  }

  public boolean isRequestedSessionIdFromURL() {
    System.err.println("Method DummyHttpServletRequest.isRequestedSessionIdFromURL has not been fully implemented yet.");
    return false;
  }

  public boolean isRequestedSessionIdFromUrl() {
    System.err.println("Method DummyHttpServletRequest.isRequestedSessionIdFromUrl has not been fully implemented yet.");
    return false;
  }

  @Override
  public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
    System.err.println("Method DummyHttpServletRequest.authenticate has not been fully implemented yet.");
    return false;
  }

  @Override
  public void login(String username, String password) throws ServletException {
    System.err.println("Method DummyHttpServletRequest.login has not been fully implemented yet.");

  }

  @Override
  public void logout() throws ServletException {
    System.err.println("Method DummyHttpServletRequest.logout has not been fully implemented yet.");

  }

  @Override
  public Collection<Part> getParts() throws IOException, ServletException {
    System.err.println("Method DummyHttpServletRequest.getParts has not been fully implemented yet.");
    return null;
  }

  @Override
  public Part getPart(String name) throws IOException, ServletException {
    System.err.println("Method DummyHttpServletRequest.getPart has not been fully implemented yet.");
    return null;
  }

  @Override
  public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass) throws IOException, ServletException {
    System.err.println("Method DummyHttpServletRequest.upgrade has not been fully implemented yet.");
    return null;
  }

  public Object getAttribute(String name) {
    return attributes.get(name);
  }

  public Enumeration<String> getAttributeNames() {
    return enumeration(attributes.keySet());
  }

  public String getCharacterEncoding() {
    System.err.println("Method DummyHttpServletRequest.getCharacterEncoding has not been fully implemented yet.");
    return null;
  }

  public void setCharacterEncoding(String val) throws UnsupportedEncodingException {
    System.err.println("Method DummyHttpServletRequest.setCharacterEncoding has not been fully implemented yet.");

  }

  public int getContentLength() {
    System.err.println("Method DummyHttpServletRequest.getContentLength has not been fully implemented yet.");
    return 0;
  }

  @Override
  public long getContentLengthLong() {
    System.err.println("Method DummyHttpServletRequest.getContentLengthLong has not been fully implemented yet.");
    return 0;
  }

  public String getContentType() {
    System.err.println("Method DummyHttpServletRequest.getContentType has not been fully implemented yet.");
    return null;
  }

  public ServletInputStream getInputStream() throws IOException {
    System.err.println("Method DummyHttpServletRequest.getInputStream has not been fully implemented yet.");
    return null;
  }

  public String getParameter(String val) {
    return paramMap.get(val);
  }

  /**
   * This method is for unit testing; allows removing parameters from the map
   * @return the previous value associated with <tt>key</tt>, or
   *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
   */
  public String removeParameter(String name) {
    return paramMap.remove(name);
  }

  /**
   * This method is for unit testing; allows adding parameters to the map
   * @return the previous value associated with <tt>key</tt>, or
   *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
   *         (A <tt>null</tt> return can also indicate that the map
   *         previously associated <tt>null</tt> with <tt>key</tt>,
   *         if the implementation supports <tt>null</tt> values.)
   */
  public String putParameter(String name, String value) {
    return paramMap.put(name, value);
  }

  public Enumeration<String> getParameterNames() {
    System.err.println("Method DummyHttpServletRequest.getParameterNames has not been fully implemented yet.");
    return null;
  }

  public String[] getParameterValues(String val) {
    System.err.println("Method DummyHttpServletRequest.getParameterValues has not been fully implemented yet.");
    return new String[0];
  }

  public Map<String, String[]> getParameterMap() {
    // must return a map with array values to match the Servlet API spec
    Map<String, String[]> arrayMap = new HashMap<>();
    for (Map.Entry<String, String> entry : paramMap.entrySet()) {
      arrayMap.put(entry.getKey(), new String[]{entry.getValue()});
    }
    return arrayMap;
  }

  public String getProtocol() {
    System.err.println("Method DummyHttpServletRequest.getProtocol has not been fully implemented yet.");
    return null;
  }

  public String getScheme() {
    System.err.println("Method DummyHttpServletRequest.getScheme has not been fully implemented yet.");
    return null;
  }

  public String getServerName() {
    System.err.println("Method DummyHttpServletRequest.getServerName has not been fully implemented yet.");
    return null;
  }

  public int getServerPort() {
    System.err.println("Method DummyHttpServletRequest.getServerPort has not been fully implemented yet.");
    return 0;
  }

  public BufferedReader getReader() throws IOException {
    System.err.println("Method DummyHttpServletRequest.getReader has not been fully implemented yet.");
    return null;
  }

  public String getRemoteAddr() {
    return remoteAddr;
  }

  public String getRemoteHost() {
    System.err.println("Method DummyHttpServletRequest.getRemoteHost has not been fully implemented yet.");
    return null;
  }

  public void setAttribute(String name, Object object) {
    attributes.put(name, object);
  }

  public void removeAttribute(String name) {
    attributes.remove(name);
  }

  public Locale getLocale() {
    if (!isEmpty(locales))
      return locales.get(0);
    else
      return Locale.getDefault();
  }

  public Enumeration<Locale> getLocales() {
    if (!isEmpty(locales))
      return enumeration(locales);
    else
      return enumeration(singletonList(Locale.getDefault()));
  }

  public DummyHttpServletRequest setLocales(List<Locale> locales) {
    this.locales = locales;
    return this;
  }

  public DummyHttpServletRequest setLocale(Locale locale) {
    this.locales = singletonList(locale);
    return this;
  }

  public boolean isSecure() {
    System.err.println("Method DummyHttpServletRequest.isSecure has not been fully implemented yet.");
    return false;
  }

  public RequestDispatcher getRequestDispatcher(String val) {
    System.err.println("Method DummyHttpServletRequest.getRequestDispatcher has not been fully implemented yet.");
    return null;
  }

  public String getRealPath(String val) {
    System.err.println("Method DummyHttpServletRequest.getRealPath has not been fully implemented yet.");
    return null;
  }

  public int getRemotePort() {
    System.err.println("Method DummyHttpServletRequest.getRemotePort has not been fully implemented yet.");
    return 0;
  }

  public String getLocalName() {
    System.err.println("Method DummyHttpServletRequest.getLocalName has not been fully implemented yet.");
    return null;
  }

  public String getLocalAddr() {
    System.err.println("Method DummyHttpServletRequest.getLocalAddr has not been fully implemented yet.");
    return null;
  }

  public int getLocalPort() {
    System.err.println("Method DummyHttpServletRequest.getLocalPort has not been fully implemented yet.");
    return 0;
  }

  @Override
  public ServletContext getServletContext() {
    System.err.println("Method DummyHttpServletRequest.getServletContext has not been fully implemented yet.");
    return null;
  }

  @Override
  public AsyncContext startAsync() throws IllegalStateException {
    System.err.println("Method DummyHttpServletRequest.startAsync has not been fully implemented yet.");
    return null;
  }

  @Override
  public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
    System.err.println("Method DummyHttpServletRequest.startAsync has not been fully implemented yet.");
    return null;
  }

  @Override
  public boolean isAsyncStarted() {
    System.err.println("Method DummyHttpServletRequest.isAsyncStarted has not been fully implemented yet.");
    return false;
  }

  @Override
  public boolean isAsyncSupported() {
    System.err.println("Method DummyHttpServletRequest.isAsyncSupported has not been fully implemented yet.");
    return false;
  }

  @Override
  public AsyncContext getAsyncContext() {
    System.err.println("Method DummyHttpServletRequest.getAsyncContext has not been fully implemented yet.");
    return null;
  }

  @Override
  public DispatcherType getDispatcherType() {
    System.err.println("Method DummyHttpServletRequest.getDispatcherType has not been fully implemented yet.");
    return null;
  }

}
