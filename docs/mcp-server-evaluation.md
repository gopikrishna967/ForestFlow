# MCP Server Evaluation for ForestFlow

## Summary

**Recommendation: Not right now.** An MCP server is not a high-priority investment for this team at this stage. The existing REST API is well-defined and covers all operational needs. The team's effort would yield more value directed elsewhere. That said, there is a narrow scenario where an MCP server becomes worthwhile — outlined below.

## What Is an MCP Server?

An MCP (Model Context Protocol) server exposes **tools**, **resources**, and **prompts** to AI coding assistants (Cursor, Claude Desktop, Windsurf, etc.). It lets an AI assistant perform actions against external systems on behalf of a developer — deploying a model, querying metrics, managing configuration — through natural language conversation rather than manual API calls.

## ForestFlow's Current API Surface

ForestFlow already exposes a comprehensive REST API:

| Operation | Endpoint | Verb |
|---|---|---|
| Deploy a model (Servable) | `/servable` | POST |
| Create/update a Contract | `/contract/{org}/{project}/{num}` | POST/PUT |
| List Contracts | `/contracts/list` | GET |
| List Servables in a Contract | `/{org}/{project}/{num}/list` | GET |
| Score (inference) | `/{org}/{project}/{num}/score` | POST |
| Get Servable metrics | `/{org}/{project}/{num}/stats` | GET |
| Get Servable metadata | `/{org}/{project}/{num}/{version}/metadata` | GET |
| Get Servable settings | `/{org}/{project}/{num}/{version}/settings` | GET |
| Update policy settings | `/{org}/{project}/{num}/{version}/policy` | PUT |
| Update logging settings | `/{org}/{project}/{num}/{version}/logging` | PUT |
| Delete a Servable | `/{org}/{project}/{num}/{version}` | DELETE |
| GraphPipe inference | `/{org}/{project}/{num}/gp` | POST |

This API is the foundation any MCP server would wrap. The question is whether that wrapper provides enough incremental value to justify building and maintaining it.

## Arguments Against Building an MCP Server Now

1. **Thin wrapper over an existing API.** The MCP server would not unlock new capabilities — it would repackage existing REST endpoints for AI assistant consumption. The REST API is already well-documented and usable with standard tools (curl, HTTPie, any HTTP client).

2. **Operational, not development-time.** MCP servers are most valuable when developers need to interact with a system *during coding sessions* — querying databases, searching logs, managing infrastructure. ForestFlow interactions (deploying models, checking stats) are primarily operational tasks that happen less frequently and are already served by the REST API, scripts, or CI/CD pipelines.

3. **Payload complexity is manageable.** While ForestFlow's deployment payloads (FQRVs, policies, flavors) have some structure, they follow consistent patterns. Template JSON files or simple wrapper scripts solve this more cheaply than an MCP server.

4. **Maintenance cost is real.** An MCP server is another component to build, test, version, and keep in sync with ForestFlow's API. For a project with a focused scope like ForestFlow, this overhead matters.

5. **Planned features offer more value.** Adding support for TensorFlow, Keras, PFA, and Spark ML models would expand ForestFlow's reach significantly more than an MCP server would.

## When an MCP Server Would Make Sense

An MCP server becomes worth building if **two or more** of the following are true:

- **The team actively uses AI coding assistants** (Cursor, Claude Desktop, etc.) as part of their daily workflow and would use conversational model management frequently.
- **Data scientists on the team struggle with ForestFlow's API.** If the JSON payloads, policy configurations, and FQRV structure cause friction, an MCP server that lets them say "deploy my H2O model from /path/to/model with immediate phase-in" could reduce errors and onboarding time.
- **The team frequently debugs model serving issues** and would benefit from asking an AI assistant "what models are deployed under the scheduling contract?" or "show me the stats for the latest energy_output servable" inline while coding.
- **ForestFlow is being offered as a platform to other teams** who may not be familiar with the API, and an MCP server serves as a more accessible interface alongside documentation.

## If You Do Build One, Keep It Minimal

If the team decides to proceed, scope it tightly to high-value operations:

### Recommended MCP Tools (Priority Order)

1. **list_contracts** — List all active contracts. Low complexity, high frequency.
2. **list_servables** — List servables under a given contract. Essential for visibility.
3. **get_servable_stats** — Retrieve metrics for servables. Useful during debugging.
4. **deploy_model** — Deploy a servable with sensible defaults. Highest value but most complex to implement correctly.
5. **get_servable_metadata** — Retrieve model input/output schema. Helpful when writing inference code.
6. **score** — Run inference against a contract. Enables testing from within the IDE.

### What to Skip

- Contract creation/update (infrequent, high-stakes — better done deliberately through reviewed config)
- Policy updates (same reasoning)
- Servable deletion (destructive, should go through established processes)
- GraphPipe operations (specialized protocol, not suited to conversational interaction)

## Conclusion

ForestFlow's REST API is already a clean, functional interface. An MCP server would be a convenience layer, not a capability expansion. Unless the team's daily workflow involves frequent, ad-hoc interaction with ForestFlow from within AI-assisted coding environments, the effort is better spent on expanding model format support or improving operational tooling. Revisit this decision if AI coding assistants become a core part of the team's development workflow.
