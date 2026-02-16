# Error Handling in Echo

This document describes how Echo handles common errors gracefully.

## Data File Errors

### Missing Data File
- **Scenario**: Data file (`./data/echo.txt`) not found on startup
- **Handling**: Storage.load() returns an empty ArrayList (line 69-70)
- **User Impact**: Application starts with empty task list, no error shown
- **Code Location**: `Storage.java` lines 68-71

### Missing Data Directory
- **Scenario**: Parent directory (`./data/`) doesn't exist
- **Handling**: Storage automatically creates the directory using `mkdirs()`
- **User Impact**: Transparent - directory created automatically
- **Code Location**: `Storage.java` lines 62-66

### Corrupted Data File
- **Scenario**: Data file contains malformed lines
- **Handling**: Corrupted lines are skipped with a warning message
- **User Impact**: Valid tasks load successfully, corrupted entries ignored
- **Code Location**: `Storage.java` lines 76-84
- **Example Warning**: `"Warning: Skipped corrupted line: ..."`

### File Write Errors
- **Scenario**: Unable to save data file (permissions, disk full, etc.)
- **Handling**: IOException caught and displayed to user
- **User Impact**: User sees error message but application continues running
- **Code Location**: `Echo.java` lines 72-73
- **Error Message**: `"Error saving to file: [exception message]"`

## User Input Errors

### Invalid Command
- **Scenario**: User enters unrecognized command (e.g., "xyz")
- **Handling**: EchoException thrown with helpful message
- **Error Message**: `"I don't understand 'xyz'. Try: todo, deadline, event, list, mark, or unmark."`
- **Code Location**: `Echo.java` lines 110-113

### Missing Task Description
- **Scenario**: User enters `todo` without description
- **Handling**: EchoException with specific guidance
- **Error Message**: `"Hmm, you forgot to tell me what the todo is! Try: todo <description>"`
- **Code Location**: `Echo.java` line 192

### Missing Deadline Date
- **Scenario**: User enters `deadline task` without `/by` keyword
- **Handling**: InputValidator.requireDelimiter() throws EchoException
- **Error Message**: `"Deadlines need a date! Use: deadline <task> /by <date>"`
- **Code Location**: `Parser.java` lines 76-77

### Missing Event Times
- **Scenario**: User enters event without `/from` or `/to`
- **Handling**: Separate error messages for each missing delimiter
- **Error Messages**:
  - `"Events need a start time! Use: event <task> /from <time> /to <time>"`
  - `"Events need an end time! Use: event <task> /from <time> /to <time>"`
- **Code Location**: `Parser.java` lines 96-101

### Invalid Task Number
- **Scenario**: User enters `mark 0`, `mark -1`, or `mark 999` (out of range)
- **Handling**: InputValidator.requireValidTaskIndex() throws EchoException
- **Error Messages**:
  - For non-positive: `"Task number must be a positive number."`
  - For out of range: `"Task number doesn't exist. Please provide a number between 1 and [max]."`
- **Code Location**: `InputValidator.java` lines 37-45

### Non-Numeric Task Number
- **Scenario**: User enters `mark abc` or `mark 3.14`
- **Handling**: InputValidator.requirePositiveInteger() catches NumberFormatException
- **Error Message**: `"Task number must be a valid number."`
- **Code Location**: `InputValidator.java` lines 60-67

### Missing Task Number
- **Scenario**: User enters just `mark`, `unmark`, or `delete` without number
- **Handling**: EchoException with specific message per command
- **Error Messages**:
  - Mark: `"Which task should I mark? Use: mark <task number>"`
  - Unmark: `"Which task should I unmark? Use: unmark <task number>"`
  - Delete: `"Please specify which task to delete."`
- **Code Location**: `Echo.java` lines 142-143, 166-167, 259-260

### Missing Search Keyword
- **Scenario**: User enters `find` without keyword
- **Handling**: EchoException with usage guidance
- **Error Message**: `"Please provide a keyword to search for. Use: find <keyword>"`
- **Code Location**: `Echo.java` lines 293-295

### Invalid Recurring Event Format
- **Scenario**: User tries to create recurring event without datetime format
- **Handling**: Validation checks start/end are proper datetime format
- **Error Message**: `"Recurring events require datetime format: yyyy-MM-dd HHmm\nExample: event meeting /from 2024-12-16 1400 /to 1500 /repeat weekly"`
- **Code Location**: `Echo.java` lines 234-237

## Unexpected Errors

### General Exception Handling
- **Scenario**: Any unexpected exception during command processing
- **Handling**: Generic Exception catch block in getResponse()
- **User Impact**: Graceful error message, application continues running
- **Error Message**: `"Uh oh! Something unexpected happened: [exception message]"`
- **Code Location**: `Echo.java` lines 74-76

## Error Handling Principles

1. **Fail Gracefully**: No crashes - all errors caught and handled
2. **Informative Messages**: Clear guidance on what went wrong and how to fix it
3. **Continue Operation**: Application remains usable after errors
4. **User-Friendly**: Non-technical language in error messages
5. **Specific Guidance**: Each error provides usage examples
6. **Data Preservation**: Corrupted data skipped, valid data preserved
7. **Silent Fallbacks**: Missing files/directories created automatically

## Testing Error Scenarios

All error scenarios are covered by unit tests:
- `InputValidatorTest.java` - 20 tests for validation edge cases
- `ParserTest.java` - Tests for malformed input parsing
- `StorageTest.java` - Tests for corrupted and missing files
- `TaskListTest.java` - Tests for invalid indices

Total test coverage: **133 tests, all passing** ✓
