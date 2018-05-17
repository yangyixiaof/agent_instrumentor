# Usage

## Running in IntelliJ IDEA

1.
```
Main class (by default): cn.yyx.research.testgen.AppMutant
**VM options**: -Xbootclasspath/p:target/test_agent-0.0.1-SNAPSHOT.jar -javaagent:target/test_agent-0.0.1-SNAPSHOT.jar=TraceWorkingDirectory
Program arguments (by default): (empty)
Working directory (by default): (the absolute path to test_agent/)

```

Running with this configuration will execute `src/test/java/cn/yyx/research/testgen/AppMutant.java`

Prerequisite: The `TraceWorkingDirectory/`, specified in `-javaagent` options, must exist.

Notice that `TraceWorkingDirectory/` is .gitignore-d.