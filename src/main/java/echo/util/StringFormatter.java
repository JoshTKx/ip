package echo.util;

/**
 * Utility class for formatting strings consistently across the application.
 *
 * AI-Assisted: Claude Code analyzed the codebase and identified repeated string formatting
 * patterns in Echo.java (lines 197-198, 214-215, 246-247, 268-269) for task count messages.
 * Suggested extracting these into a centralized utility to ensure consistent messaging
 * and easier future modifications (e.g., for pluralization improvements).
 */
public class StringFormatter {

    /**
     * Formats a task addition response message.
     *
     * @param taskString The string representation of the added task.
     * @param totalTasks The total number of tasks in the list.
     * @return A formatted message confirming the task was added.
     */
    public static String formatTaskAdded(String taskString, int totalTasks) {
        return "Got it. I've added this task:\n  " + taskString
                + "\nNow you have " + formatTaskCount(totalTasks) + " in the list.";
    }

    /**
     * Formats a task removal response message.
     *
     * @param taskString The string representation of the removed task.
     * @param totalTasks The total number of tasks remaining in the list.
     * @return A formatted message confirming the task was removed.
     */
    public static String formatTaskRemoved(String taskString, int totalTasks) {
        return "Noted. I've removed this task:\n  " + taskString
                + "\nNow you have " + formatTaskCount(totalTasks) + " in the list.";
    }

    /**
     * Formats a task count string with proper pluralization.
     *
     * AI-Assisted: Claude identified that the current code doesn't handle singular vs. plural
     * forms ("1 tasks" vs "2 tasks"). This method provides grammatically correct pluralization.
     *
     * @param count The number of tasks.
     * @return A formatted string like "1 task" or "5 tasks".
     */
    public static String formatTaskCount(int count) {
        return count + (count == 1 ? " task" : " tasks");
    }

    /**
     * Formats a numbered list item.
     *
     * @param index The 0-based index of the item.
     * @param item The item to format.
     * @return A formatted string like "1. [task details]".
     */
    public static String formatListItem(int index, Object item) {
        return (index + 1) + ". " + item;
    }
}
