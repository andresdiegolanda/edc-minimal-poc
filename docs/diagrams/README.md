# PlantUML Diagrams for EDC Project Documentation

This directory contains PlantUML source files for all diagrams referenced in the `PROJECT_OVERVIEW.md` document.

## Diagram List

### Architecture Diagrams
1. **edc-architecture-overview.puml** - High-level EDC architecture
2. **detailed-architecture.puml** - Complete system architecture with all components
3. **connector-components.puml** - Internal components of the EDC connector
4. **extension-architecture.puml** - How to add custom extensions

### Concept Diagrams
5. **asset-structure.puml** - Asset data structure and properties
6. **policy-structure.puml** - Policy definition with permissions and constraints
7. **contract-definition.puml** - How contracts link assets and policies

### Flow Diagrams
8. **data-flow-sequence.puml** - Step-by-step data discovery and access sequence
9. **api-request-flow.puml** - How API requests are processed
10. **testing-workflow.puml** - Complete testing workflow from build to verification

### Implementation Diagrams
11. **policy-enforcement.puml** - Policy enforcement during data access
12. **production-architecture.puml** - Example production deployment

## How to Generate Diagrams

### Prerequisites
- Install PlantUML: https://plantuml.com/download
- Or use online editor: https://www.plantuml.com/plantuml/

### Generate PNG Images

**Using PlantUML CLI:**
```bash
java -jar plantuml.jar *.puml
```

**Using VS Code Extension:**
1. Install "PlantUML" extension by jebbs
2. Open any `.puml` file
3. Press `Alt+D` to preview
4. Right-click → "Export Current Diagram" → Choose PNG

**Using Online Editor:**
1. Visit https://www.plantuml.com/plantuml/
2. Copy/paste diagram code
3. Download as PNG

### Recommended Settings
- **Format**: PNG
- **Resolution**: 300 DPI (for documentation)
- **Background**: White or transparent
- **Font Size**: 12pt minimum for readability

## PlantUML Template

All diagrams follow this basic structure:

```plantuml
@startuml diagram-name
!theme plain
skinparam backgroundColor white
skinparam shadowing false

' Your diagram content here

@enduml
```

## Diagram Naming Convention

- Use kebab-case: `edc-architecture-overview.puml`
- Output PNG should match: `edc-architecture-overview.png`
- Keep names descriptive and consistent with doc references

## Integration with Documentation

Each diagram is referenced in `PROJECT_OVERVIEW.md` using:

```markdown
![Diagram Title](diagrams/diagram-name.png)
*Caption describing the diagram*
```

After generating PNG files, the images will automatically display in the documentation.

## Tips for Creating Effective Diagrams

1. **Keep it simple**: Focus on one concept per diagram
2. **Use consistent styling**: Same colors for same component types
3. **Add legends**: Help readers understand symbols and colors
4. **Include captions**: Explain what the diagram shows
5. **Optimize for print**: Use high contrast, large fonts
6. **Test rendering**: Verify diagram looks good in both light/dark themes

## Example Diagrams to Create

### 1. EDC Architecture Overview
- Show connector as central component
- HTTP APIs as interfaces
- Data stores as persistence layer
- External systems as context

### 2. Data Flow Sequence
- Actor: Client application
- System: EDC Connector
- Steps: Discovery → Policy Review → Contract Check → Access

### 3. Policy Structure
- Permissions node (green)
- Constraints node (yellow)
- Prohibitions node (red)
- Duties node (blue)

### 4. Production Architecture
- Load balancer
- Multiple connector instances
- Shared database
- Monitoring systems
- Security layers

## Resources

- [PlantUML Official Guide](https://plantuml.com/guide)
- [PlantUML Component Diagrams](https://plantuml.com/component-diagram)
- [PlantUML Sequence Diagrams](https://plantuml.com/sequence-diagram)
- [PlantUML Class Diagrams](https://plantuml.com/class-diagram)
- [PlantUML Deployment Diagrams](https://plantuml.com/deployment-diagram)

---

**Note**: Start with the most important diagrams first:
1. edc-architecture-overview.puml
2. data-flow-sequence.puml
3. connector-components.puml

These three will provide the most value for users new to EDC.
