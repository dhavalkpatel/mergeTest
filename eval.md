# CI/CD Modernization Assessment

## Evaluation of Platform-Agnostic Pipeline Abstractions for Azure DevOps and GitHub

---

# Document Information

| Item              | Details                                                                                                                       |
| ----------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| Status            | Draft for Technical Review                                                                                                    |
| Objective         | Evaluate approaches for modernizing Azure DevOps Classic Pipelines while minimizing future migration effort to GitHub Actions |
| Decision Required | Identify the preferred abstraction layer for future CI/CD implementations                                                     |

---

# 1. Background

Org is currently undertaking modernization of its CI/CD.

The immediate objective is to migrate Azure DevOps Classic Pipelines to Azure DevOps YAML. In parallel, GitHub Enterprise (GHE) is expected to become available organization-wide in approximately six months, with a future transition to GitHub Actions anticipated.

Rather than performing two independent migrations (Classic → Azure DevOps YAML → GitHub Actions), this assessment evaluates whether introducing an abstraction layer can reduce long-term migration effort, improve maintainability, and better support future engineering initiatives such as AI Native SDLC.

---

# 2. Current Landscape

## CI/CD

* 200+ Azure DevOps Classic Pipelines
* Azure DevOps Repositories
* Self-hosted Azure DevOps agents

## Application Landscape

* Mostly .NET applications
* Remaining applications include Python and other tech stacks

## Automation

* PowerShell (primary)
* dotnet
* MSBuild
* NuGet
* Custom deployment scripts

## Infrastructure

* Majority of infrastructure hosted in Azure
* Remaining legacy infrastructure hosted in Telehouse
* Legacy environments are expected to contain tech debt
* Infrastructure as Code (IaC) standardized on **Bicep**

---

# 3. Current Challenges

The current implementation presents several challenges.

## Pipeline Coupling

Business logic is tightly coupled with Azure DevOps Classic pipelines.

Examples include:

* Inline PowerShell
* Azure DevOps specific tasks
* Repeated YAML logic
* Duplicated deployment steps

---

## Migration Effort

Without introducing an abstraction layer, the migration path becomes:

```text
Classic Pipelines

↓

Azure DevOps YAML

↓

GitHub Actions
```

This results in multiple migration activities for similar pipeline logic.

---

## Maintainability

Current concerns include:

* Duplicate implementation
* Difficult standardization
* Pipeline drift
* Limited reuse
* Platform-specific implementation

---

# 4. Objectives

The preferred solution should:

* Reduce migration effort
* Minimize CI platform lock-in
* Support Azure DevOps and GitHub Actions
* Reuse existing PowerShell automation
* Require minimal infrastructure changes
* Be easy for developers to adopt
* Improve maintainability
* Align with AI Native SDLC initiatives

---

# 5. Evaluation Criteria

| Criteria                     | Importance |
| ---------------------------- | ---------- |
| Migration effort             | High       |
| Learning curve               | High       |
| Infrastructure changes       | High       |
| Platform independence        | High       |
| Reuse of existing PowerShell | High       |
| GitHub migration readiness   | High       |
| AI Native SDLC alignment     | Medium     |
| Long-term maintainability    | High       |
| Enterprise scalability       | High       |

---

# 6. Options Evaluated

---

# Option 1

## Classic → Azure DevOps YAML

### Architecture

```text
Classic

↓

Azure DevOps YAML

↓

PowerShell
dotnet
MSBuild
Bicep
Deployments
```

### Advantages

* Lowest implementation effort
* Familiar approach
* No infrastructure changes

### Limitations

* Pipeline logic remains Azure DevOps specific
* Future GitHub migration requires another transformation
* Continued duplication across repositories
* Limited reuse

### Overall Assessment

Suitable only if the objective is to complete the current migration with minimal architectural improvement.

---

# Option 2

## Classic → Azure DevOps YAML + Taskfile

### Architecture

```text
Classic

↓

Azure DevOps YAML

↓

Taskfile

↓

PowerShell
dotnet
MSBuild
Bicep
Deployments
```

