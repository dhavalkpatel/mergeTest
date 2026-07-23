# CI/CD Execution Abstraction Evaluation – Taskfile vs. Dagger

## Document Purpose

This document evaluates Taskfile and Dagger as CI/CD execution abstractions to determine their suitability for the organization's current and future software delivery landscape.

The objective is **not** to identify a universal winner, but to determine which execution model best fits different workload types while maintaining a consistent CI/CD operating model across Azure DevOps and GitHub.

---

# Background

The organization is modernizing its CI/CD platform with the following goals:

* Migrate Azure DevOps Classic Pipelines to YAML
* Establish reusable CI/CD capabilities
* Reduce pipeline duplication
* Improve developer experience
* Prepare for future GitHub Enterprise adoption
* Support both traditional enterprise applications and modern cloud-native workloads

Early discussions have shown positive alignment toward **Taskfile** for simplifying pipeline orchestration and improving portability. More recently, it has been identified that part of the application estate consists of **Linux-based, containerized workloads**, where **Dagger** may provide additional benefits.

This evaluation aims to determine the most appropriate execution abstraction for each workload category.

---

# Problem Statement

Today, implementation logic is distributed across:

* Azure DevOps YAML
* Classic Pipelines
* PowerShell scripts
* Bash scripts
* Release Pipelines
* Application-specific automation

This creates:

* duplicated logic
* inconsistent implementation patterns
* difficult migration
* limited reuse
* platform coupling
* governance challenges

A reusable execution abstraction can centralize implementation while allowing Azure DevOps and GitHub to remain thin orchestration layers.

---

# Evaluation Objectives

The selected approach should:

* Reduce CI/CD duplication
* Maximize reuse of existing automation
* Improve maintainability
* Support future GitHub migration
* Enable local execution
* Scale across multiple technology stacks
* Support governance and compliance
* Minimize operational overhead

---

# Current Technology Landscape

| Area                | Current State                                    |
| ------------------- | ------------------------------------------------ |
| Primary CI Platform | Azure DevOps                                     |
| Future Direction    | GitHub Enterprise                                |
| IaC                 | Bicep                                            |
| Primary Language    | .NET                                             |
| Existing Automation | PowerShell (majority), Bash                      |
| Pipeline Types      | Classic + YAML                                   |
| Agent Estate        | Windows and Linux                                |
| Infrastructure      | Azure + on-premises workloads                    |
| Application Types   | Traditional enterprise + containerized workloads |

---

## Workload Segmentation

The organization supports a heterogeneous application estate. Rather than adopting a single execution abstraction for all workloads, the execution engine should be selected based on workload characteristics and operational requirements.

| Workload Category                   | Typical Technologies                                     | Typical Characteristics                                                        | Preferred Execution Engine    | Rationale                                                                                                                                            |
| ----------------------------------- | -------------------------------------------------------- | ------------------------------------------------------------------------------ | ----------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| Traditional Enterprise Applications | .NET Framework, ASP.NET, IIS, Windows Services           | Windows-based, PowerShell automation, Azure App Service deployments, Bicep IaC | **Taskfile**                  | Maximizes reuse of existing automation with minimal migration effort and excellent Windows support.                                                  |
| Azure PaaS Applications             | Azure App Service, Azure Functions, Logic Apps           | Azure-native deployments, Bicep, PowerShell, minimal container usage           | **Taskfile**                  | Thin orchestration with reusable deployment tasks aligns well with existing Azure delivery patterns.                                                 |
| Linux-based Applications            | Python, Java, Node.js                                    | Linux runners, Bash scripts, VM or PaaS deployments                            | **Taskfile (default)**        | Suitable where applications are not container-centric and existing scripting can be reused. Dagger may be evaluated if container adoption increases. |
| Containerized Applications          | Docker-based applications                                | OCI image builds, container registries, Linux runners                          | **Dagger**                    | Provides container-native execution, BuildKit integration, reproducible builds, and efficient caching.                                               |
| Kubernetes Workloads                | AKS, Kubernetes, Helm                                    | Container-first deployments, multi-stage pipelines, GitOps integration         | **Dagger**                    | Better suited for Kubernetes-centric delivery and container lifecycle management.                                                                    |
| Cloud-native Microservices          | Polyglot services, APIs, event-driven workloads          | Independent services, Docker, Kubernetes, ephemeral runners                    | **Dagger**                    | Designed for container-native workflows with strong portability and execution consistency.                                                           |
| Hybrid Applications                 | Windows build + Docker packaging + Kubernetes deployment | Mixed technology stack requiring both traditional and cloud-native delivery    | **Pilot Validation Required** | Evaluate whether Taskfile orchestrates Dagger or Dagger becomes the primary execution engine based on complexity and operational needs.              |

