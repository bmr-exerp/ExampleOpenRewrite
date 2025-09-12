package com.example.openrewrite;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

public class MigrateObjectsToStringHelperToMoreObjectsTest implements RewriteTest {

	@Override
	public void defaults(RecipeSpec spec) {
		spec.recipe(new MigrateObjectsToStringHelperToMoreObjects())
				.parser(JavaParser.fromJavaVersion().classpath("guava"));
	}

	@Test
	void leavesOtherObjectsMethodsAlone() {
		rewriteRun(
				java("package com.foo.time;\n" +
								"\n" +
								"import com.google.common.base.Objects;\n" +
								"\n" +
								"public class TimeSetting {\n" +
								"    private final int value;\n" +
								"    private final String timeUnit;\n" +
								"    private final String rounding;\n" +
								"    private final boolean canCallerOverride;\n" +
								"\n" +
								"    public TimeSetting(int value, String timeUnit, String rounding, boolean canCallerOverride) {\n" +
								"        this.value = value;\n" +
								"        this.timeUnit = timeUnit;\n" +
								"        this.rounding = rounding;\n" +
								"        this.canCallerOverride = canCallerOverride;\n" +
								"    }\n" +
								"\n" +
								"    public int getValue() {\n" +
								"        return value;\n" +
								"    }\n" +
								"\n" +
								"    public String getTimeUnit() {\n" +
								"        return timeUnit;\n" +
								"    }\n" +
								"\n" +
								"    public String getRounding() {\n" +
								"        return rounding;\n" +
								"    }\n" +
								"\n" +
								"    public boolean canCallerOverride() {\n" +
								"        return canCallerOverride;\n" +
								"    }\n" +
								"\n" +
								"    @Override\n" +
								"    public String toString() {\n" +
								"        return Objects.toStringHelper(this)\n" +
								"                .add(\"value\", value)\n" +
								"                .add(\"timeUnit\", timeUnit)\n" +
								"                .add(\"rounding\", rounding)\n" +
								"                .add(\"canCallerOverride\", canCallerOverride)\n" +
								"                .toString();\n" +
								"    }\n" +
								"\n" +
								"    @Override\n" +
								"    public boolean equals(Object o) {\n" +
								"        if (this == o) return true;\n" +
								"        if (o == null || getClass() != o.getClass()) return false;\n" +
								"        TimeSetting that = (TimeSetting) o;\n" +
								"        return value == that.value &&\n" +
								"                canCallerOverride == that.canCallerOverride &&\n" +
								"                timeUnit == that.timeUnit &&\n" +
								"                Objects.equal(rounding, that.rounding);\n" +
								"    }\n" +
								"\n" +
								"    @Override\n" +
								"    public int hashCode() {\n" +
								"        return Objects.hashCode(value, timeUnit, rounding, canCallerOverride);\n" +
								"    }\n" +
								"}\n",
						"package com.foo.time;\n" +
								"\n" +
								"import com.google.common.base.MoreObjects;\n" +
								"import com.google.common.base.Objects;\n" +
								"\n" +
								"public class TimeSetting {\n" +
								"    private final int value;\n" +
								"    private final String timeUnit;\n" +
								"    private final String rounding;\n" +
								"    private final boolean canCallerOverride;\n" +
								"\n" +
								"    public TimeSetting(int value, String timeUnit, String rounding, boolean canCallerOverride) {\n" +
								"        this.value = value;\n" +
								"        this.timeUnit = timeUnit;\n" +
								"        this.rounding = rounding;\n" +
								"        this.canCallerOverride = canCallerOverride;\n" +
								"    }\n" +
								"\n" +
								"    public int getValue() {\n" +
								"        return value;\n" +
								"    }\n" +
								"\n" +
								"    public String getTimeUnit() {\n" +
								"        return timeUnit;\n" +
								"    }\n" +
								"\n" +
								"    public String getRounding() {\n" +
								"        return rounding;\n" +
								"    }\n" +
								"\n" +
								"    public boolean canCallerOverride() {\n" +
								"        return canCallerOverride;\n" +
								"    }\n" +
								"\n" +
								"    @Override\n" +
								"    public String toString() {\n" +
								"        return MoreObjects.toStringHelper(this)\n" +
								"                .add(\"value\", value)\n" +
								"                .add(\"timeUnit\", timeUnit)\n" +
								"                .add(\"rounding\", rounding)\n" +
								"                .add(\"canCallerOverride\", canCallerOverride)\n" +
								"                .toString();\n" +
								"    }\n" +
								"\n" +
								"    @Override\n" +
								"    public boolean equals(Object o) {\n" +
								"        if (this == o) return true;\n" +
								"        if (o == null || getClass() != o.getClass()) return false;\n" +
								"        TimeSetting that = (TimeSetting) o;\n" +
								"        return value == that.value &&\n" +
								"                canCallerOverride == that.canCallerOverride &&\n" +
								"                timeUnit == that.timeUnit &&\n" +
								"                Objects.equal(rounding, that.rounding);\n" +
								"    }\n" +
								"\n" +
								"    @Override\n" +
								"    public int hashCode() {\n" +
								"        return Objects.hashCode(value, timeUnit, rounding, canCallerOverride);\n" +
								"    }\n" +
								"}\n")
		);
	}

