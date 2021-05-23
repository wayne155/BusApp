# Bus 公交管理系统



UserLogin: 

![image.png](https://i.loli.net/2021/05/23/jNWaSk2nV59M4OH.png)

Admin Login：



![image.png](https://i.loli.net/2021/05/23/AaCEnPegqrOpxub.png)

Admin Controll:

![image.png](https://i.loli.net/2021/05/23/RQNJf6gkm7iz5Ku.png)



## 1 Architecture



![image.png](https://i.loli.net/2021/05/23/Ur37eGEDjmRqNcz.png)



## 2 Data Modeling



###  管理员模型

| ***\*字段\**** | ***\*是否必须\**** | ***\*字段类型\**** | ***\*说明\**** |
| -------------- | ------------------ | ------------------ | -------------- |
| Id             | 是                 | Id Object          | 唯一标识       |
| account        | 是                 | String             | 管理员名       |
| Password       | 是                 | Char(128)          | 密码           |

 

###  公交站点模型

| ***\*字段\**** | ***\*是否必须\**** | ***\*字段类型\**** | ***\*说明\**** |
| -------------- | ------------------ | ------------------ | -------------- |
| Id             | 是                 | Id Object          | 唯一标识       |
| name           | 是                 | String             | 管理员名       |
| city           | 是                 | City Document      | 密码           |
| altitude       | 是                 | Float              | 站点所在纬度   |
| latitude       | 是                 | Float              | 站点所在经度   |
| Is_deleted     | 否                 | Boolean            | 是否被删除     |

 

###  公交路线模型

 

| ***\*字段\**** | ***\*是否必须\**** | ***\*字段类型\****          | ***\*说明\**** |
| -------------- | ------------------ | --------------------------- | -------------- |
| Id             | 是                 | Id Object                   | 唯一标识       |
| busStations    | 是                 | Array of busStaion Document | 经过的全部站点 |
| name           | 是                 | String                      | 路线名         |
| City           | 是                 | String                      | 所在城市       |
| Is_deleted     | 否                 | Boolean                     | 是否被删除     |