---

## Example – Taskfile Execution Model

Azure DevOps or GitHub Actions acts as the orchestration layer, while Taskfile encapsulates the implementation logic.

**Azure DevOps Pipeline**

```yaml
steps:
- checkout: self

- script: task ci
  displayName: Run CI

- script: task cd
  displayName: Deploy
```

**Application Taskfile**

```yaml
version: '3'

includes:
  build:
    taskfile: ./taskfile-library/build/dotnet.yml

  deploy:
    taskfile: ./taskfile-library/deploy/azure-appservice.yml

tasks:
  ci:
    deps:
      - build:restore
      - build:compile
      - build:test

  cd:
    deps:
      - deploy:appservice
```

**Execution Flow**

```
Azure DevOps / GitHub
        │
    task ci
        │
Application Taskfile
        │
Shared Taskfile Library
        │
PowerShell / Bicep / CLI
```

---

## Example – Dagger Execution Model

Azure DevOps or GitHub Actions invokes a Dagger pipeline, with the implementation defined in code and executed inside containers.

**Azure DevOps Pipeline**

```yaml
steps:
- checkout: self

- script: dagger call ci
  displayName: Run CI

- script: dagger call deploy
  displayName: Deploy
```

**Example Dagger Module (Conceptual)**

```go
func (m *Pipeline) CI(ctx context.Context) error {
    return dag.Container().
        From("mcr.microsoft.com/dotnet/sdk:8.0").
        WithExec([]string{"dotnet", "restore"}).
        WithExec([]string{"dotnet", "build"}).
        WithExec([]string{"dotnet", "test"}).
        Sync(ctx)
}
```

**Execution Flow**

```
Azure DevOps / GitHub
        │
 dagger call ci
        │
 Dagger Engine
        │
 OCI Containers
        │
 BuildKit / Docker
```

### Key Difference

| Aspect                    | Taskfile                         | Dagger                              |
| ------------------------- | -------------------------------- | ----------------------------------- |
| Primary abstraction       | Task orchestration               | Containerized execution             |
| Implementation            | Shell, PowerShell, CLI tasks     | Code executed in OCI containers     |
| Best suited for           | Traditional enterprise workloads | Container-native workloads          |
| Infrastructure dependency | Existing agents                  | Container runtime (Docker/BuildKit) |
| Migration effort          | Low                              | Medium–High                         |
---

# Architectural Positioning

## Taskfile

Best suited for:

* Task orchestration
* Existing scripting reuse
* Platform independence
* Local execution
* Windows workloads
* Azure deployments

Strengths

* Minimal learning curve
* Simple YAML syntax
* Excellent PowerShell integration
* Easy migration from existing pipelines
* Lightweight
* Works well on Windows and Linux
* Strong developer experience

Challenges

* Limited built-in governance
* No native container execution model
* Limited execution graph intelligence
* Governance must be implemented separately

---

## Dagger

Best suited for:

* Container-first applications
* Kubernetes
* OCI build pipelines
* Polyglot services
* BuildKit optimization
* Ephemeral execution

Strengths

* Container-native
* Reproducible execution
* Excellent caching
* Strong portability
* Rich API
* Suitable for AI-assisted automation

Challenges

* Steeper learning curve
* Requires container runtime
* Greater platform investment
* Less suitable for PowerShell-heavy Windows automation

---

# Comparison Matrix

| Capability                  | Taskfile  | Dagger      |
| --------------------------- | --------- | ----------- |
| Windows Support             | Excellent | Moderate    |
| Linux Support               | Excellent | Excellent   |
| Existing Script Reuse       | Excellent | Good        |
| PowerShell Integration      | Excellent | Limited     |
| Bash Integration            | Excellent | Excellent   |
| Container-native Workflows  | Good      | Excellent   |
| Kubernetes                  | Good      | Excellent   |
| Docker Builds               | Good      | Excellent   |
| Local Execution             | Excellent | Excellent   |
| Learning Curve              | Low       | Medium–High |
| Platform Independence       | High      | High        |
| GitHub Readiness            | High      | High        |
| Governance (out of the box) | Limited   | Limited     |
| AI-native Workflows         | Moderate  | Strong      |