	@Test
	void convertsObjectsToStringHelperToMoreObjects() {
		rewriteRun(
				java(
						"import com.google.common.base.Objects;\n" +
								"\n" +
								"class Test {\n" +
								"    public String toString() {\n" +
								"        return Objects.toStringHelper(this).add(\"value\", 42).toString();\n" +
								"    }\n" +
								"}\n",
						"import com.google.common.base.MoreObjects;\n" +
								"\n" +
								"class Test {\n" +
								"    public String toString() {\n" +
								"        return MoreObjects.toStringHelper(this).add(\"value\", 42).toString();\n" +
								"    }\n" +
								"}\n"
				)
		);
	}

	/**
	 * Tests that a static anonymous class ({@code funcConvertToTransactionUnsettledAmount}) using
	 * {@code Objects.toStringHelper} is converted to {@code MoreObjects.toStringHelper}.
	 */
	@Test
	void convertsObjectsToStringHelperToMoreObjectsForOpenAmountCollectionPolicy() {
		rewriteRun(java("package foo;\n" +
						"\n" +
						"\n" +
						"import com.google.common.base.Function;\n" +
						"import com.google.common.base.Objects;\n" +
						"\n" +
						"public class Example {\n" +
						"\n" +
						"    private static final Function<Example.Transaction, Example.UnsettledAmount> funcConvertToTransactionUnsettledAmount = new Function<>() {\n" +
						"        public Example.UnsettledAmount apply(final Example.Transaction transaction) {\n" +
						"            return new Example.UnsettledAmount() {\n" +
						"                public String getUnsettledAmount() {\n" +
						"                    return transaction.getUnsettledAmount();\n" +
						"                }\n" +
						"\n" +
						"                public String toString() {\n" +
						"                    return Objects.toStringHelper(this).add(\"key\", transaction.getId())\n" +
						"                            .add(\"unsettledAmount\", transaction.getUnsettledAmount()).toString();\n" +
						"                }\n" +
						"            };\n" +
						"        }\n" +
						"    };\n" +
						"    \n" +
						"    \n" +
						"\n" +
						"    interface Transaction {\n" +
						"        String getId();\n" +
						"\n" +
						"        String getUnsettledAmount();\n" +
						"    }\n" +
						"\n" +
						"    interface UnsettledAmount {\n" +
						"        String getUnsettledAmount();\n" +
						"\n" +
						"        String toString();\n" +
						"    }\n" +
						"}\n",
				"package foo;\n" +
						"\n" +
						"\n" +
						"import com.google.common.base.Function;\n" +
						"import com.google.common.base.MoreObjects;\n" +
						"\n" +
						"public class Example {\n" +
						"\n" +
						"    private static final Function<Example.Transaction, Example.UnsettledAmount> funcConvertToTransactionUnsettledAmount = new Function<>() {\n" +
						"        public Example.UnsettledAmount apply(final Example.Transaction transaction) {\n" +
						"            return new Example.UnsettledAmount() {\n" +
						"                public String getUnsettledAmount() {\n" +
						"                    return transaction.getUnsettledAmount();\n" +
						"                }\n" +
						"\n" +
						"                public String toString() {\n" +
						"                    return MoreObjects.toStringHelper(this).add(\"key\", transaction.getId())\n" +
						"                            .add(\"unsettledAmount\", transaction.getUnsettledAmount()).toString();\n" +
						"                }\n" +
						"            };\n" +
						"        }\n" +
						"    };\n" +
						"    \n" +
						"    \n" +
						"\n" +
						"    interface Transaction {\n" +
						"        String getId();\n" +
						"\n" +
						"        String getUnsettledAmount();\n" +
						"    }\n" +
						"\n" +
						"    interface UnsettledAmount {\n" +
						"        String getUnsettledAmount();\n" +
						"\n" +
						"        String toString();\n" +
						"    }\n" +
						"}\n"));
	}

	@Test
	void doesNotChangeOtherMethods() {
		rewriteRun(
				java(
						"import com.google.common.base.Objects;\n" +
								"\n" +
								"class Test {\n" +
								"    public int hashCode() {\n" +
								"        return Objects.hashCode(\"test\");\n" +
								"    }\n" +
								"}\n"
				)
		);
	}
}