package com.bhu19.movie.reco.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Crawler {

    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);

    protected static int MAX_ROUTE = 20;
    protected static int MAX_TOTAL = 500;
    protected static int TIMEOUT = 30000;

    private static final CloseableHttpClient defaultHttpClient;
    private static final CloseableHttpClient sslCertUnverifiedHttpClient;

    protected CloseableHttpClient httpClient;

    protected String url;

    static {
        defaultHttpClient = init(MAX_TOTAL, MAX_ROUTE, TIMEOUT);
        sslCertUnverifiedHttpClient = init(MAX_TOTAL, MAX_ROUTE, TIMEOUT, false);
    }

    public Crawler(String url) {
        this.url = url;
        this.httpClient = this.httpClient == null ? defaultHttpClient : this.httpClient;
    }

    public Crawler(String url, boolean sslCertVerify) {
        this.url = url;
        this.httpClient = this.httpClient == null ? (sslCertVerify ? defaultHttpClient : sslCertUnverifiedHttpClient) : this.httpClient;
    }

    public Crawler(String url, CloseableHttpClient client) {
        this.url = url;
        this.httpClient = client;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 构建一个http请求对象
     *
     * @param url http请求地址
     * @return
     */
    public static Crawler of(String url) {
        return new Crawler(url);
    }

    /**
     * 构建一个http请求对象
     *
     * @param url           http请求地址
     * @param sslCertVerify 是否验证对方ssl证书
     * @return
     */
    public static Crawler of(String url, boolean sslCertVerify) {
        return new Crawler(url, sslCertVerify);
    }

    /**
     * 初始化http连接，设定相关参数
     *
     * @param maxTotal      最大连接数
     * @param maxPerRoute   最大路由数，指每个站点或者域名下支持的连接数
     * @param timeout       超时时间
     * @param sslCertVerify 是否验证对方ssl证书
     */
    public static CloseableHttpClient init(int maxTotal, int maxPerRoute, int timeout, boolean sslCertVerify) {
        if (!sslCertVerify) {
            try {
                SSLContext sslCtx = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> {
                    logger.info("Crawler loadTrustMaterial chain={} authType={} trusted.", chain, authType);
                    return true;
                }).build();
                SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslCtx, NoopHostnameVerifier.INSTANCE);
                Registry registry = RegistryBuilder.create()
                        .register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", factory)
                        .build();
                PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
                cm.setMaxTotal(maxTotal);
                cm.setDefaultMaxPerRoute(maxPerRoute);
                RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout).setConnectTimeout(timeout).setSocketTimeout(timeout).build();
                SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(timeout).build();
                return HttpClients.custom().setSSLSocketFactory(factory).setConnectionManager(cm).setDefaultRequestConfig(requestConfig).setDefaultSocketConfig(socketConfig).build();
            } catch (Exception e) {
                logger.error("Crawler init error e=", e);
            }
        }
        return init(maxTotal, maxPerRoute, timeout);
    }

    public static CloseableHttpClient init(int maxTotal, int maxPerRoute, int timeout) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout).setConnectTimeout(timeout).setSocketTimeout(timeout).build();
        SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(timeout).build();
        return HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).setDefaultSocketConfig(socketConfig).build();
    }

    public String doGet() {
        return this.doGet(new HashMap<>());
    }

    /**
     * 提交一个HTTP的GET请求
     *
     * @param params 参数
     * @return 纯文本
     */
    public String doGet(Map<String, Object> params) {
        long start = System.currentTimeMillis();
        String url = this.url;
        if (params != null && params.size() > 0) {
            StringBuilder builder = new StringBuilder(this.url);
            if (!this.url.contains("?")) {
                builder.append("?");
            }
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.append(entry.getKey()).append("=").append(entry.getValue().toString()).append("&");
            }
            int lastOffset = builder.length() - 1;
            if (builder.charAt(lastOffset) == '&') {
                builder.deleteCharAt(lastOffset);
            }
            url = builder.toString();
        }
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            long cost = System.currentTimeMillis() - start;
            if (response == null) {
                logger.error("[Crawler - doGet] - no response, cost={}ms, url={} ", cost, url);
                return null;
            }
            if (response.getStatusLine().getStatusCode() != 200) {
                httpGet.abort();
                logger.error("[Crawler - doGet] - status not 200, cost={}ms, status={}, url={} ", cost, response.getStatusLine().getStatusCode(), url);
                return null;
            }
            logger.debug("[Crawler - doGet] - succeed, cost={}ms, url={} ", cost, url);
            return StreamUtils.copyToString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            long cost = System.currentTimeMillis() - start;
            logger.error("[Crawler - doGet] - cost={}ms, url={},\t occur exception", cost, url, e);
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("[Crawler - doGet] - url={},\t when close occur exception", url, e);
            }
        }
    }

    /**
     * 提交一个HTTP的POST请求
     *
     * @return 纯文本
     */
    public String doPost(String params) {
        long start = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            if (params != null && params.length() > 0) {
                httpPost.setEntity(new StringEntity(params, ContentType.APPLICATION_FORM_URLENCODED.withCharset(StandardCharsets.UTF_8)));
            }

            response = httpClient.execute(httpPost);
            long cost = System.currentTimeMillis() - start;
            if (response == null) {
                logger.error("[Crawler] - no response, cost={}ms, url={}, params={}", cost, url, params);
                return null;
            }
            if (response.getStatusLine().getStatusCode() != 200) {
                httpPost.abort();
                logger.error("[Crawler] - status not 200, cost={}ms, status={}, url={}, params={}", cost, response.getStatusLine().getStatusCode(), url, params);
                return null;
            }
            logger.debug("[Crawler] - succeed, cost={}ms, url={}, params={}", cost, url, params);
            return Optional.ofNullable(response.getEntity().getContent()).map(Object::toString).orElse("");
        } catch (Exception e) {
            long cost = System.currentTimeMillis() - start;
            logger.error("[Crawler] - cost={}ms, url={}, params={},\t occur exception", cost, url, params, e);
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("[Crawler] - url={}, params={},\t when close occur exception", url, params, e);
            }
        }
    }

    /**
     * 提交一个HTTP的POST请求
     *
     * @return 纯文本
     */

    public String doPost(Map<String, Object> params) {
        long start = System.currentTimeMillis();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {
            if (params != null && params.size() > 0) {
                List<NameValuePair> pairs = new ArrayList<>(params.size());
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
            }

            response = httpClient.execute(httpPost);
            long cost = System.currentTimeMillis() - start;
            if (response == null) {
                logger.error("[Crawler - doPost] - no response, cost={}ms, url={} ", cost, url);
                return null;
            }
            if (response.getStatusLine().getStatusCode() != 200) {
                httpPost.abort();
                logger.error("[Crawler - doPost] - status not 200, cost={}ms, status={}, url={} ", cost, response.getStatusLine().getStatusCode(), url);
                return null;
            }
            logger.debug("[Crawler - doPost] - succeed, cost={}ms, url={} ", cost, url);
            return Optional.ofNullable(response.getEntity().getContent()).map(Object::toString).orElse("");
        } catch (Exception e) {
            long cost = System.currentTimeMillis() - start;
            logger.error("[Crawler - doPost] - cost={}ms, url={},\t occur exception", cost, url, e);
            return null;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                logger.error("[Crawler - doPost] - url={},\t when close occur exception", url, e);
            }
        }
    }
}
