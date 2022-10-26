package com.ecarx.rule.config.bean
/*
 * moduleName:当前module的名称
 * dependencySubModules:依赖的所有子module集合
 */
data class DependencyModule(var moduleName:String,var dependencySubModules:List<DependencyModule>)
