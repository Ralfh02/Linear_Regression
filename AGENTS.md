# AGENTS.md

NetBeans (Ant) Java Swing desktop app: a student ML assignment using the Weka
library. No Maven/Gradle, no test sources, no README.

## Build & run
- Open in NetBeans ("Run Project"/F6) or run `ant run` from the repo root.
  `build.xml` only imports the generated `nbproject/build-impl.xml`; do not edit
  `build-impl.xml` by hand.
- Requires JDK 26 (`javac.source`/`javac.target=26`) and `weka.jar`.
- `weka.jar` is hardcoded to an absolute path in `nbproject/project.properties`
  (`file.reference.weka.jar=C:\Users\Reign\Downloads\weka.jar`). Fix it (or add the
  jar via NetBeans Libraries) before building elsewhere. `build.sysclasspath=ignore`
  means Weka is the ONLY classpath entry.
- No tests: `test.src.dir=test` is set but no `test/` directory exists.

## Architecture
- Package `salespredictionsimplelinearreg_lindog`. Entry point
  `SalesPredictionSimpleLinearReg_Lindog` opens `MainMenu`, whose 3 buttons launch
  `Main` (simple linear regression, `saleslindogss.arff`),
  `FrmEmployeePromotionLindog` (RandomForest, `employee_promotion.arff` /
  `Bad_employee_promotion.arff`), and `AssignmentForm` (`assignment.arff`).
- `StartFrame` exists but is NOT reachable from `MainMenu`; it is only opened by the
  "return" buttons in `Main`/`AssignmentForm`. Treat it as legacy/parallel.
- ML is done directly in the frame classes with Weka
  (`SimpleLinearRegression`, `RandomForest`, `Evaluation`).
- `.arff` files live in the repo root and are opened by RELATIVE filename, so the app
  must launch with the repo root as CWD. Do not move them without updating the source.

## NetBeans GUI forms
- Each frame has a paired `.java` + `.form`. In the `.java`, the region between
  `//GEN-BEGIN:initComponents` and `//GEN-END:initComponents` is generated from the
  `.form`; edit it in the Form Editor, not by hand. Put custom layout/logic in
  separate methods (e.g. `applyFullscreenLayout()` runs after `initComponents()` and
  rebuilds the panels at runtime).
- Images are loaded from the classpath (`/salespredictionsimplelinearreg_lindog/Assets/...`),
  i.e. resources live under `src/<package>/Assets/`.

## Repo / git gotchas
- Working-tree files are CRLF; committed `main` is LF. Nearly every file shows as
  "modified" purely from line-ending churn. Use `git diff --ignore-cr-at-eol` to see
  real changes and do NOT commit line-ending-only diffs.
- `build/classes/` and image resources are tracked in git (no `.gitignore`), so
  regenerated `.class` files appear as changes.
- Branch `feature/csvImplementation` exists, but the code currently reads ARFF only
  (`weka.core.converters.ConverterUtils.DataSource`); there is no CSV support yet.
