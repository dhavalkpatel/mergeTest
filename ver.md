Enterprise Taskfile Versioning & Platform Telemetry

Version: 1.0
Owner: Platform Engineering

Overview

This document defines the enterprise strategy for:

Versioning the shared Taskfile Platform Library
Consuming platform capabilities
Platform telemetry reporting
Version compliance dashboard
Governance and upgrade process

The goal is to provide a governed, observable, and scalable CI/CD platform where application teams consume standardized capabilities while Platform Engineering maintains visibility into adoption.

Why Version the Platform Library?

Without versioning:

Applications copy Taskfiles and scripts.
Bug fixes require updating hundreds of repositories.
Security fixes cannot be enforced consistently.
Platform capabilities drift over time.

Instead, applications consume a released Platform Library similar to how GitHub Actions are consumed.

Example:

- uses: actions/checkout@v4

Equivalent concept:

Platform Library v1.4.0

Applications depend on a released platform version rather than individual scripts.

Versioning Strategy

Use Semantic Versioning.

Version	Meaning	Example
Major	Breaking changes	Task renamed, parameter removed
Minor	Backward-compatible capability	Added SBOM generation
Patch	Bug fixes or security updates	Fixed deployment issue

Example releases

v1.0.0
v1.1.0
v1.2.3
v2.0.0
Repository Structure
.platform/

    VERSION

Taskfile.yml

Example

.platform/VERSION

1.4.0

This file becomes the single source of truth.

Platform Library Structure
platform-library/

build/

deploy/

security/

quality/

common/

telemetry/

Taskfile.yml

CHANGELOG.md
Application Taskfile

Application teams should never implement telemetry or version reporting.

version: '3'

includes:
  platform:
    taskfile: .platform/taskfile.yml

tasks:

  ci:
    deps:
      - build
      - test
      - platform:telemetry

  build:
    cmds:
      - dotnet build

Notice that the application simply consumes the platform capability.

Shared Platform Taskfile

Platform Engineering owns this file.

version: '3'

vars:

  PLATFORM_VERSION:
    sh: cat .platform/VERSION

  TIMESTAMP:
    sh: date -u +"%Y-%m-%dT%H:%M:%SZ"

tasks:

  telemetry:

    desc: Report platform telemetry

    cmds:

      - |
        echo '
        {
          "repository":"'$BUILD_REPOSITORY_NAME'",
          "project":"'$SYSTEM_TEAMPROJECT'",
          "branch":"'$BUILD_SOURCEBRANCHNAME'",
          "environment":"'$ENVIRONMENT'",
          "buildId":"'$BUILD_BUILDID'",
          "pipeline":"'$BUILD_DEFINITIONNAME'",
          "platformVersion":"{{.PLATFORM_VERSION}}",
          "timestamp":"{{.TIMESTAMP}}"
        }'

The payload can be sent to an internal telemetry endpoint or directly to Azure Monitor.

Telemetry Payload

Example payload

{
  "repository": "ClaimsAPI",
  "project": "Insurance",
  "branch": "main",
  "environment": "Production",
  "pipeline": "CI",
  "buildId": "8421",
  "platformVersion": "1.4.0",
  "timestamp": "2026-08-18T09:42:16Z"
}
Azure DevOps Pipeline

The pipeline simply executes Taskfile.

steps:

- checkout: self

- script: task ci
  env:

    ENVIRONMENT: Production

Everything else comes from Azure DevOps predefined variables.

Telemetry Architecture
Azure DevOps Pipeline

        │

        ▼

Taskfile

        │

        ▼

Platform Telemetry

        │

        ▼

Azure Log Analytics

        │

        ▼

Azure Workbook / Power BI
Log Analytics Table

Example custom table

PlatformTelemetry_CL

Example records

Repository	Branch	Environment	Platform Version	Timestamp
ClaimsAPI	main	Prod	1.4.0	18-Aug
BillingAPI	develop	QA	1.3.0	18-Aug
PolicyAPI	main	Prod	1.4.0	18-Aug
Dashboard
Executive Summary
Repositories                 285

Current Version              251

Upgrade Available             28

Unsupported                    6

Adoption Rate               88%
Version Distribution
v1.4.0

█████████████████████

251


v1.3.0

████

28


v1.2.0

█

6
Repository Compliance
Repository	Platform Version	Latest	Status
ClaimsAPI	1.4.0	1.4.0	✅ Current
BillingAPI	1.3.0	1.4.0	⚠ Upgrade Available
PolicyAPI	1.2.0	1.4.0	❌ Unsupported
Team Adoption
Team	Current	Upgrade	Unsupported
Claims	18	1	0
Billing	12	3	1
Policy	9	0	0
Upgrade Trend
January

40%

February

58%

March

72%

April

88%

This provides leadership with visibility into platform adoption over time.

Version Governance

Support policy

Version	Status
Latest	Supported
N-1	Supported
Older	Deprecated
End of Life	Blocked

Example

Latest	Repository	Result
1.4.0	1.4.0	✅
1.4.0	1.3.0	⚠ Warning
1.4.0	1.2.0	❌ Fail
Pipeline Validation

At pipeline startup

Read .platform/VERSION

↓

Compare against supported versions

↓

Supported?

↓

Yes

↓

Continue

No

↓

Fail Pipeline

Error

Platform Library v1.2.0 is no longer supported.

Upgrade to v1.4.0.
Release Lifecycle
Platform Team

↓

Develop

↓

Internal Validation

↓

Release v1.5.0

↓

Pilot Applications

↓

Organization Rollout

↓

Deprecate v1.3.0

↓

Retire Unsupported Versions
Future Platform Telemetry

Although initially focused on version adoption, the telemetry model is designed to evolve.

Future metadata can include:

Category	Examples
Platform	Platform Library Version, Taskfile Version
Repository	Repository, Branch, Team
Pipeline	Build Duration, Build Status
Runtime	Windows/Linux, Runner Type
Security	Checkmarx Version, SBOM Generated
Deployment	Environment, Region
CI Platform	Azure DevOps, GitHub Actions

This allows Platform Engineering to build a centralized operational dashboard for governance, adoption tracking, migration progress, and CI/CD health without requiring changes to individual application repositories. The only evolving component is the shared Platform Library, keeping application Taskfiles stable and lightweight.
