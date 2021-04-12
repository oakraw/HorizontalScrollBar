# HorizontalScrollBar
A standalone scrollbar for horizontal recyclerview.
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

### Installation

	implementation 'com.github.oakraw:HorizontalScrollBar:{Version}'
	
### Usage
Step 1. Add the JitPack repository to your build file

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

    <com.oakraw.library.HorizontalScrollBar
        android:id="@+id/scrollbar"
        android:layout_width="48dp"
        android:layout_height="4dp"
        android:layout_gravity="center"
        app:scrollbarThumbColor="#6868E0"
        app:scrollbarTrackColor="#D9D9D9"
        app:scrollbarRadius="2dp"/>

### Licence

    Copyright (c) 2020 oakraw
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
