package com.rocketzly.checks.detector;

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

import static com.rocketzly.checks.ConstantUtilsKt.LOG_CLASS_FULL_NAME;

/*
 * SourceCodeScanner：指定扫描文件类型，提供对应的方法，还有Detector.GradleScanner,ClassScanner 等等
 */
public class LogDetector extends Detector implements SourceCodeScanner {

    public static final String TAG = LogDetector.class.getSimpleName();

    public static final String MESSAGE = "不要直接使用Log,要使用LogUtils替代";

    /*
     *自定义提示规则，问题级别
     */
    public static final Issue ISSUE = Issue.create(
            "LogCheck",
            MESSAGE,
            MESSAGE,
            Category.CORRECTNESS,
            10,
            Severity.ERROR,
            new Implementation(LogDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    @Nullable
    @Override
    //根据方法名称去检查方法
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("v","d","i","w","e");
    }

    /*
     *方法访问
     *
     */
    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {
        boolean isMemberInClass = context.getEvaluator().isMemberInClass(method,LOG_CLASS_FULL_NAME);
        boolean isMemberInSubClass = context.getEvaluator().isMemberInSubClassOf(method,LOG_CLASS_FULL_NAME,true);
        System.out.println(">>>>>>>");//可以通过gradlew lint 看到打印信息
        System.out.println(TAG +"::" +method.getName());
        System.out.println("<<<<<<<");
        if(isMemberInClass || isMemberInSubClass){
            System.out.println("******");
            context.report(ISSUE,context.getLocation(node),MESSAGE);//report 上报提示信息给开发者
        }
    }
}
