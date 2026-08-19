# reference — 可复用实现手册

存放已在 Lab 验证、未来可在其他项目直接复用的成熟模块（规格书 v2.0 第 6.8 节）。

## 晋升 Reference 的验收标准

- 能独立运行或最小验证；
- 关键代码已经理解；
- 有正常测试 + 至少一个异常测试；
- 有配置说明、适用场景、不适用场景、替代方案；
- 有学习来源；
- 半年以后只看当前仓库仍然能复用。

## 目录规则

```text
reference/{technology-name}/
├── README.md          # 技术卡片（或指向 docs/modules/{technology-name}.md）
├── src/               # 最小可迁移实现
└── test/
```

一个 Reference 模块至少回答：解决什么问题 / 何时使用 / 核心代码在哪 / 最小依赖 / 如何配置 / 如何验证 / 边界条件 / 替代方案。

## 生命周期

`DISCOVERED → LAB → VERIFIED → REFERENCE →（业务需要）→ CORE`，任何阶段可 `ARCHIVED`。

## 当前状态

暂无模块。候选 Backlog 见 `../docs/TECH_INDEX.md`。

> 注意：Reference 不自动进入 Core；若被 Core 正式采用，在 Core 中保留生产实现，Reference 文档指向 Core 实现位置，不维护两份完全相同代码（规格书 6.1）。
