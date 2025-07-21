#!/bin/bash

# 1. 参数校验
if [ -z "$2" ]; then
  echo "❗️请提供项目名，例如：$0 [start|stop|restart|status] <project-name>"
  exit 1
fi

# 2. 配置
PROJECT_NAME="$2"
JAR_NAME="basic-service-${PROJECT_NAME}-0.0.1.jar"
JAR_PATH="/home/admin/app/${JAR_NAME}"
PROFILE="test"
JAVA_OPTS="${JAVA_OPTS:--Xms256m -Xmx512m -XX:MaxMetaspaceSize=256m -Xss512k}"

# 3. 检查是否运行中
get_pid() {
  pid=$(ps -ef | grep "$JAR_PATH" | grep -v grep | awk '{print $2}')
}

# 4. 启动
start() {
  get_pid
  if [ -n "$pid" ]; then
    echo "✅ $JAR_NAME 已在运行中，PID: $pid"
  else
    echo "🚀 正在启动 $JAR_NAME ..."
    nohup /usr/local/graalvm-jdk-21.0.4+8.1/bin/java $JAVA_OPTS -jar "$JAR_PATH" --spring.profiles.active=$PROFILE >/dev/null 2>&1 &
    sleep 2
    get_pid
    if [ -n "$pid" ]; then
      echo "✅ 启动成功，PID: $pid"
    else
      echo "❌ 启动失败，请检查日志配置"
    fi
  fi
}

# 5. 停止
stop() {
  get_pid
  if [ -n "$pid" ]; then
    echo "🛑 正在停止 $JAR_NAME，PID: $pid"
    kill "$pid"
    sleep 3
    get_pid
    if [ -n "$pid" ]; then
      echo "⚠️ 无法平滑停止，尝试强制 kill"
      kill -9 "$pid"
    fi
    echo "✅ 已停止"
  else
    echo "❗️$JAR_NAME 未在运行"
  fi
}

# 6. 状态
status() {
  get_pid
  if [ -n "$pid" ]; then
    echo "✅ $JAR_NAME 正在运行，PID: $pid"
  else
    echo "❌ $JAR_NAME 未运行"
  fi
}

# 7. 重启
restart() {
  stop
  sleep 2
  start
}

# 8. 入口
case "$1" in
  start) start ;;
  stop) stop ;;
  restart) restart ;;
  status) status ;;
  *)
    echo "Usage: $0 [start|stop|restart|status] <project-name>"
    exit 1
    ;;
esac
