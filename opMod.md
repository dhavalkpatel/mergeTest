# CI/CD Operating Model Considerations

## Purpose

This document outlines the operating model considerations for modernizing CI/CD across the organization. While the architectural evaluation focuses on selecting an appropriate abstraction layer (e.g., Taskfile), this document defines the governance, ownership, lifecycle, and adoption practices required to operate a scalable and maintainable CI/CD platform.

These considerations are intended to guide the evolution of the platform as adoption grows across Azure DevOps and GitHub.

---

# 1. Objectives

The operating model should enable:

* Consistent engineering practices across teams
* Reusable CI/CD capabilities
* Reduced duplication
* Platform independence where practical
* Controlled evolution of shared assets
* Clear ownership and governance
* Visibility into adoption and technical debt
* Measurable success criteria for continuous improvement

---

# 2. Operating Principles

The platform should be built around the following principles:

## Standardize the common, not the application

Every application has unique delivery requirements. The objective is not to force identical pipelines, but to standardize common capabilities such as:

* Build
* Testing
* Security scanning
* Packaging
* Infrastructure deployment
* Application deployment
* Notifications
* Versioning

Application teams should compose these capabilities rather than reimplement them.

---

## Thin orchestration, reusable implementation

Pipeline YAML should remain an orchestration layer.

Implementation logic should reside within reusable assets such as:

* Taskfile libraries
* PowerShell modules
* Bicep modules
* GitHub Composite Actions
* Shared deployment scripts

This minimizes platform-specific implementation and simplifies future migration.

---

## Platform independence

Business logic should not depend on a specific CI platform.

Platform-specific responsibilities should remain limited to:

* Triggers
* Secrets
* Variables
* Agent selection
* Approvals
* Environment controls

Everything else should be reusable.

---

# 3. Shared Platform Assets

Rather than standardizing complete pipelines, the platform should provide reusable building blocks.

Example structure:

```text
taskfile-library/

build/
    dotnet.yml
    python.yml
    node.yml

test/
    dotnet.yml
    python.yml
    security.yml

package/
    docker.yml
    nuget.yml

deploy/
    azure-appservice.yml
    azure-function.yml
    aks.yml

infra/
    bicep.yml
    keyvault.yml
    storage.yml

security/
    sast.yml
    dependency-scan.yml

common/
    git.yml
    version.yml
    notifications.yml
```

Application repositories consume only the capabilities they require.

Example:

```yaml
includes:
  build:
    taskfile: ./taskfile-library/build/dotnet.yml

  deploy:
    taskfile: ./taskfile-library/deploy/azure-appservice.yml
```

---

# 4. Pipeline Composition Model

Application pipelines should remain lightweight.

Example Azure DevOps Pipeline:

```yaml
steps:

- checkout: self

- script: task ci

- script: task cd
```

The application Taskfile orchestrates reusable platform capabilities.

Benefits:

* Minimal YAML
* Easier review
* Reduced duplication
* Easier migration between CI platforms

---

# 5. Adoption and Drift Visibility

As adoption increases, visibility becomes essential.

The platform should provide reporting on:

* Repositories onboarded
* Teams onboarded
* Version of shared assets in use
* Legacy implementations
* Deprecated assets
* Unsupported customizations

Example dashboard:

| Team   | Repositories | Shared Assets Version | Status                 |
| ------ | ------------ | --------------------- | ---------------------- |
| Team A | 25           | v2.1                  | Healthy                |
| Team B | 18           | v1.4                  | Upgrade Available      |
| Team C | 12           | Legacy                | Modernization Required |

Possible metrics include:

* % repositories using standard platform assets
* % repositories on latest supported version
* Number of custom pipelines
* Number of deprecated assets
* Average upgrade lag
* Platform adoption by business unit

This visibility enables proactive support and targeted modernization.

---

# 6. Governance Baseline

Regardless of whether Azure DevOps or GitHub is used, governance expectations should remain consistent.

Suggested governance baseline:

## Security

* Secret management
* Least privilege
* Identity federation
* Security scanning
* Dependency scanning

## Build Standards

* Standard build patterns
* Reproducible builds
* Artifact naming
* Version generation

## Deployment Standards

* Environment promotion
* Rollback capability
* Deployment approvals
* Deployment logging

## Infrastructure

* Infrastructure as Code
* Standard Bicep modules
* Resource naming
* Tagging

## Quality

* Test execution
* Code coverage
* Static analysis
* Security gates

## Observability

* Pipeline logging
* Deployment history
* Audit trail
* Metrics

These standards should apply regardless of CI platform.

---

# 7. Azure DevOps to GitHub Mapping

Separating orchestration from implementation reduces migration effort.

| Shared Capability  | Azure DevOps        | GitHub              |
| ------------------ | ------------------- | ------------------- |
| Taskfile Library   | Shared              | Shared              |
| PowerShell Modules | Shared              | Shared              |
| Bicep Modules      | Shared              | Shared              |
| Build Logic        | Shared              | Shared              |
| Deployment Logic   | Shared              | Shared              |
| Pipeline YAML      | Azure DevOps YAML   | GitHub Workflow     |
| Secrets            | Variable Groups     | GitHub Secrets      |
| Authentication     | Service Connections | OIDC / Environments |
| Triggers           | Azure Pipelines     | GitHub Events       |

