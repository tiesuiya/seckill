## 待处理
- ubuntu上service实现类没有自动加@Override，有点强迫症看不下去
- ubuntu上自动生成的实现test方法没有加test，有点强迫症看不下去

## 已处理
- Mybatis如何获取插入行的id？ 
```
    配置xml时这样写
    <insert id="insertSeckill" parameterType="Seckill" useGeneratedKeys="true" keyProperty="id">
    useGeneratedKeys：必须设置为true，否则无法获取到主键id。
    keyProperty：设置为POJO对象的主键id属性名称
```
