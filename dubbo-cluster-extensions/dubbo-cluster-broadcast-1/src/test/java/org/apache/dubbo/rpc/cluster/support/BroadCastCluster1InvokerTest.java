package org.apache.dubbo.rpc.cluster.support;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcInvocation;
import org.apache.dubbo.rpc.cluster.Directory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


import java.util.Arrays;

class BroadCastCluster1InvokerTest {
    private URL url;
    private Directory<DemoService> dic;
    private RpcInvocation invocation;
    private BroadcastCluster1Invoker clusterInvoker;

    private MockInvoker invoker1;
    private MockInvoker invoker2;
    private MockInvoker invoker3;
    private MockInvoker invoker4;

    @BeforeEach
    public void setUp() throws Exception {

        dic = mock(Directory.class);

        invoker1 = new MockInvoker();
        invoker2 = new MockInvoker();
        invoker3 = new MockInvoker();
        invoker4 = new MockInvoker();

        url = URL.valueOf("test://127.0.0.1:8080/test");
        given(dic.getUrl()).willReturn(url);
        given(dic.getConsumerUrl()).willReturn(url);
        given(dic.getInterface()).willReturn(DemoService.class);

        invocation = new RpcInvocation();
        invocation.setMethodName("test");

        clusterInvoker = new BroadcastCluster1Invoker(dic);
    }

    @Test
    void testNormal() {
        given(dic.list(invocation)).willReturn(Arrays.asList(invoker1, invoker2, invoker3, invoker4));
        // Every invoker will be called
        clusterInvoker.invoke(invocation);
        assertTrue(invoker1.isInvoked());
        assertTrue(invoker2.isInvoked());
        assertTrue(invoker3.isInvoked());
        assertTrue(invoker4.isInvoked());
    }

    @Test
    void testEx() {
        given(dic.list(invocation)).willReturn(Arrays.asList(invoker1, invoker2, invoker3, invoker4));
        invoker1.invokeThrowEx();
        assertThrows(RpcException.class, () -> {
            clusterInvoker.invoke(invocation);
        });
        // The default failure percentage is 100, even if a certain invoker#invoke throws an exception, other invokers
        // will still be called
        assertTrue(invoker1.isInvoked());
        assertTrue(invoker2.isInvoked());
        assertTrue(invoker3.isInvoked());
        assertTrue(invoker4.isInvoked());
    }

    @Test
    void testFailPercent() {
        given(dic.list(invocation)).willReturn(Arrays.asList(invoker1, invoker2, invoker3, invoker4));
        // We set the failure percentage to 75, which means that when the number of call failures is 4*(75/100) = 3,
        // an exception will be thrown directly and subsequent invokers will not be called.
        url = url.addParameter("broadcast.fail.percent", 75);
        given(dic.getConsumerUrl()).willReturn(url);
        invoker1.invokeThrowEx();
        invoker2.invokeThrowEx();
        invoker3.invokeThrowEx();
        invoker4.invokeThrowEx();
        assertThrows(RpcException.class, () -> {
            clusterInvoker.invoke(invocation);
        });
        assertTrue(invoker1.isInvoked());
        assertTrue(invoker2.isInvoked());
        assertTrue(invoker3.isInvoked());
        assertFalse(invoker4.isInvoked());
    }
}

/**
 * 借鉴给 samples 库进行使用
 */
class MockInvoker implements Invoker<DemoService> {
    private static int count = 0;
    private URL url = URL.valueOf("test://127.0.0.1:8080/test");
    private boolean throwEx = false;
    private boolean invoked = false;

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public void destroy() {}

    @Override
    public Class<DemoService> getInterface() {
        return DemoService.class;
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        invoked = true;
        if (throwEx) {
            throwEx = false;
            throw new RpcException();
        }
        return null;
    }

    public void invokeThrowEx() {
        throwEx = true;
    }

    public boolean isInvoked() {
        return invoked;
    }
}
