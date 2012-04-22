package org.nasa.openspace.gc;

import java.io.Console;

import org.nasa.openspace.gc.photo.PhotoIntentActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews.ActionException;
import android.widget.SimpleAdapter.ViewBinder;


public class GarbageCollectorActivity extends Activity   {

	LinearLayout takePicture;
	LinearLayout chooseGallery;
	LinearLayout search;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		takePicture = (LinearLayout)findViewById(R.id.takePicture);
		chooseGallery = (LinearLayout)findViewById(R.id.chooseGallery);
		search = (LinearLayout)findViewById(R.id.search);
		takePicture.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(GarbageCollectorActivity.this, PhotoIntentActivity.class);
				startActivity(intent);
			}
		});
		
		chooseGallery.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
//				startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
			}
		});
		
		search.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		switch (requestCode) {
		case 2:
			if (resultCode == RESULT_OK){ //ok when choose gallery
				
				
				
//				Bundle extras = data.getExtras();
//				Bitmap mImageBitmap = (Bitmap) extras.get("data");
//				Intent intent = new Intent(GarbageCollectorActivity.this, PhotoIntentActivity.class);
//				intent.putExtra("image", mImageBitmap);
//				startActivity(intent);
				//mImageView.setImageBitmap(mImageBitmap);							
			}
//			
//			break;

		default:
			  if (data!=null )
              {
                  Bundle b = data.getExtras();
                  String res  =    (String) b.get("Name");
                  Log.d("data",res);
              }
			break;
		}
	}
	
	

}
