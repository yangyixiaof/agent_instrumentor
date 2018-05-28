# Usage

## 工作流 workflow

1. 测 ClassAdapter 所指定的插桩配置的效果

```
cn.yyx.research.trace.instrument.CmpInstrumenter.TestInstrumentOneClass(String sourceClassName, String targetClassFile)
```

用 ClassAdapter 所指定的插桩配置，插桩 sourceClassName 输出到文件 targetClassFile。




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


# Naming style issue

The mixture of CamelCase, camelCase and snake_case is by design.

# Formatting

We format Java sources files in this module into Google Java Format, using one of:

+ https://github.com/google/google-java-format#eclipse
+ https://github.com/google/google-java-format#intellij
+ https://github.com/google/google-java-format#from-the-command-line
