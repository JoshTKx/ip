# Echo - Your Personal Task Manager

Echo is a friendly task management chatbot that helps you organize todos, deadlines, and events. Built with Java, it features a clean GUI and persistent storage to keep your tasks safe.

## Features

AI-Assisted: Claude Code analyzed user workflows to suggest these feature highlights for better documentation.

- **Multiple Task Types**: Support for todos, deadlines, and events
- **Recurring Events**: Set up daily, weekly, or monthly recurring events
- **Smart Date Parsing**: Flexible date/time formats (ISO dates, natural text)
- **Task Search**: Quickly find tasks by keyword
- **Persistent Storage**: Automatically saves your tasks
- **Input Validation**: Helpful error messages guide you to correct format

## Setting up in Intellij

Prerequisites: JDK 17, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 17** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Launcher.java` file, right-click it, and choose `Run Launcher.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see the Echo GUI window launch.
   ```
   Hello from
    ____        _        
   |  _ \ _   _| | _____ 
   | | | | | | | |/ / _ \
   | |_| | |_| |   <  __/
   |____/ \__,_|_|\_\___|
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Quick Start Guide

AI-Assisted: Claude Code suggested adding a quick reference guide based on the most common user commands and error patterns observed in the codebase.

### Basic Commands

1. **Add a Todo**
   ```
   todo read book
   ```

2. **Add a Deadline**
   ```
   deadline submit assignment /by 2024-03-15
   deadline submit report /by 2024-03-15 1400
   ```

3. **Add an Event**
   ```
   event team meeting /from 2024-03-15 1400 /to 1600
   event conference /from Monday /to Friday
   ```

4. **Add a Recurring Event**
   ```
   event standup /from 2024-03-15 0900 /to 0930 /repeat daily
   event team meeting /from 2024-03-15 1400 /to 1500 /repeat weekly
   ```

5. **List All Tasks**
   ```
   list
   ```

6. **Mark Task as Done**
   ```
   mark 1
   ```

7. **Unmark Task**
   ```
   unmark 1
   ```

8. **Delete a Task**
   ```
   delete 1
   ```

9. **Find Tasks**
   ```
   find meeting
   ```

10. **Clear All Tasks**
    ```
    clear
    ```

### Date/Time Formats

- **Date only**: `yyyy-MM-dd` (e.g., `2024-03-15`)
- **Date and time**: `yyyy-MM-dd HHmm` (e.g., `2024-03-15 1400`)
- **Natural text**: Any text (e.g., `Monday afternoon`, `next week`)

Note: Recurring events require the datetime format.

## Code Quality Improvements

AI-Assisted: This section documents improvements made with Claude Code assistance.

### New Utility Classes

1. **InputValidator** (`echo.util.InputValidator`)
   - Centralizes input validation logic
   - Provides consistent error messages
   - Reduces code duplication across Parser and Echo classes

2. **StringFormatter** (`echo.util.StringFormatter`)
   - Handles task count messages with proper pluralization
   - Ensures consistent formatting throughout the app
   - Makes future UI changes easier to implement

### Comprehensive Test Coverage

Added extensive unit tests for edge cases:
- Null and empty input validation
- Boundary conditions for task indices
- Invalid number formats
- Pluralization logic
- All 133 tests passing ✓
