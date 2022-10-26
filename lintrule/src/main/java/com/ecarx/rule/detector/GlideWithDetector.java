package com.ecarx.rule.detector;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;

import java.util.Arrays;
import java.util.List;

/*
 * 检测Glide.with传入Application，由于Application生命周期长，所以使用的时候尽量不要传Application
 */
public class GlideWithDetector extends Detector  implements SourceCodeScanner {

    public static final String TAG = LogDetector.class.getSimpleName();

    public static final String MESSAGE = "Glide.with尽量不要传入Application";

    public static final Issue ISSUE = Issue.create(
            "glideWithDetector",
            MESSAGE,
            MESSAGE, // no need
            Category.CORRECTNESS,
            7,
            Severity.WARNING,
            new Implementation(GlideWithDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("with");
    }

    /*
     * UCallExpression 中文表达式 如: Log.d("TAG","stormzsl")
     * PsiMethod 单纯的指 Log.d（）的方法
     * 获取参数的类型: method.getParameters()[0].getType()
     * 获取参数值： String argumentValue = node.getValueArguments().get(0).asSourceString();
     */
    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {
        boolean isMemberInClass = context.getEvaluator().isMemberInClass(method, "com.bumptech.glide.Glide");
        boolean isMemberInSubClassOf = context.getEvaluator().isMemberInSubClassOf(method, "com.bumptech.glide.Glide", true);
        if (isMemberInClass || isMemberInSubClassOf) {
            System.out.println("Glide2: "+node.getValueArguments().size());
            String argumentValue = node.getValueArguments().get(0).asSourceString();
            if (argumentValue.toLowerCase().contains("application".toLowerCase())) { //检验 application
                context.report(ISSUE, node, context.getLocation(node), MESSAGE);
            }
        }
    }
}
