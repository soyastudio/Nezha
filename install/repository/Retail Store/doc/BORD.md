# Business Object Requirement Document - Retail Store

Created by Qun Wen on Feb 20, 2020

|  |  |
| ------ | ------ |
| Project Name: | Enterprise Data Integrtion Services|
| Project ID : | 16684 |
| Application Code: | ESED |
| Business Object Name: | Retail Store |
| Version: | 1.0 |
| Template Version: | 1.0 |

## Reference Document

## Table of Contents

- Business Object Purpose
    - Brief Description
    - BO Description and Classification
    - Producer of BO
    - Consumer of BO
        - Assumptions
    - Dependencies
    - Business Life Cycle Description (optional)
    - Additional System Information (optional)
- System Context
- System Qualities
    - Producers of the BO
    - Consumers of the BO
    - Process Flow Diagram
- Data Requirements and Interface Definitions
    - Data Input/Out Formats
        - Data Input Formats
        - Data Output Formats
- Transformation and Processing Rules
- None-Functional Requirements
    - Service Level Requirements (per producer)
    - Service Level Requirements (per consumer)
    - Error handling requirements
    - Security Requirements
- Open Issues



## Business Object Purpose
### Brief Description
``` TODO: BRIEF_DESCRIPTION


```

### BO Description and Classification
``` TODO: BO_DESCRIPTION
[Provide an overview of the business requirements the interface is intended to address. Requirements should be concisely mentioned here as detailed requirement document can be referred if needed.] 

```

### Producer of BO
``` TODO: BO_PRODUCER
[Provide a description of the Business Object and any logical classification of the different subtypes. E.g. PO is used for warehouse ordering and it can be classified as warehous PO, Plant  PO, Ingredient PO, External PO, PO PlantWarehouse. If possible, provide a diagram to indicate how this BO is used by different business entities/systems.]

```

| Producer Name	| Domain | Contact Information | Purpose |
| ----- | ----- | ----- | ----- |
| Name of Producer ( or App Code) | Retail/Supply Chain/etc. | contact.person@albertsons.com | Provide a brief description of the business usage of the BO. E.g. PO is generated to buy ingredients for product manufacturing |


### Consumer of BO
``` TODO: BO_CONSUMER
[Provide a brief description of each consumer of this BO.]

```
| Consumer Name | BOD Version | Domain | Contact Information | Purpose Channel |
| ----- | ----- | ----- | ----- | ----- |
| Name of Consumer ( or App Code) | 1.0 | Retail/Supply Chain/etc. | contact.person@albertsons.com | Provide a brief description of the business purpose of the BO. E.g. External PO is consumed to determine what to manufacture. | Kafka/CDS/MQ/etc. |

#### Assumptions
``` TODO: BO_CONSUMER_ASSUMPTION
[Provide a list of factors that may impact the business meaning/usage of this BO. E.g.: Customer Unification Project will change all the source systems for Retail Customer BO and we will be creating this BO under that assumption that this BO is still valid after the conversion, only mapping will need to be changed.]

```

### Dependencies
``` TODO: DEPENDENCY
[Provide a brief description of each factor/system that supplied information for this BO. E.g. For SupplyChainItemMaster, SSIMS provided the main data while EXE provided the hazard material information.]

```

### Business Life Cycle Description (optional)
``` TODO: BUSINESS_LIFE_CYCLE
[Provide a description of the business/system interaction before/after the BO information exchange. It can be a description of existing process to help us understand the big picture. Provide a BPM diagram if available.]

```

### Additional System Information (optional)
``` TODO: ADDITIONAL_SYSTEM_INFOMATION


```

## System Context
``` TODO: SYSTEM_CONTEXT
[Provide a system context diagram identifying all the source and target systems and their interactions (i.e. events, data flows) within the scope of the interface requirements.]

```

## System Qualities
``` TODO: SYSTEM_QUALITY
[This section describes the system qualities for each system identified in the system context diagram for each collaboration. A system may play a different role or interact differently for different collaboration. The aim is to capture all such system qualities. Ensure that every collaboration is captured as a new subsection and you capture interaction details of each participating target and source system in separate table.  Collaboration is an interaction between source and target system(s) to achieve a desired behavior]

```

### Producers of the BO
[One table for each producer]  
|  |  |
| --- | --- |
| System | [Identify the participating producer] |
| Description | [Describe the role of the producer] |
| Communication Protocol | [What is the communication  protocol the system expects, for example,  Web Service, SOAP/HTTP,JMS,  MQ Messages, SFTP] |
| Format | [Identify the format, for example SOAP, XML, delimited or fixed length file] |
| System Interaction Style | [Identify the style of interaction for the system, for example, one way, request-reply, asynchronous or synchronous ] |
| Timing | [Batch or Real-Time execution. Are transactions processed by the system individually as they arrive or in periodically in groups? Is there a critical time window (e.g. store closing) for this producer?] |
| Process Trigger | [What are the  triggers  for the system to initiate processing in the context of   integration flow, for example, scheduled, activated on presence of file,  arrival of new order message on queue ] |
| Data Entities within Logical Unit of Work | [Describe the key data element that defines to unit of work for the dataset for transactional purposes. Examples include orders, returns, payments] |
| Transaction Qualities | [Describe any rollback or compensation requirements to ensure ACID properties of Unit Of Work. For example, an order to be sent to multiple target systems is ‘all or nothing’, all records in a file ] |


### Consumers of the BO
[One table for each consumer]  

### Process Flow Diagram
``` TODO: PROCESS_FLOW_DIAGRAM


```
    
## Data Requirements and Interface Definitions
### Data Input Formats
``` TODO: INPUT_DATA_FORMAT


```

### Data Output Formats
``` TODO: OUTPUT_DATA_FORMAT


```

## Transformation and Processing Rules
``` TODO: TRANSFORMATION


```

## None-Functional Requirements
### Service Level Requirements (per producer)
``` TODO: NFR_PRODUCER


```

### Service Level Requirements (per consumer)
``` TODO: NFR_CONSUMER


```

### Error handling requirements
``` TODO: NFR_ERROR_HANDLING


```

### Security Requirements
``` TODO: NFR_ERROR_SECURITY


```

## Open Issues
``` TODO: OPEN_ISSURE


```