# README

## 待处理

## 已处理
- Mybatis如何获取插入行的id？ 
```
    配置xml时这样写
    <insert id="insertSeckill" parameterType="Seckill" useGeneratedKeys="true" keyProperty="id">
    useGeneratedKeys：必须设置为true，否则无法获取到主键id。
    keyProperty：设置为POJO对象的主键id属性名称
```
- 业务异常与全局异常的处理
```
    1.业务异常由手动实现代码捕获：Service中抛出RuntimeException后，在Controller中手动捕获。
        这样既满足了Spring只对RuntimeException类型异常才进行事务回滚的的特性，又满足了Service类接口定义的标准和友好（标明异常类型，对客户端友好）
    2.全局异常统一异常：在前后端没有分离开发的时候，感觉这个问题不重要，因为处理起来感觉有点绕啊！可以说是一个伪命题了，因为没有多方合作你给我谈接口完备化，不是耍流氓么。
```
