# Codename One Native Data Transfer Library

This library adds drag and drop support to simulator, desktop, and Javascript builds of Codename One application.  This should not be confused with
Codename One's already built in support for drag and drop *within* apps (e.g. dragging components around).  This library adds support for dragging
files from the native platform (e.g. from your desktop) onto the app, and having the app "import" the file.

## License

Apache 2

## Supported Platforms

* Simulator
* Desktop builds
* Javascript builds

## Installation

* Download the [cn1-native-data-transfer-lib.cn1lib](bin/cn1-native-data-transfer-lib.cn1lib) file into your project's `lib` directory.
* Select "Codename One" > "Refresh cn1lib files" in your IDE.

## Usage Example

~~~~
if (DropTarget.isSupported()) {
    DropTarget dnd = DropTarget.create((evt)->{
        String srcFile = (String)evt.getSource();
        System.out.println("Src file is "+srcFile);
        System.out.println("Location: "+evt.getX()+", "+evt.getY());
        if (srcFile != null) {
            try {
                Image img = Image.createImage(FileSystemStorage.getInstance().openInputStream(srcFile));
                hi.add(img);
                hi.revalidate();
            } catch (IOException ex) {
                Log.e(ex);
            }

        }


    }, Display.GALLERY_IMAGE);
}
~~~~

See the [DNDDemo.java](DNDDemo/src/com/codename1/demos/dnd/DNDDemo.java) file for full example.

## Building From Source

~~~~
$ git clone https://github.com/shannah/cn1-native-data-transfer 
$ cd cn1-native-data-transfer
$ ant cn1lib
~~~~

### Running the Demo 

Once you've checked out the code, and run `ant configure`, you can run the demo directly from the command line.

~~~~
$ ant run-demo
~~~~

# Contact

* [Steve Hannah](http://sjhannah.com)
* [Codename One](http://www.codenameone.com)