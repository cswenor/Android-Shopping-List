CWAC TouchListView: Dragging and Dropping for Fun and Profit!
=========================================================
The standard `ListView` is a very sophisticated tool, but it
lacks any sort of drag-and-drop support. The Android open
source project has an example of implementing drag-and-drop
on a `ListView`, in the form of the `TouchInterceptor` class
used in the Music application &mdash; you use this to re-arrange
a playlist. However, that widget is not in the Android SDK
as of Android 2.2.

`TouchListView` is 95% the Android open source code for
`TouchInterceptor`. `TouchListView` also allows the widget
to be configured from an XML layout file, replacing some
hard-wired values that `TouchInterceptor` uses.

This is distributed as an Android library project, following
the conventions of [the Android Parcel Project](http://andparcel.com).
You can download a ZIP file containing just the library project
(sans sample code) from the Downloads section of this GitHub
repository.

Usage
-----
You will see a sample project that uses `TouchListView` in the
`demo/` directory of the repository. This project is designed
to be built via Ant from the command line. Compile the top-level
project first via `ant parcel`, then compile and install the
demo project via `ant demo`.

To work with `TouchListView` in your own project, place the
library project somewhere and update your project to reference
the library project (via Eclipse, via `android update lib-project`, etc.).
Then, add a `com.commonsware.cwac.tlv.TouchListView`
widget to your XML layout file. You have five customizable
attributes:

 * `normal_height`: the height of one of your regular rows (required)
 * `expanded_height`: the largest possible height of one of
 your rows (defaults to the value of `normal_height`)
 * `grabber`: the `android:id` value of an icon in your rows
 that should be used as the "grab handle" for the drag-and-drop
 operation (required)
 * `dragndrop_background`: a color to use as the background of your
 row when it is being dragged (defaults to being fully transparent)
 * `remove_mode`: can be `none` (user cannot remove entries), `slideRight`
 (user can remove entries by dragging to the right quarter of the list),
 `slideLeft`
 (user can remove entries by dragging to the left quarter of the list),
 or fling (...not quite sure what this does) (defaults to `none`)
 
**NOTE**: `remove_mode` of `slide` is equivalent to `slideRight`, but
`slideRight` is recommended.
 
For example, here is the layout from the `demo/` project:

	<?xml version="1.0" encoding="utf-8"?>
	<com.commonsware.cwac.tlv.TouchListView
		xmlns:android="http://schemas.android.com/apk/res/android"
		xmlns:tlv="http://schemas.android.com/apk/res/com.commonsware.cwac.tlv.demo"
	
		android:id="@android:id/list"
		android:layout_width="fill_parent"
		android:layout_height="fill_parent"
		android:drawSelectorOnTop="false"
		tlv:normal_height="64dip"
		tlv:grabber="@+id/icon"
		tlv:remove_mode="slideRight"
	/>

You will need to change the `com.commonsware.cwac.tlv.demo` to
your own project's package in the `tlv` namespace declaration.

In code, you set up a `TouchListView` just like a regular
`ListView`, except that you need to register a `TouchListView.DropListener`
via `setDropListener()`. In your listener, you will need to do
something to affect the re-ordering requested via the drag-and-drop
operation. In the demo project, this is a matter of removing
the entry from the old position and putting it in the new position.

If you have remove_mode enabled, you will also need to register a
`TouchListView.RemoveListener` via `setRemoveListener()`. This
will be notified when the user removes an entry from the list.
Once again, you need to make this change permanent in your
data model. In the demo project, this removes the row from the
actual `ArrayAdapter` supporting the list.

There is also a `TouchListView.DragListener` that you can register
via `setDragListener()`, if you want to know when the user
has initiated a drag operation.

**NOTE #2**: You cannot use `addHeaderView()` with `TouchListView` due
to some compatibility issues.

**NOTE #3**: You can use `addFooterView()` with
`TouchListView`, but the footer rows cannot contain a widget with
the `android:id` value you specified for the `grabber` attribute (i.e.,
they have to be distinguishable from regular draggable rows).
Also, you cannot use both `remove_mode` and `addFooterView()`,
due to some outstanding issues with how things are removed.

Dependencies
------------
This depends upon the `CWAC-Parcel` JAR for accessing
project-level resources. That can be obtained from its
[GitHub repository](http://github.com/commonsguy/cwac-parcel),
though a compatible edition of the JAR
is included in this GitHub repo for convenience.

Version
-------
This is version v0.4 of this module, meaning it is slowly gaining
acceptance, though it still has warts due to its under-documented
original code base.

Demo
----
There is a `demo/` directory containing a demo project. It uses
the library project itself to access the source code and
resources of the `TouchListView` library.

License
-------
The code in this project is licensed under the Apache
Software License 2.0, per the terms of the included LICENSE
file.

Questions
---------
If you have questions regarding the use of this code, please post a question
on [StackOverflow](http://stackoverflow.com/questions/ask) tagged with `commonsware` and `android`. Be sure to indicate
what CWAC module you are having issues with, and be sure to include source code 
and stack traces if you are encountering crashes.

Release Notes
-------------
- v0.4.0: added code to block unsupported header/footer operations
- v0.3.1: fixed to better support non-full-screen operation
- v0.3.0: added support for footer views, explicitly blocked support for header views
- v0.2.0: converted to Android library project

Bear in mind that the person who converted `TouchInterceptor`
into `TouchListView` does not fully understand the original
`TouchInterceptor` code, and so support may be limited.

[gg]: http://groups.google.com/group/cw-android
