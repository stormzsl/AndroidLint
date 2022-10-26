package com.ecarx.rule

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.ecarx.rule.detector.DependencyDetector
import com.ecarx.rule.detector.GlideWithDetector
import com.ecarx.rule.detector.LogDetector

/**
 * User: Rocket
 * Date: 2020/5/27
 * Time: 3:55 PM
 */
class CustomIssueRegistry : IssueRegistry() {

    override val issues: List<Issue>
        get() = listOf(
//            SerializableClassDetector.ISSUE,
//            HandleExceptionDetector.ISSUE,
//            AvoidUsageApiDetector.ISSUE,
//            DependencyApiDetector.ISSUE,
//            ResourceNameDetector.ISSUE,
//            LogDetector.ISSUE,
            GlideWithDetector.ISSUE,
            DependencyDetector.ISSUE

        )

    override val api: Int
        get() = 2
}