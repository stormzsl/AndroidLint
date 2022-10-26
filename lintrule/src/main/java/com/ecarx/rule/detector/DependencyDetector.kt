package com.ecarx.rule.detector

import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Severity.*
import com.ecarx.rule.config.bean.DependencyModule
import java.util.*

class DependencyDetector:Detector(),GradleScanner {

    private lateinit var module:DependencyModule
    private lateinit var buffer:StringBuffer
    companion object{
        private const val MESSAGE = "显示工程各module依赖关系图"
        var ISSUE = Issue.create(
            "DependencyDetector",
            MESSAGE,
            MESSAGE,
            Category.MESSAGES,
            5,
            WARNING,
            Implementation(DependencyDetector::class.java, EnumSet.of(Scope.GRADLE_FILE))
        )
    }



    override fun beforeCheckRootProject(context: Context) {
        super.beforeCheckRootProject(context)
        module = generateModule(context.mainProject)
        buffer = StringBuffer()
        printModuleMessage(module)
        println("*************>>>>>>")
        println(buffer.toString())
        println("*************<<<<<<>>>>>>>>>>>>")

    }

    /*
     * 然后在gradlew lint 将输出的日志 copy出来:如 ：
     * app-->module2
     * module2-->module5
     * app-->module3
     * app-->check
     * check-->module4
     * check-->module5
     * 将此字符串复制这个字符串生成图形网站上
     * (https://mermaid-js.github.io/mermaid-live-editor/edit#eyJjb2RlIjoiZ3JhcGggVERcbiAgIGFwcC0tPmNoZWNrXG4gICAgY2hlY2stLT5tb2R1bGU0XG4gICAgY2hlY2stLT5tb2R1bGU1XG4gICAgYXBwLS0-bW9kdWxlMlxuICAgIG1vZHVsZTItLT5tb2R1bGU1XG4gICAgYXBwLS0-bW9kdWxlM1xuXG4gICIsIm1lcm1haWQiOiJ7XG4gIFwidGhlbWVcIjogXCJkZWZhdWx0XCJcbn0iLCJ1cGRhdGVFZGl0b3IiOmZhbHNlLCJhdXRvU3luYyI6dHJ1ZSwidXBkYXRlRGlhZ3JhbSI6ZmFsc2V9)。
     * 能快速展示出各个module之间的依赖关系。帮助新人快速理解项目
     */
    private fun printModuleMessage(module: DependencyModule) {
        for (i in 0 until module.dependencySubModules.size) {
            val subModule = module.dependencySubModules[i]
            buffer.append("${module.moduleName}-->${subModule.moduleName}")
            printModuleMessage(subModule)
        }
    }

    private fun generateModule(project: Project):DependencyModule{
        val projects = project.directLibraries
        val modules = mutableListOf<DependencyModule>()
        val moduleNames = mutableListOf<String>()

        val module = DependencyModule(project.name,modules)

        if(projects == null || projects.isEmpty()){
            return module
        }

        projects.filter {
            it.isGradleProject && !moduleNames.contains(it.name)
        }.forEach {
            modules.add(generateModule(it))
            moduleNames.add(it.name)
        }
        return module
    }
}