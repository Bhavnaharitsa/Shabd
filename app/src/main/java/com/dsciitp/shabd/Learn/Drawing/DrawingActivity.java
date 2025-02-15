package com.dsciitp.shabd.Learn.Drawing;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dsciitp.shabd.R;
import com.dsciitp.shabd.UserConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class DrawingActivity extends AppCompatActivity {

    private DrawableView drawableView;
    private DrawableViewConfig config;
    private int selectedColorR;
    private int selectedColorG;
    private int selectedColorB;
    private int selectedColorRGB;
    private StorageReference mStorageRef;
    private SharedPreferences counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        counter = getSharedPreferences( "i", 0 );
        mStorageRef = FirebaseStorage.getInstance().getReference();
        setContentView( R.layout.activity_drawing2 );
        drawableView = findViewById( R.id.paintView );
        Button strokeWidthMinusButton = findViewById( R.id.strokeWidthMinusButton );
        Button strokeWidthPlusButton = findViewById( R.id.strokeWidthPlusButton );
        Button changeColorButton = findViewById( R.id.changeColorButton );
        Button undoButton = findViewById( R.id.undoButton );
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize( size );
        int width = size.x;
        int height = size.y - 200;
        final ColorPicker cp = new ColorPicker( DrawingActivity.this, 25, 30, 40 );

        config = new DrawableViewConfig();
        config.setStrokeColor( getResources().getColor( android.R.color.black ) );
        config.setShowCanvasBounds( true ); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth( 20.0f );
        config.setMinZoom( 1.0f );
        config.setMaxZoom( 1.0f );
        config.setCanvasHeight( height );
        config.setCanvasWidth( width );
        drawableView.setConfig( config );
        strokeWidthPlusButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                config.setStrokeWidth( config.getStrokeWidth() + 10 );
            }
        } );
        strokeWidthMinusButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                config.setStrokeWidth( config.getStrokeWidth() - 10 );
            }
        } );
        changeColorButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Random random = new Random();
                cp.show();
                Button okColor = cp.findViewById( R.id.okColorButton );

                okColor.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /* You can get single channel (value 0-255) */
                        selectedColorR = cp.getRed();
                        selectedColorG = cp.getGreen();
                        selectedColorB = cp.getBlue();

                        /* Or the android RGB Color (see the android Color class reference) */
                        selectedColorRGB = cp.getColor();
                        config.setStrokeColor(
                                Color.argb( 255, selectedColorR, selectedColorG, selectedColorB ) );
                        cp.dismiss();
                    }
                } );

            }
        } );
        undoButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                drawableView.undo();
            }
        } );
        //requestPermissionAndContinue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.drawingmenu, menu );
        return true;
    }
//    private void requestPermissionAndContinue() {
//        if (ContextCompat.checkSelfPermission( this, WRITE_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED
//                && ContextCompat.checkSelfPermission( this, READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale( this, WRITE_EXTERNAL_STORAGE )
//                    && ActivityCompat.shouldShowRequestPermissionRationale( this, READ_EXTERNAL_STORAGE )) {
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder( this );
//                alertBuilder.setCancelable( true );
//                alertBuilder.setTitle( "Permission necessary" );
//                alertBuilder.setMessage( "jaldi do" );
//                alertBuilder.setPositiveButton( android.R.string.yes, new DialogInterface.OnClickListener() {
//                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                    public void onClick(DialogInterface dialog, int which) {
//                        ActivityCompat.requestPermissions( DrawingActivity.this, new String[]{WRITE_EXTERNAL_STORAGE
//                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE );
//                    }
//                } );
//                AlertDialog alert = alertBuilder.create();
//                alert.show();
//                Log.e( "", "permission denied, show dialog" );
//            } else {
//                ActivityCompat.requestPermissions( this, new String[]{WRITE_EXTERNAL_STORAGE,
//                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE );
//            }
//        } else {
//            openActivity();
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (permissions.length > 0 && grantResults.length > 0) {
//
//                boolean flag = true;
//                for (int i = 0; i < grantResults.length; i++) {
//                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                        flag = false;
//                    }
//                }
//                if (flag) {
//                    openActivity();
//                } else {
//                    finish();
//                }
//
//            } else {
//                finish();
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    private void openActivity() {
        //add your further process after giving permission or to download images from remote server.
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {

            Toast.makeText( this, "success", Toast.LENGTH_SHORT ).show();
            drawableView.setDrawingCacheEnabled( true );
            drawableView.setDrawingCacheQuality( View.DRAWING_CACHE_QUALITY_HIGH );
            Bitmap bitmap = drawableView.getDrawingCache();
            File f = new File( Environment.getExternalStorageDirectory().getAbsolutePath(), "shabd" );
            if (!f.exists()) {
                f.mkdirs();
            }
            SharedPreferences.Editor editor = counter.edit();
            int c = counter.getInt( "i", 1 );
            editor.putInt( "i", ++c );
            editor.apply();
            String s = "image";
            if (UserConstants.displayName != null)
                s = UserConstants.displayName;
            StorageReference mDrawing = mStorageRef.child( "ShabdDrawing" ).child( s+c );
            File file = new File( f.getAbsolutePath() + "/drawingimage" + c + ".png" );

            FileOutputStream ostream;
            try {
                file.createNewFile();
                ostream = new FileOutputStream( file );
                bitmap.compress( Bitmap.CompressFormat.PNG, 100, ostream );
                Uri uri = Uri.fromFile( new File( f.getAbsolutePath() + "/drawingimage" + c + ".png" ) );

                mStorageRef.putFile( uri );
                UploadTask uploadTask = mDrawing.putFile( uri );
                uploadTask.addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d( "myStorage", "failure :(" );
                        Toast.makeText( DrawingActivity.this, "Failure", Toast.LENGTH_SHORT ).show();
                    }
                } ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d( "myStorage", "success!" );
                        Toast.makeText( DrawingActivity.this, "Successsss", Toast.LENGTH_SHORT ).show();
                    }
                } );
                ostream.flush();
                ostream.close();
                Toast.makeText( this, "Image saved", Toast.LENGTH_SHORT ).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText( this, "Not saved", Toast.LENGTH_SHORT ).show();
            }

            return super.onOptionsItemSelected( item );
        }
        return true;
    }
}
