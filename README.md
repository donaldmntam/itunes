# Swiftie App

## How to Run

To run this app, either download the APK file at the root of this project or 
open this project on Android Studio and build the app onto an emulator or onto
a connected Android phone. In case there are errors opening this project, try
opening the project again with the following version of Android Studio instead:

Android Studio Iguana | 2023.2.1 Patch 2\
Build #AI-232.10300.40.2321.11668458, built on April 4, 2024\
Runtime version: 17.0.9+0-17.0.9b1087.7-11185874 aarch64\
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.\
macOS 12.5\
GC: G1 Young Generation, G1 Old Generation\
Memory: 2048M\
Cores: 8\
Metal Rendering is ON

## How to Test Different Scenarios
There are multiple scenarios you can simulate by changing the injected
'SongListProvider' inside 'MainActivity.kt' in the 'onCreate' method:

1. Normal Scenario where the Taylor Swift's song list is fetched successfully
1. Scenario where the app always fails to fetch the song list
1. Scenario where the app fails to fetch the song list for a certain number of
times
1. Scenario where the app fetches an empty song list

The above scenarios correspond to the following 'SongListProvider'
implementations:

1. 'SongListProviderImpl'
1. 'AlwaysFailSongListProvider'
1. 'RetrySongListProvider'
1. 'EmptySongListProvider'

Changing the injected 'SongListProvider' will gets you the corresponding
scenario.

## Functionalities to Test
1. Search by song name
1. Search by album name
1. Sort and sort direction
1. Copy song link by tapping on the song
1. Reload when error is encountered
