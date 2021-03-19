easy-pod
====

## Overview

Kubernetes上のPodのネットワークや挙動を調査するためのアプリケーションです。

このプロジェクトで実装されているのは下記の機能です。

- OSの取得
- IPアドレスの取得
- ホスト名の取得
- アプリケーションのシャットダウン

## Prerequisites
- Java 11+

## Getting Started

### Run anyway?

起動するだけであればGradleで起動できます。

```sh
./gradlew bootrun
```

### Running on docker

Dockerで起動するにはrootディレクトリに配置してあるDockerfileを使ってビルドしてください。

```sh
docker build -t easy-pod .
docker run -e SHUTDOWN=graceful \
  -e SHUTDOWN_TIMEOUT=10s \
  --rm easy-pod
```

## Usage

### Environment Values

- `SHUTDOWN`
  - 正常なシャットダウンを検知した際のWebサーバのシャットダウン方法を選択できます
    - immidiate(デフォルト)
      - Webサーバを即時シャットダウンします この設定の場合内部に残っている処理は無視したままシャットダウンすることになります
    - graceful
      - Webサーバは新規のリクエストの受け取りを拒否し、内部に残っている処理がすべて終えるまでシャットダウンを延期します
      - この設定を入れる場合は、環境変数`SHUTDOWN_TIMEOUT`の数値を変更することを推奨します
- `SHUTDOWN_TIMEOUT`
  - `SHUTDOWN=graceful`の場合、内部に残っている処理のタイムアウトを設定できます この時間を過ぎても処理が完了しない場合、そレラの処理は無視してシャットダウンします
  - 例
    - 1s(デフォルト)
    - 10m
- `META_PORT`
  - アプリケーションのメタ情報を取得するためのポートを設定できます
    - このポート番号はSpringActuatorのポートへマッピングされます
    - デフォルト値 8080

### Endpoints

- /pod
  - Kubernetesにデプロイ後、各種情報の取得や検証に利用できる機能を提供します
  - `GET` /os
    - アプリケーションの起動しているOSを取得します
  - `GET` /hostname
    - アプリケーションの起動しているホスト名を取得します
  - `GET` /ipaddr
    - サーバのIPアドレスを取得します
  - `GET` /sleep
    - params
      - sec: 処理を停止する秒数を指定します
    - リクエスト受信後、指定の秒数待機したのちにレスポンスを返却します
  - /health
    - アプリケーションの健康状態を変更します
    - `POST` /readiness
      - params
        - status: `up` | `down`
      - readinessの値を変更します
    - `POST` /liveness
      - params
        - status: `up` | `down`
      - livenessの値を変更します    
- /actuator
  - spring-actuatorで提供されているエンドポイントです
  - /health
    - アプリケーションの健康状態を提供しています
    - `GET` /liveness
      - 死活状況を提供します
    - `GET` /readiness
      - アプリケーションが健全な状態になっているかを提供します
  - `GET` /prometheus
    - Prometheusメトリクスを提供します
  - `POST` /shutdown
    - アプリケーションをシャットダウンします

