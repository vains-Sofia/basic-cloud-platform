## 匿名客户端
### 授权申请
```shell
http://127.0.0.1:8080/oauth2/authorize?response_type=code&client_id=opaque-client&redirect_uri=http://127.0.0.1:5173/OAuth2Redirect&scope=openid%20profile%20message.read%20message.write&state=VGh1IE9jdCAwMyAyMDI0IDE3OjMwOjMzIEdNVCswODAwICjkuK3lm73moIflh4bml7bpl7Qp
```

### 获取access token
导入postman以后添加basis auth，替换code
```shell
客户端id：opaque-client
客户端密钥：123456
```

```shell
curl --location 'http://127.0.0.1:8080/oauth2/token' \
--header 'Authorization: Basic b3BhcXVlLWNsaWVudDoxMjM0NTY=' \
--form 'grant_type="authorization_code"' \
--form 'code="aJmSNe5wb0dZwNNghubJzXNq3tI2Y9y-rhlOGii85HzG_aHPWeWt-b-EfqDs5BHBNePwB0t3fcfFqEac9mkiCJDGauELcFqBhiCKevax3HHC0B0tYdFTRvOJ5fWPUUu9"' \
--form 'redirect_uri="http://127.0.0.1:5173/OAuth2Redirect"'
```

### 刷新access token
导入postman以后添加basis auth，替换code
```shell
客户端id：opaque-client
客户端密钥：123456
```

```shell
curl --location 'http://127.0.0.1:8080/oauth2/token' \
--header 'Authorization: Basic b3BhcXVlLWNsaWVudDoxMjM0NTY=' \
--form 'grant_type="refresh_token"' \
--form 'refresh_token="UL0LF2nOxrJJd14cmikqrBuzWucHwG8JeEg9WWMWJ6IAkH2lnEtruUT-0ebxJ5s9xYqrQ3ND_PSKWUEV9lTKWfbSlvu1oCZd9LSxdjaDkD5tLZ0AbsGloTH9wTRKAoUx"'
```