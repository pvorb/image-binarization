image-binarization
==================

This is a small Scala program that implements different _adaptive image
binarization_ algorithms. Currently, it only supports Sauvola's method.

![Screenshot](https://raw.github.com/pvorb/image-binarization/master/screenshot.png)


Download
--------

You can download the
[runnable JAR file](https://repo.vorb.de/downloads/image-binarization.jar) or
install the application
[via Java Web Start](https://repo.vorb.de/downloads/image-binarization.jnlp).


Requirements
------------

  * Java Runtime Environment (JRE) Version 6 or newer


Usage
-----

 1. You need to specify an image document that you want to binarize

    ![Example input](https://raw.github.com/pvorb/image-binarization/master/src/test/resources/color.png)

 2. You can preview the output image, by clicking the preview button
 3. Adjust the coefficient (any decimal number between 0.2 and 0.5)
 4. Hit “Save ...” and choose a destination for the resulting PNG image.

Here's the result of our example

![Example output](https://raw.github.com/pvorb/image-binarization/master/src/test/resources/sauvola.png)


License
-------

Copyright © 2013 Paul Vorbach

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the “Software”), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
