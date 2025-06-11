package com.example.aipromptmanager.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class PromptInjectorService : AccessibilityService() {

    companion object {
        @Volatile
        var instance: PromptInjectorService? = null
            private set
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 这里可以监听特定的事件，但我们主要使用主动填充方式
    }

    override fun onInterrupt() {
        // 服务被中断时的处理
    }

    /**
     * 执行文本填充操作
     * @param textToPaste 要填充的文本
     * @return 是否成功填充
     */
    fun pasteText(textToPaste: String): Boolean {
        return try {
            val rootNode = rootInActiveWindow ?: return false
            val focusedNode = rootNode.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                ?: rootNode.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY)
                ?: return false

            // 方法1：使用ACTION_SET_TEXT
            val arguments = Bundle()
            arguments.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                textToPaste
            )
            val success = focusedNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)

            // 如果方法1失败，尝试方法2：模拟粘贴操作
            if (!success) {
                // 先清空现有文本
                focusedNode.performAction(AccessibilityNodeInfo.ACTION_SELECT_ALL)
                // 然后设置新文本
                focusedNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
            }

            focusedNode.recycle()
            rootNode.recycle()
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 查找可编辑的输入框
     */
    private fun findEditableNode(node: AccessibilityNodeInfo?): AccessibilityNodeInfo? {
        node ?: return null

        if (node.isEditable && node.isFocusable) {
            return node
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            val result = findEditableNode(child)
            if (result != null) {
                return result
            }
            child?.recycle()
        }

        return null
    }

    /**
     * 自动找到输入框并填充文本
     */
    fun autoFillText(textToPaste: String): Boolean {
        return try {
            val rootNode = rootInActiveWindow ?: return false
            val editableNode = findEditableNode(rootNode)

            if (editableNode != null) {
                // 聚焦到输入框
                editableNode.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                
                // 填充文本
                val arguments = Bundle()
                arguments.putCharSequence(
                    AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                    textToPaste
                )
                val success = editableNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
                
                editableNode.recycle()
                rootNode.recycle()
                
                success
            } else {
                rootNode.recycle()
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        instance = null
        return super.onUnbind(intent)
    }
} 