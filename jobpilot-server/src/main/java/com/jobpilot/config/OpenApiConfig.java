package com.jobpilot.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * springdoc-openapi 统一配置（规格书技术栈：springdoc-openapi，第 3 章）。
 *
 * <p>所有 HTTP 接口（Controller）统一在这里集中管理元数据：</p>
 * <ul>
 *   <li><b>信息</b>：标题 / 版本 / 描述（含统一 Result 响应约定与错误码范围）</li>
 *   <li><b>服务地址</b>：servers（开发 / 部署环境可扩展）</li>
 *   <li><b>分组</b>：tags 按业务模块划分（系统 / 认证 / 简历 / 职位 / 面试 / AI 任务），
 *       后续 Phase 的新 Controller 使用同名 {@code @Tag} 自动归组</li>
 *   <li><b>安全</b>：JWT Bearer 认证方案（Phase 1 引入 Spring Security 后，
 *       受保护接口通过 {@code @SecurityRequirement(name = "bearerAuth")} 声明即可复用）</li>
 * </ul>
 *
 * <p>访问入口：Knife4j 增强 UI = <a href="/doc.html">/doc.html</a>（推荐），
 * 原生 Swagger UI = <a href="/swagger-ui.html">/swagger-ui.html</a>，
 * OpenAPI JSON = <a href="/v3/api-docs">/v3/api-docs</a>（路径见 application.yml）。</p>
 */
@Configuration
public class OpenApiConfig {

    /** 全局唯一的安全方案名，Controller 通过 @SecurityRequirement(name = "bearerAuth") 复用。 */
    public static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI jobpilotOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JobPilot API")
                        .version("0.1.0")
                        .description("""
                                JobPilot — AI 智能求职与模拟面试平台（Java 主服务 API）

                                ## 统一约定
                                - 统一响应格式：`Result { code, message, data, requestId }`，所有业务接口均以此包装返回
                                - 错误码：`0` success / `400xx` 参数业务 / `401xx` 认证 / `403xx` 权限 /
                                  `404xx` 不存在 / `409xx` 冲突 / `429xx` 限流 / `500xx` 内部错误 / `503xx` 外部服务不可用
                                - 认证：登录后获得 accessToken，受保护接口请求头携带 `Authorization: Bearer <token>`
                                - 接口路径前缀：`/api/v1`
                                """)
                        .contact(new Contact()
                                .name("JobPilot")
                                .email("dev@jobpilot.local"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("本地开发环境"),
                        new Server().url("/").description("当前部署环境（相对路径）")))
                .tags(List.of(
                        new Tag().name("认证").description("注册 / 登录 / 刷新 / 登出（Phase 1）"),
                        new Tag().name("简历").description("简历 CRUD 与解析（Phase 2）"),
                        new Tag().name("职位").description("职位 CRUD 与缓存（Phase 2）"),
                        new Tag().name("面试").description("模拟面试会话、消息与报告（Phase 3）"),
                        new Tag().name("AI 任务").description("AI 异步任务状态查询（Phase 6）")))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("登录后获得的 accessToken，请求头格式：Authorization: Bearer <token>")));
    }
}
