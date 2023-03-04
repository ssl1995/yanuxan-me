### horse-framework-starter-rocketmq 模块

> 模块基于rocketmq-spring-boot-starter 与 ons-client 实现，
> 同时支持开源版和阿里云的rocketmq
> 两种mq使用上无差别，只需要在配置文件当中指定rocketmq的服务提供方即可

- ### 注解说明
  #### @Topic
        标识mq的topic，属性名为topic的名字 提供生产和消费的入口
  #### @Consumer
        标识mq的消费者
    ```
    //属性
    String groupId() default "${spring.application.name}"; //消费组id（gid）默认为消费者服务名
    String tag() default "*"; //消息的tag
    ConsumeMode consumeMode() default ConsumeMode.CONCURRENTLY; //消费模式 并发消费和顺序消费
    MessageModel messageModel() default MessageModel.CLUSTERING;//消息模式，集群消费和广播
    ```
  #### @MessageQueueScan
       扫包注解，扫描指定路径下的Topic注解，可以理解为mybatis的MapperScan


- ### 接口说明
  #### interface MqTopic
  声明一个topic之后继承MqTopic接口即可获得生产与消费的能力

  代码示例：

    ```
        import com.msb.framework.common.mq.MqTopic;
        import com.msb.framework.common.mq.annotation.Topic;
        import com.msb.user.api.mq.body.UserTokenLogoutMessageBody;

        @Topic("user_token_logout")
        public interface UserTokenLogoutMessage extends MqTopic<UserTokenLogoutMessageBody> {

        }
    ```
  我们可以在注解中标识我们topic的name，以及在继承自MqTopic时标识我们的此topic的消息泛型
  来规定生产和消费的数据格式。

  我们可以看到 **MqTopic** 接口提供三个方法
    ```
        /**
         * 发送消息
         *
         * @param message 消息体
         */
        default void product(T message) {
        }
    
        /**
         * 自定义发送消息
         *
         * @param mqConfig 消息配置
         * @param message  消息体
         */
        default void product(MqConfig mqConfig, T message) {
        }
    
        /**
         * 消费消息
         *
         * @param message 消息体
         */
        void consumer(T message);
    ```
  这个时候我们如果想使用生产者，就可以直接使用我们@Resource注解即可直接使用
    ```
    @Resource
    private UserTokenLogoutMessage userTokenLogoutMessage;
  
    public void producerMessage() {
        userTokenLogoutMessage.product(new UserTokenLogoutMessageBody().setUserId(a).setLocalDateTime(LocalDateTime.now()));
    }
    ```
  消费者示例：直接实现Topic接口即可
    ```
    @Consumer(groupId = "GID_msb_test")
    public class UserLogoutConsumer implements UserTokenLogoutMessage {

        @Override
        public void consumer(UserTokenLogoutMessageBody message) {
           log.info("消费数据 {}", message);
        }
    }
    ```
    
    


