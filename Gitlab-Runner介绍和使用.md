一般来说，gitlab上已由负责人配置好了gitlab-runner，我们只需要编写好.gitlab.yml文件提交代码即可触发runner进行工作

首先需要编写.gitlab-ci.yml文件

```aidl
before_script:

lintDebug://任务job名称
  stage: test //stage阶段
  rules:
    - if: '$CI_PIPELINE_SOURCE == "push"' #只在push的时候执行
  script:
    - echo "执行成功"
    - export PUB_HOSTED_URL=https://pub.flutter-io.cn
    - export FLUTTER_STORAGE_BASE_URL=https://storage.flutter-io.cn
    - python3 --version
    - git submodule update --init # 拉取submodule
    - python3 lint_check.py ${CI_COMMIT_BRANCH} ${GITLAB_USER_NAME} #执行lint检查脚本
  tags:
    - android
    - lint
```

执行过程:
1.进入本地项目根目录，即包含了.gitlab.yml的目录
2.执行gitlab-runner命令： gitlab-runner exec docker lintDebug --docker-pull-policy=never


