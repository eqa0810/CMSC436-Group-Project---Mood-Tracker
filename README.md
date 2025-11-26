# CMSC436-Group-Project---Mood-Tracker

## Log Mood screen

**XML:** `fragment_log_mood.xml`  
This screen is where the user logs today’s mood with a SeekBar and writes a journal note, and it includes the mic button for voice input and an ad placeholder.

### Already satisfies:

- Uses at least one new GUI component (SeekBar).
- Counts as one of “at least 3 views.”
- Includes an ad placeholder  
  -- Saving mood entries into the Model, send it to Firebase for remote persistent data.
- Includes meaningful phone functionality through an email-sharing button.
  
### Still needs:

- Actual voice recognition(speech->text) so it fulfill “meaningful use of another app.”
- A SeekBar listener that updates the mood label.

---

## Calendar overview screen

**XML:** `fragment_calendar.xml`  
This screen shows the CalendarView and a color legend, and it displays the selected day’s mood and note.

### Already satisfies:

- Uses a second new GUI component (CalendarView).
- Counts as another required view.

### Still needs:

- Showing the correct score/color for each day.
- Passing the same Model data as the Log Mood to meet “views use the same data.”

---

## Settings screen

**XML:** `fragment_settings.xml`  
This screen lets the user set nickname, toggle reminders, and change reminder time.

### Already satisfies:

- Provides a place for meaningful local settings.
- Counts as a third view
- Currently saves all settings (nickname, email) on the page to local storage.

### Still needs:

- Should remove the notifications settings most likely, as it currently does not do anything (though the information is saved)
- Optional: wiring reminder time to a notification system to improve functionality.
  

---

## MainActivity navigation

**XML:** `activity_main.xml` + `menu_bottom_nav.xml`  
This part holds the fragment container and bottom navigation bar.

- Structure for MVC with multiple views and clean navigation.

### Already satisfies:

### Still needs:

- Nothing major besides hooking fragment replacements and ensuring the Model is used properly.

---

## Overall, what our project still needs

- Actual Model classes for mood entries and user prefs.
- Write and read all mood data through a Model layer (MVC).
- Save at least two meaningful local variables using SharedPreferences.
- Store mood entries remotely in Firebase to count as remote persistent data.
- Add voice recognition speech to text so the app uses another phone app meaningfully.
- Add at least one listener on the new GUI components that does something meaningful, like SeekBar updating “Mood: X/10” in real time or CalendarView showing a day’s details.
