package com.example.flyapigateway;

import com.flyCommon.common.ErrorCode;
import com.flyCommon.exception.BusinessException;
import com.flyCommon.model.entity.InterfaceInfoNew;
import com.flyCommon.model.entity.User;
import com.flyCommon.service.InnerInterfaceInfoNewService;
import com.flyCommon.service.InnerUserInterfaceInfoService;
import com.flyCommon.service.InnerUserService;
import com.flySdk.Utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * 全局过滤
 *
 * @author fly
 */
@Component
@Slf4j
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    // 白名单
    public static final List<String> WHIT_LIST = Arrays.asList("127.0.0.1", "localhost", "0:0:0:0:0:0:0:1");
    public static final Long FIVE_MINUTES = 60 * 5L;
    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    @DubboReference
    private InnerInterfaceInfoNewService innerInterfaceInfoNewService;

    private static final List<String> INTERFACE_HOST = Arrays.asList("http://localhost:7550");

    /**
     * @param exchange 响应交互
     * @param chain    过滤链
     * @return Mono
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. 用户发送请求到API网关
        // yml 已经配置好了

        // 2. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识=>" + request.getId());
        String method = Objects.requireNonNull(request.getMethod()).toString();
        log.info("请求方法=>" + method);
        log.info("请求来源地址=>" + Objects.requireNonNull(request.getLocalAddress()).getHostString());
        log.info("请求参数=>" + request.getQueryParams());

        String path = INTERFACE_HOST.get(0) + request.getPath().value().replaceFirst("^/api", "");
        log.info("请求路径" + path);
        ServerHttpResponse response = exchange.getResponse();
        // 3. 黑白名单
        String address = request.getLocalAddress().getHostString();
        if (!WHIT_LIST.contains(address)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            // 结束任务
            return response.setComplete();
        }

        // 4. 用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // 根据ak查到用户信息
        User invokeUserAkSkValid = innerUserService.getInvokeUserAkSkValid(accessKey);
        if (invokeUserAkSkValid == null || invokeUserAkSkValid.getSecretKey() == null) {
            throw new BusinessException(ErrorCode.NO_RIGHT_ERROR);
        }
        // 1. todo 使用数据库查询时候是有这个用户  "5a8bd83a0019aea1b4caa846bac07426"
        if (!Objects.equals(accessKey, invokeUserAkSkValid.getAccessKey())) {
            log.error("accessKey error");
            return handlerNoAuth(response);
        }

        //  2. todo 随机数，采用hashmap或者redis进行存储
        if (nonce == null || Long.parseLong(nonce) > 100000) {
            return handlerNoAuth(response);
        }

        // 3. todo 校验时间不能超过5min
        if (timestamp != null) {
            long time = System.currentTimeMillis() / 1000 - Long.parseLong(timestamp);
            if (time / 1000 > FIVE_MINUTES) {
                return handlerNoAuth(response);
            }
        } else {
            return handlerNoAuth(response);
        }

        // 4. todo 校验body,实际请求是从数据库获取的
        String secretKey = invokeUserAkSkValid.getSecretKey();
//        "b55db76db7b87adac5df9215dc0afcb8"
        String signServer = SignUtils.genSign(body, secretKey);
        if (!Objects.equals(sign, signServer)) {
            log.error("sign error");
            return handlerNoAuth(response);
        }

        // 5. 请求模拟接口是否存在
        // todo 数据库查询模拟接口是否存在，检验请求参数，方法是否一致
        InterfaceInfoNew interfaceInfo = innerInterfaceInfoNewService.getInterfaceInfo(path, method, request.getQueryParams().toString());
        if (interfaceInfo == null) {
            log.error("interfaceInfo error");
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 6. 响应日志
        log.info("响应码=>" + response.getStatusCode());

        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUserAkSkValid.getId());
        // 7. 调用成功，接口调用次数+1
        // todo 调用接口

        // 8. 调用失败，错误码
//        if (response.getStatusCode() != HttpStatus.OK) {
//            return handlerInvokeError(response);
//        }

//        log.info("custom global filter");
//        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }


    /**
     * 处理异常，判断是否有权限
     *
     * @param response
     * @return
     */
    public Mono<Void> handlerNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    /**
     * 调用接口失败
     *
     * @param response
     * @return
     */
    public Mono<Void> handlerInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


    /**
     * 装饰着模式
     *
     * @param exchange
     * @param chain
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    private Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceInfoId, Long userId) {
        try {
            // 从交换机拿到原始response
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓冲区工厂 拿到缓存数据
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到状态码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        // 对象是响应式的
                        if (body instanceof Flux) {
                            // 我们拿到真正的body
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里面写数据
                            // 拼接字符串
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // todo 7. 调用成功，接口调用次数+1
                                Boolean aBoolean = innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                if (!aBoolean) {
                                    log.error("invokeCount error");
                                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
                                }

                                // data从这个content中读取
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);// 释放掉内存
                                // 6.构建日志
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                String data = new String(content, StandardCharsets.UTF_8);// data
                                rspArgs.add(data);
                                log.info("<--- status:{} data:{}"// data
                                        , rspArgs.toArray());// log.info("<-- {} {}", originalResponse.getStatusCode(), data);
                                log.info("响应结果" + data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            // 8.调用失败返回错误状态码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);// 降级处理返回数据
        } catch (Exception e) {
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }

    }
}