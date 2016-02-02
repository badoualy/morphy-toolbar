[![Release](https://jitpack.io/v/badoualy/morphy-toolbar.svg)](https://jitpack.io/#badoualy/morphy-toolbar)

<img src="https://github.com/badoualy/morphy-toolbar/blob/master/ART/gif1.gif" width="300">
<img src="https://github.com/badoualy/morphy-toolbar/blob/master/ART/gif2.gif" width="300">
<img src="https://github.com/badoualy/morphy-toolbar/blob/master/ART/gif4.gif" width="300">
<img src="https://github.com/badoualy/morphy-toolbar/blob/master/ART/gif3.gif" width="300">
<img src="https://github.com/badoualy/morphy-toolbar/blob/master/ART/gif5.gif" width="300">

### Ever wanted a beautiful transition when switching fragments? 
Morphy Toolbar is your answer :)

## Overview

> Why did I create this library? I often read questions on stack overflow on how to reproduce an animation like this one, more or less always resulting in the use of the design support library **CollapsibleToolbar**. However, I find this library to be quite buggy, and it aims to be used for scrolling events.<br/>
> I needed this library to have a custom toolbar with a picture, title and subtitle, and the possibility to animate it when **switching fragments**.

- **Easiest integration ever**
- No need to handle transitions or whatever youself
- Perfect to animate a small changes **statically** without scrolling events
- Several customisations already possible, more to come
- Smooth animation for transitions (for example when switching fragments)

Sample
----------------

You can checkout the [Sample Application](https://play.google.com/store/apps/details?id=com.github.badoualy.morphytoolbar.sample) on the Play Store

Setup
----------------

First, add jitpack in your build.gradle at the end of repositories:
 ```gradle
repositories {
    // ...       
    maven { url "https://jitpack.io" }
}
```

Then, add the library dependency:
```gradle
compile 'com.github.badoualy:morphy-toolbar:1.0.0'
```

Now go do some awesome stuff!

Usage
----------------
```
// Attach to the given activity/toolbar
MorphyToolbar morphyToolbar = MorphyToolbar.builder(this, toolbar)
											.withToolbarAsSupportActionBar()
   	                                		.withTitle("Minions [not so] serious talk")
                                     		.withSubtitle("160 participants")
                                     		.withPicture(R.drawable.img_profile)
                                     		.withHidePictureWhenCollapsed(false)
                                     		.build();
                                            
morphyToolbar.expand();
morphyToolbar.collapse();
```

All the customizations are made using the builder methods, you can chose to hide the picture when in collpased mode, to not have any subtitle, ...

I'll gladly accept any merge request to enhance this library

Licence
----------------
The MIT License (MIT)

Copyright (c) 2015 Yannick Badoual

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
