## 1. 附录:

.git ---------------------------------git文件

hellobusBackend----------------------------Python 后端源代码(为开发版本, 服务器部署的是产品版本)`

BusApp-------------------------------------------Java/kotlin app源代码

document-------------------------------------------部分接口文档

libs--------------------------------------------本课设所使用到的仓库(百度地图androidSDK)

README.md----------------------------解释文件







## 2. 全部接口地址

### 2.1 梦网云科技验证码服务器接口

接口地址:http://api01.monyun.cn:7901/sms/v2/std/single_send

APIKEY:9821fcca86fd96b280ea7f87dbba701c

请求示例:

POST http://api01.monyun.cn:7901/sms/v2/std/single_send

```json
{
    "apikey":"9821fcca86fd96b280ea7f87dbba701c",
    "mobile":138xxxxxxxx,
  "content":"%d1%e9%d6%a4%c2%eb%a3%ba6666%a3%ac%b4%f2%cb%c0%b6%bc%b2%bb%d2%aa%b8%e6%cb%df%b1%f0%c8%cb%c5%b6%a3%a1"
}

```

返回result=0 且按照模板发送说明发送成功

### 2.2 彩云天气城市API服务器接口

接口地址: http://api.caiyunapp.com/v2/place

APIKEY: zO2HZWVkAJtEhzTu

请求示例: 

GET http://api.caiyunapp.com/v2/place?token=zO2HZWVkAJtEhzTu&lang=zn_CH&query=北

返回示例:

```json
{
    "status": "ok",
    "query": "北",
    "places": [
        {
            "id": "a0e308e879dad4a9fcc31d5ec9831caaf7619caa",
            "location": {
                "lat": 34.187044,
                "lng": -118.3812562
            },
            "place_id": "g-a0e308e879dad4a9fcc31d5ec9831caaf7619caa",
            "name": "North Hollywood",
            "formatted_address": "North Hollywood, Los Angeles, CA, USA"
        },
        {
            "id": "704ffc67b3139a5a4763d699894dff0394f8067e",
            "location": {
                "lat": 34.2381251,
                "lng": -118.530123
            },
            "place_id": "g-704ffc67b3139a5a4763d699894dff0394f8067e",
            "name": "Northridge",
            "formatted_address": "Northridge, Los Angeles, CA, USA"
        },
        {
            "id": "1d216dc20e65fdb44eebe7cfc216b40b7e31ee5b",
            "location": {
                "lat": 34.1694024,
                "lng": -118.3959999
            },
            "place_id": "g-1d216dc20e65fdb44eebe7cfc216b40b7e31ee5b",
            "name": "竹之林",
            "formatted_address": "North Hollywood, CA 91607, United States"
        },
        {
            "id": "c0e5c03b03c43dea04328b748efd74e0b5af17b3",
            "location": {
                "lat": 34.0965232,
                "lng": -118.3401879
            },
            "place_id": "g-c0e5c03b03c43dea04328b748efd74e0b5af17b3",
            "name": "Apartment North Mansfield Ave",
            "formatted_address": "1410 N Mansfield Ave, Los Angeles, CA 90028, United States"
        },
        {
            "id": "f816d549779d69768400ec78be4f0e7a25401f8f",
            "location": {
                "lat": 34.1721433,
                "lng": -118.3778873
            },
            "place_id": "g-f816d549779d69768400ec78be4f0e7a25401f8f",
            "name": "北好莱坞",
            "formatted_address": "11335, 11343 Burbank Blvd, North Hollywood, CA 91601, United States"
        }
    ]
}
```



### 2.3 自服务器Python后端接口

接口地址:http://134.175.100.199:80/v1/

请求示例:

POST http://134.175.100.199:80/v1/token 

Content-type: application/json

body:

```json
{
	"account":"zhoujj", 
	"type":100,
	"secret":"s"
}
```

返回:

```json
{
    "token": "eyJhbGciOiJIUzUxMiIsImlhdCI6MTU5MzI2NDc3OSwiZXhwIjoxNTkzMjcxOTc5fQ.eyJ1aWQiOiI1ZTllYjNhMjdkOWNhODYxMDUzZmQwYjUiLCJ0eXBlIjoxMDB9.N0Q1v9uBlIXTYnKjMTsayPcyoYKaRIuTDjGO1fL8kpII08ycSpDz7OmG7lZrgKF7TvyXH5yxIOC5DpdysfUbnw"
}
```