Azure DevOps YAML becomes a thin orchestration layer.

Example

Instead of embedding build logic inside Azure DevOps YAML:

```
Restore

Build

Test

Publish

Deploy
```

The pipeline executes:

```
task build

task test

task publish

task deploy
```

Business logic resides within Taskfile.

---

## Benefits

### Platform Independence

Azure DevOps

```
task build
```

GitHub Actions

```
task build
```

Only the orchestration layer changes.

---

### Existing Script Reuse

Current PowerShell automation continues to work without modification.

Example

```
task deploy

↓

deploy.ps1
```

Minimal refactoring required.

---

### Infrastructure Impact

Requirements:

* Task CLI installation

No requirement for:

* Docker
* Linux runners
* Container runtime
* Build infrastructure changes

Existing Windows agents continue to operate as-is.

---

### Learning Curve

Developers need to learn:

* Tasks
* Variables
* Dependencies

Estimated adoption:

* Initial learning: < 1 day
* Productive usage: 1–2 days
* Organization adoption: Low effort

---

### GitHub Migration

Migration path becomes:

```text
Classic

↓

Taskfile

↓

Azure DevOps YAML

↓

GitHub Actions
```

Task implementations remain unchanged.

---

### AI Native SDLC

Taskfile creates reusable, intent-based automation.

Instead of parsing complex CI platform YAML, AI assistants interact with reusable commands such as:

```
task build

task test

task deploy
```

Benefits include:

* Better workflow understanding
* Easier automation generation
* Simplified maintenance
* Reusable engineering patterns

---

### Risks

* Does not introduce containerized execution
* Less suitable for highly sophisticated multi-stage delivery workflows

---

# Option 3

## Classic → Azure DevOps YAML + Dagger

### Architecture

```text
Classic

↓

Azure DevOps YAML

↓

Dagger

↓

Containers

↓

PowerShell
dotnet
MSBuild
Bicep
```

Azure DevOps invokes:

```
dagger call build
```

Business logic resides inside Dagger modules.

---

## Benefits

### Platform Independence

Works consistently across:

* Azure DevOps
* GitHub Actions
* Jenkins
* Local development

---

### Modern Engineering Model

Provides:

* Reproducible builds
* Containerized execution
* Advanced caching
* Shared modules
* Pipeline portability

---

### AI Native SDLC

Dagger workflows are implemented using programming languages rather than declarative YAML.

This enables AI tools to:

* Generate workflows
* Refactor implementations
* Improve maintainability
* Perform richer code analysis

---

# Infrastructure Impact

Unlike Taskfile, Dagger introduces platform-level considerations.

## Build Infrastructure

Areas requiring assessment include:

* Availability of a supported container runtime
* Suitability of current Windows build agents
* Potential introduction of Linux runners for container-native execution
* BuildKit compatibility with the organization's build infrastructure

> **Note:** BuildKit is optimized for Linux container workflows and is not supported in the same way for native Windows container workloads. This requires careful evaluation for organizations with predominantly Windows-based build agents.

---

## Platform Operations

Potential operational changes include:

* Runner provisioning
* Container image lifecycle
* Build cache strategy
* Internal container registry
* Operational monitoring

---

## Security & Networking

Areas requiring review:

* Registry connectivity
* Firewall and proxy configuration
* Image vulnerability scanning
* Image governance
* Supply chain security

---

## Learning Curve

Developers need to understand:

* Containers
* BuildKit
* Dagger Engine
* Modules
* SDKs
* Services
* Secrets
* Container-based execution

Estimated adoption:

* Initial learning: Several days
* Productive usage: Multiple weeks
* Organization-wide enablement: Medium to High effort

---

## Migration Considerations

Existing PowerShell scripts can continue to be invoked initially.

However, the greatest benefits of Dagger are realized by progressively moving logic into native Dagger modules, requiring additional engineering effort beyond the pipeline migration itself.

---

# 7. Comparison Summary

