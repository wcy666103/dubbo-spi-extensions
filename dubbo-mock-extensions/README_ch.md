# Dubbo Mock Extension

[English](README.md)
> Dubbo Mock Extension是提供给[dubbo](https://github.com/apache/dubbo)用户用于在没有服务提供者的情况下模拟返回数据的模块，只需要配合[dubbo-admin](https://github.com/apache/dubbo-admin)就可以在没有服务提供者的情况下轻松模拟返回数据。
> 在开发过程中无需等待服务提供方将服务提供者实现开发和部署完成才能进行后续开发和测试，减少对服务提供者的依赖造成的阻塞，提升开发效率。

## 如何使用

- 引入依赖

```xml
<dependency>
    <groupId>org.apache.dubbo.extensions</groupId>
    <artifactId>dubbo-mock-admin</artifactId>
    <version>3.0.0</version>
</dependency>
```

- 开启模拟返回开关 将``` -Denable.dubbo.admin.mock=true ```添加到JVM启动参数中。

- 配置模拟规则 在[dubbo-admin](https://github.com/apache/dubbo-admin)的服务Mock菜单栏进行模拟规则的配置。


应该是 admin 那边会注册 mock相关的interface到注册中心，然后这边这个filter如果生效的话，会根据consumer的url中的registry找mock interface的地址，然后进行代理，将请求往那边发
