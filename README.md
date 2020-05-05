# Simple-File-Selector-Picker-for-Android


Android - Java Class for creating Dialouge for picking a File from all kind of storage devices connected to Android phone.

Use  :
1. add Two drawables one for folder icon and another for file icon.
2. Constructors :

FileSelector(Context context,int FileIconDrawable,int FolderIconDrawble)

FileSelector(Context context,int style,int FileIconDrawable,int FolderIconDrawble)

3. set required parameters

4. pass OnFileSelectedListener to setOnFileSelectedListener() method.

Note: for getting file with your custom extension(eg. ".mp4" or ".svg") :

1. call        setRequiredFileType(FileType.FILE_CUSTOM)
2. now call    setCustomFileExtension(".svg")

File Types Available :
FILE_TEXT, FILE_IMAGE, FILE_VIDEO, FILE_AUDIO, FILE_DOCUMENT, FILE_ARCHIVE, FILE_APK, FILE_CUSTOM, FILE_ALL
