package com.aj.whatsappchatreader;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Objects;


class FileSelector{
    private String customFileExtension=".";
    private FileType requiredFileType=FileType.FILE_ALL;
    private String basepath;
    private File currentDirectory;
    private Context context;
    private AlertDialog.Builder builder;
    private OnFileSelectedListener onFileSelectedListener;
    private String title;
    private int FileIconDrawable;
    private int FolderIconDrawble;
    private boolean onetimeflag=false;

    enum FileType{FILE_TEXT,FILE_IMAGE,FILE_VIDEO,FILE_AUDIO,FILE_DOCUMENT,FILE_ARCHIVE,FILE_APK,FILE_CUSTOM,FILE_ALL}

    FileSelector(Context context,int FileIconDrawable,int FolderIconDrawble)
    {
        this.context=context;
        builder=new AlertDialog.Builder(context);
        this.FileIconDrawable=FileIconDrawable;
        this.FolderIconDrawble=FolderIconDrawble;
        prepare();
    }
    FileSelector(Context context,int style,int FileIconDrawable,int FolderIconDrawble)
    {
        this.context=context;
        builder=new AlertDialog.Builder(context,style);
        this.FileIconDrawable=FileIconDrawable;
        this.FolderIconDrawble=FolderIconDrawble;
        prepare();
    }
    public void setRequiredFileType(FileType requiredFileType)
    {
        this.requiredFileType=requiredFileType;
    }
    public void setCustomFileExtension(String customFileExtension)
    {
        this.customFileExtension=customFileExtension;
    }
    public void setTitle(String title)
    {
        this.title=title;
    }
    public void setTitle(int titleId)
    {
        this.title=context.getResources().getString(titleId);
    }
    public void setCancelable(boolean cancelable)
    {
        builder.setCancelable(cancelable);
    }
    public void setCustomTitle(View customTitleView)
    {
        builder.setCustomTitle(customTitleView);
    }
    public void setIconAttribute(int attrId)
    {
        builder.setIconAttribute(attrId);
    }
    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener)
    {
        builder.setOnCancelListener(onCancelListener);
    }
    public void setOnFileSelectedListener(OnFileSelectedListener onFileSelectedListener)
    {
        this.onFileSelectedListener=onFileSelectedListener;
    }
    private void prepare()
    {
        builder.setAdapter(null,null);
        builder.setNeutralButton(null,null);
        final File f=new File("/storage/");
        String[] mainDirs = new String[f.listFiles().length - 1];
        mainDirs[0]="Phone Storage";
        for(int i = 1; i< mainDirs.length; i++)
        {
            mainDirs[i]="Ext Card "+(i-1);
        }

        builder.setTitle(R.string.str_selectfilestorage);
        builder.setIcon(FolderIconDrawble);

        builder.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,mainDirs ), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prepareSecond();
                if(which==0) {
                    currentDirectory=Environment.getExternalStorageDirectory();
                }
                else
                {
                    currentDirectory=new File("/storage/").listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return !(name.contains("self") | name.contains("emulated"));
                        }
                    })[which-1];
                }
                basepath=currentDirectory.getPath();
                showStorageDirs();
            }
        });




        builder.setPositiveButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


    }

    public void show()
    {
        builder.show();
    }

    private void prepareSecond()
    {
        if(title==null)
            builder.setTitle(R.string.str_selectfile);
        else
            builder.setTitle(title);
        builder.setIcon(FileIconDrawable);
        builder.setItems(null,null);
        builder.setNeutralButton(R.string.str_back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(basepath.equals(currentDirectory.getPath()))
                {
                    prepare();
                    show();
                }
                else
                {
                    currentDirectory=currentDirectory.getParentFile();
                    showStorageDirs();
                }
            }
        });
    }

    private void showStorageDirs()
    {
        final FileArrayAdapter fileArrayAdapter=new FileArrayAdapter(context,android.R.layout.simple_expandable_list_item_1,currentDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                final String name=pathname.getName();
                if(pathname.isHidden())
                    return false;
                else if(!pathname.isDirectory() && requiredFileType!=FileType.FILE_ALL)
                {
                    Log.d("path",pathname.getPath());
                    switch (requiredFileType)
                    {
                        case FILE_CUSTOM:
                            return name.endsWith(customFileExtension);
                        case FILE_APK:
                            return (name.endsWith(".apk")|name.endsWith(".APK"));
                        case FILE_TEXT:
                            return (name.endsWith(".txt")|name.endsWith(".TXT"));
                        case FILE_ARCHIVE:
                            return (name.endsWith(".zip")|name.endsWith(".tar")|name.endsWith(".rar")|name.endsWith(".7z")|name.endsWith(".ZIP")|name.endsWith(".TAR")|name.endsWith(".RAR")|name.endsWith(".7Z"));
                        case FILE_AUDIO:
                            return (name.endsWith(".mp3")|name.endsWith(".wav")|name.endsWith(".aac")|name.endsWith(".amr")|name.endsWith(".wma")|name.endsWith(".MP3")|name.endsWith(".WAV")|name.endsWith(".AAC")|name.endsWith(".AMR")|name.endsWith(".WMA"));
                        case FILE_IMAGE:
                            return (name.endsWith(".jpg")|name.endsWith(".png")|name.endsWith(".bmp")|name.endsWith(".tif")|name.endsWith(".gif")|name.endsWith(".JPG")|name.endsWith(".PNG")|name.endsWith(".BMP")|name.endsWith(".TIF")|name.endsWith(".GIF"));
                        case FILE_VIDEO:
                            return (name.endsWith(".mp4")|name.endsWith(".mkv")|name.endsWith(".mpg")|name.endsWith(".mov")|name.endsWith(".wmv")|name.endsWith(".avi")|name.endsWith(".3gp")|name.endsWith(".MP4")|name.endsWith(".MKV")|name.endsWith(".MPG")|name.endsWith(".MOV")|name.endsWith(".WMV")|name.endsWith(".AVI")|name.endsWith(".3GP"));
                        case FILE_DOCUMENT:
                            return (name.endsWith(".doc")|name.endsWith(".docx")|name.endsWith(".pdf")|name.endsWith(".PDF")|name.endsWith(".xls")|name.endsWith(".csv")|name.endsWith(".xlsx")|name.endsWith(".ppt")|name.endsWith(".pptx")|name.endsWith(".DOC")|name.endsWith(".DOCX")|name.endsWith(".XLS")|name.endsWith(".XLSX")|name.endsWith(".CSV")|name.endsWith(".PPT")|name.endsWith(".PPTX"));
                    }
                    return false;
                }
                return true;
            }
        }));
        try {

            builder.setAdapter(fileArrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final File f=fileArrayAdapter.getItem(which);
                    assert f != null;
                    if(f.isDirectory()) {
                        currentDirectory = f;
                        showStorageDirs();
                    }
                    else {
                        onFileSelectedListener.onFileSelected(f);
                    }
                }
            });

            if(onetimeflag)
            {
                builder.show();
            }
            else {
                builder.show().getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(context, ((TextView) view).getText(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });
            }
        }
        catch (Exception e)
        {
            Log.d("inshowStorageDirs",e.toString());
        }
    }

    interface OnFileSelectedListener{
        void onFileSelected(File file);
    }

    private class FileArrayAdapter extends ArrayAdapter<File>{

        public FileArrayAdapter(@NonNull Context context, int resource, @NonNull File[] objects) {
            super(context, resource, objects);
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView textView=new TextView(getContext());
            textView.setText(Objects.requireNonNull(getItem(position)).getName());
            textView.setTextSize(20f);
            textView.setCompoundDrawablePadding(20);
            textView.setPadding(20,10,5,10);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            if(getItem(position).isDirectory())
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_folder,0,0,0);
            else
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file,0,0,0);

            return textView;
        }
    }

}


