# ADR-001: API 文档 UI 采用 Knife4j（基于 springdoc-openapi）

## 状态
Accepted（2026-08-19）

## 背景
- 项目使用 springdoc-openapi 2.8.17 生成 OpenAPI 3 规范，`OpenApiConfig` 集中管理标题、描述、tag 分组、servers、JWT 安全方案等元数据
- 原生 Swagger UI（`/swagger-ui.html`）存在体验问题：界面为英文、样式简陋、接口调试与搜索能力弱、无法自定义页脚
- 需要在不改变 OpenAPI 规范生成链路（springdoc）的前提下，升级 UI 展示层，改善接口调试体验

## 决策
- 引入 `com.github.xingfudeshi:knife4j-openapi3-jakarta-spring-boot-starter:4.6.0`，Knife4j 作为 springdoc 之上的增强 UI 层
- 保留显式声明的 `org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.17`，Maven 就近原则使其优先于 knife4j 传递依赖（2.7.0），规范生成行为不变
- 入口：`/doc.html`（Knife4j 增强 UI）；`/swagger-ui.html` 与 `/v3/api-docs` 继续可用
- 配置集中在 `application.yml` 的 `knife4j.*`：中文界面、自定义页脚、顶部搜索、显示 OpenAPI 地址

## 被否决的替代方案
- **仅用 springdoc 原生 Swagger UI + custom-css/js**：可改样式但界面结构、中文、调试体验仍需大量定制，性价比低
- **自建 HTML 外壳（覆盖 index.html / swagger-ui-dist 二次开发）**：自由度最高但维护成本大，Phase 0 阶段不值得投入
- **Redoc / RapiDoc**：静态文档展示优秀，但交互调试（Try it out）与 Spring 生态集成不如 Knife4j 顺手，且无中文增强

## 影响
- 好处：中文界面、按 tag 分组浏览、调试（Try it out）体验好、零成本接入现有 `OpenApiConfig` 元数据；原 Swagger UI 入口保留，兼容已有书签/脚本
- 代价 / 风险：
  - 多一个依赖（knife4j-core + knife4j-openapi3-ui），打包体积略增
  - Knife4j 4.6.0 传递依赖 springdoc 2.7.0，本项目显式锁定 2.8.17；后续升级 springdoc 需回归验证 `/doc.html` 渲染
  - Knife4j 是增强 UI，若未来需要纯标准 OpenAPI 展示，可关闭（`knife4j.enable: false`）回退到原生 Swagger UI