The goal is to ensure only the orchestration layer changes during platform migration.

---

# 8. Ownership and Operating Model

A clear ownership model reduces ambiguity and improves maintainability.

| Responsibility            | Owner                               |
| ------------------------- | ----------------------------------- |
| CI/CD Standards           | Platform Engineering                |
| Taskfile Library          | Platform Engineering                |
| Shared PowerShell Modules | Platform Engineering                |
| Shared Bicep Modules      | Platform Engineering                |
| GitHub Reusable Workflows | Platform Engineering                |
| Version Management        | Platform Engineering                |
| Release Cadence           | Platform Engineering                |
| Application Taskfiles     | Application Teams                   |
| Application Pipelines     | Application Teams                   |
| Exception Approvals       | Platform Engineering / Architecture |

Application teams remain responsible for application-specific logic.

Platform Engineering owns reusable platform capabilities.

---

# 9. Lifecycle and Versioning of Shared Assets

Versioning applies to reusable platform assets—not to every application's pipeline.

## Assets that should be versioned

* Shared Taskfile libraries
* Shared PowerShell modules
* Shared Bicep modules
* Shared GitHub Composite Actions
* Shared Azure DevOps YAML templates
* Shared security tasks
* Shared deployment tasks

## Assets that should evolve with the application

* Application Taskfile
* Application pipeline
* Application deployment sequence
* Application-specific scripts

This approach allows application teams to adopt platform improvements at their own pace while maintaining stability.

Versioning recommendations:

* Semantic Versioning
* Backward compatibility where practical
* Documented deprecation policy
* Published release notes
* Defined support window for older versions

---

# 10. Release and Change Management

Shared platform assets should follow a controlled release process.

Suggested lifecycle:

Development

↓

Internal Validation

↓

Pilot Teams

↓

General Availability

↓

Deprecation

↓

Retirement

Major changes should be validated with representative applications before broad adoption.

---

# 11. Exceptions Management

Not every application will conform to the standard model.

Valid exceptions may include:

* Legacy technologies
* Regulatory constraints
* Vendor-managed applications
* Platform limitations

Exceptions should be:

* Documented
* Time-bound where possible
* Reviewed periodically
* Approved through a lightweight governance process

---

# 12. Pilot Success Measures

Before standardizing on a platform abstraction, representative pilots should validate assumptions.

Suggested pilot applications:

* Standard .NET API
* Azure Function
* Legacy PowerShell-heavy application
* Infrastructure deployment using Bicep

Evaluation criteria:

| Measure                    | Success Indicator                                               |
| -------------------------- | --------------------------------------------------------------- |
| Migration effort           | Reduced implementation effort compared to direct YAML migration |
| Pipeline readability       | Simpler orchestration with improved maintainability             |
| Existing script reuse      | Minimal refactoring required                                    |
| Local developer experience | Tasks executable outside CI                                     |
| Build consistency          | Same results locally and in CI                                  |
| GitHub migration readiness | Minimal orchestration changes                                   |
| Platform impact            | No significant infrastructure changes                           |
| Developer feedback         | Positive adoption experience                                    |

---

# 13. Adoption Roadmap

## Phase 1

* Build reusable platform assets
* Pilot with selected applications
* Gather feedback

## Phase 2

* Expand adoption
* Publish standards
* Introduce dashboards
* Begin governance

## Phase 3

* Organization-wide rollout
* Version management
* Automated compliance reporting
* Continuous platform improvement

---

# 14. Key Success Metrics

The operating model should be measured using objective metrics.

Examples:

* Percentage of repositories using shared platform assets
* Pipeline duplication reduction
* Migration effort reduction
* Time to onboard new applications
* Mean time to update shared capabilities
* Percentage of applications on supported versions
* Platform adoption by business unit
* Number of approved exceptions
* Pipeline execution reliability
* Developer satisfaction

---

# 15. Recommendations

1. Adopt a composable, capability-based library rather than monolithic pipelines.
2. Keep CI/CD pipelines focused on orchestration; move implementation into reusable assets.
3. Establish a clear ownership model between Platform Engineering and application teams.
4. Version shared platform assets independently from application pipelines.
5. Track adoption and drift through centralized reporting.
6. Maintain a consistent governance baseline across Azure DevOps and GitHub.
7. Validate the approach through representative pilots before broad rollout.
8. Treat governance, versioning, and operating practices as first-class components of the CI/CD platform—not as afterthoughts.

---

# Appendix A – Responsibilities (RACI)

| Activity                           | Platform Engineering | Application Team | Architecture |
| ---------------------------------- | -------------------- | ---------------- | ------------ |
| Define CI/CD standards             | R                    | C                | A            |
| Maintain shared Taskfile library   | R                    | C                | I            |
| Maintain shared Bicep modules      | R                    | C                | I            |
| Maintain reusable deployment tasks | R                    | C                | I            |
| Develop application Taskfiles      | C                    | R                | I            |
| Maintain application pipelines     | C                    | R                | I            |
| Approve exceptions                 | C                    | C                | A            |
| Define governance policies         | R                    | C                | A            |
| Measure adoption                   | R                    | I                | C            |
| Plan platform evolution            | R                    | C                | A            |

**Legend:** R = Responsible, A = Accountable, C = Consulted, I = Informed
