# Business Object Design Document - {{BOD_NAME}}

Created by {{BODD_AUTHOR}} on {{BODD_CREATED_TIME}}

|  |  |
| ------ | ------ |
| Project Name: | Enterprise Data Integrtion Services|
| Project ID : | {{PROJECT_ID}} |
| Application Code: | ESED |
| Business Object Name: | {{BOD_NAME}} |
| Version: | {{PROJECT_VERSION}} |
| Template Version: | {{TEMPLATE_VERSION}} |

## Reference Document

## Table of Contents

- How to use this document
- Interface Overview
    - Solution Statement
    - Design Considerations
        - Goals
        - Assumptions
        - Dependencies
        - Constraints
    - Design Decisions
- Integration Design
    - High level integration design
    - Integration Flows
    - Implementation Guideline 
    - Detailed Component Design
- Error Handling Strategy
- Security
- Monitoring
- Administration
    - Execution Strategy
    - Recovery Strategy
    - Housekeeping Strategy


## How to use this document
The Business Object Design Document (BODD) captures the logical design of the integration solution. BODD focus on the design to satisfy the requirements as described in the Business Object Requirement Document (BORD) – What components will be used and how they work together. This document should not be used for implementation documentation; however, some implementation guidance can be included when a design decision has significant impact on the implementation.

## Interface Overview
### Solution Statement
``` TODO: SOLUTION_STATEMENT
[Describe the high level solution in 1 or 2 sentences. E.g. This interface will get an input message from all source systems for purchase item BOD, transform it to the canonical format and publish to all consumers.]

```

### Design Considerations
#### Goals
``` TODO: DESIGN_GOAL
[Highlight key functional and non-functional requirements which will drive the design. E.g.: Publish Purchase Orders from EB2B, SSIMS, Manufacturing plants to all consumers in CMM format.]

```

#### Assumptions
``` TODO: DESIGN_ASSUMPTION
[Identify any assumptions for this interface to satisfy the requirements. E.g.: Source application will publish message to Azure Kafka and replicated to Atlas Kafka.] 

```

#### Dependencies
``` TODO: DESIGN_DEPENDENCY
[Identify any dependency for this interface to operate. E.g.:  OIAM is up and running and user id is setup with proper access rights within OIAM.]

```

#### Constraints
``` TODO: DESIGN_CONSTRAINT
[Describe any constraints that have a significant impact on the design of the solution, for example standards compliance, physical constraints such as source or target system protocols, technology constraints, etc. E.g: Target system is CheetahMail web service, requires a login call before each service call. E.g.: Source database cannot be used for transformation. ]

```

### Design Decisions
``` TODO: DESIGN_DECISION
[Identify significant design decisions or strategies behind the design solution, including the motivation, rationale and implications. Usually these will involve a decision relating to a non-functional requirement, selection of protocols/formats, error/retry, pattern selection, and so on. Please click here for samples.]

```

| | |
| ----- | ----- |
| Issue or Requirement | |
| Design Decision | |
| Assumptions | |
| Justification | |
| Implementations | |


## Integration Design
### High level integration design
``` TODO: INTEGRATION_DESIGN
[Provide a component diagram representing the solution design. This is to illustrate all the components, which together provides a solution, the dependency between each component and how they are related. If the design of this BOD falls under a standard pattern, delete this and the following section, use (include page) macro to include the HL design section from that pattern.]

```

### Integration Flows
``` TODO: INTEGRATION_FLOWS
[Provide a sequence diagram [preferred] or text description for each significant integration flow, including alternate and exception flows. All process flows in the interface requirement document should be addressed. This sequence diagram captures the processing steps within and across system boundaries.]

```

### Implementation Guideline
``` TODO: IMPLEMENTATION_GUIDELINE
[Specify the high level implementation details and any special implementation decisions taken at the design time.  This section will list down the components and highlight the implementation recommendation.] 

```

### Detailed Component Design
``` TODO: COMPONENT_DESIGN
[This section provides detailed design of components from programming point of view. Components documented in this section should have multiple sub-components and requires multi-step interaction. This is an optional section, only used when more detailed instruction is needed for complex component. Use 1 table per component.]

```

| # | Component | Description |
| ----- | ----- | -----|
| 1 | | |
| 2 | | |

## Error Handling Strategy
``` TODO: ERROR_HANDLING
[Identify the system errors which can occur and how they will be handled.] 

```

| Error | Expected behavior and design details |
| ----- | ----- |
| | |
| | |

## Security
``` TODO: SECURITY_DESIGN
[Specify how the solution is designed to meet the applicable security requirements] 

```

| Security Requirement | Design |
| ----- | ----- |
| e.g. Sensitive data must be secured | e.g.Use https at transport level and encrypt personal data |
| | |

## Monitoring
``` TODO: MONITORING_DESIGN
[For application monitoring you can find the alerts definition spreadsheet and instructions at the following link https://discoverit.safeway.com/wiki/Application_Monitoring. ] 

```

## Administration
### Execution Strategy
``` TODO: ADMIN_EXECUTION
[This section documents guidance for the runtime setup that has significant impact on the design. (e.g. Message flow should be deployed to multiple execution group running multi-threaded to achieve maximum performance. The job is triggered by exit routine of MIM.)] 

```

### Recovery Strategy
``` TODO: ADMIN_RECOVERY
[This section documents guidance for the recovery procedure when the transaction has failed. (e.g. Restart ETL job from point of failure or restart from the beginning.  )] 

```

### Housekeeping Strategy
``` TODO: ADMIN_HOUSEKEEPING
[Identify any housekeeping which needed to be performed. E.g. Log files should be purged after 7 days.] 


```