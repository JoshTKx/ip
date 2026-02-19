# Echo User Guide

![Echo Screenshot](Ui.png)

Echo is a **desktop task management application** that helps you organize your todos, deadlines, and events efficiently. Optimized for use via a **Command Line Interface** (CLI) while still having the benefits of a **Graphical User Interface** (GUI), Echo allows fast typists to manage tasks more quickly than traditional GUI applications.

## Quick Start

1. Ensure you have **Java 17 or above** installed on your computer.
2. Download the latest `echo.jar` from the releases page.
3. Copy the file to your desired folder.
4. Open a command terminal, navigate to the folder, and run:
   ```
   java -jar echo.jar
   ```
5. The GUI window should appear with any previously saved tasks.
6. Type commands in the text field at the bottom and press Enter or click "Send".

## Features

> **Note about command format:**
> - Words in `UPPER_CASE` are parameters to be supplied by you.
> - Items in square brackets `[]` are optional.
> - Dates can be in `yyyy-MM-dd` format or plain text.
> - Datetimes must be in `yyyy-MM-dd HHmm` format for recurring events.

---

### Adding a todo: `todo`

Adds a simple task without any date/time.

**Format:** `todo DESCRIPTION`

**Examples:**
```
todo Read lecture notes
todo Buy groceries
todo Complete CS2103T iP documentation
```

**Expected output:**
```
Got it. I've added this task:
  [T][ ] Read lecture notes
Now you have 1 task in the list.
```

---

### Adding a deadline: `deadline`

Adds a task with a due date.

**Format:** `deadline DESCRIPTION /by DATE`

**Examples:**
```
deadline Submit assignment /by 2024-12-25 2359
deadline Return library book /by Friday
deadline Pay phone bill /by 2024-12-30
```

**Expected output:**
```
Got it. I've added this task:
  [D][ ] Submit assignment (by: Dec 25 2024, 11:59PM)
Now you have 2 tasks in the list.
```

---

### Adding an event: `event`

Adds a task that occurs during a specific time period.

**Format:** `event DESCRIPTION /from START /to END`

**For recurring events:** `event DESCRIPTION /from START /to END /repeat FREQUENCY`
- `FREQUENCY` can be: `daily`, `weekly`, or `monthly`
- **Note:** Recurring events require datetime format (`yyyy-MM-dd HHmm`) for both start and end times.

**Examples:**
```
event Project meeting /from Monday 2pm /to 4pm
event Conference /from 2024-12-10 /to 2024-12-12
event Team standup /from 2024-12-23 1000 /to 2024-12-23 1030 /repeat weekly
event Morning workout /from 2024-12-20 0700 /to 2024-12-20 0800 /repeat daily
```

**Expected output:**
```
Got it. I've added this task:
  [E][ ] Team standup (from: Dec 23 2024, 10:00AM to: Dec 23 2024, 10:30AM) (repeats weekly)
   Next: Dec 30, Jan 6, Jan 13
Now you have 3 tasks in the list.
```

---

### Listing all tasks: `list`

Shows all tasks in your task list.

**Format:** `list`

**Expected output:**
```
Here are the tasks in your list:
1. [T][X] Read lecture notes
2. [D][ ] Submit assignment (by: Dec 25 2024, 11:59PM)
3. [E][ ] Team standup (from: Dec 23 2024, 10:00AM to: 10:30AM) (repeats weekly)
   Next: Dec 30, Jan 6, Jan 13
```

---

### Finding tasks: `find`

Searches for tasks containing the specified keyword.

**Format:** `find KEYWORD`

**Examples:**
```
find assignment
find meeting
find book
```

**Expected output:**
```
Here are the matching tasks in your list:
1. [D][ ] Submit assignment (by: Dec 25 2024, 11:59PM)
```

---

### Marking a task as done: `mark`

Marks the specified task as completed.

**Format:** `mark INDEX`
- `INDEX` refers to the task number shown in the list.
- The index must be a positive integer: 1, 2, 3, …

**Examples:**
```
mark 1
mark 3
```

**Expected output:**
```
Nice! I've marked this task as done:
  [T][X] Read lecture notes
```

---

### Marking a task as not done: `unmark`

Marks the specified task as not completed.

**Format:** `unmark INDEX`

**Examples:**
```
unmark 2
```

**Expected output:**
```
OK, I've marked this task as not done yet:
  [D][ ] Submit assignment (by: Dec 25 2024, 11:59PM)
```

---

### Deleting a task: `delete`

Removes the specified task from your list.

**Format:** `delete INDEX`

**Examples:**
```
delete 2
delete 5
```

**Expected output:**
```
Noted. I've removed this task:
  [D][ ] Submit assignment (by: Dec 25 2024, 11:59PM)
Now you have 2 tasks in the list.
```

---

### Clearing all tasks: `clear`

Removes all tasks from your list.

**Format:** `clear`

**Expected output:**
```
All tasks have been cleared!
```

---

### Exiting the program: `bye`

Closes the Echo application.

**Format:** `bye`

**Expected output:**
```
Bye. Hope to see you again soon!
```

---

## Saving Data

Echo automatically saves your tasks to `./data/echo.txt` after any command that changes the data. There is no need to save manually.

## Editing the Data File

Advanced users can edit the data file directly at `./data/echo.txt`.

> **⚠️ Caution:** If your changes make the file format invalid, Echo will skip the corrupted lines and load only valid tasks. Always backup your data before editing.

---

## Command Summary

| Action | Format | Examples |
|--------|--------|----------|
| **Add Todo** | `todo DESCRIPTION` | `todo Buy groceries` |
| **Add Deadline** | `deadline DESCRIPTION /by DATE` | `deadline Submit report /by 2024-12-25 2359` |
| **Add Event** | `event DESCRIPTION /from START /to END` | `event Meeting /from Mon 2pm /to 4pm` |
| **Add Recurring Event** | `event DESCRIPTION /from START /to END /repeat FREQUENCY` | `event Standup /from 2024-12-23 1000 /to 2024-12-23 1030 /repeat weekly` |
| **List** | `list` | `list` |
| **Find** | `find KEYWORD` | `find assignment` |
| **Mark** | `mark INDEX` | `mark 2` |
| **Unmark** | `unmark INDEX` | `unmark 3` |
| **Delete** | `delete INDEX` | `delete 1` |
| **Clear** | `clear` | `clear` |
| **Exit** | `bye` | `bye` |

---

## FAQ

**Q: How do I transfer my data to another computer?**  
A: Copy the `data` folder from your Echo directory to the same location on the new computer.

**Q: Can I use flexible date formats?**  
A: Yes! For non-recurring tasks, you can use formats like "Friday", "next Monday", or "2024-12-25". However, recurring events require the strict `yyyy-MM-dd HHmm` format.

**Q: What happens if I enter invalid datetime for recurring events?**  
A: Echo will show an error message with an example of the correct format.

---

## Known Issues

- Recurring events must use `yyyy-MM-dd HHmm` format for both start and end times.
- The search function is case-sensitive.

---

