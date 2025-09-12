package com.example.openrewrite;

import static org.openrewrite.java.tree.J.MethodInvocation;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.MethodMatcher;

public class MigrateObjectsToStringHelperToMoreObjects extends Recipe {
	public static final String OBJECTS = "com.google.common.base.Objects";
	public static final String MORE_OBJECTS = "com.google.common.base.MoreObjects";

	private static final MethodMatcher TO_STRING_HELPER_MATCHER =
			new MethodMatcher(OBJECTS + " toStringHelper(..)");

	@Override
	@NotNull
	@SuppressWarnings("DialogTitleCapitalization")
	public String getDisplayName() {
		return "Migrate Objects.toStringHelper() to MoreObjects.toStringHelper()";
	}

	@Override
	@NotNull
	public String getDescription() {
		return "Converts deprecated " + OBJECTS + "#toStringHelper() to " + MORE_OBJECTS + "#toStringHelper().";
	}

	@Override
	@NotNull
	public TreeVisitor<?, @NotNull ExecutionContext> getVisitor() {
		return new MigrateObjectsToStringHelperVisitor();
	}

	private static class MigrateObjectsToStringHelperVisitor extends JavaIsoVisitor<@NotNull ExecutionContext> {

		private final JavaTemplate template = JavaTemplate.builder("MoreObjects.toStringHelper(#{any(java.lang.Object)})")
				.imports(MORE_OBJECTS)
				.doBeforeParseTemplate(System.out::println)
				.doAfterVariableSubstitution(System.out::println)
				.javaParser(JavaParser.fromJavaVersion()
						.classpath("guava"))
				.build();

		@Override
		@NotNull
		public MethodInvocation visitMethodInvocation(@NotNull MethodInvocation method, ExecutionContext ctx) {
			MethodInvocation mi = super.visitMethodInvocation(method, ctx);

			if (TO_STRING_HELPER_MATCHER.matches(mi)) {
				maybeAddImport(MORE_OBJECTS);
				maybeRemoveImport(OBJECTS);
				mi = template.apply(updateCursor(mi), mi.getCoordinates().replace(), mi.getArguments().toArray());
			}

			return mi;
		}
	}
}