| Capability                  | Direct YAML | Taskfile  | Dagger              |
| --------------------------- | ----------- | --------- | ------------------- |
| Initial migration effort    | High        | Low       | Medium              |
| Platform independence       | Medium      | High      | Very High           |
| Existing PowerShell reuse   | Excellent   | Excellent | Excellent           |
| Existing Bicep reuse        | Excellent   | Excellent | Excellent           |
| Windows agent compatibility | Excellent   | Excellent | Requires evaluation |
| Infrastructure changes      | None        | Minimal   | Significant         |
| Container runtime required  | No          | No        | Yes                 |
| Learning curve              | Low         | Low       | High                |
| GitHub migration effort     | High        | Low       | Low                 |
| AI Native SDLC alignment    | Medium      | High      | Very High           |
| Long-term maintainability   | Medium      | High      | Very High           |

---

# 8. AI Native SDLC Considerations

A key objective of AI Native SDLC is enabling AI assistants to reason about delivery workflows.

Current model:

```text
Azure DevOps YAML

↓

Inline PowerShell

↓

Inline Bash

↓

Conditions

↓

Variables
```

Taskfile:

```text
Azure DevOps YAML

↓

task build

↓

task test

↓

task deploy
```

Dagger:

```text
Azure DevOps YAML

↓

dagger call build

↓

Typed Modules

↓

Container Workflows
```

Both Taskfile and Dagger improve AI friendliness compared to Azure DevOps-specific pipeline definitions, with Dagger providing richer semantic models at the cost of higher implementation complexity.

---

# 9. Risks

## Direct YAML

* Vendor lock-in
* Duplicate migration effort
* Continued maintenance overhead

---

## Taskfile

* Requires adoption of an additional tool
* Relies on discipline to keep business logic outside pipeline YAML

---

## Dagger

* Infrastructure modernization
* Platform engineering effort
* Container runtime strategy
* Runner strategy
* Organization-wide enablement
* Higher operational complexity

---

# 10. Recommendation

Based on the current environment:

* 200+ Classic pipelines
* Approximately 80–85% .NET applications
* Predominantly PowerShell automation
* Existing Windows self-hosted Azure DevOps agents
* Majority of infrastructure hosted in Azure
* Bicep as the IaC standard
* GitHub Enterprise rollout expected within the next six months

**Taskfile appears to provide the best balance of migration effort, operational simplicity, platform independence, and future portability.**

It enables:

* Thin Azure DevOps YAML
* Reusable automation
* Minimal infrastructure changes
* Low learning curve
* Straightforward migration to GitHub Actions
* Better alignment with AI-assisted engineering workflows

Dagger provides a more advanced engineering model and may be a strong candidate for future platform modernization. However, its adoption introduces additional infrastructure, operational, and organizational considerations that extend beyond the scope of the current Classic-to-YAML migration.

---

# 11. Proposed Roadmap

| Phase   | Recommendation                                                                                                                                                                                                     |
| ------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Phase 1 | Migrate Classic Pipelines to Azure DevOps YAML using Taskfile as the abstraction layer.                                                                                                                            |
| Phase 2 | Standardize reusable Taskfiles across engineering teams and reduce duplication.                                                                                                                                    |
| Phase 3 | Reuse Taskfiles during migration to GitHub Actions with minimal workflow changes.                                                                                                                                  |
| Phase 4 | Conduct a focused Dagger proof of concept for selected modern applications to evaluate containerized builds, reusable modules, and advanced platform engineering capabilities before considering broader adoption. |

---

# 12. Open Questions for Team Discussion

1. Is reducing future GitHub migration effort a strategic priority?
2. Should CI/CD business logic be abstracted away from Azure DevOps?
3. Is the organization prepared to introduce container-native build infrastructure in the near term?
4. Would the benefits of Dagger justify the additional operational and learning investment?
5. Should Taskfile become the standard abstraction layer for build and deployment automation across repositories?
6. Should Dagger be evaluated separately as part of a longer-term Platform Engineering modernization initiative rather than the immediate pipeline migration effort?
