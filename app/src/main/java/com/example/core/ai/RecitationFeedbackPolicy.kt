package com.example.core.ai

enum class RecitationIssueType {
    OMISSION,
    SUBSTITUTION,
    EXTRA_WORD,
    TAJWEED_RULE,
    UNCLEAR
}

data class RecitationIssue(
    val wordIndex: Int,
    val wordArabic: String,
    val type: RecitationIssueType,
    val description: String,
    val confidence: Float, // 0.0 to 1.0
    val suggestedAudioRef: String? = null
)

object RecitationFeedbackPolicy {
    // Only report issues when confidence is >= 0.70 to avoid frustrating false alarms
    const val CONFIDENCE_THRESHOLD = 0.70f

    fun filterIssues(candidateIssues: List<RecitationIssue>): List<RecitationIssue> {
        return candidateIssues.filter { it.confidence >= CONFIDENCE_THRESHOLD }
    }

    fun getEncouragingMessage(issues: List<RecitationIssue>): String {
        return if (issues.isEmpty()) {
            "Masha'Allah! Your recitation aligns cleanly with the verified ayah text."
        } else {
            "Good effort! We spotted ${issues.size} potential area(s) to refine. Tap 'Hear Reference' to listen and practice."
        }
    }
}
