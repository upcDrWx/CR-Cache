# CR-Cache

一个基于 Caffeine 和 Redis 的两级缓存框架

## 介绍

CR-Cache 是一个使用本地缓存 Caffeine 和 Redis 实现的两级缓存框架。项目最终实现了基于注解完成两级缓存的管理，并解决了分布式环境下本地缓存一致性问题。

本文中只是介绍了最基础的使用，实际中的并发问题、事务的回滚问题都需要考虑，还需要思考什么数据适合放在一级缓存、什么数据适合放在二级缓存等等的其他问题。

## 模块说明

v1 ~ v4 之间为递进关系，每个模块之间是独立的，区别在于使用了不同的实现方法。

**business**：业务示例

**v1**：在业务逻辑中手动实现两级缓存。实现逻辑简单，但业务侵入性太强。

**v2**：基于 spring 提供的 CacheManager 注解实现本地缓存，Redis 缓存还是手动在业务中实现。v2 的实现方式还是不够优雅，需要在业务中写 Redis 的缓存逻辑。

**v3**：基于自定义注解和 AOP 切面实现了两级缓存通过一个注解管理。这种实现方式更为优雅，业务代码中只需要添加一个注解就可以完成两级缓存的任务。

**v4**：通过扩展 Spring 提供的缓存接口的方式实现两级缓存，并利用了 Redis 的发布订阅机制实现了分布式环境下的缓存一致性。这种实现方式同样只需要一个注解就可以完成两级缓存的任务，几乎是业务无侵入。

## 运行测试

1. 克隆项目到本地

```
git clone https://github.com/upcDrWx/CR-Cache.git
```

2. 修改配置文件**application.properties**

```
datasource：修改数据库配置
redis：修改 Redis 配置
```

3. 启动类启动测试

## 说明

项目仅供个人学习使用。
