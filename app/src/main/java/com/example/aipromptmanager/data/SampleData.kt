package com.example.aipromptmanager.data

/**
 * 示例提示词数据
 * 用户首次使用时可以导入这些预设的提示词
 */
object SampleData {
    
    val samplePrompts = listOf(
        Prompt(
            title = "写作助手",
            content = "请帮我优化这段文字的表达，使其更加清晰、简洁且有说服力：[在此插入需要优化的文字]",
            category = PromptCategory.WRITING.name
        ),
        Prompt(
            title = "代码解释",
            content = "请详细解释以下代码的功能和工作原理，包括关键概念和最佳实践：\n\n[在此插入代码]",
            category = PromptCategory.CODING.name
        ),
        Prompt(
            title = "英文翻译",
            content = "请将以下中文翻译成自然、地道的英文，注意语法和表达习惯：\n\n[在此插入中文内容]",
            category = PromptCategory.TRANSLATION.name
        ),
        Prompt(
            title = "数据分析师",
            content = "作为一名数据分析师，请帮我分析以下数据，提供关键洞察和建议：\n\n[在此插入数据或问题描述]",
            category = PromptCategory.ANALYSIS.name
        ),
        Prompt(
            title = "创意写作",
            content = "请基于以下主题创作一个有趣的故事，要求情节生动、人物鲜明：\n\n主题：[在此插入故事主题]",
            category = PromptCategory.CREATIVE.name
        ),
        Prompt(
            title = "Bug修复助手",
            content = "我遇到了以下代码问题，请帮我诊断并提供修复方案：\n\n错误描述：[描述错误现象]\n相关代码：[粘贴相关代码]",
            category = PromptCategory.CODING.name
        ),
        Prompt(
            title = "学术论文摘要",
            content = "请为以下研究内容撰写一个结构化的学术摘要，包括研究目的、方法、结果和结论：\n\n[在此插入研究内容]",
            category = PromptCategory.WRITING.name
        ),
        Prompt(
            title = "产品需求分析",
            content = "作为产品经理，请帮我分析以下产品需求，包括可行性、优先级和实现建议：\n\n需求描述：[在此插入产品需求]",
            category = PromptCategory.ANALYSIS.name
        ),
        Prompt(
            title = "中文润色",
            content = "请帮我润色以下中文文本，使其更加流畅、准确且符合中文表达习惯：\n\n[在此插入需要润色的中文]",
            category = PromptCategory.WRITING.name
        ),
        Prompt(
            title = "算法优化",
            content = "请分析以下算法的时间复杂度和空间复杂度，并提供优化建议：\n\n[在此插入算法代码]",
            category = PromptCategory.CODING.name
        ),
        Prompt(
            title = "市场调研报告",
            content = "请基于以下信息撰写一份市场调研报告，包括市场现状、竞争分析和发展趋势：\n\n[在此插入市场信息]",
            category = PromptCategory.ANALYSIS.name
        ),
        Prompt(
            title = "创意广告文案",
            content = "请为以下产品创作一系列创意广告文案，要求吸引眼球、传达价值：\n\n产品：[产品名称]\n特点：[产品特点]\n目标用户：[目标受众]",
            category = PromptCategory.CREATIVE.name
        ),
        Prompt(
            title = "技术文档写作",
            content = "请帮我撰写以下功能的技术文档，包括功能描述、使用方法和注意事项：\n\n[在此插入功能说明]",
            category = PromptCategory.WRITING.name
        ),
        Prompt(
            title = "代码重构建议",
            content = "请审查以下代码并提供重构建议，重点关注可读性、性能和可维护性：\n\n[在此插入需要重构的代码]",
            category = PromptCategory.CODING.name
        ),
        Prompt(
            title = "头脑风暴",
            content = "让我们针对以下问题进行头脑风暴，请提供多个创新性的解决方案：\n\n问题：[在此插入需要解决的问题]",
            category = PromptCategory.CREATIVE.name
        )
    )
    
    /**
     * 获取按分类分组的示例提示词
     */
    fun getPromptsByCategory(): Map<String, List<Prompt>> {
        return samplePrompts.groupBy { it.category }
    }
    
    /**
     * 获取指定分类的示例提示词
     */
    fun getPromptsForCategory(category: String): List<Prompt> {
        return samplePrompts.filter { it.category == category }
    }
} 