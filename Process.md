### 连接池的配置
> 2017-06-09 22:47:31
- 连接池 继承 JedisPool 
- 配置文件，总的配置文件被小部分引用
- Spring的使用是否必要，因为多个Pool 的被各个命令操作模块依赖关系
- 如果使用Spring 多例模式 怎么处理切换Pool

### 大致完成了连接池，下一步做什么呢
> 2017-06-11 20:14:24
- 关于redis版本控制？ `cluster shared 集群分布式共享`
- 类中很多方法，和类很多，方法少的选择
- 是否日志先行，是否支持undo redo

在下面的情况下应当考虑使用命令模式：
1）使用命令模式作为"CallBack"在面向对象系统中的替代。"CallBack"讲的便是先将一个函数登记上，然后在以后调用此函数。
2）需要在不同的时间指定请求、将请求排队。一个命令对象和原先的请求发出者可以有不同的生命期。换言之，原先的请求发出者可能已经不在了，而命令对象本身仍然是活动的。这时命令的接收者可以是在本地，也可以在网络的另外一个地址。命令对象可以在串形化之后传送到另外一台机器上去。
3）系统需要支持命令的撤消(undo)。命令对象可以把状态存储起来，等到客户端需要撤销命令所产生的效果时，可以调用undo()方法，把命令所产生的效果撤销掉。命令对象还可以提供redo()方法，以供客户端在需要时，再重新实施命令效果。
4）如果一个系统要将系统中所有的数据更新到日志里，以便在系统崩溃时，可以根据日志里读回所有的数据更新命令，重新调用Execute()方法一条一条执行这些命令，从而恢复系统在崩溃前所做的数据更新。
对于一个场合到底用不用模式，这对所有的开发人员来说都是一个很纠结的问题。有时候，因为预见到需求上会发生的某些变化，为了系统的灵活性和可扩展性而使用了某种设计模式，但这个预见的需求偏偏没有，相反，没预见到的需求倒是来了不少，导致在修改代码的时候，使用的设计模式反而起了相反的作用，以至于整个项目组怨声载道。这样的例子，我相信每个程序设计者都遇到过。所以，基于敏捷开发的原则，我们在设计程序的时候，如果按照目前的需求，不使用某种模式也能很好地解决，那么我们就不要引入它，因为要引入一种设计模式并不困难，我们大可以在真正需要用到的时候再对系统进行一下，引入这个设计模式。
拿命令模式来说吧，我们开发中，请求-响应模式的功能非常常见，一般来说，我们会把对请求的响应操作封装到一个方法中，这个封装的方法可以称之为命令，但不是命令模式。到底要不要把这种设计上升到模式的高度就要另行考虑了，因为，如果使用命令模式，就要引入调用者、接收者两个角色，原本放在一处的逻辑分散到了三个类中，设计时，必须考虑这样的代价是否值得。

- 缺点是 命令多的时候就会导致太多的类，目前就是这种情况，命令本来就复用的少，精髓在于将简单的命令全部设成命令类，然后组合就能达到想要的功能
- 所以经过考虑还是不采用命令模式，关于引入调用者、接收者两个角色感觉就会复杂化挺多代码的

## 错误编码，异常自定义
> 2017-06-12 08:27:06
- 枚举类是实例固定不可变，final修饰所以不可派生，默认继承 java.lang.enum类，不能显示继承别的类

## Spring 日志问题
- java.lang.NoClassDefFoundError: org/apache/commons/logging/LogFactory
- 特别注意，如果使用了slf4j的另外实现方式，不是apche 那套的话，就要加入排除依赖
```xml
    <dependency>
          <groupId>org.springframework</groupId>
          <artifactId>spring-webmvc</artifactId>
          <version>${spring.version}</version>
          <exclusions>
              <exclusion>
              <groupId>commons-logging</groupId>
              <artifactId>commons-logging</artifactId>
              </exclusion>
          </exclusions>
    </dependency>
```
- gradle 排除依赖还是有问题，简直了，只好使用简单的静态类来处理，但是显然的，每次操作都在新建实例，所以，使用单例模式？

### 日志问题
> 2017-06-14 23:47:26
- 相反的，引入commons-logging依赖，反而解决了问题，也是要醉