---

# Governance Considerations

Regardless of execution engine, governance should remain platform-independent.

Key principles:

* Immutable security controls
* Shared reusable assets
* Versioned platform libraries
* Controlled configuration points
* Policy validation
* Exception management

Governance should not depend on Taskfile or Dagger capabilities.

---

# Migration Considerations

## Taskfile

Migration effort:

Low

Existing scripts can largely be reused.

Typical migration:

Classic Pipeline

↓

Azure DevOps YAML

↓

Taskfile

↓

GitHub Workflow

---

## Dagger

Migration effort:

Medium to High

Requires:

* Container runtime
* Pipeline redesign
* New execution model
* BuildKit adoption
* Team enablement

---

# Pilot Strategy

Rather than selecting one tool through theoretical comparison, validate each against representative workloads.

## Taskfile Pilot

Representative applications:

* ASP.NET API
* Azure App Service
* Azure Function
* Bicep deployment
* PowerShell-heavy application

Evaluation criteria:

* Migration effort
* Existing script reuse
* Local execution
* Developer feedback
* Pipeline maintainability

---

## Dagger Pilot

Representative applications:

* Linux service
* Docker application
* Kubernetes deployment
* Microservice
* Multi-container testing

Evaluation criteria:

* Build performance
* Caching efficiency
* Container portability
* Kubernetes integration
* Developer experience

---

# Success Criteria

The pilots should objectively measure:

* Migration effort
* Pipeline readability
* Build performance
* Script reuse
* Operational complexity
* Governance compatibility
* Platform portability
* Developer satisfaction

---

# Decision Framework

The decision should be based on workload characteristics rather than organizational preference.

| Workload                               | Recommended Execution Engine                                                                                |
| -------------------------------------- | ----------------------------------------------------------------------------------------------------------- |
| Windows/.NET                           | Taskfile                                                                                                    |
| Azure App Service                      | Taskfile                                                                                                    |
| Azure Functions                        | Taskfile                                                                                                    |
| PowerShell-heavy automation            | Taskfile                                                                                                    |
| Linux applications (non-containerized) | Taskfile (default), validate Dagger where containerization adds value                                       |
| Docker image pipelines                 | Dagger                                                                                                      |
| Kubernetes workloads                   | Dagger                                                                                                      |
| Cloud-native microservices             | Dagger                                                                                                      |
| Hybrid applications                    | Determine through pilot whether Taskfile orchestrates Dagger or Dagger becomes the primary execution engine |

---

# Proposed Target Architecture

```text
                 Azure DevOps / GitHub Actions
             (Pipeline Orchestration Layer)
                        │
        ┌───────────────┴────────────────┐
        │                                │
   Traditional Workloads          Container-native Workloads
        │                                │
     Taskfile                        Dagger
        │                                │
 PowerShell / Bicep             Docker / BuildKit / OCI
        │                                │
     Azure Apps                  Kubernetes / Containers
```

The orchestration platform remains consistent while the execution engine is selected according to workload requirements.

---

# Recommendation

At this stage, there is strong alignment toward **Taskfile** for traditional enterprise workloads due to:

* Significant reuse of existing PowerShell automation
* Minimal migration effort
* Excellent developer experience
* Thin orchestration model
* Readiness for GitHub migration

However, container-native workloads represent a distinct class of applications where **Dagger** may provide superior capabilities.

Rather than enforcing a single enterprise standard, the recommended approach is to:

1. Adopt a workload-based execution strategy.
2. Pilot Taskfile for traditional enterprise applications.
3. Pilot Dagger for container-native workloads.
4. Define a unified operating model that standardizes governance, shared assets, and ownership regardless of the execution engine.
5. Publish clear workload guidance so application teams understand when each execution engine is the preferred choice.

This approach balances standardization with flexibility, allowing the platform to support a heterogeneous application estate without forcing all workloads into a single execution model.
